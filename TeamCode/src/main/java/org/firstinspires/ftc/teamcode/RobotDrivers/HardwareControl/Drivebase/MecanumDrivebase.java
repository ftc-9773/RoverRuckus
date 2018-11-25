package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Arc;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

public class MecanumDrivebase {

    static private final double     COUNTS_PER_MOTOR_REV    = 1120;    //
    static private final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static private final double     ROBOT_DIAMETER_INCHES   = 7.322 * 2;
    static final double             COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV ) / (WHEEL_DIAMETER_INCHES * Math.PI);

    static private final double DRIVE_SCALING = 3; // Must be odd
    static private final double ROTATION_SCALING = 1;

    static final double MAX_TRANSLATIONAL_SPEED = 1.0;
    static final double MAX_ROTATIONAL_SPEED = 1.0;

    public DcMotorEx[] driveMotors;

    private double[] motorPowers = new double[4];

    Telemetry telemetry;
    SafeJsonReader reader = new SafeJsonReader("DcMotorPIDCoefficients");

    public MecanumDrivebase(HardwareMap hwMap, Telemetry telem) {
        // init wheels
        this.telemetry = telem;
        driveMotors = new DcMotorEx[4];
        driveMotors[0] = hwMap.get(DcMotorEx.class, "fldrive");
        driveMotors[1] = hwMap.get(DcMotorEx.class, "frdrive");
        driveMotors[2] = hwMap.get(DcMotorEx.class, "bldrive");
        driveMotors[3] = hwMap.get(DcMotorEx.class, "brdrive");

        for (DcMotorEx motor:driveMotors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //motor.setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDCoefficients(reader.getDouble("kp"), reader.getDouble("ki"), reader.getDouble("kd")));

        }
        runToPosition();
    }

    public void runWithEncoders(){for(DcMotor d:driveMotors){ d.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}}
    public void runToPosition(){for(DcMotor d:driveMotors){
        //d.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        d.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }}

    private double scale(double val) {
        if (Math.abs(val) > 0.05) {
            return Math.pow(val, 3);
        } else {
            return 0;
        }
    }

    private double[] scaleDriving(Vector vec, double rotation) {
        double[] finalSpeeds = new double[3];

        if (Math.abs(vec.getMagnitude()) > 0.05) {
            double mag =   DRIVE_SCALING * scale(vec.getMagnitude());
            finalSpeeds[0] = mag * Math.sin(vec.getAngle());
            finalSpeeds[1] = mag * Math.cos(vec.getAngle());

            finalSpeeds[3] = scale(rotation) * (ROTATION_SCALING * mag + 1);
        } else {
            finalSpeeds[0] = 0;
            finalSpeeds[1] = 0;

            finalSpeeds[2] = scale(rotation);
        }
        return finalSpeeds;
    }

    private double[] scaleDriving(double x, double y, double rotation) { return scaleDriving(new Vector(true, x, y), rotation); }

    /*
    public void drive(double theta, double velocity, double rotation) {
        runWithEncoders();
        double _s = Math.sin(theta + Math.PI / 4.0);
        double _c = Math.cos(theta + Math.PI / 4.0);
        double m = Math.max(Math.abs(_s), Math.abs(_c));
        double s = _s / m;
        double c = _c / m;
        double[] vel = new double[4];

        vel[0] = velocity * s + rotation;
        vel[1] = velocity * c - rotation;
        vel[2] = velocity * c + rotation;
        vel[3] = velocity * s - rotation;

        vel[1] *= -1;
        vel[3] *= -1;

        // Set motor powers
        for (int i = 0; i < 4; i++) { driveMotors[i].setPower(vel[i]); }
    }

    public void arcadeDrive(double x, double y, double rot) {

        double theta = Math.atan2(x, y);
        double mag = Math.sqrt(x * x + y * y) ;

        mag *= MAX_TRANSLATIONAL_SPEED;
        rot *= MAX_ROTATIONAL_SPEED;

        drive(theta, mag, rot);
    }
*/
    // Clean Version
    public void drive(double x, double y, double rotation) {
        runWithEncoders();

        motorPowers[0] = y - x + rotation;
        motorPowers[1] = y + x - rotation;
        motorPowers[2] = y + x + rotation;
        motorPowers[3] = y - x - rotation;

        motorPowers[1] *= -1;
        motorPowers[3] *= -1;

        double maxVal = Math.max(Math.max(Math.abs(motorPowers[0]), Math.abs(motorPowers[1])),
                        Math.max(Math.abs(motorPowers[2]), Math.abs(motorPowers[3])));

        // Check that values are < 1. Normalize otherwise
        if (maxVal > 1) { for (double val:motorPowers) { val /= maxVal;} }
    }

    public void driveScaled(double x, double y, double rotation) {
        double[] arr = scaleDriving(x, y, rotation);
        drive(arr[0], arr[1], arr[2]);
    }

    public void drivePolar(double mag, double theta, double rotation) {
        double x = Math.sin(theta);
        double y = Math.sin(theta);

        drive(x, y, rotation);
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
    }
*/

    public double[] getPos(){
        double[] pos = new double[4];
        pos[0] = driveMotors[0].getCurrentPosition();
        pos[1] = driveMotors[1].getCurrentPosition();
        pos[2] = driveMotors[2].getCurrentPosition();
        pos[3] = driveMotors[3].getCurrentPosition();
        return pos;
    }
}
