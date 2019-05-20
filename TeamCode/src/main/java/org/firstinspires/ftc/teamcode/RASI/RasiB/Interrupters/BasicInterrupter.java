package org.firstinspires.ftc.teamcode.RASI.RasiB.Interrupters;

/**
 * Never interrupts execution. Default interrupter for RASI.
 */
public class BasicInterrupter implements Interrupter {
    @Override
    public boolean isInterrupted() {
        return false;
    }
}
