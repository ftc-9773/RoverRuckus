package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes.OldAuton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "debugger")
public class debuggingRasiAuto extends BasicRasiAuton {

    @Override()
    public String fileName(){
        return "debugging";
    }

    public boolean doVision(){
        return false;
    }
}
