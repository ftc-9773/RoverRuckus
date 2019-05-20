package org.firstinspires.ftc.teamcode.RASI.RasiB.Testing;

import org.firstinspires.ftc.teamcode.RASI.RasiB.ExtendableRasiCommands;

public class NonAndroidUtils extends ExtendableRasiCommands {

    public NonAndroidUtils(){}

    public void printString(String str){
        System.out.println(str);
    }

    public void addAndPrintAB(int a, int b){
        System.out.print("" + a + " + " + b + " = " + a + b);
    }
}
