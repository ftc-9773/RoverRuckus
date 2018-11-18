package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumCont;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumRobot;
import org.firstinspires.ftc.teamcode.Utilities.misc.Button;

@TeleOp(name="DriveDist")
public class DriveDist extends LinearOpMode {
    @Override

    public void runOpMode() {
        MecanumCont drivebase = new MecanumCont(hardwareMap, telemetry);
        MecanumRobot robot = new MecanumRobot(drivebase);

        Button a = new Button();
        waitForStart();

        while(opModeIsActive()){
            a.recordNewValue(gamepad1.a);
            if(a.isJustOn()) {
                robot.driveDist(1, 1, 0, 1);
            }
        }
    }
}
