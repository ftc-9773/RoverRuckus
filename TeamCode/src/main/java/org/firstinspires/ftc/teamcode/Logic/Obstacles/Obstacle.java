package org.firstinspires.ftc.teamcode.Logic.Obstacles;

public abstract class Obstacle{
	public String Name;
	public Obstacle(String Name){
		this.Name = Name;
	}

	public abstract int[][] getValidIdxs();
}