package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "debugger")
public class debuggingRasiAuto extends BasicRasiAuton {

    @Override()
    public String fileName(){
        return "debugging.rasi";
    }

    @Override
    public boolean doVision(){
        return false;
    }
}
