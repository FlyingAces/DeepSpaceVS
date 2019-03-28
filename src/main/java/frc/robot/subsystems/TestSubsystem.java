package frc.robot.subsystems;

import frc.robot.config.MotorSpeeds;
import frc.robot.config.RobotMap;
import frc.robot.util.Conversions;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class TestSubsystem extends Subsystem {

	private static TestSubsystem _instance;

	private TalonSRX _motorAndEncoder;
	
	public TestSubsystem() {
		_motorAndEncoder = new TalonSRX(RobotMap.Talon.TEST.getChannel());
		_motorAndEncoder.configFactoryDefault();

		_motorAndEncoder.setNeutralMode(NeutralMode.Brake);
		
		_motorAndEncoder.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);

		_motorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.setSensorPhase(true);
		_motorAndEncoder.setInverted(false);

		_motorAndEncoder.setSelectedSensorPosition(_motorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, RobotMap.K_TIMEOUT_MS);

		_motorAndEncoder.configAllowableClosedloopError(0, 0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.config_kP(0, 0.6, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.config_kI(0, 0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.config_kD(0, 0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.config_kF(0, 0, RobotMap.K_TIMEOUT_MS);
		_motorAndEncoder.config_IntegralZone(0, 0, RobotMap.K_TIMEOUT_MS);

		_motorAndEncoder.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);
		
	}
	
	public static TestSubsystem getInstance(){
		if(_instance == null)
			_instance = new TestSubsystem();
		
		return _instance;
	}
	
	public void setMotorSpeed(double value) {
		_motorAndEncoder.set(ControlMode.PercentOutput, value * MotorSpeeds.TELEOP_MULTIPLIER);
	}

	public void setMotorPosition(int encoderPosition) {
		double radians = Conversions.degreeToRadian(Conversions.shoulderAndElbowEncoderPositionToAngle(getEncoderPosition()));
		_motorAndEncoder.set(ControlMode.Position, encoderPosition, DemandType.ArbitraryFeedForward, -(.05 * Math.sin(radians)));
		System.out.println("position : " + getEncoderPosition() + " error : " + _motorAndEncoder.getClosedLoopError());
	}
	
	public int getEncoderPosition() {
		return _motorAndEncoder.getSelectedSensorPosition(0);
	}

	public double getCurrent(){
		return _motorAndEncoder.getMotorOutputPercent();
	}
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
