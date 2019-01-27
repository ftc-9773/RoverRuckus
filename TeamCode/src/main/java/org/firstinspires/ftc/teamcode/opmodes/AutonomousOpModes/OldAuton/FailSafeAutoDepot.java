package org.firstinspires.ftc.teamcode.opmodes.AutonomousOpModes.OldAuton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "FailSafeDepot")
public class FailSafeAutoDepot extends BasicRasiAuton {

    @Override
    public String fileName() {
        return "FailsafeAutoDepot";
    }
}
