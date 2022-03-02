package frc.robot.controls;


import controllers.*;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.*;
import frc.robot.commands.Intake;
import frc.robot.commands.AlignToUpperHub;
import frc.robot.commands.ClimbExtenderMove;
import frc.robot.commands.ClimbRotatorMove;
import frc.robot.commands.PositionArm;
import frc.robot.commands.Shoot;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.robotConstants.cargoRotator.TraversoCargoRotatorConstants;
import frc.robot.robotConstants.climbExtender.*;
import frc.robot.robotConstants.climbRotator.*;
import frc.robot.robotConstants.shooterBelt.TraversoBeltConstants;
import frc.robot.robotConstants.shooterWheel.TraversoCargoShooterConstants;
import frc.robot.util.ClimberMethods;
import frc.robot.util.ShooterMethods;

public class Operator {

  public static GameController controller = new GameController(new Joystick(JoyConstants.kOperatorJoy));

  // these two are named a little weirdly because the command group for this needs to be at least a little readable
  public static TraversoClimbExtenderConstants extend = new TraversoClimbExtenderConstants();
  public static TraversoClimbRotatorConstants rotate = new TraversoClimbRotatorConstants();

  public static TraversoCargoRotatorConstants cargoConstants = new TraversoCargoRotatorConstants();
  public static TraversoBeltConstants beltConstants = new TraversoBeltConstants();
  public static TraversoCargoShooterConstants wheelConstants = new TraversoCargoShooterConstants();

  //operator buttons
  public static void configureButtonBindings() {
    climbBinds();
    shootBinds();
  }

  public static void climbBinds() {

    // when DPad Up is pressed, enable the extender and extend upwards to kMaxUpwards
    controller.getDPad().up().whenHeld(new ParallelCommandGroup(
      // stow the cargo subsystem
      new PositionArm(cargoConstants.kStowPos),
      new ClimbExtenderMove(extend.kMaxUpwards)
    ));

    // when DPad Down is pressed, enable the extender and compress downwards to kMaxDownwards
    controller.getDPad().down().whenHeld(new ParallelCommandGroup(
      // stow the cargo subsystem
      new PositionArm(cargoConstants.kStowPos),
      new ClimbExtenderMove(extend.kMaxDownwards)
    ));

    // when DPad Right is pressed, enable the rotator and go to kMaxForward degrees
    controller.getDPad().right().whenPressed(new SequentialCommandGroup (
      new ClimbRotatorMove(rotate.kMaxBackward)
    ));
    
    // when DPad Left is pressed, enable the rotator and go to kMaxBackward degrees
    controller.getDPad().left().whenPressed(new SequentialCommandGroup (
      new ClimbRotatorMove(rotate.kMaxBackward)
    ));

    // when LB is pressed, enable the rotator and go to kNinetyDeg degrees
    controller.getButtons().LB().whenPressed(new SequentialCommandGroup(
      new ClimbRotatorMove(rotate.kNinetyDeg)
    ));

    // // when nothing on the DPad is pressed, the extenders are disabled
    // controller.getDPad().unpressed().whenPressed(
    //   new InstantCommand(() -> ClimberMethods.disableExtender())
    // );

    // rotator goes to the bar
    controller.getButtons().LT().whenActive(new SequentialCommandGroup(
      new ClimbRotatorMove(rotate.kToBar)
    ));
  }

  public static void shootBinds() {
    controller.getButtons().RB().whenHeld(new ConditionalCommand(
      new Shoot(cargoConstants.kFrontOuttakeFarPos, beltConstants.kIntakeSpeed, ShooterMethods.getOptimalShooterSpeed(), beltConstants.kOuttakeSpeed, false),
      new Shoot(cargoConstants.kBackOuttakeFarPos, beltConstants.kIntakeSpeed, ShooterMethods.getOptimalShooterSpeed(), beltConstants.kOuttakeSpeed, false),
      ShooterMethods::isArmFront
    ));

    controller.getButtons().RT().whenActive(new ConditionalCommand(
      // shoot at a desired angle and outtake/intake speeds
      new Shoot(cargoConstants.kFrontOuttakeNearPos, beltConstants.kIntakeSpeed, wheelConstants.kFrontOuttakeNearSpeed, beltConstants.kOuttakeSpeed, false),
      new Shoot(cargoConstants.kBackOuttakeNearPos, beltConstants.kIntakeSpeed, wheelConstants.kBackOuttakeNearSpeed, beltConstants.kOuttakeSpeed, false),
      ShooterMethods::isArmFront
    ));


    // move arm to back
    controller.getButtons().A().whenPressed(new PositionArm(cargoConstants.kBackOuttakeFarPos));

    // move arm to front
    controller.getButtons().B().whenPressed(new PositionArm(cargoConstants.kFrontOuttakeFarPos));

    controller.getButtons().X().whenHeld(new Intake(cargoConstants.kIntakePos, beltConstants.kIntakeSpeed, wheelConstants.kIntakeSpeed, cargoConstants.kFrontOuttakeFarPos, false, Constants.kIsRedAlliance));
    controller.getButtons().X().whenReleased(new SequentialCommandGroup(
      new PositionArm(cargoConstants.kFrontOuttakeFarPos),
      new InstantCommand(() -> ShooterMethods.disableShiitake()),
      new InstantCommand(() -> RobotContainer.m_cargoRotator.resetPID())
    ));

    // controller.getButtons().Y().whenPressed(new PositionArm(cargoConstants.kStowPos));
    controller.getButtons().Y().whileHeld(new AlignToUpperHub(RobotContainer.m_limelight, RobotContainer.m_drive));
    // controller.getButtons().RB().whenPressed(new GetDistance(RobotContainer.m_limelight, RobotContainer.m_cargoRotator));
  }
}