package org.firstinspires.ftc.teamcode.RobotDrivers.OldHardwareControl;


import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

@Deprecated
public class MecanumDrive {

    private static final String TAG = "9773 Mecanum Drive";
    private static final boolean DEBUG = false;

    private SmartGyro myGyro;

    private MotorSpeedControl[] motors;
    private static double ROTATION_CONSTANT = 5.207; // Robot Radius / Wheel Radius
    private static double MAX_MOTOR_SPEED = 10;

    private boolean fieldCentric;


    MecanumDrive(HardwareMap hwMap, boolean fieldCentric) {

        // Initialize the wheels
        motors[0] = new MotorSpeedControl(hwMap, "flwheel");
        motors[1] = new MotorSpeedControl(hwMap, "frwheel");
        motors[2] = new MotorSpeedControl(hwMap, "blwheel");
        motors[3] = new MotorSpeedControl(hwMap, "brwheel");

        // Initialize the gyro
        myGyro = new SmartGyro(hwMap);
    }

    public void drive(Vector driveVector, double rotationRadians) {

        // Shift to field centric if enabled
        if (fieldCentric) {
            driveVector.rotateVector(-myGyro.getHeading());
        }

        // Driving Calculations:
        double x = driveVector.getX();
        double y = driveVector.getY();

        // Calculate motor powers
        double[] motorPowers = new double[4];

        motorPowers[0] = x + y + ROTATION_CONSTANT * rotationRadians;
        motorPowers[1] = -x + y - ROTATION_CONSTANT * rotationRadians;
        motorPowers[2] = -x + y + ROTATION_CONSTANT * rotationRadians;
        motorPowers[3] = x + y - ROTATION_CONSTANT * rotationRadians;

        // Check none are over MAX_MOTOR_SPEED
        double maxValue = Math.max(Math.max(motorPowers[0], motorPowers[1]), Math.max(motorPowers[2], motorPowers[3]));
        double minValue = Math.min(Math.min(motorPowers[0], motorPowers[1]), Math.min(motorPowers[2], motorPowers[3]));

        double maxMax = Math.max(maxValue, -minValue);

        // Proportionally adjust for inflation
        if (maxMax > MAX_MOTOR_SPEED) {
            for (double i : motorPowers) { i /= maxMax; }
        }

        // Set motor speeds
        for (int i = 0; i < 4; i++) { motors[i].setSpeed(motorPowers[i]); }
    }



}
