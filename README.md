# ElectricalData

A  plug-and-play WPILib utility library built by FRC Team 9015. `ElectricalData` simplifies telemetry logging for CAN bus devices across **CTRE Phoenix 6** and **REV REVLib** hardware using **AdvantageKit**.

---

## Quick Setup (WPILib Vendordep)

You can install `ElectricalData` directly into any WPILib robot project using the VS Code command palette.

1. Open your robot project in **VS Code**.
2. Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac) and select **WPILib: Manage Vendor Libraries**.
3. Choose **Install new libraries (online)**.
4. Paste the following URL:
   ```text
   [https://raw.githubusercontent.com/FRC9015/ElectricalData/main/ElectricalData.json](https://raw.githubusercontent.com/FRC9015/ElectricalData/main/ElectricalData.json)
   ```
5. Press **Enter**. WPILib will automatically add `ElectricalData` to your project's `vendordeps/` directory.

---

## Usage

`ElectricalData` automatically detects whether you pass a CTRE or REV motor and handles fetching supply current, bus voltage, and CAN connection health, making it a simple one-line addition to your code.

### 1. Basic Integration in a Subsystem

Pass your motor instance directly into `CANBusMonitor` during initialization, then call `.update()` inside your subsystem's `periodic()` loop:

```java
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.github.FRC9015.electricaldata.CANBusMonitor;

public class ShooterSubsystem extends SubsystemBase {
    // Hardware definitions
    private final TalonFX flywheelMotor = new TalonFX(5, "rio");
    private final SparkMax feederSpark = new SparkMax(6, MotorType.kBrushless);

    // CANBus monitors (Device Name, Bus Name, Motor Instance)
    private final CANBusMonitor flywheelMonitor = new CANBusMonitor("Flywheel", "rio", flywheelMotor);
    private final CANBusMonitor feederMonitor = new CANBusMonitor("Feeder", "rio", feederSpark);

    public ShooterSubsystem() {
        // Subsystem configuration...
    }

    @Override
    public void periodic() {
        // Automatically fetches metrics and logs to AdvantageKit!
        flywheelMonitor.update();
        feederMonitor.update();

        // Optional: Check hardware health
        if (!flywheelMonitor.isConnected()) {
            // Take safety precautions or alert the driver
        }
    }
}
```

---

## Reviewing Logs in AdvantageScope

All metrics are automatically routed through AdvantageKit's `@AutoLog` system. When reviewing logs in **AdvantageScope**, telemetry will be organized under a clean tree structure:

```text
Log Table
└── CANBus/
    └── <busName>/
        └── <deviceName>_<canId>/
            ├── busName (String)
            ├── canId (int)
            ├── currentAmps (double)
            ├── busVoltageVolts (double)
            └── isConnected (boolean)
```

---

## Supported Hardware

| Vendor | Hardware Class | Supported Devices |
| :--- | :--- | :--- |
| **CTRE Phoenix 6** | `TalonFX` | Talon FX, Kraken X60, Talon FXS |
| **REV REVLib** | `SparkBase` | SPARK MAX, SPARK Flex |

---

## Building from Source

If you want to contribute to `ElectricalData` or build the `.jar` locally:

```bash
# Clone the repository
git clone [https://github.com/FRC9015/ElectricalData.git](https://github.com/FRC9015/ElectricalData.git)
cd ElectricalData

# Build and run tests
./gradlew build
```