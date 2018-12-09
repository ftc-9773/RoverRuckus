package org.firstinspires.ftc.teamcode.RASI.Rasi_2_5;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Logic.PIDdriveUtil;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;

import java.math.*;

/**
 * Class for defining functions that can be used in RASI. As of version 2.5, there is no way to
 * define functions inside a RASI file.
 *
 * //TODO Create detailed guides and documentation for RASI
 * For a detailed guide on RASI, please see RASI/guides
 *
 * The return type of the function does not matter, and it is best to leave it as void. As of version
 * 2.5, there is no way to store information in a RASI script, all variables should be contained and accessed
 * in this class.
 *
 * RASI is not case sensitive, therefore CreateRobot() and CrEaTeRoBot() will cause errors. Use whichever
 * is more readable. RASI has not been tested with conflicting function names, unknown errors may occur.
 *
 * Reserved function names and their uses:
 * - addTag      : Add the following argument as a tag for this programs execution
 * - endOpMode   : This command ends the LinearOpMode, as if the user pressed the stop button on the device
 * - removeTag   : This command removes a previously added tag from the possible tags during this programs execution.
 *      The tag can still be added back later.
 * Possible future function names. While they will cause issues yet, they should not be used to prevent future incompatibilty
 * - addTags    : Works as addTag except that it adds all arguments as tags
 * - removeTags : Works as removeTag except that it removes all arguments from the Tag list
 * - end        : Terminates the execution of the RASI file
 *
 * Note: Rasi 2.5 is not fully backwards compatible with version 2.0. In particular, end has been replaced by endOpMode and semicolons
 * are not used as statement terminators. Statements must be written on one line, and end with a newline. Otherwise,
 * all other functions should work.
 *
 * ----Please add documentation to the functions you create-----
 * @author caden
 * @version 2.5
 * */

public class RasiCommands {
    Telemetry telemetry;
    //FTCRobotV1 robert;
    LinearOpMode opMode;
    PIDdriveUtil driver;

    @Deprecated
    public RasiCommands(LinearOpMode opMode){
        this.telemetry = opMode.telemetry;
        this.opMode = opMode;
    }

    public RasiCommands(LinearOpMode opMode, FTCRobotV1 robot){
        //this.robert = robot;
        this.telemetry = opMode.telemetry;
        this.opMode = opMode;
        driver = new PIDdriveUtil(robot, opMode);
    }

    /**
     * Move the robot in space
     * @param x distance to drive horizontally in inches
     * @param y distance to drive vertically in inches
     * @param h rotation in radians
     * */
    public void drive(double x, double y, double h){
        driver.driveDistStraight(x, y);
        //driver.turnToAngle(robert.getHeading() + h);
    }

    public void foo(String y){ ;
    }

    /**
     * Write data to telemetry. Does not call telemetry.update()
     * @param caption String to write to telemetry
     * */
    public void Write(double caption){
        telemetry.addLine(caption + "");
    }

    /**
     * Calls telemetry.update() on opMode telemetry
     * */
    public void telemUpdate(){
        telemetry.addLine("telemUpdate");
        telemetry.update();
    }

    /**
     * Stops executing for a duration of time
     * @param timeInSeconds The amount of seconds to pause execution
     * */
    public void Wait(double timeInSeconds){
        System.out.println("Waiting for " + timeInSeconds);
        long startTime = System.currentTimeMillis();
        while(startTime + timeInSeconds*1000 > System.currentTimeMillis() && !opMode.isStopRequested()){continue;}
    }

    /**
     * As Wait but with a duration specified in milliseconds
     * @param milli Amount of milliseconds to pause duration for.
     * */
//    public void waitMilli(double milli){
//      System.out.println("Waiting for " + milli + " milliseconds");
//      long startTime = System.currentTimeMillis();
//      while(startTime + milli > System.currentTimeMillis() && !opMode.isStopRequested()){continue;}
//    }
}
