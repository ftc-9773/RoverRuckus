package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes.OldAuton;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.Logic.DriveUtil;
import org.firstinspires.ftc.teamcode.Logic.oldRasi.RasiActions;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.IntakeV2;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.misc.Timer;
import org.firstinspires.ftc.teamcode.Vision.MyGoldDetector;
import org.firstinspires.ftc.teamcode.Vision.Positions;

public abstract class BasicRasiAuton extends LinearOpMode {
    Timer driveTimer;
    MyGoldDetector detector;



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

        RasiActions rasiActions = new RasiActions(fileName(), null, robot, this);

        // run vision
        detector = new MyGoldDetector();
        // init the vision
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(),0);
        detector.enable();
        Positions goldPosition = Positions.center;
        while (!opModeIsActive() && !isStopRequested()) {
            Positions pos = detector.getPosition();
            if (pos!= null) goldPosition = pos;
            telemetry.addData("VisionReading", goldPosition.toString());
            telemetry.update();
        }
        // pass tags to RASI
        String [] tags = new String[1];
        tags[0] = Character.toString(goldPosition.toString().charAt(0)).toUpperCase();
        Log.d("RASI AUTO", "Got tag " + tags[0]);
        rasiActions.rasiParser.rasiTag = tags;

        waitForStart();

        detector.disable();
        // DO EVERYTHING

        try {
            rasiActions.runRasi();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.stop();
    }


    private void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }

    public abstract String fileName();


}