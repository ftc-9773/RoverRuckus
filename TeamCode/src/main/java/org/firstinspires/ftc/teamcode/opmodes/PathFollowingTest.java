package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;

@TeleOp(name = "PathFollowing")
public class PathFollowingTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        //Create everything ...
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

        sendTelemetry("Waiting for start...");
        waitForStart();
        boolean temp = true;


        while(opModeIsActive()) {
            if (temp){
                sendTelemetry("Started");
                temp = false;
            }

        }
    }

    private void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }
}

