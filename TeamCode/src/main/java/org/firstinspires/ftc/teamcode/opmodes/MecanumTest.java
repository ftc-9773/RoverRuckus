package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.OdometryController;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;

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
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);

        telemetry.addData("drivebase", 0);
        telemetry.update();
        //OdometryController oc = new OdometryController(hardwareMap);

        telemetry.addData("oc", 0);
        telemetry.update();
        FTCRobotV1 robot = new FTCRobotV1(drivebase, gyro, telemetry);

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
