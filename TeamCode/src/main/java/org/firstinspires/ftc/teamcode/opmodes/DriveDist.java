package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.OdometryController;
import org.firstinspires.ftc.teamcode.Utilities.misc.PushButton;

@TeleOp(name="DriveDist")
public class DriveDist extends LinearOpMode {
    @Override

    public void runOpMode() {
        Gyro gyro = new Gyro(hardwareMap);
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
        //OdometryController oc = new OdometryController(hardwareMap);
        FTCRobotV1 robot = new FTCRobotV1(drivebase, gyro, telemetry);

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
