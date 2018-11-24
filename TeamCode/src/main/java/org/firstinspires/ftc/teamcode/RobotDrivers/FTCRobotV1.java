package org.firstinspires.ftc.teamcode.RobotDrivers;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.VerticalLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.OdometryController;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

import java.util.Arrays;

/**
 * @author cadence
 * @version 1.0
 * */
public class FTCRobotV1 {
    //Attachments
    MecanumDrivebase drivebase;
    Intake intake;
    OdometryController OC;
    VerticalLift lift;

    //sensors
    Gyro gyro;

    //state
    double heading = 0;
    Telemetry telemetry;
    Point pos;

    //helpers
    boolean usingOdometry = false;
    boolean usingGyros = false;

    //Tracking position
    double lastTime;
    Vector lastVelocity;
    double headingVelocity;


    public FTCRobotV1(MecanumDrivebase drivebase,  Telemetry telemetry) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        this.telemetry = telemetry;
        this.lastTime = System.currentTimeMillis();
        this.lastVelocity = new Vector(0,0);
        this.headingVelocity = 0;
    }

    public FTCRobotV1(MecanumDrivebase drivebase, Gyro gyro, Telemetry telemetry) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        this.gyro = gyro;
        this.gyro.setZeroPosition();
        this.telemetry = telemetry;
        usingGyros = true;
    }

    public FTCRobotV1(MecanumDrivebase drivebase, Gyro gyro, OdometryController odometryController, Telemetry telemetry, VerticalLift lift) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        this.OC = odometryController;
        this.gyro = gyro;
        this.gyro.setZeroPosition();
        this.telemetry = telemetry;
        this.lift = lift;
        usingGyros = true;
        usingOdometry = true;
    }

    public void driveVelocity(double xV, double yV, double rotV){
        drivebase.arcadeDrive(xV, yV, rotV);
        if(!usingGyros && !usingOdometry) {
            Point temp = new Point(this.pos.xCord, this.pos.yCord);
            this.pos = temp;
        }
    }

    public void driveDist(double x, double y, double rotation, double speed){
        drivebase.driveDist(x, y, rotation, speed);
        this.pos.xCord += x;
        this.pos.yCord += y;
        this.heading += rotation;
    }

    public Point getPos(){
        return this.pos;
    }

    public double getHeading() {
        return heading;
    }

    public void update(){
        if (usingOdometry) {
            double[] ocPos = this.OC.getPosition();
            double x = ocPos[0];
            double y = ocPos[1];
            this.pos = new Point(x, y);
            this.heading = ocPos[2];
            telemetry.addData("Got Positions from OC:", Arrays.toString(ocPos));
        }
        if (usingGyros) {
            if (this.gyro.isUpdated()) {
                this.heading = gyro.getHeading(true);
                telemetry.addData("Got heading from Gyro", this.heading);
            }
        }
    }

    public void setLiftPerc(double pow){this.lift.setPerc(pow);}

    public void setLiftVel(double vel){this.lift.setSpeed(vel);}

    public void setLiftPos(int pos){this.lift.setPos(pos);} //Uses encoder position
}
