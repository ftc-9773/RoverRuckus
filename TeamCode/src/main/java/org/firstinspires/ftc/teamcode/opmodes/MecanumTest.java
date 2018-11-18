package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotDrivers.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumCont;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumRobot;
import org.firstinspires.ftc.teamcode.RobotDrivers.OdometryController;
import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;

@TeleOp(name = "MecanumTest")
public class MecanumTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        //init
        telemetry.addData("init", 0);
        telemetry.update();
        Gyro gyro = new Gyro(hardwareMap);

        telemetry.addData("gyro", 0);
        telemetry.update();
        MecanumCont drivebase = new MecanumCont(hardwareMap, telemetry);

        telemetry.addData("drivebase", 0);
        telemetry.update();
        OdometryController oc = new OdometryController(hardwareMap);

        telemetry.addData("oc", 0);
        telemetry.update();
        MecanumRobot robot = new MecanumRobot(drivebase, gyro, oc, telemetry);

        telemetry.addData("robot", 0);
        telemetry.update();
        waitForStart();

        /*
        double last_heading = robot.getHeading();
        double last_sample_time = System.currentTimeMillis();
        double dth = 0;
        PIDController tPId = new PIDController(0.1, 0.1, 0.1);
        */
        telemetry.addData("started", 0);
        telemetry.update();
        while(opModeIsActive()) {

            /*
            if(vt == 0){
                vt += tPId.getPIDCorrection(last_heading - robot.getHeading());
            }
            else tPId.resetPID();
               */
            robot.update();
            //last_heading = robot.getHeading();
            robot.driveVelocity(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            telemetry.update();

        }

    }
}
