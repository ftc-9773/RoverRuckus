package org.firstinspires.ftc.teamcode.RASI.RasiB;

import java.util.ArrayList;

public class RasiLine{
    int lineNumber;
    int printLineNumber;
    boolean is_goto;
    int target = -1;
    ArrayList<String> tags = new ArrayList<>();
    String method;
    ArrayList<String> params = new ArrayList<>();

    private class foo{
        public boolean isString;
        public String str;
        public foo(String str, boolean isString){
            this.isString = isString;
            this.str = str;
        }
    }

    public RasiLine(String line){
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
            method = StringUtils.deleteWhitespace(commandString.split(" ", 1)[0]);
            parameters = StringUtils.deleteFrontWhitespace(commandString.split(" ", 1)[1]);
            if (containsString){
                String[] strings = parameters.split("\"");
                if (strings.length % 2 != 0){throw new ArithmeticException("Your Rasi Strings are wrong in line " + printLineNumber);}
                foo[] stuffs = new foo[strings.length];
                int magic = parameters.charAt(0) == '"' ? 0 : 1;
                for (int i = 0; i < strings.length; i++){
                    if ((i + magic) % 2 == 0){stuffs[i] = new foo(strings[i], true);}
                }
                for (foo bar : stuffs){
                    if (bar.isString){
                        params.add(bar.str);
                    } else {
                        String[] moreMagic = StringUtils.deleteFrontWhitespace(bar.str).split(" ");
                        for (String str : moreMagic){
                            params.add(StringUtils.deleteWhitespace(str));
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) { // There are no parameters
            method = StringUtils.deleteWhitespace(commandString);
        }
        if (method == "goto"){
            is_goto = true;
            target = Integer.valueOf(params.get(0));
        }

    }
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

    public void addTag(String tag){this.tags.add(tag);}
}
