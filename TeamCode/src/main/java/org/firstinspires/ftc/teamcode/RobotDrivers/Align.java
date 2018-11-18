package org.firstinspires.ftc.teamcode.RobotDrivers;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class Align {
    DistanceSensor rightSensor,leftSensor;
    MecanumCont drivebase;


    public Align(DistanceSensor r, DistanceSensor l, MecanumCont drivebase){
        this.rightSensor = r;
        this.leftSensor = l;
        this.drivebase = drivebase;
    }

    public void Align(){




        //math
        double rightDistance = rightSensor.getDistance(DistanceUnit.CM);
       double leftDistance =  leftSensor.getDistance(DistanceUnit.CM);
        double distanceBetweenSensorsCM = 30.48;

        if(rightDistance < leftDistance +1){ //the +1 is to give it some wiggle room so it doesn't ping.
            drivebase.driveDist(0,0, -Math.atan((leftDistance-rightDistance)/distanceBetweenSensorsCM),1);
        }else if(leftDistance<rightDistance+1){  //the +1 is to give it some wiggle room so it doesn't ping.
            drivebase.driveDist(0,0, Math.atan((-leftDistance+rightDistance)/distanceBetweenSensorsCM),1);
        }







    }


}






