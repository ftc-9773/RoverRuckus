package org.firstinspires.ftc.teamcode.Logic.Pathfinding;

import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Coord.Line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Path {
    public ArrayList<Line> lines;

    public Path(List<Line> l){
        lines = new ArrayList<>(l);
    }
    public Path(Line l){
        lines = new ArrayList<>();
        lines.add(l);
    }
}
