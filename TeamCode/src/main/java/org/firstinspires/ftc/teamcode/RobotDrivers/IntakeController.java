package org.firstinspires.ftc.teamcode.RobotDrivers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractIntake;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;
import org.json.JSONObject;

public class IntakeController extends AbstractIntake {
    Servo leftServo;
    Servo rightServo;
    Servo bucketServo;
    Servo downServo;

    DcMotor armMotor;
    SafeJsonReader reader = new SafeJsonReader("ServoPositions.json");

    JSONObject servoPositions=  reader.getJSONObject("IntakeServoPositions");
    //potentially swap for json values
    double BucketServoPosition = .5;

    public void IntakeController(HardwareMap hwmp, String lsn, String rsn, String bsn, String dsn, String amn){
        leftServo = hwmp.get(Servo.class, lsn);
        rightServo = hwmp.get(Servo.class, rsn);
        bucketServo = hwmp.get(Servo.class, bsn);
        downServo = hwmp.get(Servo.class, dsn);

        armMotor = hwmp.get(DcMotor.class, amn);


        double leftServoPositions = 0;

    }
    @Override
    public void intakeOff() {
        leftServo.setPosition(0);
        rightServo.setPosition(0);
    }

    @Override
    public void intakeOn() {
        leftServo.setPosition(.85);  //!correct values needed!
        rightServo.setPosition(.85);
    }

    @Override
    public void intakeState() {
        bucketServo.setPosition(.5);
    }

    @Override
    public void storeState() {
        bucketServo.setPosition(0);
    }

    @Override
    public void transferState() {
        bucketServo.setPosition(.5);
    }
    public void armDcOnOut (){
        armMotor.setPower(.5);
    }
    public void armDcOnIn (){
        armMotor.setPower(-0.5);
    }
    public void armDcOff(){
        armMotor.setPower(0);
    }
}
