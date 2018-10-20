package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumDrivebase;

public class MecanumTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        //init
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, "", "", "", "");

        waitForStart();

        while(opModeIsActive()){
            drivebase.setForwardBackPower(gamepad1.left_stick_y);
            drivebase.setLeftRightPower(gamepad1.left_stick_x);


        }




    }
}
