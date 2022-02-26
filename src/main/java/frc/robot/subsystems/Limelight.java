package frc.robot.subsystems;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.robotConstants.limelight.TraversoLimelightConstants;

public class Limelight extends SubsystemBase {
  public static TraversoLimelightConstants constants = new TraversoLimelightConstants();

  private static Limelight instance;

  private NetworkTable m_table;
  private String m_tableName;

  private boolean m_hasValidTarget; // Whether target is detected
  private double m_horizontalAngularOffset; // Horizontal angular displacement of target from crosshair
  private double m_verticalAngularOffset; // Vertical angular displacement of target from crosshair
  private double m_targetArea; // Target area
  private double m_skew; // Skew or rotation
  private Pipeline m_pipeline;
  private double m_latency;
  private boolean m_isDriverCamera = true;
  private LEDMode m_ledMode = LEDMode.OFF;

  private MedianFilter m_TxMedianFilter = new MedianFilter(5);
  private MedianFilter m_TyMedianFilter = new MedianFilter(5);
  private Debouncer m_TvDebouncer = new Debouncer(0.1, DebounceType.kBoth);

  BooleanSupplier m_getIsFacingFront;

  public static Limelight getInstance(BooleanSupplier getIsFacingFront) {
    if (instance == null) {
      instance = new Limelight(getIsFacingFront);
    }
    return instance;
  }

  public Limelight(BooleanSupplier getIsFacingFront) {
    m_tableName = "limelight";
    m_table = NetworkTableInstance.getDefault().getTable(m_tableName);
    m_getIsFacingFront = getIsFacingFront;
  }

  public Limelight(String tableName, BooleanSupplier getIsFacingFront) {
    m_tableName = tableName;
    m_table = NetworkTableInstance.getDefault().getTable(m_tableName);
  }

  public Limelight(NetworkTable table, BooleanSupplier getIsFacingFront) {
    m_table = table;
  }

  @Override
  public void periodic() {
    
  }

  private void updateData() {
    setCameraMode(m_isDriverCamera);
    setLedMode(m_ledMode);
    m_latency = getTl();

    if (!m_isDriverCamera) {
      m_pipeline = getPipeline();
      m_hasValidTarget = m_TvDebouncer.calculate(getTv() == 1.0);

      if (m_hasValidTarget) {
        m_targetArea = getTa();
        m_skew = getTs();

        if (constants.kIsMountedHorizontally) {
          m_horizontalAngularOffset = getTx();
          m_verticalAngularOffset = getTy();
        } else {
          m_horizontalAngularOffset = getTy();
          m_verticalAngularOffset = getTx();
        }

        if (!isFacingFront()) {
          m_horizontalAngularOffset *= -1;
          m_verticalAngularOffset *= -1;
          m_skew *= -1;
        }
      } else {
        setNullValues();
      }
    } else {
      setNullValues();
    }
  }

  private double getTl() {
    return m_table.getEntry("tl").getDouble(Double.NaN);
  }

  private boolean isFacingFront() {
    return m_getIsFacingFront.getAsBoolean();
  }

  private void setNullValues() {
    m_targetArea = Double.NaN;
    m_skew = Double.NaN;
    m_horizontalAngularOffset = Double.NaN;
    m_verticalAngularOffset = Double.NaN;
  }

  public enum LEDMode {
    PIPELINE, OFF, BLINK, ON
  }

  public void setLedMode(LEDMode ledMode) {
    switch (ledMode) {
      case PIPELINE:
        m_table.getEntry("ledMode").setNumber(0);
        break;
      case OFF:
        m_table.getEntry("ledMode").setNumber(1);
        break;
      case BLINK:
        m_table.getEntry("ledMode").setNumber(2);
        break;
      case ON:
        m_table.getEntry("ledMode").setNumber(3);
        break;
      default:
        m_table.getEntry("ledMode").setNumber(0);
        m_ledMode = LEDMode.PIPELINE;
    }
  }

  public void setCameraMode(boolean isDriverCamera) {
    if (isDriverCamera) {
      m_table.getEntry("camMode").setNumber(1);
    } else {
      m_table.getEntry("camMode").setNumber(0);
    }
  }

  public boolean isDriverCamera() {
    return m_isDriverCamera;
  }

  private double getTv() {
    double tv = m_table.getEntry("tv").getDouble(Double.NaN);
    return tv;
  }

  private double getTs() {
    double ts = m_table.getEntry("ts").getDouble(Double.NaN);
    return ts;
  }

  private double getTx() {
    double tx = m_table.getEntry("tx").getDouble(Double.NaN);
    return m_TxMedianFilter.calculate(tx);
  }

  private double getTy() {
    double ty = m_table.getEntry("ty").getDouble(Double.NaN);
    return m_TyMedianFilter.calculate(ty);
  }

  private double getTa() {
    double ta = m_table.getEntry("ta").getDouble(Double.NaN);
    return ta;
  }

  public double getSkew() {
    return m_skew;
  }

  public double getLatency() {
    return m_latency;
  }

  public Pipeline getPipeline() {
    double pipeline = m_table.getEntry("getpipe").getDouble(Double.NaN);
    switch ((int) Math.round(pipeline)) {
      case 0:
        return Pipeline.RED_CARGO;
      case 1:
        return Pipeline.BLUE_CARGO;
      case 2:
        return Pipeline.UPPER_HUB;
      default:
        return Pipeline.NO_PIPELINE;
    }
  }

  public enum Pipeline {
    RED_CARGO, BLUE_CARGO, UPPER_HUB, NO_PIPELINE
  }

  private void setPipeline(Pipeline pipeline) {
    m_isDriverCamera = false;
    m_ledMode = LEDMode.PIPELINE;

    switch (pipeline) {
      case RED_CARGO:
        m_table.getEntry("pipeline").setNumber(constants.kRedCargoPipeline);
        break;
      case BLUE_CARGO:
        m_table.getEntry("pipeline").setNumber(constants.kBlueCargoPipeline);
        break;
      case UPPER_HUB:
        m_table.getEntry("pipeline").setNumber(constants.kUpperHubPipeline);
        break;
      default:
        m_isDriverCamera = true;
    }
    updateData();
  }

  private double getLimelightHeight(double armAngle) {
    return constants.kPivotHeight
        + (constants.kPivotToLimelightDistance * Math.sin(Units.degreesToRadians(armAngle)));
  }

  private double getLimelightAngle(double armAngle) {
    return armAngle + constants.kPivotToLimelightAngleDifference;
  }

  public double getTargetArea() {
    return m_targetArea;
  }

  public boolean hasValidTarget() {
    return m_hasValidTarget;
  }

  private void setBallPipeline(boolean isRedBall) {
    Pipeline pipeline;
    if (isRedBall) {
      pipeline = Pipeline.RED_CARGO;
    } else {
      pipeline = Pipeline.BLUE_CARGO;
    }
    setPipeline(pipeline);
  }

  private void setUpperHubPipeline() {
    setPipeline(Pipeline.UPPER_HUB);
  }

  public boolean alignedToTarget() {
    return m_horizontalAngularOffset < 2;
  }

  public double getHubDistance(double armAngle) {
    setUpperHubPipeline();
    SmartDashboard.putNumber("Horizontal Error", m_horizontalAngularOffset);
    return getDistance(armAngle, constants.kHubTargetHeight);
  }

  public double getBallDistance(double armAngle, boolean isRedBall) {
    setBallPipeline(isRedBall);
    return getDistance(armAngle, constants.kBallTargetHeight);
  }

  private double getDistance(double armAngle, double targetHeight) {
    double distance = ((targetHeight - getLimelightHeight(armAngle))
        / (Math.tan(Units.degreesToRadians(getLimelightAngle(armAngle) + m_verticalAngularOffset))));

    return distance;
  }

  public double getHubHorizontalAngularOffset() {
    setUpperHubPipeline();

    double horizontalAngularOffset = m_horizontalAngularOffset;

    return horizontalAngularOffset;
  }

  public double getBallHorizontalAngularOffset(boolean isRedBall) {
    setBallPipeline(isRedBall);

    double horizontalAngularOffset = m_horizontalAngularOffset;

    return horizontalAngularOffset;
  }
}
