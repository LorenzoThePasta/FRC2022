package frc.robot.robotConstants.cargoRotator;

public class TraversoCargoRotatorConstants {
    // the duty cycle encoder ports of the arm
    public final int kArmEncoder = 2;

    // motor clamping
    public final double kMotorClamp = 0.3;

    // the motor ports of the arm
    public final int kArmMotor = 2;


    // Current Tick Value * Degree Multiple = Current Angle
    public final double kArmDegreeMultiple = 360.0;
    public final double kArmZeroEncoderDegrees = 0;

    // the distance allowed from the setpoint (IN DECIMAL DEGREES (1 = 360, .5 = 180, .25 = 90))
    public final double kArmTolerance = 3;


    public final double kArmEncoderOffset = 0;
    
    // locations
    public final double kIntakePos = 0;
    public final double kbBckOutakePos = 135;
    public final double kFrontOutakePos = 80;
    public final double kFrontOutakeFarPos = 30;
    public final double kStowPos = 20;
    
    public final double kP = 0.03;
    public final double kI = 0.00;
    public final double kD = 0.00;

    // feed forward constants
    public final double kS = 0;
    public final double kG = 0.15;
    public final double kV = 2.91;
    public final double kA = 0.01;

    public final double kSupplyCurrentLimit = 30;
    public final double kSupplyTriggerThreshold = 30;
    public final double kSupplyTriggerDuration = 0;
    public final boolean kCoast = false;
    
}
