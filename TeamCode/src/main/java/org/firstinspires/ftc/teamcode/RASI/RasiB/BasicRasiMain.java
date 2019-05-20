package org.firstinspires.ftc.teamcode.RASI.RasiB;


import org.firstinspires.ftc.teamcode.RASI.RasiB.Core.RasiInterpreter;
import org.firstinspires.ftc.teamcode.RASI.RasiB.Interrupters.BasicInterrupter;
import org.firstinspires.ftc.teamcode.RASI.RasiB.Interrupters.Interrupter;

/**
 * */
public abstract class BasicRasiMain {
    RasiInterpreter interpreter;

    public BasicRasiMain(){
        this.interpreter = new RasiInterpreter(getFileName(), getFilePath(), getInterrupter(), getRasiCommandClass());
    }

    public abstract String getFilePath();

    public abstract String getFileName();

    /**
     * Override to return a LinearOpModeInterrupter if you are compiling for FTC.
     * */
    public Interrupter getInterrupter(){
        return new BasicInterrupter();
    }

    /**
     * Return an instance of a class that contains all the methods you want exposed to RASI, and that extends ExtendableRasiCommands
     * */
    public abstract ExtendableRasiCommands getRasiCommandClass();

    public void run(){
        this.interpreter.run();
    }
}
