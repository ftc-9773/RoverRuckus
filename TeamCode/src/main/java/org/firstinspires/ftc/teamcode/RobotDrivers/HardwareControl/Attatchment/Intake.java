package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attatchment;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;


/**
 * <h1> </h1>
 * The HelloWorld program implements an application that
 * simply displays "Hello World!" to the standard output.
 * <p>
 * Giving proper comments in your program makes it more
 * user friendly and it is assumed as a high quality code.
 *
 *
 * @author  Zara Ali
 * @version 1.0
 * @since   2014-03-31
 */


public class Intake implements Attatchment {
    private enum IntakeState { //todo:intake

    }

    // hardware components
    Servo leftIntakeServo;
    Servo rightIntakeServo;
    Servo bucketServo;
    DcMotor armMotor;

    // initialization stuff
    SafeJsonReader jsonReader;


    // Servo positions
    double bucketServoTransferPosition ;
    double bucketServoIntakePosition;
    double carryPosition;
    //double 393 servo vals
    double stopVal, fowardsVal, backwardsVal;
    /// pid coefficents
    double kp, ki, kd ;

    double intakeMotorPower, intakebBucketPosition;

    public void IntakeController(HardwareMap hwmp ){
        // initialize harware map stuff
        leftIntakeServo = hwmp.get(Servo.class, "liServo");
        rightIntakeServo = hwmp.get(Servo.class, "riServo");
        bucketServo = hwmp.get(Servo.class, "trServo");
        armMotor = hwmp.get(DcMotor.class, "iaMotor");

        // Initialize all values from configuration files
        jsonReader = new SafeJsonReader("IntakePositions.json") ;
        // bucket servo positions
        bucketServoTransferPosition = jsonReader.getDouble( "bucketServoTransferPosition");
        bucketServoIntakePosition = jsonReader.getDouble( "bucketServoIntakePosition");
        carryPosition = jsonReader.getDouble("bucketServoCarryPosition");
        // intake stuff
        stopVal = jsonReader.getDouble("intakeStopVal", 0.5);
        fowardsVal = jsonReader.getDouble("intakeFowardsVal",0.85);
        backwardsVal = jsonReader.getDouble("intakeBackwardsVal",0.15);
        // liftCoefficents
        kp = jsonReader.getDouble("liftKp",0.1);
        ki = jsonReader.getDouble("liftKi", 0.0);
        kd = jsonReader.getDouble("liftKd", 0.0);
    }

    public void intakeOff() {
        intakeMotorPower = stopVal;
        intakebBucketPosition = carryPosition;
    }

    public void intakeOn() {

    }


    public void intakeState() {
        bucketServo.setPosition(bucketServoIntakePosition);
    }

    public void storeState() {
        bucketServo.setPosition(0);

    }


    public void transferState() {
        bucketServo.setPosition(this.bucketServoTransferPosition);
    }
    public void update(){

    }

    @Override
    public void stop() {

    }


}
