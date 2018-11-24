package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;
@Deprecated
public class VerticalLift implements Attachment{
    public static double MAX_RAD_PER_SECOND = Math.PI * 4;

    DcMotorEx leftMotor;
    DcMotorEx rightMotor;
    Servo hookServo;
    // TODO: For this stuff we really only need one json reader. it only clogs up memory to have more, especially as we dont have to write any of this stuff
    SafeJsonReader servoReader = new SafeJsonReader("hookServoPositions");
    SafeJsonReader motorReader = new SafeJsonReader("DcMotorPIDCoefficients");
    double hsOpen;
    double hsClosed;

    public VerticalLift(HardwareMap hwmp, String lmn, String rmn, String hsn){
        this.leftMotor = hwmp.get(leftMotor.getClass(), lmn);
        this.rightMotor = hwmp.get(rightMotor.getClass(), rmn);
        this.hookServo = hwmp.get(hookServo.getClass(), hsn);

        this.hsOpen = servoReader.getDouble("hookServoOpen");
        this.hsClosed = servoReader.getDouble("hookServoClosed");

        PIDCoefficients pidCoefficients = new PIDCoefficients(motorReader.getDouble("kp"), motorReader.getDouble("ki"), motorReader.getDouble("kd"));
        this.leftMotor.setPIDCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidCoefficients);
        this.rightMotor.setPIDCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidCoefficients);
    }

    public void setSpeed(double speed){
        this.leftMotor.setVelocity(speed, AngleUnit.RADIANS);
        this.rightMotor.setVelocity(speed, AngleUnit.RADIANS);
    }

    public void setPerc(double perc){
        this.leftMotor.setVelocity(perc * MAX_RAD_PER_SECOND);
        this.rightMotor.setVelocity(perc * MAX_RAD_PER_SECOND);
    }

    public void setPos(int pos){
        this.leftMotor.setTargetPosition(leftMotor.getCurrentPosition() + pos);
        this.rightMotor.setTargetPosition(rightMotor.getCurrentPosition() + pos);
    }

    public void open(){this.hookServo.setPosition(hsOpen);}

    public void close(){this.hookServo.setPosition(hsClosed);}

    @Override
    public void stop(){

    }

    @Override
    public void update(){

    }
}
