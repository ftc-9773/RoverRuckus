package org.firstinspires.ftc.teamcode.RobotDrivers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.MotorSpeedControl;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.SmartGyro;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

public class MecanumDrivebase extends AbstractMethodError {
    private HardwareMap hwmp;

    private static final double ROTATION_CONSTANT = 5.207;
    private static double MAX_MOTOR_SPEED = 10;
    private MotorSpeedControl[] motors;
    private double motorPowers[];
    private SmartGyro myGyro;

    private boolean fieldCentric;

    private Vector currDriveVector = new Vector(true, 0,0);


    public MecanumDrivebase(HardwareMap hwmp){
        this.hwmp = hwmp;

        // Initialize the wheels
        motors[0] = new MotorSpeedControl(hwmp, "flwheel");
        motors[1] = new MotorSpeedControl(hwmp, "frwheel");
        motors[2] = new MotorSpeedControl(hwmp, "blwheel");
        motors[3] = new MotorSpeedControl(hwmp, "brwheel");

        motorPowers[0] = 0;
        motorPowers[1] = 0;
        motorPowers[2] = 0;
        motorPowers[3] = 0;

        myGyro = new SmartGyro(hwmp);
        fieldCentric = false;

    }
    public void setFieldCentric(boolean state){this.fieldCentric = state;}

    public void setFowardBackPower(double pow){
        //Use x direction
        this.currDriveVector.set(true, pow * 10, currDriveVector.getY());

    }

    public void setLeftRightPower(double pow){
        this.currDriveVector.set(true, currDriveVector.getX(), pow * 10);
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
    }
    
    public void update(){
        for (int i=0; i<4; i++){motors[i].setSpeed(motorPowers[i]);}
    }

}
