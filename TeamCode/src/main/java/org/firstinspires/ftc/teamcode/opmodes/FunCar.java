package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.TankDrivebase;

@TeleOp(name = "RobotInThreeDays", group = "AAA robotInThreeDays")
public class FunCar extends LinearOpMode {

    public void runOpMode(){

        TankDrivebase drivebase = new TankDrivebase( "lm", "rm", hardwareMap);
        waitForStart();
        drivebase.setFowardBackPower(gamepad1.left_stick_y);
        drivebase.setLeftRightPower(gamepad1.right_stick_y);
    }
}
