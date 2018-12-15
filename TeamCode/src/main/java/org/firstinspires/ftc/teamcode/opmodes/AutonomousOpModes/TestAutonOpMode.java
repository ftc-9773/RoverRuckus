package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes.BasicRasiAuton;

/**
 * Created by zacharye on 12/6/18.
 */

@Autonomous(name = "Auton") // runs vision, and acts as a testing platform for autonomous testing
public class TestAutonOpMode extends BasicRasiAuton {


    @Override
    public String fileName() {
        return "Auton";
    }
}
