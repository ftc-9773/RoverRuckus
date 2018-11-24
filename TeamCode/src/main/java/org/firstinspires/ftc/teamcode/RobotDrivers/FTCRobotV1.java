package org.firstinspires.ftc.teamcode.RobotDrivers;


import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.VerticalLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.OdometryController;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;
import org.firstinspires.ftc.teamcode.Utilities.misc.Button;

import java.util.Arrays;

public class FTCRobotV1 {
    public MecanumDrivebase drivebase;
    public Intake intake;
    public OdometryController odometry;
    public Gyro gyro0;

    public CubeLift lift;

    public double heading = 0;
    Telemetry telemetry;
    Point pos;
    double x = 0;
    double y = 0;
    // teleop values
    Button toggleLeftRightButton = new Button();
    boolean leftRightState = false;

    public FTCRobotV1(MecanumDrivebase drivebase, Gyro gyro, Telemetry telemetry) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        //this.OC = odometryController;
        this.gyro0 = gyro;
        gyro0.setZeroPosition();
        this.telemetry = telemetry;
    }

    public FTCRobotV1(MecanumDrivebase drivebase, Gyro gyro, OdometryController odometryController, Telemetry telemetry, CubeLift lift) {
        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        this.odometry = odometryController;
        this.gyro0 = gyro;
        gyro0.setZeroPosition();
        this.telemetry = telemetry;
        this.lift = lift;
    }
    public FTCRobotV1(HardwareMap hwmap, Point startPos, Telemetry telem){
        // position tracking
        gyro0 = hwmap.get(gyro0.getClass(), "imuLeft");
        gyro0.setZeroPosition();
        this.telemetry = telemetry;
        this.pos = startPos;
        this.heading = 0.0;
        // init hardware stuff
        drivebase = new MecanumDrivebase(hwmap, telem);
        odometry = new OdometryController(hwmap);
        lift = new CubeLift(hwmap);
    }

    public void driveVelocity(double xV, double yV, double rotV){
        drivebase.arcadeDrive(xV, yV, rotV);
    }

    public void driveDist(double x, double y, double rotation, double speed){
        drivebase.driveDist(x, y, rotation, speed);
        this.pos.xCord += x;
        this.pos.yCord += y;
        this.heading += rotation;
        //update();
    }

    // teleop functions
    public void runGamepadCommands(Gamepad gp1, Gamepad gp2){
        // update button objects
        toggleLeftRightButton.recordNewValue(gp2.x);
        // drive functions
        driveVelocity(gp1.left_stick_x, -gp1.left_stick_y,gp1.right_stick_x);
        // button push lift positions
        if(gp2.a) lift.goToLowPos();
        else if(gp2.b) lift.goToScorePos();
        else if (gp2.y) lift.goToHangPos();
        // left/right side toggle
        if (toggleLeftRightButton.isJustOn()){
            leftRightState = !leftRightState;
            if(leftRightState)lift.setLefScoreSide();
            else lift.setRightScoreSide();
        }
        // dumping
        if(gp2.right_bumper)lift.dump();
        else lift.stopDump();

        //button push intake functions;
        if(gp2.dpad_down) intake.goToTransfer();
        if(gp2.left_trigger > 0.5) intake.intakeOn();
        else if(gp2.left_bumper) intake.reverseIntake();
        else intake.stopIntake();
        // basic automation
        if(intake.isInTransferState() && lift.isInTransferState()){
            intake.transferMinerals();
        }
        // now hand controls for lifts
        intake.setExtensionPowerFromGamepad(-gp2.left_stick_y);
        lift.adjustLift(-gp2.right_stick_y);


    }

    public Point getPos(){
        //update();
        return this.pos;
    }

    public double getHeading() {
        //update();
        return heading;
    }

    public void update(){
        /*double[] ocPos = this.OC.getPosition();
        this.x = ocPos[0];
        this.y = ocPos[1];
        this.pos = new Point(x, y);
        this.heading = ocPos[2];
        */if (this.gyro0.isUpdated()){this.heading = gyro0.getHeading(true);}
        //telemetry.addData("Got Positions from OC:", Arrays.toString(ocPos));
        if (gyro0.isUpdated()) telemetry.addData("Got heading from Gyro", this.heading);
        telemetry.update();

    }

}
