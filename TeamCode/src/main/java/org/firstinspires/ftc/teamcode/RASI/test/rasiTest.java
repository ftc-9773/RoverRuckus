package org.firstinspires.ftc.teamcode.RASI.test;

import org.firstinspires.ftc.teamcode.RASI.Rasi_2_5.RasiInterpreter;

public class rasiTest{
  public static void main(String[] args) {
    System.out.print("Init\n");
    RasiInterpreter i = new RasiInterpreter("", args[0]);
    i.runRasi();
  }
}
