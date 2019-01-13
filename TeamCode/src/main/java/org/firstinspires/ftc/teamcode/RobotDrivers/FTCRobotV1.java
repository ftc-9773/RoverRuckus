package org.firstinspires.ftc.teamcode.RobotDrivers;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.V1.CubeLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments.V1.Intake;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors.OdometryController;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Point;
import org.firstinspires.ftc.teamcode.Utilities.Geometry.Vector;
import org.firstinspires.ftc.teamcode.Utilities.json.SafeJsonReader;
import org.firstinspires.ftc.teamcode.Utilities.misc.Button;

import java.util.Arrays;

/**
 * Class for manipulating the robot
 * */
public class FTCRobotV1 {
    //Attachments
    public MecanumDrivebase drivebase;
    public Intake intake;
    public OdometryController odometry;
    public CubeLift lift;

    //sensors
    public Gyro gyro;

    //state
    double heading = 0;
    Telemetry telemetry;
    Point pos;

    //helpers
    boolean usingOdometry = false;
    boolean usingGyros = false;
    double iter = 0;

    //Tracking position
    double[] lastEncoderPos;

    // teleop values
    Button toggleLeftRightButton = new Button();
    boolean leftRightState = false;

    double slowFactor;

    public FTCRobotV1(MecanumDrivebase drivebase, Gyro gyro, Telemetry telemetry, CubeLift lift, Intake intake) {
        SafeJsonReader reader = new SafeJsonReader("FTCRobotJson");
        slowFactor = reader.getDouble("SlowFactor", 0.3);

        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        if (reader.getBoolean("RunWithEncoders"))
            this.drivebase.runWithEncoders();
        else
            this.drivebase.runWithoutEncoders();
        //this.OC = odometryController;
        this.gyro = gyro;
        this.gyro.setZeroPosition();
        this.telemetry = telemetry;
        this.lift = lift;
        this.intake = intake;
    }

    public FTCRobotV1(MecanumDrivebase drivebase, Telemetry telemetry, CubeLift lift, Intake intake){
        SafeJsonReader reader = new SafeJsonReader("FTCRobotJson");
        slowFactor = reader.getDouble("SlowFactor", 0.3);


        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        if (reader.getBoolean("RunWithEncoders"))
            this.drivebase.runWithEncoders();
        else
            this.drivebase.runWithoutEncoders();
        //this.OC = odometryController;
        this.telemetry = telemetry;
        this.lift = lift;
        this.intake = intake;

    }

    public FTCRobotV1(MecanumDrivebase drivebase,Telemetry telemetry) {
        SafeJsonReader reader = new SafeJsonReader("FTCRobotJson");
        slowFactor = reader.getDouble("SlowFactor", 0.3);


        this.pos = new Point( 0, 0);
        this.heading = 0;
        this.drivebase = drivebase;
        this.gyro = gyro;
        this.gyro.setZeroPosition();
        this.telemetry = telemetry;
        this.lift = lift;
    }

    public FTCRobotV1(HardwareMap hwmap, Point startPos, LinearOpMode op, Telemetry telem, boolean reZero){
        // position tracking
        this.telemetry = telem;
        this.pos = startPos;
        this.heading = 0.0;
        // init hardware stuff
        drivebase = new MecanumDrivebase(hwmap, telem);
        odometry = new OdometryController(hwmap);
        lift = new CubeLift(hwmap,reZero);
        intake = new Intake(hwmap, op, reZero);
    }

    /**
     * Drive at velocity [xV, yV] and rotate at velocity rotV
     * Does not scale inputs for gamepad input, use driveSpeedScaled instead.
     * @param xV velocity left
     * @param yV velcoty fowards
     * @param rotV angular velocity
     * */
    public void driveSpeed(double xV, double yV, double rotV){
        drivebase.drive(xV, yV, rotV, false);
    }

    /***
     * As driveSpeed but scaled for more precision at lower input values.
     */
    public void driveSpeedScaled(double x, double y, double rot) {
        Vector vec = new Vector(true, x, y);
        //vec.rotateVector(heading);
        drivebase.drive(vec.getX(), vec.getY(), rot, true);
    }

    // teleop functions
    public void runGamepadCommands(Gamepad gp1, Gamepad gp2){
        // readSensors button objects
        toggleLeftRightButton.recordNewValue(gp2.x);
        // drive functions
        double x = gp1.left_stick_x;
        double y = -gp1.left_stick_y;
        double rot = gp1.right_stick_x;

        if (gp1.left_trigger > 0.2){
            x   *= slowFactor;
            y   *= slowFactor;
            rot *= slowFactor;
        }

        telemetry.addData("X Power", x);
        telemetry.addData("Y Power", y);
        telemetry.addData("Rot Power", rot);

        driveSpeedScaled(x, y, rot);

        // button push lift positions
        if(gp2.a) {
            lift.goToLowPos();
            intake.carryPos();
        } else if(gp2.b) {lift.goToScorePos(); intake.carryPos();}
        else if (gp2.y) {
            lift.goToHangPos();
            intake.carryPos();
        }

        // lift "jog" functions
        lift.adjustLift(-2*gp2.right_stick_y);

        // left/right side toggle
        if (toggleLeftRightButton.isJustOn() && gp2.left_bumper){
            leftRightState = !leftRightState;
            if(leftRightState)lift.setLefScoreSide();
            else lift.setRightScoreSide();
        }

        //lift close
        if (gp2.left_bumper)
            lift.closeLatch();

        // dumping
        if(gp2.right_bumper)lift.dump();
        else lift.stopDump();

        //button push intake functions;
        telemetry.addData("Left bumper", gp2.left_bumper);
        if(gp2.dpad_down) {
            intake.goToTransfer();
            lift.goToLowPos();
        }
        if (gp2.dpad_right) intake.carryPos();
        if(gp2.left_trigger > 0.5) intake.intakeOn();
        else if(gp2.right_trigger >0.5) intake.reverseIntake();
        else
            intake.stopIntake();
        if (gp2.dpad_left) intake.transferMinerals();
        if(intake.isInTransferState()&&lift.isInTransferState()) {
            intake.transferMinerals();
        }
        // homing thing
        if(gp1.y){
            lift.homingThing();
        } else {
            lift.homingThing = false;
        }



        // now hand controls for lifts
      telemetry.addData("Arm motor pos", -gp2.left_stick_y);
      telemetry.addData("Curr arm motor pos", intake.getArmPos());
      telemetry.addData("TransferState boolean val", intake.isInTransferState());
      intake.setExtensionPowerFromGamepad(gp2.left_stick_y);



        update();

    }

    public Point getPos(){
        //readSensors();
        return this.pos;
    }

    public double getHeading() {
        //readSensors();
        return heading;
    }

    public void readSensors(){
            if (usingOdometry) {
                double[] ocPos = this.odometry.getPosition();
                double x = ocPos[0];
                double y = ocPos[1];
                this.pos = new Point(x, y);
                this.heading = ocPos[2];
                telemetry.addData("Got Positions from OC:", Arrays.toString(ocPos));
            } else{

            }
            if (usingGyros) {
                if (this.gyro.isUpdated()) {
                    this.heading = gyro.getHeading(true);
                    telemetry.addData("Got heading from Gyro", this.heading);
                }
            }
    }
    public void update(){
        if(intake.isInTransferState() && lift.isInTransferState()){
            intake.transferMinerals();
        }
        this.lift.update();
        this.intake.update();
        this.drivebase.update();

        // temproary
    }

    public void stop() {
        this.lift.stop();
        this.drivebase.stop();
        this.intake.stop();

        if (this.gyro != null)
            this.gyro.close();
    }
}
