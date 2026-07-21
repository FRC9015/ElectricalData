package com.github.FRC9015.electricaldata;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase;
import org.littletonrobotics.junction.Logger;

public class CANBusMonitor {
    private final CANMotorInputsAutoLogged inputs = new CANMotorInputsAutoLogged();
    private final String logPath;
    private final int canId;
    private final String busName;

    // References to hardware if provided
    private TalonFX talonFX = null;
    private SparkBase spark = null;

    // Constructor for TalonFX (Phoenix 6)
    public CANBusMonitor(String deviceName, String busName, TalonFX motor) {
        this.busName = busName;
        this.canId = motor.getDeviceID();
        this.logPath = "CANBus/" + busName + "/" + deviceName + "_" + canId;
        this.talonFX = motor;
    }

    // Constructor for SPARK MAX / SPARK Flex (REVLib)
    public CANBusMonitor(String deviceName, String busName, SparkBase motor) {
        this.busName = busName;
        this.canId = motor.getDeviceId();
        this.logPath = "CANBus/" + busName + "/" + deviceName + "_" + canId;
        this.spark = motor;
    }

    /**
     * Call this in periodic(). Reads hardware signals automatically.
     */
    public void update() {
        if (talonFX != null) {
            inputs.busName = busName;
            inputs.canId = canId;
            inputs.currentAmps = talonFX.getSupplyCurrent().getValueAsDouble();
            inputs.busVoltageVolts = talonFX.getMotorVoltage().getValueAsDouble();
            // Status IS ok means device responded on CAN
            inputs.isConnected = talonFX.getSupplyCurrent().getStatus().isOK();
        } else if (spark != null) {
            inputs.busName = busName;
            inputs.canId = canId;
            inputs.currentAmps = spark.getOutputCurrent();
            inputs.busVoltageVolts = spark.getBusVoltage() * spark.getAppliedOutput();
            // REV returns system errors if device drops off CAN
            inputs.isConnected = (spark.getLastError() == com.revrobotics.REVLibError.kOk);
        }

        Logger.processInputs(logPath, inputs);
    }

    public boolean isConnected() {
        return inputs.isConnected;
    }
}