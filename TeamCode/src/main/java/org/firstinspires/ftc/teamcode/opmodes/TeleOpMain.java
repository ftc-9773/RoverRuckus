package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;

@TeleOp(name="TeleOpOpmode")
public class TeleOpMain extends LinearOpMode{

    @Override
    public void runOpMode(){

        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
        sendTelemetry("Drivebase created");

        Intake intake = new Intake(hardwareMap, this, true);
        sendTelemetry("Intake created");

        CubeLift lift = new CubeLift(hardwareMap, true);
        sendTelemetry("CubeLift created");

        FTCRobotV1 robot = new FTCRobotV1(drivebase,telemetry,lift,intake);
        sendTelemetry("Robot created");


        sendTelemetry("Waiting for start...");
        robot.drivebase.runWithoutEncoders();
        // opmode start
        waitForStart();
        boolean temp = true;
        while(opModeIsActive()) {
            if (temp){
                sendTelemetry("Started");
                temp = false;
            }

            robot.runGamepadCommands(gamepad1, gamepad2);
            robot.readSensors();
            robot.update();
            robot.drivebase.getPowersLogged(telemetry);
            // probably remove sometime in the future
            robot.intake.debugIntakeArmServos(telemetry);
            telemetry.addData ("liftPosition", robot.lift.getLiftPos());
            telemetry.update();
        }

    }
    private void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }
}
