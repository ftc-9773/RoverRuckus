package org.firstinspires.ftc.teamcode.RASI.RasiB.Core;


import org.firstinspires.ftc.teamcode.RASI.RasiB.Interrupters.Interrupter;
import org.firstinspires.ftc.teamcode.RASI.RasiB.RasiExceptions.RasiPreproccesingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for processing the input file
 * Returns a RasiScript object to be passed to RasiLinker
 * */
public class RasiPreprocesser {
    private BufferedReader reader;
    private HashMap<String, Object> defs = new HashMap<>(); //Global constant definitions. (Equivalent to #DEFINE in c).
    private RasiScript out = new RasiScript();
    private HashMap<String, Integer> labelMapping = new HashMap<>();
    private Interrupter interrupter;

    public RasiPreprocesser(String fileName, String filePath, Interrupter interrupter) throws RasiPreproccesingException, IOException {
        this.interrupter = interrupter;
        try {
            reader = new BufferedReader(new FileReader(new File(filePath + fileName + ".rasi")));
        } catch (FileNotFoundException e){
            //Alert user file was not found
        }
            String line;
            RasiLine rl;
            int pln = 0;
            while((line = reader.readLine()) != null && !interrupter.isInterrupted()){
                if (line == ""){pln++;continue;}
                rl = new RasiLine(line, pln);
                ArrayList<Object> realParams = new ArrayList<>();
                if (rl.method_name.charAt(0) == '#'){
                    if (rl.method_name == "#DEF"){
                        defs.put(rl.params_strings.get(0).str, getType(rl.params_strings.get(1)));
                        pln++;
                        continue;
                    }
                    if (rl.method_name == "#LABEL"){
                        labelMapping.put(rl.params_strings.get(0).str, (out.rasiLines.size()));
                        pln++;
                        continue;
                    }
                    throw new RasiPreproccesingException("Bad keyword command" + rl.method_name + " in RasiFile " + fileName + " at line " + pln);
                }
                for(int i = 0; i < rl.params_strings.size(); i++){
                    if (defs.get(rl.params_strings.get(i)) != null){
                        realParams.add(defs.get((rl.params_strings.get(i).str)));
                    } else {
                        realParams.add(getType(rl.params_strings.get(i)));
                    }
                }
                if (rl.method_name == "GOTO"){
                    realParams.set(0, labelMapping.get(rl.params_strings.get(0).str));
                }
                rl.params = realParams.toArray();
                out.addLine(rl);
                pln++;
            }
            reader.close();
    }

    public RasiScript getScript(){
        return out;
    }

    private Object getType(RasiLine.stringWrapper strw){
        if (strw.isString){return strw.str;}
        String t = strw.str;
        if (t.charAt(0) == '0' || t.charAt(0) == '1' || t.charAt(0) == '2' || t.charAt(0) == '3' ||t.charAt(0) == '4' || t.charAt(0) == '5' || t.charAt(0) == '6' || t.charAt(0) == '7' || t.charAt(0) == '8' || t.charAt(0) == '9'){
            if (t.contains(".")){
                return Double.valueOf(t);
            } else{
                Integer.valueOf(t);
            }
        }
        if (t.length() < 5){
            return t;
        }
        else if (t.substring(0, 4) == "true"){
            return true;
        } else if (t.substring(0, 5) == "false") {
            return false;
        }
        return t;
    }
}
