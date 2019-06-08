package org.firstinspires.ftc.teamcode.RASI.RasiB.Core;

import org.firstinspires.ftc.teamcode.RASI.RasiB.Core.RasiLine;
import org.firstinspires.ftc.teamcode.RASI.RasiB.Core.RasiMethodParser;
import org.firstinspires.ftc.teamcode.RASI.RasiB.Core.RasiPreprocesser;
import org.firstinspires.ftc.teamcode.RASI.RasiB.Core.RasiScript;
import org.firstinspires.ftc.teamcode.RASI.RasiB.ExtendableRasiCommands;
import org.firstinspires.ftc.teamcode.RASI.RasiB.Interrupters.Interrupter;
import org.firstinspires.ftc.teamcode.RASI.RasiB.RasiExceptions.RasiPreproccesingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * TODO: Add in error reporting. (Probably through a class called Logger, which can be extended/overriden)
 * */
public class RasiInterpreter {
    private RasiScript script;
    private RasiPreprocesser preprocesser;
    private RasiMethodParser methodParser;
    private Interrupter interrupter;

    public RasiInterpreter(String filename, String filepath, Interrupter interrupter, ExtendableRasiCommands commands){
        this(filename, filepath, interrupter);
        attachObject(commands);
    }

    public RasiInterpreter(String filename, String filepath, Interrupter interrupter){
        this.interrupter = interrupter;
        try{
            preprocesser = new RasiPreprocesser(filename, filepath, interrupter);
        } catch (IOException e){

        } catch (RasiPreproccesingException e){

        }
        script = preprocesser.getScript();
        methodParser = new RasiMethodParser(script);
        addInbuiltMethods();
    }

    private void addInbuiltMethods(){
        methodParser.addMethod(getMethod("GOTO", script), script);
        methodParser.addMethod(getMethod("END", script), script);
    }

    private Method getMethod(String name, Object o){
        Method[] methods = o.getClass().getMethods();
        for (Method m: methods) {
            if (m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }
    /**
     * Makes all methods of object visible inside RASI file.
     * */
    public void attachObject(Object o){
        methodParser.addAllMethods(o);
    }

    public void run(){
        RasiLine l;
        while (!interrupter.isInterrupted() && (l = script.nextLine()) != null){
            try{
                l.execute();
            } catch (InvocationTargetException e){
                //Add in error reporting later
            } catch (IllegalAccessException e){

            }
        }
    }
}
