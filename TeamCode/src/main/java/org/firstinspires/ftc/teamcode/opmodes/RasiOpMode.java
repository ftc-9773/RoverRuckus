/*
package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiCommands;
import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiInterpreter;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.V1.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.V1.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Vision.MyGoldDetector;

@Disabled
@Autonomous(name = "RASI")
public class RasiOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
        sendTelemetry("Drivebase created");
        Intake intake = new Intake(hardwareMap, this, true);
        sendTelemetry("Intake created");
        CubeLift lift = new CubeLift(hardwareMap, true);
        sendTelemetry("CubeLift created");
        Gyro gyro = new Gyro(hardwareMap);
        sendTelemetry("Gyro created");
        FTCRobotV1 robot = new FTCRobotV1(drivebase,gyro,telemetry,lift,intake);
        sendTelemetry("Robot created");

//        MyGoldDetector detector = new MyGoldDetector();
//        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 0);
//        detector.enable();
//        RasiInterpreter.d = detector;

//        RasiInterpreter interpreter = new RasiInterpreter("/sdcard/FIRST/team9773/Rasi19/","AutonOpMode.rasi", this, robot);
//        sendTelemetry("Waiting for start");

        sendTelemetry("Started");
        waitForStart();

//        interpreter.runRasi();
        sendTelemetry("Done");


    }

    public void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }
}
*/