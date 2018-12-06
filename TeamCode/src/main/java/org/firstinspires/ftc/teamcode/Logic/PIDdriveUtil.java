package org.firstinspires.ftc.teamcode.Logic;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

import com.qualcomm.robotcore.util.Range;

/**
 * The PID drive util class creates and holds various methods for driving
 * This program is made to be able to be used in various circumstances,
 * <p>
 * This program is made in the paradigm followed by the rest of the robot drivers. Between each loop,
 * it calls the robot.update(); method, allowing other processes to happen simultaneously.
 *
 * @author  Zachary Eichenberger , -ftc robocracy 9773
 * @version 1.0
 * */

public class PIDdriveUtil {

    static final String TAG = "ftc_9773_drivePID";
    static final boolean scalingClip = false;
    //operator things;
    FTCRobotV1 robot;
    LinearOpMode opMode;
    MecanumDrivebase drivebase;
    Gyro gyro;

    // information
    double ticksPerInch;

    // Json
    SafeJsonReader json = new SafeJsonReader("DrivePidVals");
    // pid coeffs
    static double[] distPidCoeffs = new double[3];
    double distTol;
    PIDController distPid;
    static double[] rotPidCoeffs = new double[3];
    double rotTol, rotExitSpeed;
    PIDController rotPid;
    static double[] headingPidCoeffs = new double[3];
    PIDController headingPid;

    /**
     * Constructor for the PID drive Util class.
     * initializes all of the parts, and reads values from JSON
     *
     * takes the robot and linear opMode classes, used to initialize and eventuallly update stuff
     * @param robot
     * @param opMode
     */
    public PIDdriveUtil(FTCRobotV1 robot, LinearOpMode opMode){
        this.robot = robot;
        this.opMode = opMode;
        drivebase = robot.drivebase;
        gyro = robot.gyro;
        ticksPerInch = drivebase.COUNTS_PER_INCH;

        //todo: update with actual values
        //  distPid
        distPidCoeffs[0] = json.getDouble("distKp",0.028);
        distPidCoeffs[1] = json.getDouble("distKi", 0.0);
        distPidCoeffs[2] = json.getDouble("distKd", 0.17);
        distPid = new PIDController(distPidCoeffs[0],distPidCoeffs[1], distPidCoeffs[2]);
        distTol = json.getDouble("distTol", 0.5);
        //  rotPid
        rotPidCoeffs[0] = json.getDouble("rotKp",0.11);
        rotPidCoeffs[1] = json.getDouble("rotKi", 0);
        rotPidCoeffs[2] = json.getDouble("rotKd", 0.15);
        rotPid = new PIDController(rotPidCoeffs[0],rotPidCoeffs[1], rotPidCoeffs[2]);
        rotTol = json.getDouble("rotTol", 0.0174533);// currently 1 degree
        rotExitSpeed = json.getDouble("rotExitSpeed", 0.0872665); // currently 5º/sec
        //  rotPid
        headingPidCoeffs[0] = json.getDouble("headingKp",0.1);
        headingPidCoeffs[1] = json.getDouble("headingKi", 0);
        headingPidCoeffs[2] = json.getDouble("headingKd", 0);
        headingPid = new PIDController(headingPidCoeffs[0],headingPidCoeffs[1], headingPidCoeffs[2]);



    }
     public void driveDistStraight(double dist, double power){
        drivePolar(dist, gyro.getHeading(), power);
     }
     public void drivePolar(double distance, double theta, double power ) { // may be less accurate
         double initialHeading = gyro.getHeading();
         distPid.resetPID();
         while (opMode.isStopRequested()) {
             double error = distance - getAverageDistInches();
             double correction = distPid.getPIDCorrection(error);
             // might change this
             if(scalingClip) {
                 // scales continuously
                 correction = Range.clip(correction, -1, 1);
                 correction*= power;
             } else {
                 // otherwise just clip
                 correction = Range.clip(correction, -power, power);
             }
             //logs
             Log.d(TAG+" error", Double.toString(error));
             Log.d(TAG+" pow", Double.toString(correction));

             driveHoldHeading(correction, theta, initialHeading);
             if(Math.abs(error) <= Math.abs(distTol))
                 break;

         }
     }
     private double getAverageDistInches(){
        long[] dists = drivebase.getMotorPositions();
        double sum = 0;
        for(Long l: dists){
            sum += (l / ticksPerInch);
        }
        return sum / dists.length;
     }
    /**
     * a semi-internal funciton used to keep the robot pointed in a certian direction while driving.
     * @param magnitude driving function magnitude in motor power (i.e. on range -1.0 to 0.0 to 1.0)
     * @param angle the angle at which the robot is desired to drive in radians.
     * @param heading the heading that the robot is desired to face.
     */
     public void driveHoldHeading (double magnitude, double angle, double heading){
         double currHeading = gyro.getHeading();
         double correction = headingPid.getPIDCorrection(heading - currHeading);
         drivebase.drivePolar(magnitude, angle, correction, false);
     }

    /**
     * A function that turns the robot to a certian feild centric position
     *  runs a loop while it can, and exits once at the correct position
     * @param goalHeading the indended feild centric heading in degrees.
     */
     public void turnToAngle(double goalHeading) {


         final double targetAngleRad = goalHeading;

         // For calculating rotational speed:
         double lastHeading;
         double currentHeading = gyro.getHeading();

         double lastTime;
         double currentTime = System.currentTimeMillis();

         // For turning PID
         double error;

         boolean firstTime = true;
         while (!opMode.isStopRequested()) {

             // update time and headings:
             lastHeading = currentHeading;
             currentHeading = gyro.getHeading();

             lastTime = currentTime;
             currentTime = System.currentTimeMillis();

             error = setOnNegToPosPi(targetAngleRad - currentHeading);

             double rotation = rotPid.getPIDCorrection(error);
             Log.e("Error: ", "" + error);

             Log.e(" Rotation Pow: ", "" + rotation);

             // may add this in if dt is too weak
//             if (rotation > 0) {
//                 rotation += baseSpeed;
//             } else if (rotation < 0) {
//                 rotation -= baseSpeed;
//             }

             drivebase.drivePolar(0.0, 0, rotation, false);

             // Check to see if it's time to exit
             // Calculate speed
             double speed;
             if (currentTime == lastTime || firstTime) {
                 speed = 0.003;
             } else {
                 speed = Math.abs(error) / (currentTime - lastTime);
             }

             if (speed < rotExitSpeed && error < rotTol) {
                 break;
             }
             firstTime = false;
             // update robot
             robot.update();
         }
        rotPid.resetPID();
     }

    private double setOnNegToPosPi (double num) {
        while (num > Math.PI) {
            num -= 2*Math.PI;
        }
        while (num < -Math.PI) {
            num += 2*Math.PI;
        }
        return num;
    }


}
