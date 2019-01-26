package org.firstinspires.ftc.teamcode.zachStuff;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.IntakeV2;

@Autonomous(name = "visionTest")
public class TestOpode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CubeLift cl = new CubeLift(hardwareMap, true);
        IntakeV2 intake = new IntakeV2(hardwareMap, this, false);

        waitForStart();
        while(opModeIsActive()){
            cl.writePositionsToTelem(telemetry, true);
            intake.writeTelemPositions(telemetry);
            telemetry.update();
        }


    }
}
