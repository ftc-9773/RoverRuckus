package org.firstinspires.ftc.teamcode.Logic;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;
import org.firstinspires.ftc.teamcode.Utilities.misc.Timer;

import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;

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

    static final String TAG = "ftc9773_drivePID";
    static final boolean scalingClip = false;
    //operator things;
    FTCRobotV1 robot;
    LinearOpMode opMode;
    MecanumDrivebase drivebase;
    Gyro gyro;

    // information
    double ticksPerInch;

    // Json
    SafeJsonReader json;


    // pid coeffs
    static double[] distPidCoeffs = new double[3];
    final double minDistPow, minExitDist;
    final double maxTurnPower;
    double distTol;
    PIDController distPid;
    static double[] rotPidCoeffs = new double[3];
    double rotTol, rotExitSpeed;
    double rotMinPow;
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
        drivebase.runWithEncoders();
        gyro = robot.gyro;
        ticksPerInch = drivebase.COUNTS_PER_INCH;

        json = new SafeJsonReader("DrivePidVals");
        //  distPid
        distPidCoeffs[0] = json.getDouble("distKp",0.028);
        distPidCoeffs[1] = json.getDouble("distKi", 0.0);
        distPidCoeffs[2] = json.getDouble("distKd", 0.17);
        distPid = new PIDController(distPidCoeffs[0],distPidCoeffs[1], distPidCoeffs[2]);
        Log.d(TAG, "created dist PID controller pid coeff array: " + Arrays.toString(distPidCoeffs));
        distTol = json.getDouble("distTol", 0.5);
        minDistPow = json.getDouble("minDistPow", 0.2);
        minExitDist = json.getDouble("minExitDist", 0.02);
        maxTurnPower = json.getDouble("maxTurnPow", 0.6);

        //  rotPid
        rotPidCoeffs[0] = json.getDouble("rotKp",0.11);
        rotPidCoeffs[1] = json.getDouble("rotKi", 0);
        rotPidCoeffs[2] = json.getDouble("rotKd", 0.15);
        rotPid = new PIDController(rotPidCoeffs[0],rotPidCoeffs[1], rotPidCoeffs[2]);
        Log.d(TAG, "created rotation PID controller pid coeff array: " + Arrays.toString(rotPidCoeffs));
        rotTol = json.getDouble("rotTol", 0.0174533);// currently 1 degree
        rotExitSpeed = json.getDouble("rotExitSpeed", 0.2); // currently 5º/sec
        rotMinPow = json.getDouble("rotMinSpeed", 0.14);
        //  rotPid
        headingPidCoeffs[0] = json.getDouble("headingKp",0.1);
        headingPidCoeffs[1] = json.getDouble("headingKi", 0);
        headingPidCoeffs[2] = json.getDouble("headingKd", 0);
        headingPid = new PIDController(headingPidCoeffs[0],headingPidCoeffs[1], headingPidCoeffs[2]);
        Log.d(TAG, "created heading PID controller pid coeff array: " + Arrays.toString(headingPidCoeffs));

        drivebase.runWithoutEncoders();

    }
    public void driveQuick(double dist, double power){
        double initialHeading = gyro.getHeading();
        distPid.resetPID();
        long[] initialEncoderDists = drivebase.getMotorPositions();

        long lastCheckTime = System.currentTimeMillis();
        double lastDist = 0;

        drivebase.runWithEncoders();

        while (!opMode.isStopRequested()) {
            double error = dist - avgDistElapsedInches(initialEncoderDists);
            Log.d("encoder", "" + initialEncoderDists[0]);
            double correction = distPid.getPIDCorrection(error);
            Log.d(TAG,"error:" + error);
            Log.d(TAG, "correction" +correction);
            // might change this
            if(scalingClip) {
                // scales continuously
                correction = Range.clip(correction, -1, 1);
                correction*= power;
            } else {
                // otherwise just clip
                correction = Range.clip(correction, -power, power);
            }


            if(Math.abs(correction) < minDistPow){
                correction = Math.signum(correction)*Math.abs(minDistPow);
            }
            //logs
            Log.d(TAG+" error", Double.toString(error));
            Log.d(TAG+" pow", Double.toString(correction));


            driveHoldHeading(correction, 0, initialHeading);
            double distTraveled = Math.abs(avgDistElapsedInches(initialEncoderDists));
            Log.d(TAG, "at position: " + distTraveled);

            if( Math.signum(dist) * (distTraveled - dist) > -distTol)
                break;

            if (System.currentTimeMillis() - lastCheckTime > 300) {
                double currentDist = avgDistElapsedInches(initialEncoderDists);
                if (Math.abs(lastDist - currentDist) < minExitDist)
                    break;

                lastDist = currentDist;
                lastCheckTime = System.currentTimeMillis();
            }


        }
        drivebase.stop();
    }

     public void driveDistStraight(double dist, double power){
         double initialHeading = gyro.getHeading();
         distPid.resetPID();
         long[] initialEncoderDists = drivebase.getMotorPositions();

         long lastCheckTime = System.currentTimeMillis();
         double lastDist = 0;

         drivebase.runWithEncoders();

         while (!opMode.isStopRequested()) {
             double error = dist - avgDistElapsedInches(initialEncoderDists);
             Log.d("encoder", "" + initialEncoderDists[0]);
             double correction = distPid.getPIDCorrection(error);
             Log.d(TAG,"error:" + error);
             Log.d(TAG, "correction" +correction);
             // might change this
             if(scalingClip) {
                 // scales continuously
                 correction = Range.clip(correction, -1, 1);
                 correction*= power;
             } else {
                 // otherwise just clip
                 correction = Range.clip(correction, -power, power);
             }


             if(Math.abs(correction) < minDistPow){
                 correction = Math.signum(correction)*Math.abs(minDistPow);
             }
             //logs
             Log.d(TAG+" error", Double.toString(error));
             Log.d(TAG+" pow", Double.toString(correction));


             driveHoldHeading(correction, 0, initialHeading);
             double distTraveled = Math.abs(avgDistElapsedInches(initialEncoderDists));
             Log.d(TAG, "at position: " + distTraveled);

             if( Math.abs(distTraveled - dist) < distTol)
                 break;

             if (System.currentTimeMillis() - lastCheckTime > 300) {
                 double currentDist = avgDistElapsedInches(initialEncoderDists);
                 if (Math.abs(lastDist - currentDist) < minExitDist)
                     break;

                 lastDist = currentDist;
                 lastCheckTime = System.currentTimeMillis();
             }


         }
         drivebase.stop();
    }

    public void strafeTime(double time, double power) {
        Timer mytimer = new Timer(time);

        drivebase.drive(power, 0, 0, false);

        while(!mytimer.isDone() && !opMode.isStopRequested()) {
            robot.update();
            Log.i("ftc9773_Strafe_Time", "timerTime = " + mytimer.timeElapsedSeconds());
        }
        drivebase.stop();
    }

    public double avgDistElapsedInches(long[] initpositions){
         long[] positions = drivebase.getMotorPositions();
         double sum=0;
         for(int i = 0; i<4; i++){
             double diff = (double) positions[i] - initpositions[i];
             Log.d(TAG, "adding motor position: " +i+" ticks: " + positions[i]  + " diff:" + diff);
             if(i==0 ||i==2) diff *=-1;
             sum +=diff;
         }
          sum /=4;
        sum /= drivebase.COUNTS_PER_INCH;
        return sum ;
    }

    // todo: implement this using the kinematics described in this paper:
    //    public void drivePolar(double distance, double theta, double power ) { // may be less accurate
//         double initialHeading = gyro.getHeading();
//         distPid.resetPID();
//         initialEncoderDists = drivebase.getMotorPositions();
//         while (!opMode.isStopRequested()) {
//             double error = distance - getAverageDistInches();
//             double correction = distPid.getPIDCorrection(error);
//             Log.d(TAG,"error:" + error);
//             Log.d(TAG, "correction" +correction);
//             // might change this
//             if(scalingClip) {
//                 // scales continuously
//                 correction = Range.clip(correction, -1, 1);
//                 correction*= power;
//             } else {
//                 // otherwise just clip
//                 correction = Range.clip(correction, -power, power);
//             }
//             //logs
//             Log.d(TAG+" error", Double.toString(error));
//             Log.d(TAG+" pow", Double.toString(correction));
//
//
//             driveHoldHeading(correction, theta, initialHeading);
//             if(Math.abs(distance) <= Math.abs(distance+distTol))
//                 break;
//
//         }
//    }
    /**
     * a semi-internal funciton used to keep the robot pointed in a certian direction while driving.
     * @param magnitude driving function magnitude in motor power (i.e. on range -1.0 to 0.0 to 1.0)
     * @param angle the angle at which the robot is desired to drive in radians.
     * @param heading the heading that the robot is desired to face.
     */
     public void driveHoldHeading (double magnitude, double angle, double heading){
         double currHeading = gyro.getHeading();
         double error = heading - currHeading;
         Log.d(TAG, "Error: " + error);
         double correction = headingPid.getPIDCorrection(error);

         angle = Math.toRadians(angle +90);
         //angle = currHeading - angle;

         double x = Math.cos(angle)*magnitude;
         double y = Math.sin(angle)*magnitude;

         Log.d(TAG, "wrote to drive. correction was" + correction +" x was " + x + " y was " + y);

         drivebase.drive(x, y, -correction, false);
         drivebase.update();
     }

    /**
     * A function that turns the robot to a certian feild centric position
     *  runs a loop while it can, and exits once at the correct position
     * @param goalHeading the indended feild centric heading in degrees.
     */
     public void turnToAngle(double goalHeading) {
         Log.d(TAG,"startingTurnToAnlge: " + goalHeading);

         final double targetAngleRad = Math.toRadians(goalHeading);

         // For calculating rotational speed:
         double lastHeading;
         double currentHeading = gyro.getHeading();
         Log.d(TAG,"startingHeading: " + currentHeading );


         double lastTime;
         double currentTime = System.currentTimeMillis();
         double lastError =0.0;

         // For turning PID
         double error;

         boolean firstTime = true;
         while (!opMode.isStopRequested()) {

             // update time and headings:
             currentHeading = gyro.getHeading();

             lastTime = currentTime;
             currentTime = System.currentTimeMillis();

             error = setOnNegToPosPi(targetAngleRad - currentHeading);
             double rotation = rotPid.getPIDCorrection(error);

             // may add this in if dt is too weak
             if (rotation > 0 && Math.abs(rotation) <rotMinPow) {
                 rotation = rotMinPow;
             } else if (rotation < 0 && Math.abs(rotation) <rotMinPow) {
                 rotation = -rotMinPow;
             }

             if (rotation > maxTurnPower)
                 rotation = maxTurnPower;
             else if (rotation < -maxTurnPower)
                 rotation = -maxTurnPower;


             Log.d(TAG,"writingToDrive: Error: "+ error + " Correction: " + rotation );
             Log.d(TAG, "error in degrees: "+ Math.toDegrees(error));
             drivebase.drive(0.0, 0, -rotation, false);
             drivebase.update();

             // Check to see if it's time to exit
             // Calculate speed
             double speed;
             if (currentTime == lastTime || firstTime) {
                 speed = 0.003;
             } else {
                 speed = Math.abs(error - lastError) / (currentTime - lastTime);
             }
             lastError = error;

             if ((speed < rotExitSpeed ) && Math.abs(error) < Math.abs(rotTol)) {
                 Log.i(TAG, "ending rotation, should be at heading");
                 break;
             }
             firstTime = false;
             // update robot
             robot.update();
             // temporary
         }
         drivebase.stop();
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
