package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotDrivers.MecanumCont;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Arc;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

@TeleOp(name = "PathFollowing")
public class PathFollowingTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumCont drivebase = new MecanumCont(hardwareMap);

        //SafeJsonReader reader = new SafeJsonReader("ArcPositions");

        double elapsedDistance = 0;
        double startheading = Math.PI / 8;

        Arc arc = new Arc(new Point(0,0),new Point(10,10),startheading);
        double speed = 5;
        waitForStart();

        double out[];
        while(opModeIsActive()){
            if(elapsedDistance < arc.getLength() && gamepad1.left_trigger > 0.2){
                out = drivebase.arcStep(arc, speed, startheading);
                drivebase.driveDist(out[0], out[1], out[2], speed);
                elapsedDistance += Math.sqrt(out[0] * out[0] + out[1] * out[1]);
            }
        }
    }
}
