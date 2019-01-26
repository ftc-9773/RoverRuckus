package org.firstinspires.ftc.teamcode.RASI.RasiCommands;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Logic.PIDdriveUtil;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;

public class RobotV1Commands extends RasiCommands {
    FTCRobotV1 robert;
    LinearOpMode opMode;
    Telemetry telemetry;
    PIDdriveUtil driver;

    public RobotV1Commands(LinearOpMode o, FTCRobotV1 r) {
        super(o);
        this.robert = r;
        this.opMode = o;
        this.telemetry = o.telemetry;
        this.driver = new PIDdriveUtil(robert, opMode);
    }

    public void driveQuick(double dist, double speed){
        driver.driveQuick(dist, speed);
    }

    public void driveEncoder(double dist, double speed){
        driver.driveDistStraight(dist, speed);
    }

    public void drive(double dist, double speed){
        driver.driveDistStraight(dist, speed);
    }

    public void turn(double angle){
        driver.turnToAngle(angle);
    }

    public void drop(){
        robert.lift.unLatchStopper();
        Wait(0.5);
        robert.lift.goToHangPos();
    }

    public void liftLow(){
        robert.lift.goToLowPos();
    }

    public void extendIntake(double dist){
        robert.intake.setPos(dist);
        opMode.telemetry.addLine("Wrote dist " + dist + " to arm");
        opMode.telemetry.update();
    }

    public void driveMP(double dist){
        driver.driveStraight(dist);
    }
    public void dropIntake(){
        robert.intake.setPos(5);
        Wait(1);
    }

    public void strafeTime(double foo, double spam){
        driver.strafeTime(foo, spam);
    }

//    public void stop(){
//        robert.stop();
//        opMode.requestOpModeStop();
//    }

    public void driveTime(double x, double y, double t){
        robert.drivebase.drive(x, y, t, false);
        Wait(1);
        robert.drivebase.stop();
    }

}
