package com.github.FRC9015.electricaldata;

import java.lang.Cloneable;
import java.lang.Override;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class CANMotorInputsAutoLogged extends CANMotorInputs implements LoggableInputs, Cloneable {
  @Override
  public void toLog(LogTable table) {
    table.put("BusName", busName);
    table.put("CanId", canId);
    table.put("CurrentAmps", currentAmps);
    table.put("BusVoltageVolts", busVoltageVolts);
    table.put("IsConnected", isConnected);
  }

  @Override
  public void fromLog(LogTable table) {
    busName = table.get("BusName", busName);
    canId = table.get("CanId", canId);
    currentAmps = table.get("CurrentAmps", currentAmps);
    busVoltageVolts = table.get("BusVoltageVolts", busVoltageVolts);
    isConnected = table.get("IsConnected", isConnected);
  }

  public CANMotorInputsAutoLogged clone() {
    CANMotorInputsAutoLogged copy = new CANMotorInputsAutoLogged();
    copy.busName = this.busName;
    copy.canId = this.canId;
    copy.currentAmps = this.currentAmps;
    copy.busVoltageVolts = this.busVoltageVolts;
    copy.isConnected = this.isConnected;
    return copy;
  }
}
