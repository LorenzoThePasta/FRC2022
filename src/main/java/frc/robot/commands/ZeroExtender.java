package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimbExtender;
import frc.robot.util.ClimberMethods;

public class ZeroExtender extends SequentialCommandGroup{
    
    ClimbExtender extender;

    public ZeroExtender(ClimbExtender extender){
        addRequirements(extender);
        this.extender= extender;
        addCommands(
            new InstantCommand(() -> extender.setOutput(0.02)),
            new WaitCommand(0.2),
            new WaitUntilCommand(this::compressed),
            new InstantCommand(() -> extender.setOutput(0)),
            new InstantCommand(() -> extender.zero())

        );
    }

    private boolean compressed(){
        return extender.compressionLimitSwitch() || Math.abs(extender.getVelocity()) < 0.01;
    }
}
