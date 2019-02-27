package frc.robot.commands;

import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.util.Conversions;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTo extends DriveCommand {
	
	private double _distancePosition;
	private double _initialRightPosition;
	private double _initialLeftPosition;
	
	public DriveTo(double distanceInches) {
		super((distanceInches > 0)? 
				DriveCommand.Direction.FORWARD : 
				DriveCommand.Direction.BACKWARD);
		
		_distancePosition = Conversions.inchToEncoderPosition(Math.abs(distanceInches));
		
	}
	
	@Override
	protected void initialize() {
		_initialRightPosition = DriveTrainSubsystem.getInstance().getCurrentLeftPosition();
		_initialLeftPosition = DriveTrainSubsystem.getInstance().getCurrentRightPosition();
		super.initialize();
	}
	
	@Override
	protected boolean isFinished() {
		
		double leftDistancePosition = Math.abs(DriveTrainSubsystem.getInstance().getCurrentLeftPosition() - _initialRightPosition);
		double rightDistancePosition = Math.abs(DriveTrainSubsystem.getInstance().getCurrentRightPosition() - _initialLeftPosition);
		
		//System.out.println("leftDistance : " + Conversions.encoderPositionToInches(leftDistancePosition) + ", rightDistance : " + Conversions.encoderPositionToInches(rightDistancePosition));
		return (leftDistancePosition >= _distancePosition) || (rightDistancePosition >= _distancePosition);
	}
	
}
