package org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LiftController extends AbstractLift {

    DcMotorEx leftMotor;
    DcMotorEx rightMotor;
    Servo hookServo;

    public LiftController(HardwareMap hwmp, String lmn, String rmn, String hsn){
        this.leftMotor = hwmp.get(DcMotorEx.class, lmn);
        this.rightMotor = hwmp.get(DcMotorEx.class, rmn);
        this.hookServo = hwmp.get(Servo.class, hsn);
    }

    public LiftController(HardwareMap hwmp){
        this(hwmp, "leftLiftMotor", "rightLiftMotor", "hookServo");
    }

    public void detach(){

    }

    public void setServo(double pos){
        this.hookServo.setPosition(pos);
    }

    public void setPower(double pow){
        this.rightMotor.setPower(-pow);
        this.leftMotor.setPower(pow);
    }


}
