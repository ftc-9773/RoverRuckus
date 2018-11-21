package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.sensor.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumCont;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumRobot;
import org.firstinspires.ftc.teamcode.RobotDrivers.OdometryController;
import org.firstinspires.ftc.teamcode.Utilities.misc.PushButton;

@TeleOp(name="DriveDist")
public class DriveDist extends LinearOpMode {
    @Override

    public void runOpMode() {
        Gyro gyro = new Gyro(hardwareMap);
        MecanumCont drivebase = new MecanumCont(hardwareMap, telemetry);
        OdometryController oc = new OdometryController(hardwareMap);
        MecanumRobot robot = new MecanumRobot(drivebase, gyro, oc, telemetry);

        PushButton a = new PushButton(gamepad1, "a");
        waitForStart();

        while(opModeIsActive()){
            a.record();
            if(a.isJustOn()) {
                robot.driveDist(1, 1, 0, 1);
            }
        }
    }
}
