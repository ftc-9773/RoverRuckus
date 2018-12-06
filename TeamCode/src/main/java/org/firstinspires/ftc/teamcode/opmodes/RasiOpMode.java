package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiInterpreter;

public class RasiOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        RasiInterpreter i = new RasiInterpreter("../RASI/Scripts/","init.rasi", this);
        i.runRasi();
    }
}
