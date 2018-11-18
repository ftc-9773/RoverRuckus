package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "tankDtTest")
public class TestTankDt extends LinearOpMode {
    DcMotor[] motors = new DcMotor[4];
    double motorPowers[] = new double[4];
    private static final double ROTATION_CONSTANT = .507;
    private static double MAX_MOTOR_SPEED = 1;


    @Override
    public void runOpMode() throws InterruptedException {
        // init
        motors[0] = hardwareMap.dcMotor.get("fldrive");
        motors[1] = hardwareMap.dcMotor.get("frdrive");
       // motors[2] = hardwareMap.dcMotor.get("bldrive");
       // motors[3] = hardwareMap.dcMotor.get("brdrive");

        motorPowers[0] = 0;
        motorPowers[1] = 0;
        motorPowers[2] = 0;
        motorPowers[3] = 0;

        waitForStart();
        while (opModeIsActive()) {
            // Shift to field centric if enabled
            double fowardPower = -gamepad1.left_stick_y;
            double turningPower = ROTATION_CONSTANT * gamepad1.right_stick_x;

            motorPowers[0] = turningPower - fowardPower;
            motorPowers[1] = turningPower + fowardPower;
            motorPowers[2] = turningPower - fowardPower;
            motorPowers[3] = turningPower + fowardPower;

            double maxMagnitude = Math.max(Math.max(Math.abs(motorPowers[0]),Math.abs(motorPowers[1]))
                    ,Math.max(Math.abs(motorPowers[2]),Math.abs(motorPowers[3])));

            if(maxMagnitude >1.0) for(double d: motorPowers) d/=maxMagnitude;

            for(int i=0; i<2; i++) motors[i].setPower(motorPowers[i]);

        }
    }
}