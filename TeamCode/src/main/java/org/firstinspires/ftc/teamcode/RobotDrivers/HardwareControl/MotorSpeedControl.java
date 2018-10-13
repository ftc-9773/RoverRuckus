package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

@Deprecated
public class MotorSpeedControl {

    private static boolean DEBUG = true;
    private static String TAG = "MotorSpeedControl";

    private DcMotorEx motor;

    private SafeJsonReader myJsonReader;


    MotorSpeedControl (HardwareMap hwMap, String motorName) {


        motor = hwMap.get(DcMotorEx.class, motorName);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // PID Init
        myJsonReader = new SafeJsonReader("MotorSpeedControl");
        double kp = myJsonReader.getDouble("kp");
        double ki = myJsonReader.getDouble("ki");
        double kd = myJsonReader.getDouble("kd");

        PIDCoefficients curCoeffs = motor.getPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        if (DEBUG) { Log.d(TAG, String.format("Default Coefficients: %f, %f, %f", curCoeffs.p, curCoeffs.i, curCoeffs.d)); }

        PIDCoefficients motorPIDCoeffs = new PIDCoefficients(kp, ki, kd);
        motor.setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, motorPIDCoeffs);

    }


    public void setSpeed(double radPerSec) { motor.setVelocity(radPerSec, AngleUnit.RADIANS); }

    public void motorReverse() { motor.setDirection(DcMotorSimple.Direction.REVERSE); }

    public void motorForward() { motor.setDirection(DcMotorSimple.Direction.FORWARD); }

    public double getSpeed() {return motor.getVelocity(AngleUnit.RADIANS);}
}
