package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;

public class AutoBalance {
    public static boolean run() {
        AHRS ahrs = new AHRS(SPI.Port.kMXP);
        double speed = 1;
        double rollAngleDegrees = ahrs.getRoll();
        DriveSubsystem driveSubsystem = new DriveSubsystem();
        while(!(!DriveSubsystem.autoBalanceXMode && (Math.abs(rollAngleDegrees) >= Math.abs(DriveSubsystem.kOffBalanceAngleThresholdDegrees)))) {
            driveSubsystem.MoveForward(speed);
        }
        double yawAngleRadians = rollAngleDegrees * (Math.PI / 180.0);
        speed = Math.sin(yawAngleRadians) * -0.5;
        try {
            DriveSubsystem.Left1.set(ControlMode.PercentOutput, speed);
            DriveSubsystem.Left2.set(ControlMode.PercentOutput, speed);
            DriveSubsystem.Right1.set(ControlMode.PercentOutput, speed);
            DriveSubsystem.Right2.set(ControlMode.PercentOutput, speed);
          } catch (RuntimeException ex) {
              String err_string = "Drive system error:  " + ex.getMessage();
              DriverStation.reportError(err_string, true);
          }
      
        System.out.println("speed: " + speed + " yaw: " + rollAngleDegrees);
        /*while(!onRamp) {
            pitch = ahrs.getPitch();
            if(pitch>=onHillDegrees) {
                onRamp = true;
            }
            driveSubsystem.MoveForward(speed);
        }
        while(pitch>=onHillDegrees) {
            pitch = ahrs.getPitch();
            driveSubsystem.MoveForward(speed);
        }*/
        ahrs.close();
        return true;
    }
}
