package org.firstinspires.ftc.teamcode.RASI.RasiB.Core;

import org.firstinspires.ftc.teamcode.RASI.RasiB.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RasiLine{
    int printLineNumber;
    ArrayList<String> tags = new ArrayList<>();
    String method_name; //Name of the method, determined on creation.
    ArrayList<stringWrapper> params_strings = new ArrayList<>(); //determined on creation
    Method method = null;
    Object callableRoot = null;
    Object[] params = null;
    /**
     * Has something to do with parsing strings
     * */
    public class stringWrapper {
        public boolean isString;
        public String str;
        public stringWrapper(String str, boolean isString){
            this.isString = isString;
            this.str = str;
        }
    }

    /**
     * Create and parse a line.
     * */
    public RasiLine(String line, int pln){
        printLineNumber = pln;
        String commandString;
        try { //Extract and parse tags
            String taglistString = line.split(":")[0];
            commandString = StringUtils.deleteFrontWhitespace(line.split(":")[1]);
            String[] alltags = taglistString.split("");
            for (String tag: alltags) {
                if (tag != "") {
                    tags.add(StringUtils.deleteWhitespace(tag));
                }
            }
        } catch(IndexOutOfBoundsException e){ //There are no tags
            commandString = StringUtils.deleteFrontWhitespace(line);
        }
        boolean containsString = commandString.split("\"").length > 1;

        try {
            String parameters;
            method_name = StringUtils.deleteWhitespace(commandString.split(" ", 1)[0]);
            parameters = StringUtils.deleteFrontWhitespace(commandString.split(" ", 1)[1]);
            if (containsString){
                String[] strings = parameters.split("\"");
                if (strings.length % 2 != 0){throw new ArithmeticException("Your Rasi Strings are wrong in line " + printLineNumber);}
                stringWrapper[] stuffs = new stringWrapper[strings.length];
                int magic = parameters.charAt(0) == '"' ? 0 : 1;
                for (int i = 0; i < strings.length; i++){
                    if ((i + magic) % 2 == 0){stuffs[i] = new stringWrapper(strings[i], true);}
                }
                for (stringWrapper bar : stuffs){
                    if (bar.isString){
                        params_strings.add(bar);
                    } else {
                        String[] moreMagic = StringUtils.deleteFrontWhitespace(bar.str).split(" ");
                        for (String str : moreMagic){
                            params_strings.add(new stringWrapper(StringUtils.deleteWhitespace(str), false));
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) { // There are no parameters
            method_name = StringUtils.deleteWhitespace(commandString);
        }
    }

    /**
     * Whether or not to execute the command.
     * */
    public boolean doExecute(ArrayList<String> activeTags){
        //Run stuff here
        boolean doRun = false;
        if (tags.size() > 0){
            for (String tag: tags){
                if(activeTags.contains(tag)){
                    doRun = true;
                    break;
                }
            }
        } else if (!doRun){
            return false; //Don't execute
        }
        return true; //Execute
    }

    /**
     * Add another tag that could be active.
     * */
    public void addTag(String tag){this.tags.add(tag);}

    /**
     * Should be called in RasiInterpreter
     * */
    public void execute() throws InvocationTargetException, IllegalAccessException {
        method.invoke(callableRoot, params);
    }
}
