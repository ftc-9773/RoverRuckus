package org.firstinspires.ftc.teamcode.RASI.RasiB.RasiExceptions;

public class RasiPreproccesingException extends Exception {

    public RasiPreproccesingException(){
        super("An unknown error occured in RasiPreprocceser");
    }

    public RasiPreproccesingException(String mes){
        super(mes);
    }
}
