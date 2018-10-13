package org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts;

import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Path;
import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractRobot;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;

public abstract class AbstractPathfinder {
     AbstractRobot robot = null;
     AbstractEnvironment environment = null;

     public abstract Path getPath(Point toPoint, double heading);

     public abstract Path getPath(Point toPoint, double heading, double toPointTolerance, double headingTolerance);

}
