package org.firstinspires.ftc.teamcode.RobotDrivers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;


/**
 * Class for controlling DcMotors.
 * @author Cadence
 * */
public class MotorDriver {
    DcMotorEx motor;
    DcMotor.RunMode runMode = RunMode.RUN_WITHOUT_ENCODER;
    double power = 0;
    double wheelRadius = 5.207;

    /**
     * @param hwmp Hardware map defined by the opmode
     * @param Name String name of motor
     * */
    public MotorDriver(HardwareMap hwmp, String Name){
        this.motor = hwmp.get(DcMotorEx.class, Name);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * @param pow speed to set the motor to
     * */
    public void setPow(double pow){
        setMode(RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(pow);
        this.power = pow;
    }

    /**
     * Set the motor to turn some amount of rotations
     * @param encoderDistance distance encoder steps to take
     * */

    public void setDistance(int encoderDistance){
        setMode(RunMode.RUN_TO_POSITION);
        int currEncoderPosition = motor.getCurrentPosition();
        int targetPostion = currEncoderPosition + encoderDistance;
        motor.setTargetPosition(targetPostion);
    }

    /**
     * Set mode
     * */
    public void setMode(RunMode mode){
        this.motor.setMode(mode);
        this.runMode = mode;
    }
    public double getPow(){
        return this.power;
    }
}
