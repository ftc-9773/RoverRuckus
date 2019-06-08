package org.firstinspires.ftc.teamcode.zachStuff;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.IntakeV2;

@Autonomous(name = "TankTest")
public class TestOpode extends LinearOpMode {
    boolean isTank = false;
    final static double driveCoeff = 0.8;
    final static double turnCoeff = 1.0;


    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor leftMotor1, rightMotor1, leftMotor2, rightMotor2;

        rightMotor1 = hardwareMap.dcMotor.get("right1");
        rightMotor2 = hardwareMap.dcMotor.get("right2");

        leftMotor1 = hardwareMap.dcMotor.get("left1");
        leftMotor2 = hardwareMap.dcMotor.get("left2");



        waitForStart();
        while(opModeIsActive()){
            if(isTank){
                rightMotor1.setPower(gamepad1.right_stick_y);
                rightMotor2.setPower(gamepad1.right_stick_y);

                leftMotor1.setPower(-gamepad1.left_stick_y);
                leftMotor2.setPower(-gamepad1.left_stick_y);
            } else {
                double speed, turningPow;

                speed = - gamepad1.left_stick_y * driveCoeff;
                turningPow = gamepad1.right_stick_x * turnCoeff;

                double leftPow = speed + turningPow;
                double rightPow = speed - turningPow;

                if(Math.abs(leftPow) > 1.0 || Math.abs(rightPow) > 1.0){
                    double divider = Math.max(Math.abs(leftPow), Math.abs(rightPow));
                    leftPow /=  divider;
                    rightPow /= divider;
                }
                rightMotor1.setPower(rightPow);
                rightMotor2.setPower(rightPow);

                leftMotor1.setPower(-leftPow);
                leftMotor2.setPower(-leftPow);



            }




        }


    }
}
