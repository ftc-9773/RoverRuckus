package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;
/**
 * <h1>Cube Lift (hardware driver) class </h1>
 * The Cube Lift class acts as a driver to allow other robot functions to access and modify the state
 * of the vertical lift, cube distributor, and hanging mech.
 * <p>
 * This program is made in the paradigm followed by the rest of the robot drivers:
 * Most of the methods simply update the "goal state" of the class. <b> The update(); method MUST be used
 * in order for changes to happen in robot function.</b>
 *
 *
 * @author  Zachary Eichenberger , -ftc robocracy 9773
 * @version 1.0
 */

public class CubeLift implements Attachment {

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

    int liftZeroPos;

    // goal states
    int liftTargetPosition = 100;


    /**
     * Initializes the hardware map, and gets configurables from json
     * @param hwmap the hardwae map taken from runOpMode();
     */
    public CubeLift(HardwareMap hwmap){
        // initialize hardware map stuff
             // servos
        this.distributorServo = hwmap.servo.get("distributorServo");
        leftSorterServo = hwmap.servo.get("leftSorterServo");
        rightSorterServo = hwmap.servo.get("rightSorterServo");
        hookServo = hwmap.servo.get("hookServo");
             //motors
        leftLiftMotor = (DcMotorEx)hwmap.dcMotor.get("llMotor");
        rightLiftMotor = (DcMotorEx)hwmap.dcMotor.get("rlMotor");
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


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
        minLiftPosition = json.getInt("minLiftPosition", 30);
        maxLiftPosition = json.getInt("maxLiftPosition",4200);
        transferTol = json.getInt("transferTol", 20);

        joggingScalar = json.getDouble("joggingScalar", 3);

        // pid
        kp = json.getDouble("kp",0.0025);
        ki = json.getDouble("ki", 0.009);
        kd = json.getDouble("kd", 0.0002);
        pid = new PIDController(kp,ki,kd);
    }
    /**
     * Void method that tells the lift to go to the low position.
     * Note that the update() function must be called after this to update motor state.
     */
    public void goToLowPos(){
        liftTargetPosition = liftLowPos;
    }
    /**
     * Void method that tells the lift to go to the scoring position. also opens the hanging hook to avoid accidental catching on the latch
     * Note that the update() function must be called after this to update motor state.
     */
    public void goToScorePos(){
        liftTargetPosition = liftScorePos;
    }
    /**
     * Void method that tells the lift to move so that the hook is at the same height as the hanging latch.
     * this should be used either to drop from hang position, or to re-align to that position to re-latch in endgame
     * Note that the update() function must be called after this to update motor state.
     */
    public void goToHangPos(){
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
        // update dumpState
        dumpState = true;
    }
    /**
     * Void method that moves servos to the default, non-dumping position
     */
    public void stopDump(){
        // avoid unnecessary actions
        if(!dumpState)
        leftSorterServo.setPosition(leftSorterUpPos);
        rightSorterServo.setPosition(rightSorterUpPos);
        // update dumpState
        dumpState = false;
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
        if(Math.abs(input)>0.08) {
            int correction = (int)(input*joggingScalar);
            liftTargetPosition = bound(minLiftPosition, maxLiftPosition, liftTargetPosition + correction);
        }
    }
    //todo: code the home() method.
    public void zero(){
        liftZeroPos = rightLiftMotor.getCurrentPosition();
    }

    /**
     * A function for telling whether or not the lift is in the zone for transferring
     * @return a boolean value indicating the state of the lift; true if it is in the transfer state, false otherwise
     */
    public boolean isInTransferState(){
        return (getLiftPos() < (liftLowPos + transferTol) &&( Math.abs(liftTargetPosition - liftLowPos) < transferTol));
    }

    /**
     * The main update method of the class. this executes any actions needed to update the position of the
     * lift motors, reading from hardware, and takes care of all necessary sensor reads and writes.
     * This needs only be called once a cycle
     */
    @Override
    public void update() {
        if(isEnded) return;
        // correct motor, might eventually do other stuff;
        setLiftPower(pid.getPIDCorrection((double)liftTargetPosition, (double)getLiftPos()));
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
    // used internally to set the power of the lift. might eventually make this public
    private void setLiftPower(double power){
        leftLiftMotor.setPower(-power);
        rightLiftMotor.setPower(power);
    }
    // used internally to find the position of the lift
    public int getLiftPos(){
        return rightLiftMotor.getCurrentPosition()- liftZeroPos;
    }
    // used internally to bound the fnucthion
    private int bound(int min, int max, int input){
        if (input < min) return min;
        else if (input > max) return max;
        else return input;

    }
}
