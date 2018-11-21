package org.firstinspires.ftc.teamcode.Logic;

import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.Utilities.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Arc;

import java.util.ArrayList;

public class ArcFollower {
    FTCRobotV1 robot;
    ArrayList<Arc> arcQueue = new ArrayList<>();
    Arc currArc;

    //SafeJsonReader reader = new SafeJsonReader("PIDCoefficients");

    double tolerance = 0.5;
    /*
    PIDController pidX = new PIDController(reader.getDouble("KPx"), reader.getDouble("KIx"), reader.getDouble("KDx"));
    PIDController pidY = new PIDController(reader.getDouble("KPy"), reader.getDouble("KIy"), reader.getDouble("KDy"));
    PIDController pidTheta = new PIDController(reader.getDouble("KPθ"), reader.getDouble("KIθ"), reader.getDouble("KDθ"));
    */
    PIDController pidX = new PIDController(0.1, 0.1, 0.1);
    PIDController pidY = new PIDController(0.1, 0.1, 0.1);
    PIDController pidTheta = new PIDController(0.1, 0.1, 0.1);

    double dx = 0;
    double dy = 0;
    double dTheta = 0;


    double steps = 20;

    double prevX = 0;
    double prevY = 0;
    double prevTheta = 0;

    public ArcFollower(FTCRobotV1 robot){
        this.robot = robot;
    }

    public void appendArc(Arc arc){
        if (this.currArc == null){
            this.updateArc(arc);
        }
        else{
            this.arcQueue.add(arc);
        }

    }
    public void updateArc(Arc arc){
        this.currArc = arc;
        if (!arc.getStartPoint().AreSame(robot.getPos(), 1)){
            arcQueue.set(0, arc);
            this.currArc = new Arc(robot.getPos(), arc.getStartPoint(), Math.PI / 2);

        }
    }

    private void update(){
        double l = this.currArc.getLength();
        dx  = l / steps;
        dy  = l / steps;
        dTheta = 0;
    }

    public void next(){
        if (this.currArc.getEndPoint().AreSame(this.robot.getPos(), tolerance)){
            this.currArc = this.arcQueue.get(0);
            this.arcQueue.remove(0);
            update();
        }

        if(this.currArc != null){
            double targetPosX = prevX + dx + pidX.prevError;
            double targetPosY = prevY + dy + pidY.prevError;
            double targetTheta = prevTheta + dTheta + pidTheta.prevError;

            double xCorrection = pidX.getPIDCorrection(targetPosX, robot.getPos().xCord);
            double yCorrection = pidX.getPIDCorrection(targetPosY, robot.getPos().yCord);
            double tCorrection = pidX.getPIDCorrection(targetTheta, robot.getHeading());


            this.robot.driveDist(dx + xCorrection, dy + yCorrection, dTheta + tCorrection, this.getSpeed());

            this.prevX = robot.getPos().xCord;
            this.prevY = robot.getPos().yCord;
            this.prevTheta = robot.getHeading();
        }
    }

    public double getSpeed(){
        return 5;
    }


}
