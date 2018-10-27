package org.firstinspires.ftc.teamcode.RobotDrivers.OldHardwareControl;

import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

@Deprecated
public class DriveStraight {

    // Useful objects / PID stuff
    PIDController turningPID;
    PIDController drivingPID;
    private double baseSpeed;
    private double errorThreshold;
    private double speedThreshold;
    private static SmartGyro mySmartGyro;


    private boolean toggleSpeedPID = false;

    public DriveStraight (MecanumDrive myMecanumDrive) {
        SafeJsonReader driveStraightJSON = new SafeJsonReader("DriveStaightPID");

        // Get values for turning PID from JSON files
        double Kp = driveStraightJSON.getDouble("Kp");
        double Ki = driveStraightJSON.getDouble("Ki");
        double Kd = driveStraightJSON.getDouble("Kd");
        baseSpeed = driveStraightJSON.getDouble("min");
        turningPID = new PIDController(Kp, Ki, Kd);
        errorThreshold = driveStraightJSON.getDouble("errorThreshold");
        speedThreshold = driveStraightJSON.getDouble("speedThreshold");

        // Get values for drive straight PID from JSON files
        double driveKp = driveStraightJSON.getDouble("driveKp");
        double driveKi = driveStraightJSON.getDouble("driveKi");
        double driveKd = driveStraightJSON.getDouble("driveKd");

    }

    public void opModeRunning() {

        // Target Angle in Radians
        double targetAngle = 0;

        /* Turning PID error value and correction
        * double error = SmartGyro.getImuAngularVelocity() - targetAngle;
        */

    }

}
