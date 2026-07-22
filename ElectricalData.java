// package frc.robot.util; // change this to whatever package you want the file to live in

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase;
import org.littletonrobotics.junction.Logger;

/**
 * Drop-in CAN bus telemetry monitor for CTRE Phoenix 6 and REV REVLib devices.
 * Originally from FRC9015/ElectricalData (https://github.com/FRC9015/ElectricalData),
 * flattened into a single file so it can be copy-pasted directly into a robot project
 * instead of pulled in as a vendordep.
 *
 * Requires: WPILib, AdvantageKit (org.littletonrobotics.junction.Logger), and whichever
 * of CTRE Phoenix 6 / REVLib you're actually using. If a project only uses one vendor's
 * motors, it's safe to delete the constructor/import for the other one.
 *
 * Usage (identical to the original library):
 *   private final CANBusMonitor flywheelMonitor = new CANBusMonitor("Flywheel", "rio", flywheelMotor);
 *   ...
 *   flywheelMonitor.update(); // call in periodic()
 */
public class CANBusMonitor {
    private final String logPath;
    private final int canId;
    private final String busName;

    private TalonFX talonFX = null;
    private SparkBase spark = null;
    private boolean isConnected = false;

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
     * Call this in periodic(). Reads hardware signals and logs them via AdvantageKit.
     */
    public void update() {
        double currentAmps = 0.0;
        double busVoltageVolts = 0.0;

        if (talonFX != null) {
            currentAmps = talonFX.getSupplyCurrent().getValueAsDouble();
            busVoltageVolts = talonFX.getMotorVoltage().getValueAsDouble();
            isConnected = talonFX.getSupplyCurrent().getStatus().isOK();
        } else if (spark != null) {
            currentAmps = spark.getOutputCurrent();
            busVoltageVolts = spark.getBusVoltage() * spark.getAppliedOutput();
            isConnected = (spark.getLastError() == com.revrobotics.REVLibError.kOk);
        }

        Logger.recordOutput(logPath + "/busName", busName);
        Logger.recordOutput(logPath + "/canId", canId);
        Logger.recordOutput(logPath + "/currentAmps", currentAmps);
        Logger.recordOutput(logPath + "/busVoltageVolts", busVoltageVolts);
        Logger.recordOutput(logPath + "/isConnected", isConnected);
    }

    public boolean isConnected() {
        return isConnected;
    }
}