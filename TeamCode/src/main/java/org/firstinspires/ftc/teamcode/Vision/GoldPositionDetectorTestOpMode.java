package org.firstinspires.ftc.teamcode.Vision;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous (name = "CubeDetector" )
public class GoldPositionDetectorTestOpMode  extends LinearOpMode {
    Positions pos = Positions.unknown;
    MyGoldDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {
        // init stuff

        // init vision
        detector = new MyGoldDetector();
        // init the vision
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(),0);
        detector.enable();
        //detector.onCameraViewStarted(1920,1080);

        // read vision and send telemetry
        while(!isStopRequested() && !isStopRequested()){
            telemetry.addData("current position reading", detector.getPosition().toString());
            telemetry.update();
        }
        waitForStart();
        detector.returnMat = false;
        Thread.sleep(100);
        detector.disable();

    }
}
