package org.firstinspires.ftc.teamcode.Logic.Pathfinding.Graphs;


/**
 * @author Cadence
 * @version 2018-7-19
 * For algorithem implemented by Pathfinder (Hybrid A*)
 * */
public class MotionPrimitive {
    float minDistance = (float) Math.sqrt(2);
    float maxCurveture = Math.PI / 2; //Probably should hardcode this
    float headingChangeStep;

    public MotionPrimitive(float minDistance, float maxCurveture, float headingChangeStep){
        this.minDistance = this.minDistance * minDistance;
        this.maxCurveture = maxCurveture;
        this.headingChangeStep = headingChangeStep;
    }
}
