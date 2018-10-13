package org.firstinspires.ftc.teamcode.Logic.Obstacles;

public abstract class ImmutableObstacle extends Obstacle{
	public String Name;
	public ImmutableObstacle(String Name){
		super(Name);
	}

	public abstract int[][] getValidIdxs();
}