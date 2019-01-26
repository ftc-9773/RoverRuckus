package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.Logic.PIDdriveUtil;
import org.firstinspires.ftc.teamcode.RASI.Rasi.RasiInterpreter;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.misc.Timer;
import org.firstinspires.ftc.teamcode.Vision.MyGoldDetector;
import org.firstinspires.ftc.teamcode.Vision.Positions;

public abstract class BasicRasiAuton extends LinearOpMode {
    Timer driveTimer;
    MyGoldDetector detector;

    //Override if you don't want to run the camera
    public boolean doVision(){
        return true;
    }

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
        //sendTelemetry("Robot created");
        PIDdriveUtil pidDrive = new PIDdriveUtil(robot,this);
        //sendTelemetry("starting vision...");
        // wait to begin opMode

        RasiInterpreter rasiInterpreter = new RasiInterpreter("/sdcard/FIRST/team9773/rasi19/", fileName(), this, robot);

        if (doVision()) {
            // run vision
            detector = new MyGoldDetector();
            // init the vision
            detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 0);
            detector.enable();
            Positions goldPosition = Positions.center;
            while (!opModeIsActive() && !isStopRequested()) {
                Positions pos = detector.getPosition();
                if (pos != null) goldPosition = pos;
                telemetry.addData("VisionReading", goldPosition.toString());
                telemetry.update();
            }
            // pass tags to RASI
            String[] tags = new String[1];
            tags[0] = Character.toString(goldPosition.toString().charAt(0)).toUpperCase();
            rasiInterpreter.setTags(tags);
        }
        rasiInterpreter.preproccess();
        sendTelemetry("Wating for start");
        waitForStart();
        if (doVision()){
        detector.disable();
        }
        // DO EVERYTHING

        rasiInterpreter.run();

        robot.stop();
    }


    private void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }

    public abstract String fileName();


}
