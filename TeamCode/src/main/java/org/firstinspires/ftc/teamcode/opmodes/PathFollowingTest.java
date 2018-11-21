package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Logic.ArcFollower;
import org.firstinspires.ftc.teamcode.RobotDrivers.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumCont;
import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumRobot;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Arc;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;

@TeleOp(name = "PathFollowing")
public class PathFollowingTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumCont drivebase = new MecanumCont(hardwareMap, telemetry);
        Gyro gyro = new Gyro(hardwareMap);
        //SafeJsonReader reader = new SafeJsonReader("ArcPositions");

        double elapsedDistance = 0;
        double startheading = Math.PI / 2;
        OdometryController OC = new OdometryController(hardwareMap);

        MecanumRobot robot = new MecanumRobot(drivebase, gyro, OC, telemetry);

        Arc arc = new Arc(new Point(0,0),new Point(0,10),startheading);
        ArcFollower follower = new ArcFollower(robot);

        double speed = 5;
        waitForStart();

        while(opModeIsActive()) {
            Arc arc1 = new Arc(new Point(0,0), new Point(0,10), 0);
            follower.updateArc(arc1);
            while(!robot.getPos().AreSame(arc.getEndPoint(), 0.5)){
                follower.next();
            }
        }
    }
}
