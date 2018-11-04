package org.firstinspires.ftc.teamcode.RobotDrivers;


import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractIntake;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

public class MecanumRobot {
    MecanumCont drivebase;
    AbstractIntake intake;

    public double heading = 0;

    Point pos;

    public MecanumRobot(MecanumCont drivebase) {
        this.pos = new Point( 0, 0);
        this.heading = 0;

        this.drivebase = drivebase;
    }

    public void driveVelocity(double xV, double yV, double rotV){
        drivebase.arcadeDrive(xV, yV, rotV);
    }

    public void driveDist(double x, double y, double rotation, double speed){
        drivebase.driveDist(x, y, rotation, speed);
        Vector temp = this.pos.toVector();
        temp.addVector(new Vector(true, x, y));
        this.pos = temp.toPoint();
        this.heading += rotation;
    }

    public Point getPos(){
        return this.pos;
    }


}
