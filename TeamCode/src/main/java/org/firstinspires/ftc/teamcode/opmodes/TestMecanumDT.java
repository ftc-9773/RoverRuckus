package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.MecanumController;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumDrivebase;

@TeleOp(name = "TestMecanum" )
public class TestMecanumDT extends LinearOpMode {
    MecanumController drivebase ;

    @Override
    public void runOpMode() throws InterruptedException {
        // init
        drivebase = new MecanumController(hardwareMap);
        waitForStart();
        while(opModeIsActive()){
            drivebase.arcadeDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

    }
}
