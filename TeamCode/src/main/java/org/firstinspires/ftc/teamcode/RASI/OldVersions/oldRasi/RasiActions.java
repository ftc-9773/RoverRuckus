package org.firstinspires.ftc.teamcode.RASI.OldVersions.oldRasi;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Logic.DriveUtil;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;

/**
 * Created by vikesh on 1/5/18.
 */

public class RasiActions {
    public RasiParser rasiParser;
    public FTCRobotV1 robot;
    public DriveUtil drive;
    private LinearOpMode opMode;

    // Init
    public RasiActions(String rasiFilename, String[] rasiTag, FTCRobotV1 robot, LinearOpMode opMode){
        this.opMode = opMode;
        this.robot = robot;
        this.drive = new DriveUtil(robot, opMode);
        rasiParser = new RasiParser(rasiFilename,rasiTag);
        Log.i("RasiActions", "Done with Rasi init");
        }

    // Run the rasi commands
    public void runRasi() throws InterruptedException {
        rasiParser.loadNextCommand();
        while (!opMode.isStopRequested()) {
            Log.i("RasiActions", "Started new loop!");
            Log.i("RasiActions", "Parameter - " + rasiParser.getParameter(0));
            switch (rasiParser.getParameter(0)) {
                // put all your commands here
                case "driveQuick":
                    drive.driveQuick(rasiParser.getAsDouble(1), rasiParser.getAsDouble(2));
                    break;
                case "driveEncoder":
                    break;
                case "drive":
                    drive.driveDistStraight(rasiParser.getAsDouble(1), rasiParser.getAsDouble(2));
                    break;
                case"turn":
                    drive.turnToAngle(rasiParser.getAsDouble(1));
                    break;
                case "drop":
                    robot.lift.unLatchStopper();
                    Wait(0.5);
                    robot.lift.goToHangPos();
                    break;
                case"liftLow":
                    robot.lift.goToLowPos();
                    break;
                case "extendIntake":
                    robot.intake.transferMinerals();
                    double dist = rasiParser.getAsDouble(1);
                    robot.intake.setPos(dist);
                    opMode.telemetry.addLine("Wrote dist " + dist + " to arm");
                    opMode.telemetry.update();
                    break;
                case "dropIntake":
                    robot.intake.setPos(5);
                    Wait(1);
                    break;
                case "wait":
                    Wait(rasiParser.getAsDouble(1));
                    break;
                case "strafeTime":
                    drive.strafeTime(rasiParser.getAsDouble(1), rasiParser.getAsDouble(2));
                    break;
                case "deposit":
                    robot.lift.setLefScoreSide();
                    robot.lift.dump();
                    break;
                case "end":
                    robot.stop();
                    opMode.requestOpModeStop();
                    return;
                case "driveTime":
                    robot.drivebase.drive(0, rasiParser.getAsDouble(2), 0, false);
                    Wait(rasiParser.getAsDouble(1));
                    robot.drivebase.stop();
                    break;
                case "driveMP":
                    drive.driveStraight(rasiParser.getAsDouble(1));
                    break;
                default:
                    break;
            }
            rasiParser.loadNextCommand();
        }
    }

    public void Wait(double timeInSeconds){
        System.out.println("Waiting for " + timeInSeconds);
        long startTime = System.currentTimeMillis();
        while(startTime + timeInSeconds*1000 > System.currentTimeMillis() && !opMode.isStopRequested()){
            robot.update();
        }
    }

}
