package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Attachments;

public interface Attachment {

    /**
     * Applies target configuration
     * */
    public void update();

    /**
     *  Sets configuration to a safe stop state
     * */
    public void stop();
    // public void set position
}