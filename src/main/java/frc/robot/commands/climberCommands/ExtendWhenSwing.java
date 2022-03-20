package frc.robot.commands.climberCommands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.RobotContainer;
import frc.robot.robotConstants.climbExtender.TraversoClimbExtenderConstants;
import frc.robot.util.ClimberMethods;

public class ExtendWhenSwing extends SequentialCommandGroup {

    TraversoClimbExtenderConstants extenderConstants = new TraversoClimbExtenderConstants();

    public ExtendWhenSwing() {
      addRequirements(RobotContainer.m_extenderL, RobotContainer.m_extenderR);
      addCommands(
        // enable the extender
        new InstantCommand(() -> ClimberMethods.enableExtender()),

        new WaitUntilCommand(() -> RobotContainer.m_drive.isPitchAbove(extenderConstants.kMinPitchForExtension)),

        // set the setpoints of the extenders
        // please note: these extensions are different to account for the left not reaching as high as it should
        new InstantCommand(() -> RobotContainer.m_extenderL.set(extenderConstants.kLeftMaxUpwards)),
        new InstantCommand(() -> RobotContainer.m_extenderR.set(extenderConstants.kRightMaxUpwards)),

        // wait until both extenders reach their setpoints
        new WaitUntilCommand(() -> ClimberMethods.isExtenderAtSetpoint())
      );
      }
}
