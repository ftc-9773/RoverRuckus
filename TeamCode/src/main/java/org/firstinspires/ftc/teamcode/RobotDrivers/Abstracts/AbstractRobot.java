package org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts;

public abstract class AbstractRobot {
    AbstractDrivebase drivebase;
    AbstractLift lift;
    AbstractIntake intake;
    AbstractScorer scorer;

    public abstract void setFowardPow(double pow);
    public abstract void turn(double angle);
    public abstract void none();
}
