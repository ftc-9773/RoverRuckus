package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Arc;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

public class MecanumDrivebase {

    static private final double     COUNTS_PER_MOTOR_REV    = 1120;    //
    static private final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static private final double     ROBOT_DIAMETER_INCHES   = 7.322 * 2;
    static final double             COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV ) / (WHEEL_DIAMETER_INCHES * Math.PI);

    static final double MAX_TRANSLATIONAL_SPEED = 1.0;
    static final double MAX_ROTATIONAL_SPEED = 1.0;

    public DcMotorEx[] driveMotors;

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

    /**
     * Drive a certain distance, using the built in target position PID
     * @param x Distance in inches to move left
     * @param y Distance in inces to move foward
     * @param headingChange radians to rotate
     * @param speed Speed in inches for second.
     * */
    //TODO: Adapt to use PIDF rather than PID.
    public void driveDist(double x, double y, double headingChange, double speed){
        runToPosition();
        double theta = Math.atan2(x, y);
        double distance = Math.sqrt(x * x + y * y);

        double _s = Math.sin(theta + Math.PI / 4.0);
        double _c = Math.cos(theta + Math.PI / 4.0);
        double m = Math.max(Math.abs(_s), Math.abs(_c));
        double s = _s / m;
        double c = _c / m;
        int[] pos = new int[4];

        pos[0] = (int) ((COUNTS_PER_INCH) * (distance * s + headingChange * Math.sqrt(2) * ROBOT_DIAMETER_INCHES / WHEEL_DIAMETER_INCHES));
        pos[1] = (int) ((COUNTS_PER_INCH) * (distance * c - headingChange * Math.sqrt(2) * ROBOT_DIAMETER_INCHES / WHEEL_DIAMETER_INCHES));
        pos[2] = (int) ((COUNTS_PER_INCH) * (distance * c + headingChange * Math.sqrt(2) * ROBOT_DIAMETER_INCHES / WHEEL_DIAMETER_INCHES));
        pos[3] = (int) ((COUNTS_PER_INCH) * (distance * s - headingChange * Math.sqrt(2) * ROBOT_DIAMETER_INCHES / WHEEL_DIAMETER_INCHES));

        pos[1] *= -1;
        pos[3] *= -1;

        telemetry.addData("Positions:", pos);

        double radpsec = speed / WHEEL_DIAMETER_INCHES * 2; //Converts inches per second into radians per second
        // Set motor powers
        for (int i = 0; i < 4; i++) {
            driveMotors[i].setTargetPosition(pos[i]);
            driveMotors[i].setVelocity(radpsec, AngleUnit.RADIANS);
        }
    }

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

    public double[] getPos(){
        double[] pos = new double[4];
        pos[0] = driveMotors[0].getCurrentPosition();
        pos[1] = driveMotors[1].getCurrentPosition();
        pos[2] = driveMotors[2].getCurrentPosition();
        pos[3] = driveMotors[3].getCurrentPosition();
        return pos;
    }
}
