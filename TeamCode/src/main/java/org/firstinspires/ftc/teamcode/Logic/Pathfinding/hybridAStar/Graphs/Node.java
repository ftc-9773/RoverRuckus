package org.firstinspires.ftc.teamcode.Logic.Pathfinding.Graphs;

import android.support.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractEnvironment;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;

import java.util.List;

public class Node implements Comparable {
    Arc motionPrimitive;
    Point discreetPosition;
    Point position;
    double totalDistance = 0;
    double EstimatedRemainingCost;
    Node prevNode;

    public Node(AbstractEnvironment env, Point currPoint, double prec, double headingPrecision,
                double heading, Node prevNode){
                    this.position = currPoint;
                    discreetX = currPoint.xCord - (currPoint.xCord % prec);
                    discreetY = currPoint.yCord - (currPoint.yCord % prec);
                    this.discreetPosition = new Point(discreetX, discreetY);
                    this.prevNode = prevNode;

    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }

    public Point getPosition(){
        return this.position;
    }
    public List<Double> genValidHeadings(){
        return null;
    }
    public Node getNextNode(double headingStep){
        return null;
    }

    public boolean isSameNode(Node node){
        if (node.position == this.position && node.movement == this.movement){
            return true;
        }

        return false;
    }
}
