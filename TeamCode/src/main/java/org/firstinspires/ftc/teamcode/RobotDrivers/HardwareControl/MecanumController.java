package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumController {

    static final double MAX_TRANSLATIONAL_SPEED = 1.0;
    static final double MAX_ROTATIONAL_SPEED = 1.0;

    DcMotor[] driveMotors;

    public MecanumController(HardwareMap hwMap) {
        // init wheels
        driveMotors = new DcMotor[4];
        driveMotors[0] = hwMap.dcMotor.get("flMotor");
        driveMotors[1] = hwMap.dcMotor.get("frMotor");
        driveMotors[2] = hwMap.dcMotor.get("blMotor");
        driveMotors[3] = hwMap.dcMotor.get("brMotor");

        for(DcMotor d:driveMotors){ d.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }
    }

    public void drive(double theta, double velocity, double rotation) {
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
}
