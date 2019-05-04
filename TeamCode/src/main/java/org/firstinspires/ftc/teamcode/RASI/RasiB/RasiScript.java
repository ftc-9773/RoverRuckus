package org.firstinspires.ftc.teamcode.RASI.RasiB;

import org.firstinspires.ftc.teamcode.RASI.RasiCommands.RasiCommands;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RasiScript {
    private HashMap<String, Object> vars;
    private ArrayList<String> tags;
    private RasiLine currLine;
    private ArrayList<RasiLine> rasiLines;
    public int clock = 0;

    public RasiScript(ArrayList<RasiLine> rLines){
        this.rasiLines = rLines;
    }

    public void nextLine(){

    }

}
