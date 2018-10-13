package org.firstinspires.ftc.teamcode.Logic.GoalPlanning;

import android.util.JsonReader;

import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractEnvironment;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractGoalPlanner;
import org.firstinspires.ftc.teamcode.Logic.GoalPlanning.Goals.Goal;
import org.firstinspires.ftc.teamcode.Telemetry.Telemetry;

public class SimpleGoalPlanner implements AbstractGoalPlanner{
    //private double remainingTimeMilli = 0.; //Need to figure what this is?
    AbstractEnvironment env;
    Telemetry telemetry;

    public SimpleGoalPlanner(AbstractEnvironment env, Telemetry telemetry){
        this.env = env;
        this.telemetry = telemetry;
        openJSON("GoalList.json");
    }

    public void openJSON(String filename){
        JsonReader rdr = Json.createReader();
    }

    @Override
    public Goal[] getCurrentGoal() {
        return new Goal[0];
    }
}
