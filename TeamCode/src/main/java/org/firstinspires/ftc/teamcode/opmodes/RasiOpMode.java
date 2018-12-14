package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiCommands;
import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiInterpreter;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.misc.initter;
import org.firstinspires.ftc.teamcode.Vision.MyGoldDetector;

@Autonomous(name = "RASI")
public class RasiOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        initter.set(hardwareMap, telemetry, this);
        FTCRobotV1 robot = initter.init();

        MyGoldDetector detector = new MyGoldDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 0);
        detector.enable();
        RasiInterpreter.d = detector;

        RasiInterpreter interpreter = new RasiInterpreter("/sdcard/FIRST/team9773/rasi19/","AutonOpMode.rasi", this, robot);
        sendTelemetry("Waiting for start");

        robot.lift.adjustLift(-0.1);

        sendTelemetry("Started");
        interpreter.runRasi();


    }
    public void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }
}
