package org.firstinspires.ftc.teamcode.RobotDrivers;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.VerticalLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.OdometryController;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;

import java.util.Arrays;

public class FTCRobotV1 {
    MecanumDrivebase drivebase;
    Intake intake;
    OdometryController OC;
    Gyro gyro0;

    VerticalLift lift;

    public double heading = 0;
    Telemetry telemetry;
    Point pos;
    double x = 0;
    double y = 0;

    public FTCRobotV1(MecanumDrivebase drivebase, Gyro gyro, Telemetry telemetry) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        //this.OC = odometryController;
        this.gyro0 = gyro;
        gyro0.setZeroPosition();
        this.telemetry = telemetry;
    }

    public FTCRobotV1(MecanumDrivebase drivebase, Gyro gyro, OdometryController odometryController, Telemetry telemetry, VerticalLift lift) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        this.OC = odometryController;
        this.gyro0 = gyro;
        gyro0.setZeroPosition();
        this.telemetry = telemetry;
        this.lift = lift;
    }

    public void driveVelocity(double xV, double yV, double rotV){
        drivebase.arcadeDrive(xV, yV, rotV);
    }

    public void driveDist(double x, double y, double rotation, double speed){
        drivebase.driveDist(x, y, rotation, speed);
        this.pos.xCord += x;
        this.pos.yCord += y;
        this.heading += rotation;
        //update();
    }

    public Point getPos(){
        //update();
        return this.pos;
    }

    public double getHeading() {
        //update();
        return heading;
    }

    public void update(){
        /*double[] ocPos = this.OC.getPosition();
        this.x = ocPos[0];
        this.y = ocPos[1];
        this.pos = new Point(x, y);
        this.heading = ocPos[2];
        */if (this.gyro0.isUpdated()){this.heading = gyro0.getHeading(true);}
        //telemetry.addData("Got Positions from OC:", Arrays.toString(ocPos));
        if (gyro0.isUpdated()) telemetry.addData("Got heading from Gyro", this.heading);
        telemetry.update();
    }

    public void setLiftPerc(double pow){this.lift.setPerc(pow);}

    public void setLiftVel(double vel){this.lift.setSpeed(vel);}

    public void setLiftPos(int pos){this.lift.setPos(pos);} //Uses encoder position
}
