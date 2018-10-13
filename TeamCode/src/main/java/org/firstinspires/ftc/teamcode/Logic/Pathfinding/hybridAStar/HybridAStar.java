package org.firstinspires.ftc.teamcode.Logic.Pathfinding.hybridAStar;


import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractRobot;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractEnvironment;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Abstracts.AbstractPathfinder;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Path;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Graphs.Node;

import java.util.ArrayList;
import java.util.Collections;

/**@author Cadence
 * @version 2018-07-22
 *Implements the algorithm in the paper.
 * */

public class HybridAStar extends AbstractPathfinder {
    AbstractRobot robot;
    AbstractEnvironment env;
    double headingPrec = 0.1;
    double positionPrec = 0.01;

    public HybridAStar(AbstractRobot robot, AbstractEnvironment environment) {
        this.robot = robot;
        this.env = environment;
    }
    public Path getPath(Point p, double c, double a, double b){
        return new Path();
    }
    public Path getPath(Point toPoint, double tolerance) {
        ArrayList<Node> ProccesingQueue = new ArrayList<>();
        ArrayList<Node> ProccessedNodes = new ArrayList<>();

        Node rootNode = new Node(this.env, toPoint, this.positionPrec, this.headingPrec ,this.robot.getHeading());

        ProccesingQueue.add(rootNode);

        while (ProccesingQueue.size() != 0){
            int minNodeID = ProccesingQueue.indexOf(Collections.min(ProccesingQueue));
            Node currNode = ProccesingQueue.get(minNodeID);
            ProccesingQueue.remove(minNodeID);
            ProccessedNodes.add(currNode);

            if (currNode.getPosition().AreSame(toPoint, (float) tolerance)) {
                return constructPath(currNode);
            }
            else{
                updateNeigbors(ProccesingQueue, ProccessedNodes, currNode);
            }
        }


        return null;
    }

    private Path constructPath(Node node){
        return null;
    }

    private void updateNeigbors(ArrayList O, ArrayList C, Node node){
        for(double heading:node.genValidHeadings()){
            Node nextNode = node.getNextNode(heading);

            if(!C.contains(nextNode)){
                if(this.env.isObstacle(nextNode.getPosition())){
                    C.add(nextNode);
                }
                Object checkNodes[] = isUnique(O ,nextNode);

                if(checkNodes.length == 1){
                    O.add(nextNode);
                    break;
                }
                else {
                    int index = (int) checkNodes[2];
                    if( (Node) checkNodes[0].compareTo((Node) checkNodes[1]) > 0){

                    }
                }
            }

        }
        return;
    }

    /**Goes through input array checking if a Node is found within it.
     * If so returns {Node InputNode, Node FoundNode, int indexOfFoundNode}
     * Otherwise it returns {Node InputNode}
     * */
    private Object[] isUnique(ArrayList<Node> O, Node node){
        Node outnode = null;
        int outindex = -1;

        for (int index = 0; index < O.size(); index++){
            if (node == O.get(index)){ //Need different test
                outnode = O.get(index);
                outindex = index;
                break;
            }
        }
        if (outnode == null){
            Object[] output = {node};
            return output;
        }

        Object[] output = {node, outnode, outindex};
        return output;
    }


    /**Gets a path to a set of Points to be visited in order they were passed
     *
     * */
    public Path getPaths(double tolerance, Point ... wayPoints){
        return null;
    }

}
