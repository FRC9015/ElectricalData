package com.github.FRC9015.electricaldata;

import org.littletonrobotics.junction.AutoLog;

@AutoLog
public class CANMotorInputs {
    public String busName = "rio";
    public int canId = 0;
    public double currentAmps = 0.0;
    public double busVoltageVolts = 0.0;
    public boolean isConnected = false;
}