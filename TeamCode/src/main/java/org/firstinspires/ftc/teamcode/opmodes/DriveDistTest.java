package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.misc.PushButton;

@TeleOp(name = "driveDist")
public class DriveDistTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        sendTelemetry("init");
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
        sendTelemetry("Drivebase created");
        Intake intake = new Intake(hardwareMap, this);
        sendTelemetry("Intake created");
        CubeLift lift = new CubeLift(hardwareMap);
        sendTelemetry("CubeLift created");
        Gyro gyro = new Gyro(hardwareMap);
        sendTelemetry("Gyro created");
        FTCRobotV1 robot = new FTCRobotV1(drivebase,gyro,telemetry,lift,intake);
        sendTelemetry("Robot created");

        //Pushbuttons
        PushButton a = new PushButton(gamepad1, "a");
        PushButton b = new PushButton(gamepad1, "b");
        PushButton x = new PushButton(gamepad1, "x");
        PushButton y = new PushButton(gamepad1, "y");

        sendTelemetry("waiting for start");
        waitForStart();
        sendTelemetry("started");

        while(opModeIsActive()){
            a.record();
            b.record();
            x.record();
            y.record();

            if (a.isJustOn()){
                robot.driveDist(3, 3, 0, 1.5);
            }
            if (b.isJustOn()){
                robot.driveDist(3,0,0,1.5);
            }
        }

    }

    private void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }
}