package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.Logic.DriveUtil;
import org.firstinspires.ftc.teamcode.RASI.Rasi.RasiInterpreter;
import org.firstinspires.ftc.teamcode.RASI.RasiCommands.RobotV1Commands;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.IntakeV2;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Vision.MyGoldDetector;
import org.firstinspires.ftc.teamcode.Vision.Positions;

/**
 * implement filename to return the filename
 * ovverride doVision() to return false if you don't want to do vision
 * */
public abstract class BasicRasiAuto extends LinearOpMode {
    MyGoldDetector detector;

    public boolean doVision(){
        return true;
    }

    public void runOpMode(){
        // init robot.
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
        sendTelemetry("Drivebase created");
        IntakeV2 intake = new IntakeV2(hardwareMap, this, true);
        sendTelemetry("Intake created");
        CubeLift lift = new CubeLift(hardwareMap, true);
        sendTelemetry("CubeLift created");
        Gyro gyro = new Gyro(hardwareMap);
        sendTelemetry("Gyro created");
        FTCRobotV1 robot = new FTCRobotV1(drivebase,gyro,telemetry,lift,intake);
        //sendTelemetry("Robot created");
        DriveUtil pidDrive = new DriveUtil(robot,this);
        //sendTelemetry("starting vision...");
        // wait to begin opMode
        RobotV1Commands rc = new RobotV1Commands(this, robot);
        RasiInterpreter rasiInterpreter = new RasiInterpreter("/sdcard/FIRST/team9773/MercurialRasi/", fileName(), this, rc);

        intake.setBucketServoToStartPos();

        if (doVision()) {
            // run vision
            detector = new MyGoldDetector();
            // init the vision
            detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 0);
            detector.enable();
            Positions goldPosition = Positions.center;
            int i = 0;
            while (!opModeIsActive() && !isStopRequested()) {
                Positions pos = detector.getPosition();
                if (pos != null) goldPosition = pos;
                telemetry.addData("VisionReading", goldPosition.toString());
                telemetry.update();
                i++;
            }
            // pass tags to RASI
            String[] tags = new String[1];
            tags[0] = Character.toString(goldPosition.toString().charAt(0)).toUpperCase();
            rasiInterpreter.setTags(tags);
            sendTelemetry("Set tags to " + tags[0]);
            Log.d("RasiAuto", "Set tag to " + tags[0]);
        }
        //rasiInterpreter.runRasiActually();
        sendTelemetry("Waiting for start");
        waitForStart();
        if (doVision()){
            detector.disable();
        }
        // DO EVERYTHING
        rasiInterpreter.runRasiActually();
        //rasiInterpreter.run();

        robot.stop();
    }


    private void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }

    public abstract String fileName();

}

