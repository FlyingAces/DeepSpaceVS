package frc.robot.subsystems;

import frc.robot.config.MotorSpeeds;
import frc.robot.config.RobotMap;
import frc.robot.util.RobotArmCalculations;
import frc.robot.util.RobotArmCalculations.HandState;
import frc.robot.util.Conversions;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

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

	private TalonSRX _cylanoidTalon;
	
	private ArmSubsystem() {
		super("ArmSubsystem");
		
		_shoulderMotorAndEncoder = new TalonSRX(RobotMap.Talon.SHOULDER_MOTOR.getChannel());
		_shoulderMotorAndEncoder.configFactoryDefault();

		_shoulderMotorAndEncoder.setNeutralMode(NeutralMode.Brake);
		
		_shoulderMotorAndEncoder.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.configPeakOutputForward(RobotMap.SHOULDER_GAINS.getPeak(), RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.configPeakOutputReverse(-RobotMap.SHOULDER_GAINS.getPeak(), RobotMap.K_TIMEOUT_MS);

		_shoulderMotorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.setSensorPhase(true);
		_shoulderMotorAndEncoder.setInverted(true);

		_shoulderMotorAndEncoder.setSelectedSensorPosition(_shoulderMotorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, RobotMap.K_TIMEOUT_MS);

		_shoulderMotorAndEncoder.configAllowableClosedloopError(0, 0, RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.config_kP(0, RobotMap.SHOULDER_GAINS.getKp(), RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.config_kD(0, RobotMap.SHOULDER_GAINS.getKd(), RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.config_kI(0, RobotMap.SHOULDER_GAINS.getKi(), RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.config_kF(0, RobotMap.SHOULDER_GAINS.getKf(), RobotMap.K_TIMEOUT_MS);
		_shoulderMotorAndEncoder.config_IntegralZone(0, RobotMap.SHOULDER_GAINS.getIzone(), RobotMap.K_TIMEOUT_MS);


		_elbowMotorAndEncoder = new TalonSRX(RobotMap.Talon.ELBOW_MOTOR.getChannel());
		_elbowMotorAndEncoder.configFactoryDefault();

		_elbowMotorAndEncoder.setNeutralMode(NeutralMode.Brake);

		_elbowMotorAndEncoder.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.configPeakOutputForward(RobotMap.ELBOW_GAINS.getPeak(), RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.configPeakOutputReverse(-RobotMap.ELBOW_GAINS.getPeak(), RobotMap.K_TIMEOUT_MS);

		_elbowMotorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.setSensorPhase(true);
		_elbowMotorAndEncoder.setInverted(false);

		_elbowMotorAndEncoder.setSelectedSensorPosition(-(_elbowMotorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);

		_elbowMotorAndEncoder.configAllowableClosedloopError(0, 0, RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.config_kP(0, RobotMap.ELBOW_GAINS.getKp(), RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.config_kD(0, RobotMap.ELBOW_GAINS.getKd(), RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.config_kI(0, RobotMap.ELBOW_GAINS.getKi(), RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.config_kF(0, RobotMap.ELBOW_GAINS.getKf(), RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.config_IntegralZone(0, RobotMap.ELBOW_GAINS.getIzone(), RobotMap.K_TIMEOUT_MS);

		
		_wristMotorAndEncoder = new TalonSRX(RobotMap.Talon.WRIST_MOTOR.getChannel());
		_wristMotorAndEncoder.configFactoryDefault();

		_wristMotorAndEncoder.setNeutralMode(NeutralMode.Brake);

		_wristMotorAndEncoder.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.configPeakOutputForward(RobotMap.WRIST_GAINS.getPeak(), RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.configPeakOutputReverse(-RobotMap.WRIST_GAINS.getPeak(), RobotMap.K_TIMEOUT_MS);

		_wristMotorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.setSensorPhase(true);
		_wristMotorAndEncoder.setInverted(false);

		_wristMotorAndEncoder.setSelectedSensorPosition(-(_wristMotorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);

		_wristMotorAndEncoder.configAllowableClosedloopError(0, 0, RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.config_kP(0, RobotMap.WRIST_GAINS.getKp(), RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.config_kD(0, RobotMap.WRIST_GAINS.getKd(), RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.config_kI(0, RobotMap.WRIST_GAINS.getKi(), RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.config_kF(0, RobotMap.WRIST_GAINS.getKf(), RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.config_IntegralZone(0, RobotMap.WRIST_GAINS.getIzone(), RobotMap.K_TIMEOUT_MS);
		
		_calculations = new RobotArmCalculations(RobotMap.Angle.SHOULDER_START_ANGLE.getAngle(),
												 RobotMap.Angle.ELBOW_START_ANGLE.getAngle(),
												 RobotArmCalculations.HandState.LOCKED);
		_calculations.setWristAngle(RobotMap.Angle.WRIST_START_ANGLE.getAngle());

		_cylanoidTalon = new TalonSRX(RobotMap.Talon.CYLANOID.getChannel());
		_cylanoidTalon.configFactoryDefault();
		_cylanoidTalon.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_cylanoidTalon.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_cylanoidTalon.configPeakOutputForward(1, RobotMap.K_TIMEOUT_MS);
		_cylanoidTalon.configPeakOutputReverse(0, RobotMap.K_TIMEOUT_MS);
	}
	
	public static ArmSubsystem getInstance(){
		if(_instance == null)
			_instance = new ArmSubsystem();
		
		return _instance;
	}

	public void setEncodersToStart(){
		_shoulderMotorAndEncoder.getSensorCollection().setQuadraturePosition((int)Conversions.shoulderAndElbowAngleToEncoderPosition(RobotMap.Angle.SHOULDER_START_ANGLE.getAngle()), RobotMap.K_TIMEOUT_MS);
		_elbowMotorAndEncoder.getSensorCollection().setQuadraturePosition(-(int)Conversions.shoulderAndElbowAngleToEncoderPosition(RobotMap.Angle.ELBOW_START_ANGLE.getAngle()), RobotMap.K_TIMEOUT_MS);
		_wristMotorAndEncoder.getSensorCollection().setQuadraturePosition(-(int)Conversions.wristAngleToEncoderPosition(RobotMap.Angle.WRIST_START_ANGLE.getAngle()), RobotMap.K_TIMEOUT_MS);
	}

	public double getAngle(Angle angle) {
		double anglesAngle = 0.0;
		
		switch(angle) {
		case SHOULDER:
			anglesAngle = Conversions.shoulderAndElbowEncoderPositionToAngle(_shoulderMotorAndEncoder.getSelectedSensorPosition(0));
			break;
		case ELBOW:
			anglesAngle = Conversions.shoulderAndElbowEncoderPositionToAngle(_elbowMotorAndEncoder.getSelectedSensorPosition(0));
			break;
		case WRIST:
			anglesAngle = Conversions.wristEncoderPositionToAngle(_wristMotorAndEncoder.getSelectedSensorPosition(0));
			break;
		}
		
		return anglesAngle;
	}

	public boolean isInverted() {
		return getAngle(Angle.ELBOW) < 0.0;
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
	
	public void setMotorSpeed(double shoulderMotor, 
							   double elbowMotor, 
							   double wristMotor) {
								   
		setMotorSpeed(Angle.SHOULDER, shoulderMotor);
		setMotorSpeed(Angle.ELBOW, elbowMotor);
		setMotorSpeed(Angle.WRIST, wristMotor);
	}
	
	public void setMotorSpeed(Angle angle, double speed) {
		switch(angle) {
		case SHOULDER:
			double combatGravity = 1.0 - (Math.abs(getAngle(Angle.SHOULDER)) / RobotMap.Angle.SHOULDER_START_ANGLE.getAngle());
			combatGravity *= (RobotMap.SHOULDER_MAX_COMBAT_GRAVITY - RobotMap.SHOULDER_MIN_COMBAT_GRAVITY);
			combatGravity += RobotMap.SHOULDER_MIN_COMBAT_GRAVITY;

			speed = (speed > 0)? speed - combatGravity : speed + combatGravity;
			speed *= MotorSpeeds.SHOULDER_MOTOR_SPEED;

			double shouldRadians = Conversions.degreeToRadian(getAngle(Angle.SHOULDER));			
			double shoulderValueVSGavity = speed - (combatGravity * Math.sin(shouldRadians));
			_shoulderMotorAndEncoder.set(ControlMode.PercentOutput, shoulderValueVSGavity);
			
			System.out.println("shoulder speed : " + speed + " position : " + _shoulderMotorAndEncoder.getSelectedSensorPosition());
			break;
		case ELBOW:
			speed = (speed > 0)? speed - RobotMap.ELBOW_MIN_COMBAT_GRAVITY : speed + RobotMap.ELBOW_MIN_COMBAT_GRAVITY;
			speed *= MotorSpeeds.ELBOW_MOTOR_SPEED;

			double elbowRadians = Conversions.degreeToRadian(getAngle(Angle.SHOULDER) + getAngle(Angle.ELBOW));
			double elbowValueVSGavity = speed - (RobotMap.ELBOW_MIN_COMBAT_GRAVITY * Math.sin(elbowRadians));
			_elbowMotorAndEncoder.set(ControlMode.PercentOutput, elbowValueVSGavity);
			
			System.out.println("elbow speed : " + speed + " position : " + _elbowMotorAndEncoder.getSelectedSensorPosition());
			break;
		case WRIST:
			speed = (speed > 0)? speed - RobotMap.WRIST_MIN_COMBAT_GRAVITY : speed + RobotMap.WRIST_MIN_COMBAT_GRAVITY;
			speed *= MotorSpeeds.WRIST_MOTOR_SPEED;

			double wristRadians =  Conversions.degreeToRadian(getAngle(Angle.WRIST) + getAngle(Angle.SHOULDER) + getAngle(Angle.ELBOW));
			double wristValueVSGavity = speed - (RobotMap.WRIST_MIN_COMBAT_GRAVITY * Math.sin(wristRadians));
			_wristMotorAndEncoder.set(ControlMode.PercentOutput, wristValueVSGavity);
			
			System.out.println("wrist speed : " + speed + " position : " + _wristMotorAndEncoder.getSelectedSensorPosition());
			break;
		}
	}
	

	public void setMotorPosition(int shoulderPos, int elbowPos, int wristPos) {
		setMotorPosition(Angle.SHOULDER, shoulderPos);
		setMotorPosition(Angle.ELBOW, elbowPos);
		setMotorPosition(Angle.WRIST, wristPos);
	}

	public void setMotorPosition(Angle angle, int position){
		switch(angle) {
			case SHOULDER:
				double combatGravity = 1.0 - (Math.abs(getAngle(Angle.SHOULDER)) / RobotMap.Angle.SHOULDER_START_ANGLE.getAngle());
				combatGravity *= (RobotMap.SHOULDER_MAX_COMBAT_GRAVITY - RobotMap.SHOULDER_MIN_COMBAT_GRAVITY);
				combatGravity += RobotMap.SHOULDER_MIN_COMBAT_GRAVITY;

				double shouldRadians = Conversions.degreeToRadian(getAngle(Angle.SHOULDER));
				_shoulderMotorAndEncoder.set(ControlMode.Position, position, DemandType.ArbitraryFeedForward, -(combatGravity * Math.sin(shouldRadians)));
				break;
			case ELBOW:
				double elbowRadians = Conversions.degreeToRadian(getAngle(Angle.SHOULDER) + getAngle(Angle.ELBOW));
				_elbowMotorAndEncoder.set(ControlMode.Position, position, DemandType.ArbitraryFeedForward, -(RobotMap.ELBOW_MIN_COMBAT_GRAVITY * Math.sin(elbowRadians)));
				break;
			case WRIST:
				double wristRadians =  Conversions.degreeToRadian(getAngle(Angle.WRIST) + getAngle(Angle.SHOULDER) + getAngle(Angle.ELBOW));
				_wristMotorAndEncoder.set(ControlMode.Position, position,  DemandType.ArbitraryFeedForward, -(RobotMap.WRIST_MIN_COMBAT_GRAVITY * Math.sin(wristRadians)));
				break;
			}
	}

	public void pushCylanoid(){
		_cylanoidTalon.set(ControlMode.PercentOutput, 1.0);
	}

	public void releaseCylanoid(){
		_cylanoidTalon.set(ControlMode.PercentOutput, 0.0);
	}

	@Override
	protected void initDefaultCommand() {
		// setDefaultCommand(new CommandRobotArmWithController());
	}

}
