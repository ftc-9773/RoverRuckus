package org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;

import java.security.InvalidParameterException;

public class MecanumScorer{

    Servo leftServo, rightServo, tiltServo;

    double rsUp, rsDn, lsUp, lsDn, tsR, tsL;
    double dist, start;
    SafeJsonReader reader = new SafeJsonReader("ScorerServoPosition");

    public MecanumScorer(HardwareMap hwmp, String rsn, String lsn, String tsn){
        leftServo = hwmp.get(leftServo.getClass(), lsn);
        rightServo = hwmp.get(rightServo.getClass(), rsn);
        tiltServo = hwmp.get(tiltServo.getClass(), rsn);

        rsUp = reader.getDouble("rightServoUpPostion");
        rsDn = reader.getDouble("rightServoDownPosition");
        lsUp = reader.getDouble("leftServoUpPosition");
        lsDn = reader.getDouble("leftServoDownPostion");
        tsR = reader.getDouble("tiltServoRightPosition");
        tsL = reader.getDouble("tiltServoLeftPostion");
        dist = tsR - tsL  < 0 ? tsL - tsR : tsR - tsL;
        start = tsR > tsL ? tsR : tsL;
    }

    public void leftDown(){
        this.leftServo.setPosition(lsDn);
    }
    public void leftUp(){
        this.leftServo.setPosition(lsUp);
    }
    public void rightDown(){
        this.rightServo.setPosition(rsDn);
    }
    public void rightUp(){
        this.rightServo.setPosition(rsUp);
    }
    public void tiltLeft(){
        this.tiltServo.setPosition(tsL);
    }
    public void tiltRight(){
        this.tiltServo.setPosition(tsR);
    }
    public void tiltPerc(double perc) throws InvalidParameterException{
        if(!(0 <= perc && perc <=1)){
            throw new InvalidParameterException(String.format("Invalid Percentage %d; Percentages must be between 0 and 1, inclusive", perc));
        }
        this.tiltServo.setPosition(dist * perc + start);
    }
}
