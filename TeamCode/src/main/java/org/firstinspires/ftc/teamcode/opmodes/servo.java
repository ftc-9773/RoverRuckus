package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.IntakeV2;

@Autonomous(name = "Servo")
public class servo extends LinearOpMode {

    public void runOpMode(){
        IntakeV2 lift = new IntakeV2(hardwareMap, this, true);
        waitForStart();
        lift.transferMinerals();
        double stat = System.currentTimeMillis();

        while (!this.isStopRequested()){lift.update();}

        lift.update();;
    }
}
