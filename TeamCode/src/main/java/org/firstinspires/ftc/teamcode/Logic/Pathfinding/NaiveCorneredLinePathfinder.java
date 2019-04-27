package org.firstinspires.ftc.teamcode.Logic.Pathfinding;

import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Coord.Line;
import org.firstinspires.ftc.teamcode.Logic.Pathfinding.Coord.Point;
import org.firstinspires.ftc.teamcode.Utilities.Interrupters.Interrupter;

public class NaiveCorneredLinePathfinder extends AbstractPathfinder {
    Interrupter i;

    public NaiveCorneredLinePathfinder(Field env, Interrupter i){
        this.env = env;
    }

    @Override
    public Path findQuickestPath(Point targetPoint) {
        Point startPoint = env.me.center;
        Line line = new Line(startPoint, targetPoint);
        Path guess = new Path(line);
        while(!isValidPath(guess) && i.isNotInterrupted()){
            guess = updateGuess(guess);
        }
        return null;
    }

    /**
     * Generates a new path, that is as close as possible to path p
     * */
    private Path updateGuess(Path p){



        return null;
    }

}
