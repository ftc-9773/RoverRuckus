package org.firstinspires.ftc.teamcode.RASI.RasiB.Core;

import java.util.ArrayList;

public class RasiScript {
    private ArrayList<String> tags;
    public ArrayList<RasiLine> rasiLines;
    public int clock = 0;
    public boolean isDone = false;
    public RasiScript(){}

    /**
     * Will only work if the line is in the same state as the script.
     * */
    public void addLine(RasiLine line){
        this.rasiLines.add(line);
        return ;
    }

    public RasiLine nextLine(){
        if(clock == rasiLines.size()){
            isDone = true;
            return null;
        }
        RasiLine out = rasiLines.get(clock);
        clock++;
        while (!out.doExecute(tags) && clock < rasiLines.size()){
            out = rasiLines.get(clock);
            clock++;
        }
        return out;
    }

    public void addActiveTag(String tag){tags.add(tag);}

    public void delActiveTag(String tag){tags.remove(tag);}

    public void GOTO(int i){
        clock = i;
    }

    public void END(){
        clock = rasiLines.size();
        isDone = true;
    }


}
