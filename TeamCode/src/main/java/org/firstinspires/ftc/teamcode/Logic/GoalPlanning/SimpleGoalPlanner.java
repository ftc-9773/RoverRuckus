//package org.firstinspires.ftc.teamcode.Logic.GoalPlanning;
//
//import android.util.JsonReader;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractEnvironment;
//import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractGoalPlanner;
//import org.firstinspires.ftc.teamcode.Logic.GoalPlanning.Goals.Goal;
//
//public class SimpleGoalPlanner implements AbstractGoalPlanner{
//    //private double remainingTimeMilli = 0.; //Need to figure what this is?
//    AbstractEnvironment env;
//    Telemetry telemetry;
//    public SimpleGoalPlanner(AbstractEnvironment env, Telemetry telemetry){
//        this.env = env;
//        this.telemetry = telemetry;
//        openJSON("GoalList.json");
//    }
//
//    public void openJSON(String filename){
//        // TODO: implement this
//    }
//
//    @Override
//    public Goal[] getCurrentGoal() {
//        return new Goal[0];
//    }
//}
