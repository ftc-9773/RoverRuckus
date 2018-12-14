package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.Logic.PIDdriveUtil;
import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiInterpreter;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.misc.Timer;
import org.firstinspires.ftc.teamcode.Vision.MyGoldDetector;

/**
 * Created by zacharye on 12/6/18.
 */

@Autonomous(name = "testAuton") // runs vision, and acts as a testing platform for autonomous testing
public class TestAutonOpMode extends LinearOpMode{

    Timer driveTimer;
    MyGoldDetector detector;



    public void runOpMode(){
        // init robot.
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
        PIDdriveUtil pidDrive = new PIDdriveUtil(robot,this);
        sendTelemetry("Waiting for start...");
        // wait to begin opMode
        RasiInterpreter rasi = new RasiInterpreter("/sdcard/FIRST/team9773/rasi19/","AutonCrater.rasi", this, robot);


        // run vision
        detector = new MyGoldDetector();
        // init the vision
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(),0);
        detector.enable();


        waitForStart();





    }

    private void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }


}
