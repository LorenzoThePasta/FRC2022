package frc.robot.controls;

import controllers.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.*;
import frc.robot.robotConstants.climbExtender.*;
import frc.robot.robotConstants.climbRotator.*;
import frc.robot.util.ClimberMethods;

public class Operator{

  public static GameController controller = new GameController(new Joystick(JoyConstants.kOperatorJoy));

  public static TraversoClimbExtenderConstants extend = new TraversoClimbExtenderConstants();
  public static TraversoClimbRotatorConstants rotate = new TraversoClimbRotatorConstants();

  //operator buttons
  public static void configureButtonBindings() {
    
    //arm testing

    // controller.getButtons().A().whenPressed(
    //     () -> RobotContainer.m_climbArmR.enable());
    // controller.getButtons().B().whenPressed(
    //     () -> RobotContainer.m_climbArmR.disable());

    // controller.getButtons().Y().whenPressed(
    //     () -> RobotContainer.m_climbArmR.setOutput(0.1));
    // // controller.getButtons().X().whenPressed(
    // //     () -> RobotContainer.m_testArm.setOutput(0));

    // controller.getButtons().X().whileHeld(
    //     () -> RobotContainer.m_climbArmR.setOutput(
    //     controller.getJoystickAxis().leftY()));
    // controller.getButtons().X().whenReleased
    //     (() -> RobotContainer.m_climbArmR.setOutput(0));
        
    // controller.getButtons().RB().whenPressed(
    //     () -> RobotContainer.m_climbArmR.setEncoder(SmartDashboard.getNumber("set encoder", 0)));
    // controller.getButtons().LB().whenPressed(
    //     () -> RobotContainer.m_climbArmR.setGoal(SmartDashboard.getNumber("goal", 0)));


    // extender goes up a little bit for the driver to go hook onto the mid-bar
    controller.getButtons().A().whenPressed(new SequentialCommandGroup(

      // the extender goes up a small amount
      new InstantCommand(() -> ClimberMethods.extenderHardExtend(extend.kSlightlyUpward)),

      // wait until it reaches its setpoint
      new WaitUntilCommand(ClimberMethods::isExtenderAtSetpoint)
    ));

    // this extends the arm to its lowest point, extends the arm upwards a little,
    // the arm rotates backwards to its maximum, and the arm extends to its maxium
    controller.getButtons().B().whenPressed(new SequentialCommandGroup(

      new InstantCommand(() -> ClimberMethods.extenderHardExtend(extend.kMaxDownwards)),

      // wait until it reaches its setpoint
      new WaitUntilCommand(ClimberMethods::isExtenderAtSetpoint),

      // extend slightly upwards
      new InstantCommand(() -> ClimberMethods.extenderHardExtend(extend.kSlightlyUpward)),

      // wait until it reaches its setpoint
      new WaitUntilCommand(ClimberMethods::isExtenderAtSetpoint),

      // rotate to the maximum backwards
      new InstantCommand(() -> ClimberMethods.rotatorHardAngle(rotate.kMaxBackward)),

      // wait until both reach their setpoints
      new WaitUntilCommand(ClimberMethods::isRotatorAtSetpoint),

      // extender goes to its maximum point
      new InstantCommand(() -> ClimberMethods.extenderHardExtend(extend.kMaxUpwards)),

      // wait until the extender reaches its maximum point
      new WaitUntilCommand(ClimberMethods::isExtenderAtSetpoint)
    ));

    // this rotates the arm to the next bar, straightens the arm to 90 degrees while also compressing
    controller.getButtons().X().whenPressed(new SequentialCommandGroup(

      // rotate the arm to the bar
      new InstantCommand(()-> ClimberMethods.rotatorHardAngle(rotate.kToBar)),

      // wait until the rotator reaches it setpoint
      new WaitUntilCommand(ClimberMethods::isRotatorAtSetpoint),

      // extender goes to its lowest point
      new InstantCommand(() -> ClimberMethods.extenderHardExtend(extend.kMaxDownwards)),

      // rotator goes to 90 degrees
      new InstantCommand(() -> ClimberMethods.rotatorHardAngle(rotate.kNinetyDeg))
      .andThen(
        // wait until both reach their setpoints
        new WaitUntilCommand(ClimberMethods::isExtenderAtSetpoint),
        new WaitUntilCommand(ClimberMethods::isRotatorAtSetpoint)
      )
    ));
  }
}
