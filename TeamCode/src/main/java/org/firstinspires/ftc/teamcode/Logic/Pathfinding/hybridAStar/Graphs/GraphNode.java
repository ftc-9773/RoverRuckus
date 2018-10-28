//package org.firstinspires.ftc.teamcode.Logic.Pathfinding.Graphs;
//
////import org.firstinspires.ftc.teamcode.Logic.AbstractEnvironment;
//
//
//public class GraphNode implements Comparable<GraphNode>{
//    public AbstractEnvironment environment;
//    public int[] discretePosition;
//    public double heading;
//    public double[] Position;
//    public double cumulativeCost;
//    public int priorityQueueKey;
//    public GraphNode backpointer;
//
//    public GraphNode(AbstractEnvironment environment, int[] discretePosition,double[] Position, double heading, double cost, int priorityQueueKey, GraphNode backpointer){
//        this.environment = environment;
//        this.discretePosition = discretePosition;
//        this.Position = Position;
//        this.heading = heading;
//        this.cost = cost;
//        this.priorityQueueKey = priorityQueueKey;
//        this.backpointer = backpointer;
//    }
//
//    public GraphNode(AbstractEnvironment environment, double[] Position, double heading, double cost, int priorityQueueKey, GraphNode backpointer){
//        this(environment,new double[]{environment.getDiscretizeX(heading[0]),environment.getDiscretizeY(heading[1])},cost,priorityQueueKey,backpointer);
//    }
//
//    public int compareTo(GraphNode o){
//        return Integer.compare(this.priorityQueueKey, o.priorityQueueKey);
//    }
//}
