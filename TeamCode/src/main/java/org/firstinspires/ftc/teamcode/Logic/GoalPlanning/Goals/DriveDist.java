package org.firstinspires.ftc.teamcode.Logic.Goalplanning.Goals;

import org.firstinspires.ftc.teamcode.Logic.GoalPlanning.Goals.Goal;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

import static org.firstinspires.ftc.teamcode.Logic.GoalPlanning.Goals.Goal.FLAGS.DRIVE_DIST;

public class DriveDist extends Goal {
    public final FLAGS flag = DRIVE_DIST;
    public Vector driveVector;
    public double heading;
    public double distTol;
    public double headingTol;

    public DriveDist(double driveVector, double heading) {this(driveVector, heading, 0.5, 0.05)}

    public DriveDist(Vector driveVector, double heading, double distTol, double headingTol) {
        this.driveVector = driveVector;
        this.heading = heading;
        this.distTol = distTol;
        this.headingTol = headingTol;
    }
}
