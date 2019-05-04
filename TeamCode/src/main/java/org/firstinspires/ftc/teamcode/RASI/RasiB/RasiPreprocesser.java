package org.firstinspires.ftc.teamcode.RASI.RasiB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Class for preprocessing the input
 * Returns an array of lines. Does some basic stuff.
 * */
public class RasiPreprocesser {
    private BufferedReader reader; // Get input from the file
    private HashMap<String, Object> defs;


    public RasiPreprocesser(String fileName, String filePath){
        try {
            reader = new BufferedReader(new FileReader(new File(filePath + fileName + ".rasi")));
        } catch (FileNotFoundException e){
            //Alert user file was not found
        }
        

    }
}
