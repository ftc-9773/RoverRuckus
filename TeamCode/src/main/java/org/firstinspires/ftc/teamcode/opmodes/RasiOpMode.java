package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiInterpreter;

@TeleOp(name = "RASITEST")
public class RasiOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        RasiInterpreter i = new RasiInterpreter("/sdcard/FIRST/team9773/rasi19/","init.rasi", this);
        i.runRasi();
    }
}
