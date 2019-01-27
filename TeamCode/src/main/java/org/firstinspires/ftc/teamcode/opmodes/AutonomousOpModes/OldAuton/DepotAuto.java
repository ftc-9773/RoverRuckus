package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes.OldAuton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "DepotAut") // does autonomous for depot side
public class DepotAuto extends BasicRasiAuton {

    @Override
    public String fileName() {
        return "DepotAuton";
    }

}
