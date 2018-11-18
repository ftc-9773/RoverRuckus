package org.firstinspires.ftc.teamcode.RobotDrivers;


import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractIntake;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

public class MecanumRobot {
    MecanumCont drivebase;
    AbstractIntake intake;
    //OdometryController OC;
    Gyro gyro0;

    public double heading = 0;

    Point pos;

    public MecanumRobot(MecanumCont drivebase, Gyro gyro) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        //this.OC = odometryController;
        this.gyro0 = gyro;
        gyro0.setZeroPosition();
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
        this.heading = gyro0.getHeading();
    }


}
