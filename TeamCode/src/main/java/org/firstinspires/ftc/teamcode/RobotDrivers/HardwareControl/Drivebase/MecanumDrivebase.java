package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Arc;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

public class MecanumDrivebase {

    static private final double     COUNTS_PER_MOTOR_REV    = 560;    //
    static private final double     WHEEL_TURNS_PER_MOTOR_REV = 30.0 / 38.0;
    static private final double     WHEEL_DIAMETER_INCHES   = 3.94 ;     // For figuring circumference
    static private final double     ROBOT_DIAMETER_INCHES   = 7.322 * 2;
    public static final double      COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV*WHEEL_TURNS_PER_MOTOR_REV ) / (WHEEL_DIAMETER_INCHES * Math.PI);

    static private final double DRIVE_SCALING = 3; // Must be odd
    static private final double ROTATION_SCALING = 0.8; // 0 is none

    static final double MAX_TRANSLATIONAL_SPEED = 1.0;
    static final double MAX_ROTATIONAL_SPEED = 1.0;




    public DcMotor[] driveMotors;

    private double[] motorPowers = new double[4];

    Telemetry telemetry;
//    SafeJsonReader reader = new SafeJsonReader("DrivePidVals.json");

    public MecanumDrivebase(HardwareMap hwMap, Telemetry telem) {
        // init wheels
        this.telemetry = telem;
        driveMotors = new DcMotor[4];
        driveMotors[0] = hwMap.get(DcMotor.class, "fldrive");
        driveMotors[1] = hwMap.get(DcMotor.class, "frdrive");
        driveMotors[2] = hwMap.get(DcMotor.class, "bldrive");
        driveMotors[3] = hwMap.get(DcMotor.class, "brdrive");
        //pid coeffs for different motion stuff.


        for (DcMotor motor:driveMotors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //motor.setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDCoefficients(reader.getDouble("kp"), reader.getDouble("ki"), reader.getDouble("kd")));

        }
        runWithEncoders();
    }

    private double scale(double val) {
        if (Math.abs(val) > 0.05) {
            return Math.pow(val, 3);
        } else {
            return 0;
        }
    }

    private double[] scaleDriving(double x, double y, double rotation) {
        Vector vec = new Vector(true, x, y);

        double[] finalSpeeds = new double[3];

        if (Math.abs(vec.getMagnitude()) > 0.05) {
            double mag =   DRIVE_SCALING * scale(vec.getMagnitude());
            finalSpeeds[0] = mag * Math.sin(vec.getAngle());
            finalSpeeds[1] = mag * Math.cos(vec.getAngle());

            finalSpeeds[2] = scale(rotation) * (ROTATION_SCALING * mag + 1);
        } else {
            finalSpeeds[0] = 0;
            finalSpeeds[1] = 0;

            finalSpeeds[2] = scale(rotation);
        }
        return finalSpeeds;
    }

    // Clean Version
    public void drive(double xIn, double yIn, double rotationIn, boolean scale) {

        double x = xIn;
        double y = yIn;
        double rotation = rotationIn;

        if (scale)  {
            double[] arr = scaleDriving(x, y, rotation);
            x = arr[0];
            y = arr[1];
            rotation = arr[2];
        }

        runWithEncoders();

        motorPowers[0] = y + x + rotation;
        motorPowers[1] = y - x - rotation;
        motorPowers[2] = y - x + rotation;
        motorPowers[3] = y +  x - rotation;

        motorPowers[0] *= -1;
        motorPowers[2] *= -1;


        double maxVal = Math.max(Math.max(Math.abs(motorPowers[0]), Math.abs(motorPowers[1])),
                        Math.max(Math.abs(motorPowers[2]), Math.abs(motorPowers[3])));

        // Check that values are < 1. Normalize otherwise
        if (maxVal > 1) { for (int i = 0; i < 4; i++) { motorPowers[i] /= maxVal;} }

        telemetry.addData("MotorPowers", String.format("%.1f, %.1f, %.1f, %.1f", motorPowers[0], motorPowers[1], motorPowers[2], motorPowers[3]));

    }

    public void drivePolar(double mag, double theta, double rotation, boolean scale) {
        double x = Math.sin(theta)*mag;
        double y = Math.cos(theta)*mag;

        drive(x, y, rotation, scale);
    }


    public void update() { for (int i = 0; i<4; i++) { driveMotors[i].setPower(motorPowers[i]); } }
    public void stop() { for (DcMotor motor:driveMotors) {motor.setPower(0);} }
    /*
    public double[] arcStep(Arc arc, double speed, double currHeading) {
        double headingChange = 2 * currHeading;
        double steps = arc.getLength() / speed / 20; //20 steps minimum

        double deltaX = Math.cos(arc.theta) / steps;
        double deltaY = Math.sin(arc.theta) / steps;
        double out[] = new double[3];
        out[0] = deltaX;
        out[1] = deltaY;
        out[2] = headingChange / steps;
        return out;
    }  */
    public long[] getMotorPositions(){
        long[] value = new long[4];
        for (int i = 0; i< 4; i++)
            value[i] = driveMotors[i].getCurrentPosition();
        return value;
    }

    public void runWithEncoders(){for(DcMotor d:driveMotors){ /*/d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/}}
    public void runWithoutEncoders() { for (DcMotor d:driveMotors) {d.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}}
    public void runToPosition(){for(DcMotor d:driveMotors){
        //d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        d.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }}


    public double[] getPos(){
        double[] pos = new double[4];
        pos[0] = driveMotors[0].getCurrentPosition();
        pos[1] = driveMotors[1].getCurrentPosition();
        pos[2] = driveMotors[2].getCurrentPosition();
        pos[3] = driveMotors[3].getCurrentPosition();
        return pos;
    }
    public void getPowersLogged(Telemetry telemetry){
        for(int i = 0; i< 4; i++){
            telemetry.addData("Motor power of motor: "+ i, driveMotors[i].getPower());
            Log.d("ftc9773_motorPowers", "motor " + i + driveMotors[i].getPowerFloat());

        }
    }
}
