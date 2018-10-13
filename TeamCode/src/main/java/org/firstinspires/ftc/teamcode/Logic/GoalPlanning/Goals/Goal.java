package org.firstinspires.ftc.teamcode.Logic.GoalPlanning.Goals;

import org.firstinspires.ftc.teamcode.Interfaces.AbstractRobot;

public abstract class Goal {
    // FLAGs points to which robot mechanism this goal references
    public enum FLAGS  {DRIVE_TO, DRIVE_DIST, INTAKE, CUBE_TRAY, JEWEL_ARM, RELIC_ARM /*etc ...*/ }; //whether or not this is part of the current goal
    public FLAGS flag;
    private AbstractRobot.COMPLETION_STATE isComplete = null;

    public FLAGS getType(){
        return this.flag;
    };

    public AbstractRobot.COMPLETION_STATE isComplete(){return this.isComplete;}
    public void setComplete(AbstractRobot.COMPLETION_STATE state){this.isComplete = state;}
}
