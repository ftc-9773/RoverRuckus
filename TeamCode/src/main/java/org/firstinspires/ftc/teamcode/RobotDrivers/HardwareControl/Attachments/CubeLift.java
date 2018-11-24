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

    DcMotorEx leftLiftMotor,rightLiftMotor;
    Servo distributorServo, leftSorterServo, rightSorterServo, hookServo;
    boolean isDistributorRight = true;
    boolean isEnded = false;
    // servo positions
    double leftSorterUpPos,leftSorterDownPos, rightSorterUpPos, rightSorterDownPos, distributorLeftPos,distributorRightPos;
    double hookOpenPos, hookClosedPos;
    // lift stuff
    int liftLowPos, liftHookPos, liftScorePos, minLiftPosition, maxLiftPosition;
    int transferTol;
    double kp, ki,kd;
    SafeJsonReader json;
    PIDController pid ;

    double liftZeroPos;

    // goal states
    int liftTargetPosition = 0;


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


        // JSON
        json = new SafeJsonReader("cubeLift");

        //servos
        leftSorterUpPos = json.getDouble("leftSorterUpPos");
        leftSorterDownPos = json.getDouble("leftSorterDownPos");
        rightSorterUpPos = json.getDouble("rightSorterUpPos");
        rightSorterDownPos = json.getDouble("rightSorterDownPos");
        distributorLeftPos = json.getDouble("distributorLeftPos");
        distributorRightPos = json.getDouble("distributorRightPos");
        hookOpenPos = json.getDouble("hookOpenPos");
        hookClosedPos = json.getDouble("hookClosedPos");

        // lift
        liftLowPos = json.getInt("liftLowPos");
        liftHookPos = json.getInt ("liftHookPos");
        liftScorePos = json.getInt ("liftScorePos");
        minLiftPosition = json.getInt("minLiftPosition", 10);
        maxLiftPosition = json.getInt("maxLiftPosition");
        transferTol = json.getInt("transferTol", 20);

        // pid
        kp = json.getDouble("kp");
        ki = json.getDouble("ki");
        kd = json.getDouble("kd");
        pid = new PIDController(kp,ki,kd);
    }

    public void goToLowPos(){
        liftTargetPosition = liftLowPos;
    }
    public void goToScorePos(){
        liftTargetPosition = liftScorePos;
        hookServo.setPosition(hookOpenPos);
    }
    public void goToHangPos(){
        liftTargetPosition = liftHookPos;
        hookServo.setPosition(hookOpenPos);
    }
    public void dump(){
        if(isDistributorRight) rightSorterServo.setPosition(rightSorterDownPos);
        else leftSorterServo.setPosition(leftSorterDownPos);

    }
    public void setLefScoreSide(){
        if(isDistributorRight)
            distributorServo.setPosition(distributorLeftPos);
        isDistributorRight = false;
    }
    public void setRightScoreSide(){
        if(!isDistributorRight)
            distributorServo.setPosition(distributorRightPos);
        isDistributorRight = true;

    }
    public void stopDump(){
        leftSorterServo.setPosition(leftSorterUpPos);
        rightSorterServo.setPosition(rightSorterUpPos);
    }

    public void adjustLift(int input){
        if(Math.abs(input)>.15)
            liftTargetPosition = bound(minLiftPosition, maxLiftPosition,  liftTargetPosition+ input);
    }
    //todo: code the zero() method.
    public void zero(){}

    public boolean isInTransferState(){
        return (getLiftPos() < (liftLowPos + transferTol) &&( Math.abs(liftTargetPosition - liftLowPos) < transferTol));
    }

    @Override
    public void update() {
        if(isEnded) return;
        // correct motor, might eventually do other stuff;
        setLiftPower(pid.getPIDCorrection((double)liftTargetPosition, (double)getLiftPos()));
    }
    @Override
    public void stop() {
        leftLiftMotor.setPower(0);
        rightLiftMotor.setPower(0);
        isEnded = true;
    }

    private void setLiftPower(double power){
        leftLiftMotor.setPower(power);
        rightLiftMotor.setPower(-power);
    }

    private int getLiftPos(){
        return leftLiftMotor.getCurrentPosition()- minLiftPosition;
    }
    private int bound(int min, int max, int input){
        if (input < min) return min;
        else if (input > max) return max;
        else return input;

    }
}
