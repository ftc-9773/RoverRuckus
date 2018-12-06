package org.firstinspires.ftc.teamcode.RASI.Rasi_2_5_beta;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Main way to run RASI files from inside java
 *
 * construct with
 * RasiInterpreter rasi = new RasiInterpreter("Path/to/file", "filename.rasi", this);
 * (this is a LinearOpMode instance)
 * call rasi.runRasi() to run the file.
 * */
public class RasiInterpreter {

    //Helpers
    private boolean isnull;
    private int numberOfParams;

    //Telemetry stuff
    private static String LOG_TAG = "RasiExecutor";

    //Current parser
    private RasiLexer currentRasiLexer = null;
    private String currFilename;
    private String currPath;

    //File tracker
    private ArrayList<String[]> queue;

    //Opmode
    private LinearOpMode linearOpMode;

    private HashMap<String, String> hashMap;
    private RasiCommands rasiCommands;
    private String methodString;
    private String lcString;
    private String type;
    private String command;
    private String mixedCaseString;
    private String[] parameters;
    private Object[] finalParameters;
    private StringBuilder stringBuilder;
    private HashMap<String, String[]> infoHashmap;
    private HashMap<String, Method> methodsHashMap;
    private boolean hasArguments;
    private Method method;

    public RasiInterpreter(String filepath, String filename, LinearOpMode opmode){
        this.linearOpMode = opmode;
        rasiCommands = new RasiCommands(opmode);
        addFile(filename, filepath);
        hashMap = new HashMap<>();
        infoHashmap = new HashMap<>();
        methodsHashMap = new HashMap<>();

        //Parse RasiCommands
        for(int x = 0; x < rasiCommands.getClass().getMethods().length; x++){ //runs for every method in the TeamRasiCommands Class
            Log.d("RasiExecutor", Integer.toString(x));
            System.out.println("Method " + x + " is " + rasiCommands.getClass().getMethods()[x].toString());
            if(rasiCommands.getClass().getMethods()[x].toString().contains("RasiCommands.")){ //filters out the stuff that java puts there and hides.
                System.out.println("Method " + x + " is " + rasiCommands.getClass().getMethods()[x].toString());
                method = rasiCommands.getClass().getMethods()[x];
                methodString = method.toString();
                Log.d("Rasi_MethodString1", methodString);
                stringBuilder = new StringBuilder(methodString); //StringBuilder to format the method text to be more usable.
                int index = 0;

                //remove spaces and close parenthesis:
                while(index < stringBuilder.length()){
                    if(stringBuilder.charAt(index) == ' ' || stringBuilder.charAt(index) == ')'){
                        stringBuilder.deleteCharAt(index);
                    }
                    else{
                        index++;
                    }
                }
                methodString = stringBuilder.toString();
                Log.d("RasiExecutor",methodString);
                String[] tempArray = methodString.split("\\(");
                tempArray = tempArray[0].split("\\."); //set mixedCaseString to the name of the method. removes the remaining parenthesis and dots.
                mixedCaseString = tempArray[tempArray.length-1];
                Log.d("rasimixedCaseString", mixedCaseString);
                Log.d("Rasi_MethodString2", methodString);
                if(methodString.charAt(methodString.length()-1)!= '(') {
                    //make a string of the arguments to the method
                    parameters = methodString.split("\\(");
                    parameters = parameters[1].split(",");
                    hasArguments = true;
                    Log.d(LOG_TAG, "parameters initialized to actual parameters");
                    numberOfParams = parameters.length;
                    Log.d("rasi_parameters", Arrays.asList(parameters).toString());
                }
                else{
                    hasArguments= false;
                    parameters = null;
                    Log.i(LOG_TAG, "Parameters set to empty array");
                    numberOfParams = 0;
                }
                lcString = mixedCaseString.toLowerCase();
                Log.d("rasilcString", lcString);
                hashMap.put(lcString, mixedCaseString);
                if(parameters != null)
                    Log.i("RasiExecutor", parameters.toString());
                Log.i("RasiExecutor", mixedCaseString);
                infoHashmap.put(mixedCaseString, parameters);
                Log.i(LOG_TAG, "added parameters array, length: " + Integer.toString(numberOfParams));
                methodsHashMap.put(mixedCaseString, method);
            }
        }
    }

    public void addFile(String filename, String filepath){
        String[] pair = new String[2];
        pair[1] = filename;
        pair[0] = filepath;
        queue.add(pair);
        if (queue.size() == 1){
            popRasi();
        }
    }
    public void addFile(String filename){
        addFile(filename, currPath);
    }

    public void popRasi(){
        if (queue.size() != 0){
            currentRasiLexer = new RasiLexer(queue.get(0)[0], queue.get(0)[1], linearOpMode);
            currFilename = queue.get(0)[1];
            currPath = queue.get(0)[0];
            queue.remove(0);
        }
        currentRasiLexer = null;
    }

    public void runRasi() {
        command = currentRasiLexer.getCommand();
        while (!currentRasiLexer.fileEnded && !linearOpMode.isStopRequested()) {

            Log.d("RasiCommand", hashMap.get(command.toLowerCase()));
            Log.d("infohashmapout", infoHashmap.get(hashMap.get(command.toLowerCase())).toString());
            if (infoHashmap.get(hashMap.get(command.toLowerCase())) != null) {
                isnull = false;
            } else {
                isnull = true;
            }
            Log.d("rasiisnull", Boolean.toString(isnull));
            if (!isnull) {
                finalParameters = new Object[infoHashmap.get(hashMap.get(command.toLowerCase())).length];
                for (int index = 0; index < finalParameters.length; index++) {
                    type = infoHashmap.get(hashMap.get(command.toLowerCase()))[index];
                    switch (type) {
                        case "int":
                            finalParameters[index] = Integer.valueOf(currentRasiLexer.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Int" + currentRasiLexer.parameters[index]);
                            break;
                        case "char":
                            finalParameters[index] = currentRasiLexer.parameters[index+1].charAt(0);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Char" + currentRasiLexer.parameters[index]);
                            break;
                        case "long":
                            finalParameters[index] = Long.valueOf(currentRasiLexer.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Long" + currentRasiLexer.parameters[index]);
                            break;
                        case "float":
                            finalParameters[index] = Float.valueOf(currentRasiLexer.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Float" + currentRasiLexer.parameters[index]);
                            break;
                        case "double":
                            finalParameters[index] = Double.valueOf(currentRasiLexer.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Double" + currentRasiLexer.parameters[index]);
                            break;
                        case "java.lang.String":
                            finalParameters[index] = currentRasiLexer.parameters[index+1];
                            Log.i(LOG_TAG + "Parameter is String", currentRasiLexer.parameters[index]);
                            break;
                        case "boolean":
                            finalParameters[index] = Boolean.valueOf(currentRasiLexer.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Boolean" + currentRasiLexer.parameters[index]);
                            break;
                    }
                }
            } else {
                finalParameters = null;
                Log.d("finalparamsisnull", "true");
            }
            if(finalParameters != null)
                Log.d("rasifinalparams", Arrays.asList(finalParameters).toString());

                Log.d("RasiExecutor", method.toString());
                try {
                    methodsHashMap.get(hashMap.get(command.toLowerCase())).invoke(rasiCommands, finalParameters);
                } catch (IllegalAccessException e) {
                    Log.e("rasiExecutor", "illegalAccessException");
                } catch (InvocationTargetException e) {
                    Log.e("rasiExecutor", "InvocationTargetException");
                }
            command = currentRasiLexer.getCommand();
        }
        popRasi();
    }

    public void runAll(){
        do {
            runRasi();
        } while (queue.size() != 0 && currentRasiLexer != null);
    }

    public void setTags(String[] Tags){ //sets the rasi tags
        currentRasiLexer.setTags(Tags);
    }

    public void addTag(String tag){ //adds a rasi tag
        currentRasiLexer.addTag(tag);
    }

    public void removeTag(String tag){ // removes a rasi tag
        currentRasiLexer.removeTag(tag);
    }

}
