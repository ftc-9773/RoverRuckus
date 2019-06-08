package org.firstinspires.ftc.teamcode.RASI.RasiB.Core;

import java.lang.reflect.Method;
import java.util.HashMap;

public class RasiMethodParser {
    public RasiScript rasiScript;
    public HashMap<String, Method> methodHashMap = new HashMap<>();
    public HashMap<String, Object> commandHashMap = new HashMap<>();

    public RasiMethodParser(RasiScript rs){
        this.rasiScript = rs;
    }

    public void addAllMethods(Object o){
        Method method;
        String methodString;
        StringBuilder stringBuilder;
        for(int x = 0; x < o.getClass().getMethods().length; x++){ //runs for every method in the TeamRasiCommands Class
            if(o.getClass().getMethods()[x].toString().contains(o.getClass().getName().split(".")[o.getClass().getName().split(".").length - 1])){ //filters out the stuff that java puts there and hides.
                method = o.getClass().getMethods()[x];
                methodString = method.toString();
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
                String[] tempArray = methodString.split("\\(");
                tempArray = tempArray[0].split("\\."); //set mixedCaseString to the name of the method. removes the remaining parenthesis and dots.
                methodString = tempArray[tempArray.length-1];
                methodHashMap.put(methodString, method);
            }
        }
    }

    public void addMethod(Method method, Object o){
        String methodString = method.toString();
        StringBuilder stringBuilder = new StringBuilder(methodString); //StringBuilder to format the method text to be more usable.
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
        String[] tempArray = methodString.split("\\(");
        tempArray = tempArray[0].split("\\."); //set mixedCaseString to the name of the method. removes the remaining parenthesis and dots.
        methodString = tempArray[tempArray.length-1];
        methodHashMap.put(methodString, method);
        commandHashMap.put(methodString, o);
    }

    public RasiScript proccessRasiScript(){
        return this.proccessRasiScript(this.rasiScript);
    }

    public RasiScript proccessRasiScript(RasiScript rs){
        String methodName;
        RasiLine currLine;
        for (int i = 0; i < rs.rasiLines.size(); i++) {
            currLine = rs.rasiLines.get(i);
            methodName = currLine.method_name;
            currLine.method = methodHashMap.get(methodName);
            currLine.callableRoot = commandHashMap.get(methodName);
        }
        return rs;
    }

}
