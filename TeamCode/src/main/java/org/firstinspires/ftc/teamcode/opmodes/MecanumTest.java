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
        Gyro gyro = new Gyro(hardwareMap);
        MecanumCont drivebase = new MecanumCont(hardwareMap, telemetry);
        OdometryController oc = new OdometryController(hardwareMap);

        MecanumRobot robot = new MecanumRobot(drivebase, gyro, oc);

        waitForStart();

        double vx = 0;
        double vy = 0;
        double vt = 0;
        double last_heading = robot.getHeading();
        double last_sample_time = System.currentTimeMillis();
        double dth = 0;
        PIDController tPId = new PIDController(0.1, 0.1, 0.1);

        while(opModeIsActive()) {

            vx = gamepad1.left_stick_x;
            vy = gamepad1.left_stick_y;
            vt = gamepad1.left_stick_y;
            if(vt == 0){
                vt += tPId.getPIDCorrection(last_heading - robot.getHeading());
            }
            else tPId.resetPID();

            robot.update();
            last_heading = robot.getHeading();
            robot.driveVelocity(vx, vy, vt);

        }

    }
}
