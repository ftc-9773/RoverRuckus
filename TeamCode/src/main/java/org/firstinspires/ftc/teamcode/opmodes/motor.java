package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "Testd")
public class motor extends LinearOpMode{
    @Override
    public void runOpMode(){
        DcMotor motor = hardwareMap.get(DcMotor.class, "iaMotor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Motor Encoder pos", motor.getCurrentPosition());
            telemetry.update();
            motor.setPower(-gamepad1.left_stick_y);
        }
    }
}
