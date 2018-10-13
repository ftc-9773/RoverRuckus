package org.firstinspires.ftc.teamcode.Logic.Environment;

import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractEnvironment;
import org.firstinspires.ftc.teamcode.Utilities.Core.Measurement;
import org.firstinspires.ftc.teamcode.Logic.Obstacles.Obstacle;
import org.firstinspires.ftc.teamcode.Logic.Obstacles.ImmutableObstacle;

/**
* Map of the playfield in which the robot resides
* @author Jonathan
* @version 2018-7-20
*/
public class Environment implements AbstractEnvironment{
    // Basic information
    private int xRes; // x Resolution of the map
    private int yRes; // y Reolution of the map
    public Measurement cellSize; // Size of cell, unit of length, basis of unit conversions
    public Measurement[] origin;

    //private enum inferenceTypes; // TODO

    // Hardcoded or observed obstacles with distinct shapes and very high confidence of existence, takes presidence over
    public List<Obstacle> ObstacleLedger = new ArrayList<Obstacle>(); // List of all high confidence obstacles
    private byte[][] ObstacleMap; // Known obstacles on each cell, incremented or decremented as such objects are "created" and "destroyed"

    // Observations which are continually taken from sensors and may override
    public double ObservationProbablityThreshold; // How high the probablity of a real observation to be considered an obstacle
    private double[][] ObservationProbablityMap; // May be of use, internally it is a double array

    public Environment(int xRes, int yRes, Measurement cellSize, double ObservationProbablityThreshold){
        this.xRes = xRes;
        this.yRes = yRes;
        this.cellSize = cellSize;
        this.origin = new Measurement[2];
        origin[0] = new Measurement(0., cellSize.unit,0.,"EnvMap Origin X");
        origin[1] = new Measurement(0., cellSize.unit,0.,"EnvMap Origin Y");

        this.ObstacleLedger = new ArrayList<Obstacle>();
        this.ObstacleMap = new byte[xRes][yRes];

        this.ObservationProbablityThreshold = ObservationProbablityThreshold;
        this.ObservationProbablityMap = new double[xRes][yRes];
    }

    // Get discrete indexes from Measurements
    public int getDiscretizeX(Measurement X)throws Exception{return this.getDiscretizeX((X.convert(cellSize.unit).value));}
    public int getDiscretizeY(Measurement Y)throws Exception{return this.getDiscretizeX((Y.convert(cellSize.unit).value));}

    /**
    * Writes probablities to the observation map
    * @param idxs Integer array of indices in the with shape (?,2) each entry corresponds to the x and y indices of a cell in the ObservationProbablityMap
    * @param probablities Array of probabilites which corresponds to the each entry in the idx parameter
    * @param override If set to true, results in the probability values in the map being replaced by those specified in the probabilites parameter, else will combine the probabilites with the prior (multiply for now)
    * Note, more inference methods may be added in the future
    */
    public void writeToObservationProbabilityMap(int[][] idxs, double[] probablities, boolean override) {
        if (override) {
            for (int i=0;i<probablities.length ;i++) {
                ObservationProbablityMap[idxs[i][0]][idxs[i][1]] = probablities[i];
            }
        }
        else {
            for (int i=0;i<probablities.length ;i++) {
            ObservationProbablityMap[idxs[i][0]][idxs[i][1]] *= probablities[i];
            }
        }
    }

    public void addObstacle(Obstacle myObstacle){
        ObstacleLedger.add(myObstacle);
        int[][] ValidIdxs = myObstacle.getValidIdxs();
        for(int[] i:ValidIdxs) {
            this.safeIncrement(i[0],i[1]);
        }
    }

    public void removeObstacle(Obstacle myObstacle) {
        // Remove obstacle from ledger
        // ...
        int[][] ValidIdxs = myObstacle.getValidIdxs();
        for(int[] i:ValidIdxs) {
            this.safeDecrement(i[0],i[1]);
        }
    }

    public void redrawObstacle(Obstacle myObstacle) { // TODO: IMPLEMENT
        // Delete prior location
        // ...
        // removeObstacle(...)
        // Get new location
        // adObstacle(...)
        ;
    }

    /**
    * @param myObstacle, instance of the ImmutableObstacle type, once added to the Enviroment, cannot be removed or changed.
    */
    public void addObstacle(ImmutableObstacle myObstacle){
        ObstacleLedger.add(myObstacle);
        int[][] ValidIdxs = myObstacle.getValidIdxs();
        for(int[] i:ValidIdxs) {
            ObstacleMap[i[0]][i[1]] = -1;
        }
    }

    public boolean isObstacle(Measurement X, Measurement Y) throws Exception{
        return this.isObstacle(this.getDiscretizeX(X),this.getDiscretizeY(Y));
    }

    public boolean isObstacle(int[] idxs) {
        return this.isObstacle(idxs[0],idxs[1]);
    }

    public boolean isObstacle(int xIdx, int yIdx) {
        boolean ObstacleBoolMapVote = (ObservationProbablityMap[xIdx][yIdx]>=ObservationProbablityThreshold);
        boolean ObstacleMapVote = (ObservationProbablityMap[xIdx][yIdx]>0);
        return ObstacleBoolMapVote||ObstacleMapVote; // Both parties must agree for an area to be considered to be non-obstacle  
    }

    //Prevents overflow and makes sure ImmovableObstacle locations are never manipulated
    private void safeIncrement(int x,int y){if(ObstacleMap[x][y] >= 0 && ObstacleMap[x][y]<127){ObstacleMap[x][y]++;}}
    private void safeDecrement(int x,int y){if(ObstacleMap[x][y] > 0){ObstacleMap[x][y]--;}}

    // get Discrete indicies for pathfinding
    private int getDiscretizeX(double X){return (int) Math.floor((X-origin[0].value)/this.cellSize.value);}
    private int getDiscretizeY(double Y){return (int) Math.floor((Y-origin[1].value)/this.cellSize.value);}

    // public void flush(String FileName) throws IOException{ //Writes all data to a file
    //     BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename));

    // }
}
