package org.firstinspires.ftc.teamcode.RASI.Rasi_2_5;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.Vision.MyGoldDetector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private boolean isnull;
    private int numberOfParams;
    private String LOG_TAG = "RasiInterpreter";
    private RasiLexer rasiParser;
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

    public RasiInterpreter(String filepath, String filename, LinearOpMode opmode, FTCRobotV1 robot){
        this.linearOpMode = opmode;
        rasiCommands = new RasiCommands(opmode, robot);
        rasiParser = new RasiLexer(filepath, filename, opmode);
        hashMap = new HashMap<String, String>();
        infoHashmap = new HashMap<String, String[]>();
        methodsHashMap = new HashMap<String, Method>();


        for(int x = 0; x < rasiCommands.getClass().getMethods().length; x++){ //runs for every method in the TeamRasiCommands Class
            Log.d(LOG_TAG, x + "");

            if(rasiCommands.getClass().getMethods()[x].toString().contains("RasiCommands.")){ //filters out the stuff that java puts there and hides.

                method = rasiCommands.getClass().getMethods()[x];
                methodString = method.toString();
                Log.d(LOG_TAG, "Initial String was " + methodString);
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
                Log.d(LOG_TAG, "Method String:" + methodString);
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
//        System.out.println("HashMap is " + hashMap);
//        System.out.println("Methods are " + methodsHashMap);
//        System.out.println("Info is " + infoHashmap);
        Log.i("RasiCommands is null", (rasiCommands == null) + "  ");
    }

    public void runRasi() {
        Log.d(LOG_TAG, "IN RUN RASI");
        command = rasiParser.getCommand();
        while (!rasiParser.fileEnded && !linearOpMode.isStopRequested()) {

            Log.d(LOG_TAG, "RasiCommand:" + hashMap.get(command.toLowerCase()));
//            Log.d(LOG_TAG, "infohashmapout: " + infoHashmap.get(hashMap.get(command.toLowerCase())).toString());
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
                            finalParameters[index] = Integer.valueOf(rasiParser.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Int" + rasiParser.parameters[index]);
                            break;
                        case "char":
                            finalParameters[index] = rasiParser.parameters[index+1].charAt(0);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Char" + rasiParser.parameters[index]);
                            break;
                        case "long":
                            finalParameters[index] = Long.valueOf(rasiParser.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Long" + rasiParser.parameters[index]);
                            break;
                        case "float":
                            finalParameters[index] = Float.valueOf(rasiParser.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Float" + rasiParser.parameters[index]);
                            break;
                        case "double":
                            finalParameters[index] = Double.valueOf(rasiParser.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Double" + rasiParser.parameters[index]);
                            break;
                        case "java.lang.String":
                            finalParameters[index] = rasiParser.parameters[index+1];
                            Log.i(LOG_TAG + "Parameter is String", rasiParser.parameters[index]);
                            break;
                        case "boolean":
                            finalParameters[index] = Boolean.valueOf(rasiParser.parameters[index+1]);
                            Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Boolean" + rasiParser.parameters[index]);
                            break;
                    }
                }
            } else {
                finalParameters = null;
                Log.d("finalparamsisnull", "true");
            }
            rasiCommands.Wait(1);
            if(finalParameters != null)
                Log.d("rasifinalparams", Arrays.asList(finalParameters).toString());

                Log.d(LOG_TAG, method.toString());
                Log.d(LOG_TAG, command.toLowerCase());
                try {
                    String command_lower = command.toLowerCase();
                    Log.d("Command: ",command_lower);
                    String hash = hashMap.get(command_lower);
                    Log.d("Hash", hash + " ");
                    Method method = methodsHashMap.get(hash);
                    Log.d("Method:", method.toString());
                    //methodsHashMap.get(hashMap.get(command.toLowerCase())).invoke(rasiCommands, finalParameters);
                    if (rasiCommands == null){Log.i("Rasi:", "TeamRasiCommands is null");}
                    if (rasiCommands == null){Log.i("Rasi:", "TeamRasiCommands is null");}
                    else{Log.d("RasiCommands", rasiCommands.toString());}
                    if (finalParameters != null){
                    Log.d("finalParameters", finalParameters.toString());}
                    Log.d("Final Parameters", finalParameters.toString());
                    method.invoke(rasiCommands, finalParameters);
                } catch (IllegalAccessException e) {
                    Log.e(LOG_TAG, "illegalAccessException", e);
                } catch (InvocationTargetException e) {
                    Log.e(LOG_TAG, "InvocationTargetException", e);
                } catch (NullPointerException e){
                    Log.e(LOG_TAG, "NullPointerException", e);
                }
            command = rasiParser.getCommand();

        }
    }
    public void setTags(String[] Tags){ //sets the rasi tags
        rasiParser.setTags(Tags);
    }
    public void addTag(String tag){ //adds a rasi tag
        rasiParser.addTag(tag);
    }
    public void removeTag(String tag){ // removes a rasi tag
        rasiParser.removeTag(tag);
    }

}
