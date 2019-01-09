package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments;

public interface Attachment {

    /**
     * Applies target configuration
     * */
    void update();

    /**
     *  Sets configuration to a safe stop state
     * */
    void stop();

    boolean inStableState();

}