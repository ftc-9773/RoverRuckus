package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes.OldAuton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "FailsafeAuto") // runs vision, and acts as a testing platform for autonomous testing
public class FailsafeAuto extends BasicRasiAuton {

    @Override
    public String fileName() {
        return "FailsafeAuto";
    }
}
