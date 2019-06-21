package org.firstinspires.ftc.teamcode.RASI.RasiCommands;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Logic.DriveUtil;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;

public class RobotV1Commands extends RasiCommands {
    FTCRobotV1 robert;
    LinearOpMode opMode;
    Telemetry telemetry;
    DriveUtil driver;

    public RobotV1Commands(LinearOpMode o, FTCRobotV1 r) {
        super(o);
        this.robert = r;
        this.opMode = o;
        this.telemetry = o.telemetry;
        this.driver = new DriveUtil(robert, opMode);
    }

    @Deprecated
    public void driveQuick(double dist, double speed){
        driver.driveQuick(dist, speed);
    }

    @Deprecated
    public void driveEncoder(double dist, double speed){
        driver.driveDistStraight(dist, speed);
    }

    @Deprecated
    public void drive(double dist, double speed){
        driver.driveDistStraight(dist, speed);
    }

    public void turn(double angle){
        driver.turnToAngle(angle);
        Log.d("RASI-RV1", "Turned to angle " + angle);
    }

    public void drop(){
        robert.lift.unLatchStopper();
        Wait(0.5);
        robert.lift.goToHangPos();
    }

    public void unlatchHook(){
        robert.lift.goToHangPos();
        robert.lift.update();
        robert.lift.openHookServo();
    }

    public void openHook(){
        robert.lift.openHookServo();
    }

    public void liftLow(){
        robert.lift.goToLowPos();
    }

    public void extendIntake(double dist){
        robert.intake.transferMinerals();
        robert.intake.setPos(dist);
        opMode.telemetry.addLine("Wrote dist " + dist + " to arm");
        opMode.telemetry.update();
    }

    public void retractIntake(){
        robert.intake.setPos(0);
    }

    public void intakeOn(){
        robert.intake.intakeOn();
    }

    public void intakeOff(){
        robert.intake.intakeOff();
    }

    public void carry(){robert.intake.carryPos();}

    //Use this one. It is the motion profiling
    public void driveMP(double dist){
        driver.driveStraight(dist);
        Log.d("RASI-RV1", "Drove to dist " + dist);
    }

    public void dropMarker(){
        robert.lift.setLefScoreSide();
        robert.lift.dump();
    }

    public void resetServo(){
        robert.lift.stopDump();
        robert.lift.update();
    }


    public void dropIntake(){
        robert.intake.setPos(5);
        Wait(1);
    }

    public void strafeTime(double time, double power){
        driver.strafeTime(time, power);
    }


    public void stop(){
        robert.stop();
        opMode.requestOpModeStop();
    }

    @Deprecated
    public void driveTime(double x, double y, double t){
        robert.drivebase.drive(x, y, t, false);
        Wait(1);
        robert.drivebase.stop();
    }

    public void Wait(double time){
        long startTime = System.currentTimeMillis();
        while(startTime + time*1000 > System.currentTimeMillis() && !opMode.isStopRequested()){
            robert.update();
        }
    }

    public void strafeMP(double dist){
        driver.strafeStraight(dist);
    }

    public void killOpmode(){
        this.opMode.requestOpModeStop();
    }
}
