package org.firstinspires.ftc.teamcode.RASI.RasiB;

public class StringUtils {

    public static String deleteFrontWhitespace(String str){
        StringBuilder b = new StringBuilder(str);
        Character c = b.charAt(0);
        while (c == ' '){
            b.deleteCharAt(0);
            c = b.charAt(0);
        }
        return b.toString();
    }
    public static String deleteWhitespace(String str){
        StringBuilder b = new StringBuilder(str);
        int index = 0;
        while (index < b.length()){
            if (b.charAt(index) == ' '){
                b.deleteCharAt(index);
            } else {
                index++;
            }
        }
        return b.toString();
    }
}
