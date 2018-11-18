package org.firstinspires.ftc.teamcode.RobotDrivers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractDrivebase;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;

/**
 * @author Cadence
 * For controlling a mecanum drive
 * */
public class MecanumDrivebase extends AbstractDrivebase {
    private HardwareMap hwmp;

    private static final double ROTATION_CONSTANT = 5.207;// Robot radius / wheel radius. Needs to be found
    private static double MAX_MOTOR_SPEED = 1;
    private MotorDriver[] motors = new MotorDriver[4];
    private double motorPowers[] = new double[4];

    private boolean fieldCentric;
    private boolean isTempCall = true;

    private Vector driveVector = new Vector(true, 0,0);

    /**
     * @param hwmp Hardware map defined in the opmode
     * @param mn0 Name of the front left motor
     * @param mn1 Name of the front right motor
     * @param mn2 Name of the back left motor
     * @param mn3 Name of the back right motor
     * */
    public MecanumDrivebase(HardwareMap hwmp, String mn0, String mn1, String mn2, String mn3){
        this.hwmp = hwmp;

        // Initialize the wheels
        motors[0] = new MotorDriver(hwmp, mn0);
        motors[1] = new MotorDriver(hwmp, mn1);
        motors[2] = new MotorDriver(hwmp, mn2);
        motors[3] = new MotorDriver(hwmp, mn3);

        motorPowers[0] = 0;
        motorPowers[1] = 0;
        motorPowers[2] = 0;
        motorPowers[3] = 0;

        fieldCentric = false;

    }

    /**
     * Toggle whether or not to use fieldCentric mode
     * @param state whether or to use fieldCentric
     * */
    public void setFieldCentric(boolean state){this.fieldCentric = state;}

    /**
     * @param speed Speed to move forward at
     * */
    public void setForwardBackPower(double speed){
        //Use x direction
        if (fieldCentric && !isTempCall){
            Vector velocityVector = new Vector(true, speed, 0);
            this.setVelocity(velocityVector);

            this.isTempCall = true;
            return;
        }
        double maxMax = 1;
        for (int i=0;i < 4; i++){
            MotorDriver motor = motors[i];
            double currPow = motor.getPow();
            currPow += speed;
            if (Math.abs(currPow) > maxMax){maxMax = Math.abs(currPow);}
            motorPowers[i] = currPow;
        }
        double scaleFactor = 1 / maxMax;
        for (int i=0;i<4;i++){
            motorPowers[i] = motorPowers[i] * scaleFactor;
            motors[i].setPow(motorPowers[i]);
        }
    }

    /**
     * @param speed speed to move right at.
     * */
    public void setLeftRightPower(double speed){
        //Use y direction
        if (fieldCentric && !isTempCall){
            Vector velocityVector = new Vector(true, speed, 0);
            this.setVelocity(velocityVector);
            this.isTempCall = true;
            return;
        }
        double maxMax = 1;
        for (int i=0;i < 4; i++){
            MotorDriver motor = motors[i];
            double currPow = motor.getPow();
            currPow += speed * ((i % 2 == 0) ? -1 : 1);
            if (Math.abs(currPow) > maxMax){maxMax = Math.abs(currPow);}
            motorPowers[i] = currPow;
        }
        double scaleFactor = 1 / maxMax;
        for (int i=0;i<4;i++){
            motorPowers[i] = motorPowers[i] * scaleFactor;
            motors[i].setPow(motorPowers[i]);
        }
    }

    public void setVelocity(Vector velocityVector){
        if(fieldCentric) {
            //velocityVector.rotateVector(-myGyro.getHeading());
        }
        isTempCall = true;
        this.setForwardBackPower(velocityVector.getX());
        this.setLeftRightPower(velocityVector.getY());
        isTempCall = false;
    }

    /**
     * @param driveVector distance to drive in x and y direction
     * */
    @Override
    public void driveDist(Vector driveVector, double rotationRadians) {

    }

    @Override
    public void turnAngle(double angle) {

    }
    @Override
    public void turnPow(double pow){

    }
    public void zero(){
        for (MotorDriver motor:motors) {
            motor.setPow(0);
        }
    }

}
