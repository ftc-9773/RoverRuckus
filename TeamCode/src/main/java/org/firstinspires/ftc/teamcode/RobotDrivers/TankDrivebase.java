package org.firstinspires.ftc.teamcode.RobotDrivers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractDrivebase;

/**
 * @version 1.0
 * @author Cadence
 * driver for the tank drive on the robot in three days robot.
 * */
public class TankDrivebase extends AbstractDrivebase {
    DcMotor leftDriveMotor0,  rightDriveMotor0;
    HardwareMap hwmp;
    double leftPower = 0;
    double rightPower = 0;

    double rightAdjustment = 0;
    double leftAdjustment = 0;

    /**
     * @param ldm0 The name of the first left drive motor.
     * @param rdm0 The name of the first right dive motor
     * @param hwmp The hardware map defined in the opmode
     * */
    public TankDrivebase(String ldm0, String rdm0, HardwareMap hwmp ){
        this.hwmp = hwmp;
        this.leftDriveMotor0 = hwmp.dcMotor.get(ldm0);
        this.rightDriveMotor0 = hwmp.dcMotor.get(rdm0);
    }
    /**
     * @param pow Double between -1 and 1 specifying the power. Negative numbers are backwards
     * */
    public void setRightPow(double pow){
        this.rightDriveMotor0.setPower(pow);
    }
    /**
     * @param pow Double between -1 and 1 specifying the power. Negative numbers are backwards
     * */
    public void setLeftPow (double pow){
        this.leftDriveMotor0.setPower(-pow);
    }
    /**
     * @param pow double between -1 and one specifying how fast you want to drive
     * */
    public void setFowardBackPower(double pow){
        this.leftPower = pow;
        this.rightPower = pow;

        this.setLeftPow(pow + leftAdjustment);
        this.setRightPow(pow + rightAdjustment);
    }
    /**
     * @param pow The speed at which you want to turn
     * */
    public void setLeftRightPower(double pow){
        this.leftAdjustment = pow / 2;
        this.rightAdjustment = -pow / 2;


        this.setLeftPow((leftPower + leftAdjustment) / (1 + leftAdjustment));
        this.setRightPow((rightPower + rightAdjustment) / (1 + rightAdjustment));
    }
}
