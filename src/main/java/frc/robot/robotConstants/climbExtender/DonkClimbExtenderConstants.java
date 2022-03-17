package frc.robot.robotConstants.climbExtender;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class DonkClimbExtenderConstants {
  // extending motors (for climber)
  public final int kRightExtenderPort = 10;
  public final int kLeftExtenderPort = 11;

  // the arm's length in ticks
  public final double kExtenderMaxArmTicks = 506900;

  // tolerance allowed to the PID 
  public final double kExtenderTolerance = 2000;

  // motor clamps
  public final double kMotorClampDown = -0.9; //should be negative!
  public final double kMotorClampUp = .7;     //should be positive!
  
  // locations
  public final double kMaxUpwards = kExtenderMaxArmTicks;
  public final double kMaxDownwards = 0;
  public final double kHalfway = kExtenderMaxArmTicks / 2;
  public final double kSlightlyUpward = kExtenderMaxArmTicks / 6;
  
  // off load PID constants
  public final double kP = 0.08;
  public final double kI = 0.00;
  public final double kD = 0.02;

  public final double kSupplyCurrentLimit = 40;
  public final double kSupplyTriggerThreshold = 40;
  public final double kSupplyTriggerDuration = 0;
  public final NeutralMode kNeutralMode = NeutralMode.Brake;
}