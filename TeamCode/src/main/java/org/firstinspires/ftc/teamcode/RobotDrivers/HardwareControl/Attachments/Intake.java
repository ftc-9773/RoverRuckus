package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;


/**
 * <h1>Intake (hardware driver) class </h1>
 * The Intake class acts as a driver to allow other robot functions to access and modify the state
 * of the intake arm and slide
 * <p>
 * This program is made in the paradigm followed by the rest of the robot drivers:
 * Most of the methods simply readSensors the "goal state" of the class. <b> The readSensors(); method MUST be used
 * in order for changes to happen in robot function.</b>
 *
 *
 * @author  Zachary Eichenberger , -ftc robocracy 9773
 * @version 1.0
 */

public class Intake {

   // private enum IntakeState { Transfer, Intake, Returning}

    // hardware components
    private Servo leftIntakeServo;
    private Servo rightIntakeServo;
    private Servo bucketServo;
    private DcMotorEx armMotor;

    // initialization stuff
    SafeJsonReader jsonReader;
    LinearOpMode opmode;

    // pid controller
    private PIDController extensionPID;
    int armTargetPos;
    boolean pidEnabled = true;
    double gamepadArmPower;

    // Servo positions
    private double bucketServoTransferPosition ;
    private double bucketServoIntakePosition;
    private double carryPosition;

    //double 393 servo vals
    private double stopVal, forwardsVal, backwardsVal;
    /// armPositions
     int armInPosition = 10;
     int transferThreshold = 80;
     int armOutExtraThreshold = 400;

     int zeroPosition = 0;

     static final String TAG = "ftc9773_Intake";

    private double intakeMotorPower, intakeBucketServoPosition;
    /*
        Some stuff for me: pushing the file
        cd TeamCode/src/main/java/org/firstinspires/ftc/teamcode/json19/
        // to push
        ~/Library/Android/sdk/platform-tools/adb push IntakePositions.json /sdcard/FIRST/team9773/json19
        or
        ~/Library/Android/sdk/platform-tools/adb push CubeLift.json /sdcard/FIRST/team9773/json19
        ~/Library/Android/sdk/platform-tools/adb push DrivePidVals.json /sdcard/FIRST/team9773/json19


        // to pull
        ~/Library/Android/sdk/platform-tools/adb pull /sdcard/FIRST/team9773/json18/CubeTrayServoPositions.json
     */

    /**
     * The only constructor of the IntakeController class. It serves two main functions:
     * Firstly it takes the hardware map from the opMode, and uses it to match the servo and motor objects
     * with their respective real world counterparts.
     * <p>
     * Secondly, the function initializes all the necessary "tunables" from the JSON file.
     * @param hwmp the hardware map from the robot; used to map objects to real world counterparts
     */
    public Intake(HardwareMap hwmp, LinearOpMode op, boolean initArmPos){
        // initialize harware map stuff
        leftIntakeServo = hwmp.get(Servo.class, "liServo");
        rightIntakeServo = hwmp.get(Servo.class, "riServo");
        bucketServo = hwmp.get(Servo.class, "trServo");
        armMotor = hwmp.get(DcMotorEx.class, "iaMotor");
        // initialize the motor
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        // Initialize all values from configuration files
        jsonReader = new SafeJsonReader("IntakePositions") ;
        // bucket servo positions
        bucketServoTransferPosition = jsonReader.getDouble( "bucketServoTransferPosition", 0.04);
        bucketServoIntakePosition = jsonReader.getDouble( "bucketServoIntakePosition",0.55);
        carryPosition = jsonReader.getDouble("bucketServoCarryPosition", 0.46);
        // intake stuff
        stopVal = jsonReader.getDouble("intakeStopVal", 0.5);
        forwardsVal = jsonReader.getDouble("intakeFowardsVal",0.09);
        backwardsVal = jsonReader.getDouble("intakeBackwardsVal",0.85);
        // liftCoefficents
        double kp = jsonReader.getDouble("liftKp",0.0025);
        double ki = jsonReader.getDouble("liftKi", 0.0009);
        double kd = jsonReader.getDouble("liftKd", 0.0002);
        extensionPID = new PIDController(kp,ki,kd);

        armInPosition= jsonReader.getInt("armInPosition", 10);
        transferThreshold =jsonReader.getInt("transferThreshold", 80);
        armOutExtraThreshold = jsonReader.getInt("armOutExtraThreshold",300);

        opmode = op;
        retractArm();
        if(initArmPos) {
            reset();
        }
    }

    /**
     * Void method that stops the motion of the intake. Also raises the intake bucket for transit
     * Note that the readSensors() function must be called after this for any action to happen
     */
    public void stopIntake() {
        intakeMotorPower = stopVal;
        if(getArmPos() > (armInPosition + armOutExtraThreshold))
        intakeBucketServoPosition = carryPosition;
    }

    public void carryPos(){
        intakeBucketServoPosition = carryPosition;
    }

    public void retractArm(){
        double lastTime = System.currentTimeMillis();
        double lastEncoderValue = getArmPos();
        double speed = 1000;
        armMotor.setPower(0.15); //Low power retraction
        while(speed > 10 && !opmode.isStopRequested() && !opmode.isStarted()){
            double dx = getArmPos() - lastEncoderValue;
            double dt = lastTime - System.currentTimeMillis();
            speed = dx / dt;
        }
        armMotor.setPower(0);
    }

    public void reset(){
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    /**
     * Void method that starts the motion of the intake. Also lowers the bucket to allow collection
     * Note that the readSensors() function must be called after this for any action to happen
     */
    public void intakeOn() {
        intakeMotorPower = forwardsVal;
        if(!pidEnabled)
        intakeBucketServoPosition = bucketServoIntakePosition;
    }

    /**
     * Void method that reverses the motion of the intake.
     * Note that the readSensors() function must be called after this for any action to happen
     */
    public void reverseIntake(){
        intakeMotorPower = backwardsVal;
    }


    /**
     * sets the extension lift's power (supposed to be) from the gamepad value. if the value
     * is outside of a dead zone, it is assumed that the action is intentional,
     * and the value read is written to the motor, allowing manual control of the arm.
     * Note that the readSensors() function must be called after this for any action to happen
     */
    public void setExtensionPowerFromGamepad(double power){
        if(Math.abs(power) >= 0.09 ) {
            pidEnabled = false;
            gamepadArmPower = -power;
        } else gamepadArmPower = 0.0;
    }

    /**
     * Void method that raises the bucket to the transfer position. should be called by
     * an exterior method to control handoff
     * Note that the readSensors() function must be called after this for any action to happen
     */
    public void transferMinerals() {
        intakeBucketServoPosition = bucketServoTransferPosition   ;
    }

    /**
     * Switcehs into automatic control to return the intake lift to the transfer position.
     * Also lifts the bucket to the storage position, untill the lift is back in.
     * Note that the readSensors() function must be called after this for any action to happen
     */
    public void goToTransfer(){
        pidEnabled = true;
        armTargetPos = armInPosition;
        intakeBucketServoPosition = carryPosition;
    }

    /**
     * The main readSensors method of the class. this executes any actions needed to readSensors the position of the
     * lift, and servos, reading from hardware, and writing to it when done.
     */
    public void update(){
        if(pidEnabled&&isInTransferState()) pidEnabled = false;
        if(pidEnabled) {
            armMotor.setPower(extensionPID.getPIDCorrection(armTargetPos, getArmPos()));
        }
        else
            armMotor.setPower(gamepadArmPower);
        // set servoPositions
        leftIntakeServo.setPosition(intakeMotorPower);
        rightIntakeServo.setPosition(intakeMotorPower);
        bucketServo.setPosition(intakeBucketServoPosition);
        //TODO: this might be dumb and not work
        if(getArmPos() <0){
            zeroPosition = armMotor.getCurrentPosition();
            Log.i(TAG, "adjusting zero position to " + zeroPosition);
        }
    }

    public int getArmPos(){
        return armMotor.getCurrentPosition() - zeroPosition;
    }


    /**
     * Allows the user to tell if the arm is in the transfer state. used to coordinate handoff process
     * @return whether or not the arm is within the bounds of the "transfer zone"
     */
    public boolean isInTransferState(){
        return Math.abs(getArmPos())< Math.abs(transferThreshold);
    }


    public void stop() {
        armMotor.setPower(0.0);

    }
}
