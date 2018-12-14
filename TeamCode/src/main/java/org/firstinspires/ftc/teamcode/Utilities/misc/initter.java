package org.firstinspires.ftc.teamcode.Utilities.misc;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;

/**
 * Helper class for creating the robot
 * call set followed by init, or just init.
 *
 * @author cadence
 * */
public class initter {
    static Telemetry telemetry;
    static LinearOpMode opMode;
    static HardwareMap hwmp;

    public static void set(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode){
        initter.telemetry = telemetry;
        initter.opMode = opMode;
        initter.hwmp = hardwareMap;
    }
    public static FTCRobotV1 init(){
        return initter.init(hwmp, telemetry, opMode);
    }

    public static FTCRobotV1 init(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode){
        initter.telemetry = telemetry;
        initter.opMode = opMode;
        initter.hwmp = hardwareMap;
        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
        sendTelemetry("Drivebase created");
        Intake intake = new Intake(hardwareMap, opMode, true);
        sendTelemetry("Intake created");
        CubeLift lift = new CubeLift(hardwareMap, true);
        sendTelemetry("CubeLift created");
        Gyro gyro = new Gyro(hardwareMap);
        sendTelemetry("Gyro created");
        FTCRobotV1 robot = new FTCRobotV1(drivebase,gyro,telemetry,lift,intake);
        sendTelemetry("Robot created");
        return robot;
    }

    public static void sendTelemetry(String msg) {
        if (telemetry == null){
            Log.e("INITTER: ", "called send telemetry with message before inited. Msg:" + msg);
        }else{
            telemetry.addLine(msg);
            telemetry.update();
        }
    }

}
