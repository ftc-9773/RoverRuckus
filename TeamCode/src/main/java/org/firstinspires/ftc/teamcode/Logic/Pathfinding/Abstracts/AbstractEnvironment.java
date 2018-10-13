package org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts;
/**
 * THIS INTERFACE IS IN PROGRESS and is likely to CHANGE DRASTICALLY. Please wait to implement,
 * or expect to redo a lot of work.
 * */

import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractRobot;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;

/**
 * @author Cadence
 * @version 2018-07-22
 *
 * Creates a model of the playing field
 * */
public interface AbstractEnvironment {
    AbstractRobot robot = null;

    /**Checks whether a Point P is an obstacle
     *
     * */
    public boolean isObstacle(Point point);

    /**Checks whether the region defined by p1 and p2 contains an obstacle
     *
     * */
    public boolean isObstacle(Point p1, Point p2);
}
