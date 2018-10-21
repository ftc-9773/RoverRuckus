package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/***
public class OdometryController {

    // Encoders
    private DcMotor encoder1;
    private DcMotor encoder2;
    private DcMotor encoder3;

    //Last Encoder Position
    private long lastEncoderPos1 = 0;
    private long lastEncoderPos2 = 0;
    private long lastEncoderPos3 = 0;


    // time
    private long lastTime;

    // Last Position
    private double lastX;
    private double lastY;
    private double lastAng;

    // Last Velocity
    private double lastXVel;
    private double lastYVel;
    private double lastAngVel;

    // Last Acceleration
    private double lastXAcel;
    private double lastYAcel;
    private double lastAngAcel;

    // Current Position
    public double curX;
    public double curY;
    public double curAng;

    // Current Velocity
    public double curXVel;
    public double curYVel;
    public double curAngVel;

    // Current Acceleration
    public double curXAcel;
    public double curYAcel;
    public double curAngAcel;

    // Current Jerk
    public double curXJerk;
    public double curYJerk;
    public double curAngJerk;

    private final static double[][] inverseMatrix = {{-0.66807589, -0.60518156,  0.94387478},
                                                     { 0.11784677,  1.33008978, -0.72400748},
                                                     { 0.08750452,  0.08525622,  0.00385007}};

    public OdometryController(HardwareMap hwMap, double X, double Y, double Ang) {
      encoder1 = hwMap.dcMotor.get("encoder1");
      encoder2 = hwMap.dcMotor.get("encoder2");
      encoder3 = hwMap.dcMotor.get("encoder3");

      this.curX = X;
      this.curY = Y;
      this.curAng = Ang;
    }


    public void update(){
        //Distance from the last time calculated
        int curPos1 = encoder1.getCurrentPosition();
        int curPos2 = encoder2.getCurrentPosition();
        int curPos3 = encoder3.getCurrentPosition();

        int d1 = (int)(curPos1 - lastEncoderPos1);
        int d2 = (int)(curPos2 - lastEncoderPos2);
        int d3 = (int)(curPos3 - lastEncoderPos3);


        // Updating X, Y, and Ang
        curX += inverseMatrix[0][0] * d1 + inverseMatrix[0][1] * d2 + inverseMatrix[0][2] * d3;
        curY += inverseMatrix[1][0] * d1 + inverseMatrix[1][1] * d2 + inverseMatrix[1][2] * d3;
        curAng += inverseMatrix[2][0] * d1 + inverseMatrix[2][1] * d2 + inverseMatrix[2][2] * d3;


        // Current Velocity, Acceleration, Jerk

        long curTime = System.currentTimeMillis();
        int dt = (int)(curTime - lastTime);

        // Velocity
        curXVel = (curX - lastX) / dt;
        curYVel = (curY - lastY) / dt;
        curAngVel = (curAng - lastAng) / dt;


        // Acceleration
        curXAcel = (curXAcel - lastXVel) / dt;
        curYAcel = (curYAcel - lastYVel) / dt;
        curAngAcel = (curAngAcel - lastAngVel) / dt;

        // Jerk
        curXJerk = (curXAcel - lastXAcel) / dt;
        curYJerk = (curYAcel - lastYAcel) / dt;
        curAngJerk = (curAngAcel - lastAngAcel) / dt;


        //Updating variables
        lastX = curX;
        lastY = curY;
        lastAng = curAng;

        lastEncoderPos1 = curPos1;
        lastEncoderPos2 = curPos2;
        lastEncoderPos3 = curPos3;

        lastXVel = curXVel;
        lastYVel = curYVel;
        lastAngVel = curAngVel;

        lastXAcel = curXAcel;
        lastYAcel = curYAcel;
        lastAngAcel = curAngAcel;

        lastTime = curTime;
    }
    }
}
***/