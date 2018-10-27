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

    }

    @Override
    public void intakeOn() {

    }

    @Override
    public void intakeState() {

    }

    @Override
    public void storeState() {

    }

    @Override
    public void transferState() {

    }
}
