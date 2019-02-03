package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RasiScratchPad")
public class RasiScratchPad extends BasicRasiAuto {
    @Override
    public String fileName() {
        return "RasiScratchPad";
    }

    @Override
    public boolean doVision() {
        return false;
    }
}
