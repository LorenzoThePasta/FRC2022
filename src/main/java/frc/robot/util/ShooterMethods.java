package frc.robot.util;


import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;

public class ShooterMethods {
  // cargo arm methods
  public static void setAngle(double angle) {
    RobotContainer.m_cargoRotator.setPosition(angle);
  }

  public static double getOptimalFrontShooterSpeed() {
    double distance = Units.metersToInches(RobotContainer.m_limelight.getHubDistance(RobotContainer.m_cargoRotator.currentAngle()));
    SmartDashboard.putNumber("Distance", distance);
    double speed = -(9.6*distance + 1405);
    System.out.println("Speed: " + speed);
    System.out.println("Distance: " + distance);
    return speed;
  }

  public static double getOptimalBackShooterSpeed() {
    double distance = Units.metersToInches(RobotContainer.m_limelight.getHubDistance(RobotContainer.m_cargoRotator.currentAngle()));
    SmartDashboard.putNumber("Distance", distance);
    double speed = -(9.6*distance + 1405);
    System.out.println("Speed: " + speed);
    System.out.println("Distance: " + distance);
    return speed;
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

  public static boolean isArmBack(){
    return RobotContainer.m_cargoRotator.isBackOutakeFar() || RobotContainer.m_cargoRotator.isBackOutakeNear();
  }

  public static boolean isArmFront(){
    // return RobotContainer.m_cargoRotator.isFrontOutakeFar() || RobotContainer.m_cargoRotator.isFrontOutakeNear();
    return RobotContainer.m_cargoRotator.currentAngle() < 123;
  }

  //

  // belt methods
  public static void setBeltSpeed(double speed) {
    RobotContainer.m_cargoBelt.setOutput(speed);
  }

  public static void setBeltPower(double power) {
    RobotContainer.m_cargoBelt.setPower(power);
  }

  public static void enableBelt() {
    RobotContainer.m_cargoBelt.enable();
  }

  public static void disableBelt() {
    RobotContainer.m_cargoBelt.disable();
  }

  public static void stopBelt() {
    RobotContainer.m_cargoShooter.setStop();
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

  public static boolean isWheelAtSetpoint() {
    return RobotContainer.m_cargoShooter.reachedSetpoint();
  }

  public static void stopWheel() {
    RobotContainer.m_cargoShooter.setStop();
  }
  //

  public static void enableAll() {
    RobotContainer.m_cargoRotator.enable();
    RobotContainer.m_cargoBelt.enable();
    RobotContainer.m_cargoShooter.enable();
  }

  public static void disableShooter() {
    RobotContainer.m_cargoBelt.disable();
    RobotContainer.m_cargoShooter.disable();
  }

  public static boolean isBallContained() {
    // System.out.println(RobotContainer.m_balldetector.containsBallSecurely());
    return RobotContainer.m_balldetector.containsBallSecurely();
  }

  public static boolean isBallShot() {
    return !isBallContained();
  }

  public static void multiSetter(double armAngle, double beltPower, double shooterSpeed) {
    RobotContainer.m_cargoRotator.setPosition(armAngle);
    RobotContainer.m_cargoBelt.setPower(beltPower);
    RobotContainer.m_cargoShooter.setSpeed(shooterSpeed);
  }

}
