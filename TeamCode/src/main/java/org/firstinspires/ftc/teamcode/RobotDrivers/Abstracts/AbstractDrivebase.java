package org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts;

import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

/**
 * Defines the functionality that must be implemented by all drivebases
 * @author Princess Clarence Weedle
 * */
public abstract class AbstractDrivebase {

    /**
     * Set the speed at which the robot goes foward
     * @param pow double from -1 to 1: Speed foward
     * */
    public abstract void setForwardBackPower(double pow);

    /**
     * Set the speed at which the robot goes sideways
     * @param pow double from -1 to 1: Speed right
     * */
    public abstract void setLeftRightPower(double pow);

    /**
     * Drive according to vector, and rotate according to rotate Angle. X direction is left-right
     * Y direction is forward-back
     * @param driveVector Vector of the form [inches in X direction, inches in Y direction]
     * @param rotateAngle angle in radians which to rotate the robot
     * */
    public abstract void driveDist(Vector driveVector, double rotateAngle);

    /**
     * Rotate the robot a specific angle
     * @param angle the angle by which to rotate the robot
     * */
    public abstract void turnAngle(double angle);

    /**
     * Rotate the robot at a set speed
     * @param pow speed at which to rotate the robot.
     * */
    public abstract void turnPow(double pow);
}
