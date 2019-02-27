package frc.robot.subsystems;

import frc.robot.config.RobotMap;
import frc.robot.commands.CommandByController;
import frc.robot.config.MotorSpeeds;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class DriveTrainSubsystem extends Subsystem {
	private static DriveTrainSubsystem _instance; 
	
	private ADXRS450_Gyro _gyro;
	
	private TalonSRX _leftEncoder;
	private TalonSRX _rightEncoder;
	
	private DifferentialDrive _wheels;
	
	private DriveTrainSubsystem() {
		super("DriveTrain");
		
		_gyro = new ADXRS450_Gyro();
		
		WPI_TalonSRX leftMaster = new WPI_TalonSRX(RobotMap.Talon.LEFT_FRONT.getChannel());
		WPI_TalonSRX rightMaster = new WPI_TalonSRX(RobotMap.Talon.RIGHT_FRONT.getChannel());
		WPI_TalonSRX leftSlave = new WPI_TalonSRX(RobotMap.Talon.LEFT_BACK.getChannel());
		WPI_TalonSRX rightSlave = new WPI_TalonSRX(RobotMap.Talon.RIGHT_BACK.getChannel());

		SpeedControllerGroup leftGroup = new SpeedControllerGroup(leftMaster, leftSlave);
		SpeedControllerGroup rightGroup = new SpeedControllerGroup(rightMaster, rightSlave);

		_wheels = new DifferentialDrive(leftGroup, rightGroup);
		_wheels.setSafetyEnabled(false);
		
		_leftEncoder = leftSlave;
		_rightEncoder = rightSlave;
        
        _leftEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        _leftEncoder.setSelectedSensorPosition(_leftEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, 0);
        _leftEncoder.setSensorPhase(true);
        
        _rightEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        _rightEncoder.setSelectedSensorPosition(_rightEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, 0);

	}
	
	public void arcadeDrive(double moveValue, double rotateValue){
		if(DriverStation.getInstance().isOperatorControl()) 
			moveValue *= MotorSpeeds.TELEOP_MULTIPLIER;
		else
			moveValue *= MotorSpeeds.AUTONOMOUS_MULTIPLIER;
		
		_wheels.arcadeDrive(moveValue * MotorSpeeds.DRIVE_SPEED_ACCEL, rotateValue * MotorSpeeds.DRIVE_SPEED_TURN);
	}
	
	public void tankDrive(double leftMoveValue, double rightMoveValue) {
		if(DriverStation.getInstance().isOperatorControl()) {
			rightMoveValue *= MotorSpeeds.TELEOP_MULTIPLIER;
			leftMoveValue *= MotorSpeeds.TELEOP_MULTIPLIER; 
		} else {
			rightMoveValue *= MotorSpeeds.AUTONOMOUS_MULTIPLIER;
			leftMoveValue *= MotorSpeeds.AUTONOMOUS_MULTIPLIER;
		}
		
		_wheels.tankDrive(leftMoveValue, rightMoveValue);
	}
	
	public static DriveTrainSubsystem getInstance(){
		if(_instance == null)
			_instance = new DriveTrainSubsystem();
		
		return _instance;
	}
	
	public int getCurrentLeftPosition() {
		return _leftEncoder.getSelectedSensorPosition(0);
	}
	
	public int getCurrentLeftVelocity() {
		return _leftEncoder.getSelectedSensorVelocity(0);
	}
	
	public int getCurrentRightPosition() {
		return _rightEncoder.getSelectedSensorPosition(0);
	}
	
	public int getCurrentRightVelocity() {
		return _rightEncoder.getSelectedSensorVelocity(0);
	}
	
	public double gyroAngle() {
		return _gyro.getAngle();
	}
		
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new CommandByController());
	}

}
