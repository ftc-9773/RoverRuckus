package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractDrivebase;
import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractIntake;
import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.Abstracts.AbstractScorer;
import org.firstinspires.ftc.teamcode.RobotDrivers.Dumper;
import org.firstinspires.ftc.teamcode.RobotDrivers.RitDIntake;
import org.firstinspires.ftc.teamcode.RobotDrivers.RitDLift;
import org.firstinspires.ftc.teamcode.RobotDrivers.TankDrivebase;

/**
 *
 * Implements Zach's Robot in three days code with separate classes.
 * @author Cadence
 * */
@TeleOp(name = "RobotInThreeDays", group = "AAA robotInThreeDays")
public class RitDv2 extends LinearOpMode {

    public void runOpMode(){
        // init
        RitDLift lift = new RitDLift("liftMotorA", "liftMotorB", hardwareMap);
        RitDIntake intake = new RitDIntake("ritkServo", "litkServo", "armMotor", "intakeMotor",  hardwareMap);
        TankDrivebase drivebase = new TankDrivebase("lMotorA", "lMotorB", "rMotorA", "rMotorB", hardwareMap);
        Dumper scorer = new Dumper("sortServo", "lDump", "rDump", hardwareMap);

        //Check that each class extends or implements an Abstract
        if(!validateClasses(drivebase, intake, lift, scorer)){
            throw new ArithmeticException("Invalid class");
        }
        waitForStart();

        while (opModeIsActive()){
            //Opmode

            //Driving
            drivebase.setForwardBackPower(gamepad1.left_stick_y);
            drivebase.setLeftRightPower(gamepad1.right_stick_y);

            //Set lift power
            lift.setPower(gamepad2.left_stick_y);
            intake.setArmMotor(gamepad2.right_stick_y);

            if(gamepad2.left_bumper){
                scorer.dump(); //Release the cubes/balls
            }
            else{
                scorer.reset(); //close the gates
            }


            if(gamepad2.left_trigger > 0.2){
                scorer.setBeltSpeed(0.85); //Turn sorter belt on.
            }
            else{scorer.setBeltSpeed(0.5);}

            //change the intake state based on user command
            if(gamepad2.y){intake.storeState();}
            else if(gamepad2.b) {intake.intakeState();}
            else if(gamepad2.x) {intake.transferState();}

            //Set the correct value for the intake
            if(gamepad2.right_bumper){
                intake.intakeOn();
            }
            else if(gamepad2.right_trigger > 0.2){
                intake.intakeOn(-gamepad2.right_trigger);
            }
            else {
                intake.intakeOff(); }

        }
    }
    /**
     * Checks to make sure all classes are extending the abstract classes
     * */
    public boolean validateClasses(AbstractDrivebase drivebase, AbstractIntake intake, AbstractLift lift, AbstractScorer scorer){
        return true;
    }

}
