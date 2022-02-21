package frc.robot.util;

import frc.robot.RobotContainer;

public class ShooterMethods {
  // cargo arm methods
  public static void setAngle(double angle) {
    RobotContainer.m_cargoRotator.setPosition(angle);
  }

  public static void enableArm() {
    RobotContainer.m_cargoRotator.enable();
  }

  public static void disableArm() {
    RobotContainer.m_cargoRotator.disable();
  }

  public static boolean isArmAtSetpoint() {
    return RobotContainer.m_cargoRotator.reachedSetpoint();
  }
  //

  // belt methods
  public static void setBeltSpeed(double speed) {
    RobotContainer.m_cargoBelt.setOutput(speed);
  }

  public static void enableBelt() {
    RobotContainer.m_cargoBelt.enable();
  }

  public static void disableBelt() {
    RobotContainer.m_cargoBelt.disable();
  }
  //

  // wheel methods
  public static void setWheelSpeed(double speed) {
    RobotContainer.m_cargoShooter.setSpeed(speed);
  }

  public static void enableWheel() {
    RobotContainer.m_cargoShooter.enable();
  }
  public static void disableWheel() {
    RobotContainer.m_cargoShooter.disable();
  }
  //

  public static void enableAll() {
    RobotContainer.m_cargoRotator.enable();
    RobotContainer.m_cargoBelt.enable();
    RobotContainer.m_cargoShooter.enable();
  }

  public static void disableAll() {
    RobotContainer.m_cargoRotator.disable();
    RobotContainer.m_cargoBelt.disable();
    RobotContainer.m_cargoShooter.disable();
  }
}
