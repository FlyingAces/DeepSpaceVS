package frc.robot.subsystems;

import frc.robot.config.MotorSpeeds;
import frc.robot.config.RobotMap;
import frc.robot.commands.CommandRobotArmWithController;
import frc.robot.util.RobotArmCalculations;
import frc.robot.util.RobotArmCalculations.HandState;
import frc.robot.util.Conversions;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ArmSubsystem extends Subsystem {
	public static enum Angle {
		SHOULDER, ELBOW, WRIST;
	}
	
	private static ArmSubsystem _instance;
	
	private RobotArmCalculations _calculations;
	
	private TalonSRX _shoulderMotorAndEncoder;
	private TalonSRX _elbowMotorAndEncoder;
	private TalonSRX _wristMotorAndEncoder;
	
	private DigitalInput _shoulderSwitch;
	private double _zeroAngleShoulderPosition;
	
	private DigitalInput _elbowSwitch;
	private double _zeroAngleElbowPosition;
	
	private DigitalInput _wristSwitch;
	private double _zeroAngleWristPosition;
	
	private double _wristAngle;
	
	private ArmSubsystem() {
		super("ArmSubsystem");
		
		_shoulderMotorAndEncoder = new TalonSRX(RobotMap.Talon.SHOULDER_MOTOR.getChannel());
		_shoulderMotorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		_shoulderMotorAndEncoder.setSelectedSensorPosition(_shoulderMotorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, 0);
        
		_elbowMotorAndEncoder = new TalonSRX(RobotMap.Talon.ELBOW_MOTOR.getChannel());
		_elbowMotorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		_elbowMotorAndEncoder.setSelectedSensorPosition(_elbowMotorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, 0);
		
		_wristMotorAndEncoder = new TalonSRX(RobotMap.Talon.WRIST_MOTOR.getChannel());
		_wristMotorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		_wristMotorAndEncoder.setSelectedSensorPosition(_wristMotorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, 0);
		
		_shoulderSwitch = new DigitalInput(RobotMap.Switch.SHOULDER_ZERO.getChannel());
		_zeroAngleShoulderPosition = -Conversions.shoulderAndElbowAngleToEncoderPosition(RobotMap.Angle.SHOULDER_START_ANGLE.getAngle());
		
		_elbowSwitch = new DigitalInput(RobotMap.Switch.ELBOW_ZERO.getChannel());
		_zeroAngleElbowPosition = -Conversions.shoulderAndElbowAngleToEncoderPosition(RobotMap.Angle.ELBOW_START_ANGLE.getAngle());
		
		_wristSwitch = new DigitalInput(RobotMap.Switch.WRIST_ZERO.getChannel());
		_zeroAngleWristPosition = -Conversions.wristAngleToEncoderPosition(RobotMap.Angle.WRIST_START_ANGLE.getAngle());
		
		_calculations = new RobotArmCalculations(RobotMap.Angle.SHOULDER_START_ANGLE.getAngle(),
												 RobotMap.Angle.ELBOW_START_ANGLE.getAngle(),
												 RobotArmCalculations.HandState.LOCKED);
		_calculations.setWristAngle(RobotMap.Angle.WRIST_START_ANGLE.getAngle());
		
		_wristAngle = _calculations.getWristAngle();
	}
	
	public static ArmSubsystem getInstance(){
		if(_instance == null)
			_instance = new ArmSubsystem();
		
		return _instance;
	}
	
	public void zeroOutAnglePosition(Angle angle) {
		switch(angle) {
		case SHOULDER:
			_zeroAngleShoulderPosition = _shoulderMotorAndEncoder.getSelectedSensorPosition(0);
			break;
		case ELBOW:
			_zeroAngleElbowPosition = _elbowMotorAndEncoder.getSelectedSensorPosition(0);
			break;
		case WRIST:
			_zeroAngleWristPosition = _wristMotorAndEncoder.getSelectedSensorPosition(0);
			break;
		}
	}
	
	public boolean isAngleSwitchSet(Angle angle) {
		
		switch(angle) {
		case SHOULDER:
			//return _shoulderSwitch.get();
			return Math.abs(getAngle(Angle.SHOULDER)) < 1;
		case ELBOW:
			//return _elbowSwitch.get();
			return Math.abs(getAngle(Angle.ELBOW)) < 1;
		case WRIST:
			//return _wristSwitch.get();
			return Math.abs(getAngle(Angle.WRIST)) < 1;
		}
		
		return false;
	}
	
	public double getAngle(Angle angle) {
		double anglesAngle = 0.0;
		
		switch(angle) {
		case SHOULDER:
			anglesAngle = Conversions.shoulderAndElbowEncoderPositionToAngle(_shoulderMotorAndEncoder.getSelectedSensorPosition(0) - 
		    														         _zeroAngleShoulderPosition);
			anglesAngle = _calculations.getShoulderAngle();
			break;
		case ELBOW:
			anglesAngle = Conversions.shoulderAndElbowEncoderPositionToAngle(_elbowMotorAndEncoder.getSelectedSensorPosition(0) - 
																			 _zeroAngleElbowPosition);
			anglesAngle = _calculations.getElbowAngle();
			break;
		case WRIST:
			anglesAngle = Conversions.shoulderAndElbowEncoderPositionToAngle(_wristMotorAndEncoder.getSelectedSensorPosition(0) - 
			         														 _zeroAngleWristPosition);
			anglesAngle = _wristAngle;
			break;
		}
		
		return anglesAngle;
	}
	
	public boolean isInverted() {
		return _calculations.isInverted();
	}
	
	public HandState getHandState() {
		return _calculations.getHandState();
	}
	
	public double getWristTargetX() {
		_calculations.setShoulderAngle(getAngle(Angle.SHOULDER));
		_calculations.setElbowAngle(getAngle(Angle.ELBOW));
		
		return _calculations.getWristTargetX();
	}
	
	public double getWristTargetY() {
		_calculations.setShoulderAngle(getAngle(Angle.SHOULDER));
		_calculations.setElbowAngle(getAngle(Angle.ELBOW));
		
		return _calculations.getWristTargetY();
	}
	
	public void setHandState(HandState position) {
		_calculations.setHandState(position);
	}
	
	public void setMotorSpeeds(double shoulderMotor, 
							   double elbowMotor, 
							   double wristMotor) {

		setMotorSpeed(Angle.SHOULDER, shoulderMotor);
		setMotorSpeed(Angle.ELBOW, elbowMotor);
		setMotorSpeed(Angle.WRIST, wristMotor);
	}
	
	public void setMotorSpeed(Angle angle, double speed) {
		speed *= (DriverStation.getInstance().isOperatorControl()) ? MotorSpeeds.TELEOP_MULTIPLIER : MotorSpeeds.AUTONOMOUS_MULTIPLIER;
		
		switch(angle) {
		case SHOULDER:
			//_shoulderMotorAndEncoder.set(ControlMode.PercentOutput, speed * MotorSpeeds.SHOULDER_MOTOR_SPEED);
			_calculations.setShoulderAngle(speed * 7.5 + getAngle(Angle.SHOULDER) * MotorSpeeds.SHOULDER_MOTOR_SPEED);
			break;
		case ELBOW:
			//_elbowMotorAndEncoder.set(ControlMode.PercentOutput, speed * MotorSpeeds.ELBOW_MOTOR_SPEED);
			_calculations.setElbowAngle(speed * 7.5 + getAngle(Angle.ELBOW) * MotorSpeeds.ELBOW_MOTOR_SPEED);
			break;
		case WRIST:
			//_wristMotorAndEncoder.set(ControlMode.PercentOutput, speed * MotorSpeeds.WRIST_MOTOR_SPEED);
			_wristAngle += speed * 7.5 * MotorSpeeds.WRIST_MOTOR_SPEED;
			break;
		}
	}
	
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new CommandRobotArmWithController());
	}

}
