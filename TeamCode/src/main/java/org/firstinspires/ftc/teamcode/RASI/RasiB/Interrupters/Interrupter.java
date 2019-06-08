package org.firstinspires.ftc.teamcode.RASI.RasiB.Interrupters;

public interface Interrupter {
    boolean isInterrupted = false;
    /**
     * Whether or not to stop processing/executing RASI file and exit. Intended for use with LinearOpMode.isStopRequested().
     * */
    boolean isInterrupted();
}
