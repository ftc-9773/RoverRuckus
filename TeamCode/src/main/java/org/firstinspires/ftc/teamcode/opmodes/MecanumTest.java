package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumCont;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumRobot;

@TeleOp(name = "MecanumTest")
public class MecanumTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        //init
        MecanumCont drivebase = new MecanumCont(hardwareMap);
        MecanumRobot robot = new MecanumRobot(drivebase);


        waitForStart();

        while(opModeIsActive()){
            robot.driveVelocity(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x);
        }

    }
}
