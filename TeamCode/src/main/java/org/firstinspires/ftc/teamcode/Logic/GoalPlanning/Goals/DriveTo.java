package org.firstinspires.ftc.teamcode.Logic.GoalPlanning.Goals;

import org.firstinspires.ftc.teamcode.Logic.GoalPlanning.Goals.Goal;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;

public class DriveTo extends Goal {
    public final FLAGS flag = FLAGS.DRIVE_TO;
    public Point toPoint;
    public double heading;
    public double toPointTol;
    public double headingTol;
    public DriveTo(Point toPoint, double heading) {this(toPoint,heading,0.,0.);}
    public DriveTo(Point toPoint, double heading, double toPointTol, double headingTol) {
        this.toPoint = toPoint;
        this.heading = heading;
        this.toPointTol = toPointTol;
        this.headingTol = headingTol;
    }
}
