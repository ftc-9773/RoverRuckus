//package org.firstinspires.ftc.teamcode.opmodes;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DistanceSensor;
//
//import org.firstinspires.ftc.teamcode.Logic.Align;
//import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
//import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.OdometryController;
//import org.firstinspires.ftc.teamcode.Utilities.misc.Button;
//
//@TeleOp(name="AlignTest")
//public class AlignTest extends LinearOpMode {
//    @Override
//
//    public void runOpMode() {
//        MecanumDrivebase drivebase = new MecanumDrivebase(hardwareMap, telemetry);
//        //OdometryController oc = new OdometryController(hardwareMap);
//        DistanceSensor leftDistSensor = hardwareMap.get(DistanceSensor.class, "leftDist");
//        DistanceSensor rightDistSensor = hardwareMap.get(DistanceSensor.class, "rightDist");
//
//        Align alignController = new Align(rightDistSensor, leftDistSensor, drivebase);
//
//        Button a = new Button();
//
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            a.recordNewValue(gamepad1.a);
//            if (a.isJustOn()) {
//                alignController.Align();
//            }
//        }
//    }
//}
//
//
