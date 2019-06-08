package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="zzzzzInverseCrator")
public class InverseCrator extends BasicRasiAuto {

    public String fileName(){
        return "InverseCrator";
    }

    @Override
    public boolean doVision() {
        return false;
    }
}
