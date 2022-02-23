package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutoConstants;
import frc.robot.autonomous.drivetrain.Pathweaver;

public class ShuffleboardManager {

  
  SendableChooser<Command> autoCommand = new SendableChooser<>();

  // Command pathweaver = Pathweaver.pathweaverCommand();

  public void setup() {
    autoCommand.setDefaultOption("pathweaver", Pathweaver.pathweaverCommand(AutoConstants.kTrajectoryName));
    // m_chooser.setDefaultOption("pathweaver", new DifferentialDrive(Drivetrain.getInstance()));

    // time();
  }

  public void update() {
    driveMode();
    subsystemSpam();
  }

  public void time() {
    SmartDashboard.putNumber("Time Left", DriverStation.getMatchTime());
    SmartDashboard.putNumber("Time Left Until Endgame", DriverStation.getMatchTime() - 30);
    SmartDashboard.putNumber("Auto Wait", 0);
  }

  public void driveMode() {
    // m_chooser.addOption("teleop", new TeleopDrive(Drivetrain.getInstance()));
    autoCommand.addOption("Spin baby spin", new RunCommand(() -> RobotContainer.m_drive.tankDrive(0.5, -0.5), RobotContainer.m_drive));
    // adds auto to shuffle board
    SmartDashboard.putData(autoCommand);
     
    // SmartDashboard.putString("Drive Mode", Driver.getDriveMode().toString());
    SmartDashboard.putBoolean("Teleop", DriverStation.isTeleop());
  }

  public void subsystemSpam() {
    // put subsystem shuffleboard things in here!

    // RobotContainer.m_extenderL.loadExtenderShuffleboard();
    // RobotContainer.m_extenderR.loadExtenderShuffleboard();

    // RobotContainer.m_climbRotatorL.loadRotatorShuffleboard();
    // RobotContainer.m_climbRotatorR.loadRotatorShuffleboard();

    RobotContainer.m_cargoBelt.loadCargoBeltShuffleboard();
    RobotContainer.m_cargoShooter.loadCargoShooterShuffleboard();
    RobotContainer.m_cargoRotator.loadCargoRotatorShuffleboard();
  }

  public Command getAutonomousCommand() {
    autoCommand.setDefaultOption("pathweaver", Pathweaver.pathweaverCommand(AutoConstants.kTrajectoryName));
    return autoCommand.getSelected();
  }

  public Command getAutonomousWaitCommand() {
    return new WaitCommand(SmartDashboard.getNumber("Auto Wait", 0));
  }
}
  