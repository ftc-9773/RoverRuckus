package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

@Autonomous(name = "FINDMINPOWER")
public class findMinPower extends LinearOpMode {

    @Override
    public void runOpMode() {
        SafeJsonReader writer = new SafeJsonReader("DrivePidVals");
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
        sendTelemetry("Waiting for Start");
        waitForStart();
        double p = drivebase.getMinPower(this);
        telemetry.addData("Wrote to file", writer.modifyDouble("minDistPow", p));
        telemetry.update();
        writer.updateFile();
        while(!isStopRequested()){
            continue;
        }

    }
    public void sendTelemetry(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }
}
