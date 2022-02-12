/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.commands.*;
import frc.robot.controls.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.cameraserver.CameraServer;

import frc.robot.Constants.*;
import frc.robot.autonomous.drivetrain.Pathweaver;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot
 * (including subsystems, commands, and button mappings) should be declared
 * here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  //public static Drivetrain m_drive = new Drivetrain();
  //public static BallDetection m_ballDetection = new BallDetection();
  //public static TestWheel m_testWheel = new TestWheel();
  public static ClimbArm m_arm = new ClimbArm(true);
  public static Drivetrain m_drive = new Drivetrain();


  public RobotContainer() {

    // default command to run in teleop
    
    m_drive.setDefaultCommand(new DifferentialDrive(m_drive));
    m_arm.setDefaultCommand(new armPID(m_arm));

    // Start camera stream for driver
    //CameraServer.startAutomaticCapture();
    
    // Configure the button bindings
    Driver.configureButtonBindings();
    Operator.configureButtonBindings();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Attempt to load trajectory from PathWeaver
    //Pathweaver.setupAutonomousTrajectory(AutoConstants.kTrajectoryName);
    return new armPID(m_arm);
  }
}
