package frc.robot.subsystems;

import frc.robot.config.MotorSpeeds;
import frc.robot.config.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class TestSubsystem extends Subsystem {

	private static TestSubsystem _instance;

	private TalonSRX _motorAndEncoder;
	
	public TestSubsystem() {
		_motorAndEncoder = new TalonSRX(RobotMap.Talon.TEST.getChannel());
		_motorAndEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		_motorAndEncoder.setSelectedSensorPosition(_motorAndEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff, 0, 0);
	}
	
	public static TestSubsystem getInstance(){
		if(_instance == null)
			_instance = new TestSubsystem();
		
		return _instance;
	}
	
	public void setMotorSpeed(double value) {
		_motorAndEncoder.set(ControlMode.PercentOutput, value * MotorSpeeds.TELEOP_MULTIPLIER);
	}
	
	public int getEncoderPosition() {
		return _motorAndEncoder.getSelectedSensorPosition(0);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
