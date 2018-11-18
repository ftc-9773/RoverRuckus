package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumCont;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumRobot;
import org.firstinspires.ftc.teamcode.RobotDrivers.OdometryController;

@TeleOp(name = "MecanumTest")
public class MecanumTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        //init
        MecanumCont drivebase = new MecanumCont(hardwareMap);
        //OdometryController oc = new OdometryController(hardwareMap);

        MecanumRobot robot = new MecanumRobot(drivebase);

        waitForStart();
        boolean isA = false;
        double dt = 0;
        double last = System.currentTimeMillis();
        while(opModeIsActive()) {
            if (isA) {
                robot.driveVelocity(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            }
            if (!isA) {
                drivebase.arcadeDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            }
            isA = gamepad1.a && System.currentTimeMillis() - last > 100? !isA:isA;
            last = System.currentTimeMillis() - last > 100? System.currentTimeMillis():last;
        }

    }
}
