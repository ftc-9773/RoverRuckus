package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes.OldAuton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by zacharye on 12/6/18.
 */
@Disabled
@Autonomous(name = "Auton") // runs vision, and acts as a testing platform for autonomous testing
public class TestAutonOpMode extends BasicRasiAuton {


    @Override
    public String fileName() {
        return "Auton";
    }
}
