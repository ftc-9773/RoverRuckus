package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;
import org.firstinspires.ftc.teamcode.Utilities.misc.Timer;

/**
 * <h1>Cube Lift (hardware driver) class </h1>
 * The Cube Lift class acts as a driver to allow other robot functions to access and modify the state
 * of the vertical lift, cube distributor, and hanging mech.
 * <p>
 * This program is made in the paradigm followed by the rest of the robot drivers:
 * Most of the methods simply readSensors the "goal state" of the class. <b> The readSensors(); method MUST be used
 * in order for changes to happen in robot function.</b>
 *
 *
 * @author  Zachary Eichenberger , -ftc robocracy 9773
 * @version 1.0
 */

public class CubeLift implements Attachment {
    private final static String TAG = "ftc9773_Cube Lift:";

    private DcMotorEx leftLiftMotor,rightLiftMotor;
    private Servo distributorServo, leftSorterServo, rightSorterServo, hookServo;
    private boolean isDistributorRight = true;
    private boolean isEnded = false;
    // servo positions
    private double leftSorterUpPos,leftSorterDownPos, rightSorterUpPos, rightSorterDownPos, distributorLeftPos,distributorRightPos;
    private double hookOpenPos, hookClosedPos;
    // lift stuff
    private int liftLowPos, liftHookPos, liftScorePos, minLiftPosition, maxLiftPosition;
    private int transferTol;
    private double joggingScalar;
    private double kp, ki,kd;
    private SafeJsonReader json;
    private PIDController pid ;
    boolean dumpState = false;
    Timer unLatching = null;

    // pls no change
    public boolean homingThing = false;

    int liftZeroPos = 0;

    // goal states
    int liftTargetPosition = 100;


    /**
     * Initializes the hardware map, and gets configurables from json
     * @param hwmap the hardwae map taken from runOpMode();
     */
    public CubeLift(HardwareMap hwmap, boolean initLift){
        // initialize hardware map stuff
             // servos
        this.distributorServo = hwmap.servo.get("distributorServo");
        leftSorterServo = hwmap.servo.get("leftSorterServo");
        rightSorterServo = hwmap.servo.get("rightSorterServo");
        hookServo = hwmap.servo.get("hookServo");
             //motors
        leftLiftMotor = (DcMotorEx)hwmap.dcMotor.get("llMotor");
        rightLiftMotor = (DcMotorEx)hwmap.dcMotor.get("rlMotor");
        if(initLift) {
            leftLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        Log.d(TAG, "Initialized liftMotors. LeftMotorPosition =" + getLiftPos());
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Log.d(TAG, "Initialised motors and servos");

        // JSON
        json = new SafeJsonReader("CubeLift");

        //servos
        leftSorterUpPos = json.getDouble("leftSorterUpPos", 0.9);
        leftSorterDownPos = json.getDouble("leftSorterDownPos", 0.55);

        rightSorterUpPos = json.getDouble("rightSorterUpPos", 0.5);
        rightSorterDownPos = json.getDouble("rightSorterDownPos", 0.8);

        distributorLeftPos = json.getDouble("distributorLeftPos",0.54);
        distributorRightPos = json.getDouble("distributorRightPos", 0.37);
        hookOpenPos = json.getDouble("hookOpenPos", 0.05);
        hookClosedPos = json.getDouble("hookClosedPos", 0.38);

        // lift
        liftLowPos = json.getInt("liftLowPos",195);
        liftHookPos = json.getInt ("liftHookPos",2718);
        liftScorePos = json.getInt ("liftScorePos",3700);
        minLiftPosition = json.getInt("minLiftPosition", -20);
        maxLiftPosition = json.getInt("maxLiftPosition",4200);
        transferTol = json.getInt("transferTol", 20);

        joggingScalar = json.getDouble("joggingScalar", 3);

        // pid
        kp = json.getDouble("kp",0.0025);
        ki = json.getDouble("ki", 0.009);
        kd = json.getDouble("kd", 0.0002);
        pid = new PIDController(kp,ki,kd);

        Log.d(TAG, "Read and set from JSON");
        setLefScoreSide();
    }
    /**
     * Void method that tells the lift to go to the low position.
     * Note that the readSensors() function must be called after this to readSensors motor state.
     */
    public void goToLowPos(){
        Log.v(TAG, "Set target position to Low position");
        liftTargetPosition = liftLowPos;
    }
    /**
     * Void method that tells the lift to go to the scoring position. also opens the hanging hook to avoid accidental catching on the latch
     * Note that the readSensors() function must be called after this to readSensors motor state.
     */
    public void goToScorePos(){
        Log.v(TAG, "Set target position to scoring position");
        liftTargetPosition = liftScorePos;
    }
    /**
     * Void method that tells the lift to move so that the hook is at the same height as the hanging latch.
     * this should be used either to drop from hang position, or to re-align to that position to re-latch in endgame
     * Note that the readSensors() function must be called after this to readSensors motor state.
     */
    public void goToHangPos(){
        Log.v(TAG, "Set target to hang pos");
        liftTargetPosition = liftHookPos;
        hookServo.setPosition(hookOpenPos);
    }

    /**
     * Void method that moves servos to the dumping positon
     */
    public void dump(){
        // avoid unnecessary actions
        if(dumpState) return;
        if(isDistributorRight) rightSorterServo.setPosition(rightSorterDownPos);
        else leftSorterServo.setPosition(leftSorterDownPos);
        // readSensors dumpState
        dumpState = true;
        Log.v(TAG, "Set dump state");
    }
    /**
     * Void method that moves servos to the default, non-dumping position
     */
    public void stopDump(){
        // avoid unnecessary actions
        if(!dumpState)
        leftSorterServo.setPosition(leftSorterUpPos);
        rightSorterServo.setPosition(rightSorterUpPos);
        // readSensors dumpState
        dumpState = false;
        Log.v(TAG, "Moved away from dump state");
    }


    /**
     * sets the distributor configuration to dump on the right side .
     */
    public void setLefScoreSide(){
        if(isDistributorRight)
            distributorServo.setPosition(distributorLeftPos);
        isDistributorRight = false;
    }
    /**
     * sets the distributor configuration to dump on the right side .
     */
    public void setRightScoreSide(){
        if(!isDistributorRight)
            distributorServo.setPosition(distributorRightPos);
        isDistributorRight = true;
    }

    /**
     * a method that allows for adjustment of the lift position. it "jogs" the position by a small amount
     * @param input the gamepad adjustment to be "jogged"
     */
    public void adjustLift( double input){
        if( Math.abs(input)>0.08) {
            Log.d(TAG, "Got input to adjustLift: " + input);
            int correction = (int)(input*joggingScalar);
            liftTargetPosition = bound(minLiftPosition, maxLiftPosition, liftTargetPosition + correction);
        }
        else{
            Log.v(TAG, "Set lift position to 0, input was " + input);
        }
    }
    //todo: code the home() method.
    public void zero(){
        liftZeroPos = -leftLiftMotor.getCurrentPosition();
    }

    /**
     * A function for telling whether or not the lift is in the zone for transferring
     * @return a boolean value indicating the state of the lift; true if it is in the transfer state, false otherwise
     */
    public boolean isInTransferState(){
        return (Math.abs(getLiftPos()) < Math.abs(liftLowPos) + Math.abs(transferTol) );
    }

    /**
     * The main readSensors method of the class. this executes any actions needed to readSensors the position of the
     * lift motors, reading from hardware, and takes care of all necessary sensor reads and writes.
     * This needs only be called once a cycle
     */
    @Override
    public void update() {
        if(isEnded) return;
        if (unLatching!= null){
            if (unLatching.isDone()){
                unLatching = null;
                return;
            }
            else return;
        }
        //homing thing;
        if(homingThing){
        } else {
            // correct motor, might eventually do other stuff;

            double correction = pid.getPIDCorrection((double) liftTargetPosition, (double) getLiftPos());

            setLiftPower(correction);
            Log.d(TAG, " pid correction set:" + correction + " position: " + getLiftPos());
        }
        if(getLiftPos() < 0){
            liftZeroPos = -leftLiftMotor.getCurrentPosition();
            Log.i(TAG, "adjusting zero position to " + liftZeroPos);
        }
    }
    /**
     * ends all motion of the components allows for ending of all functions.
     */
    @Override
    public void stop() {
        leftLiftMotor.setPower(0);
        rightLiftMotor.setPower(0);
        isEnded = true;
    }

    public void homingThing(){
        homingThing = true;
        setLiftPower(-0.25);
        if(getLiftPos() < 0){
            liftZeroPos = -leftLiftMotor.getCurrentPosition();
            Log.i(TAG, "adjusting zero position to " + liftZeroPos);
        }
    }

    public void unLatchStopper(){
        unLatching = new Timer(0.5);
        setLiftPower(-0.2);
    }
    // used internally to set the power of the lift. might eventually make this public
    private void setLiftPower(double power){
        leftLiftMotor.setPower(-power);
        rightLiftMotor.setPower(power);
    }
    // used internally to find the position of the lift
    public int getLiftPos(){
        return (-leftLiftMotor.getCurrentPosition())- liftZeroPos;
    }

    public void closeLatch(){
        hookServo.setPosition(hookClosedPos);
    }

    // used internally to bound the function
    private int bound(int min, int max, int input){
        if (input < min) return min;
        else if (input > max) return max;
        else return input;
    }

    @Override
    public boolean inStableState() {
        return pid.getPIDCorrection(liftTargetPosition, getLiftPos()) > 0.1;
    }
}
