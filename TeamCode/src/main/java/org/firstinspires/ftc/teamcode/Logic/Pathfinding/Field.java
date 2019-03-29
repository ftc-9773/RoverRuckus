package org.firstinspires.ftc.teamcode.Logic.Pathfinding;

import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Coord.Line;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Coord.Point;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Coord.Polygon;

import java.util.ArrayList;

public class Field {
    ArrayList<Polygon> immutableStuff;
    ArrayList<Polygon> robots;
    ArrayList<Polygon> otherStuff;
    ArrayList<Line> walls;

    Polygon me;

    /**
     * Create an empty field
     * */
    public Field(){
        walls.add(new Line(new Point(-1, -1, 0), new Point(-1, 1, 0)));
        walls.add(new Line(new Point(-1, 1, 0), new Point(1, 1, 0)));
        walls.add(new Line(new Point(1, 1, 0), new Point(1, -1, 0)));
        walls.add(new Line(new Point(1, -1, 0), new Point(-1, -1, 0)));
    }

    /**
     * Create Field from xml file. Generate xml via python script or inkscape or whatever.
     * */
    public Field(String fileName, String filePath){
        //Code.....
    }

    public void updateMe(double dx, double dy, double dt){
        me.move(dx, dy);
        me.rotate(dt);
    }

}
