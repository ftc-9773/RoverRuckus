package org.firstinspires.ftc.teamcode.RobotDrivers.HardwareControl.Sensors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class VoltSensor extends SensorInterface {
    public VoltageSensor vs;

    public VoltSensor(HardwareMap hwmap){
        //vs = hwmap.get(vs.getClass(), "voltageSensor");
    }

    public double getVoltage(){
        return 0.0;
    }
}
