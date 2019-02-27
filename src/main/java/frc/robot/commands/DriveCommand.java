package frc.robot.commands;

import java.util.HashMap;
import java.util.Map;

import frc.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DriveCommand extends Command {
	public static enum Direction {
		FORWARD(1),
		BACKWARD(-1);
		
		private int _moveValue;
		
		private Direction(int moveValue) {
			_moveValue = moveValue;
		}
		public int getMoveValue() {
			return _moveValue;
		}
	}
	
	public static enum Rotate {
		RIGHT(1),
		LEFT(-1);
		
		private int _moveValue;
		
		private Rotate(int moveValue) {
			_moveValue = moveValue;
		}
		public int getMoveValue() {
			return _moveValue;
		}
	}
	
	private Direction _direction;
	private Rotate _rotate;
	private DriveTrainSubsystem _drive;
	
	public DriveCommand(Direction direction) {
		super("DriveStraight");
		_drive = DriveTrainSubsystem.getInstance();
		requires(_drive);
		
		_direction = direction;
	}
	
	public DriveCommand(Rotate rotate) {
		super("DriveRotate");
		_drive = DriveTrainSubsystem.getInstance();
		requires(_drive);
		
		_rotate = rotate;
	}
	
	@Override
	protected void initialize() {
		if(_direction != null) {
			driveStraight(1.0);
		}else {
			driveRotate(1.0);
		}
    }
	
	@Override
	protected void execute() {
		if(_direction != null) {
			driveStraight(1.0);
		}else {
			driveRotate(1.0);
		}
	}
	
	protected void driveStraight(double speed) {
		Map<String, Double> velocity = calculateVelocity(speed);
		_drive.tankDrive(_direction.getMoveValue() * velocity.get("leftVelocity"),
						 _direction.getMoveValue() * velocity.get("rightVelocity"));
	}
	
	protected void driveRotate(double speed) {
		Map<String, Double> velocity = calculateVelocity(speed);
		_drive.tankDrive(_rotate.getMoveValue() * velocity.get("leftVelocity"),
						 -_rotate.getMoveValue() * velocity.get("rightVelocity"));
	}
	
	private Map<String, Double> calculateVelocity(double initialVelocity){
		Map<String, Double> velocity = new HashMap<String, Double>();
		
		final double leftVelocity = (double)Math.abs(_drive.getCurrentLeftVelocity());
		final double rightVelocity = (double)Math.abs(_drive.getCurrentRightVelocity());
		double speedChangeLeft = initialVelocity;
		double speedChangeRight = initialVelocity;
		
		if(leftVelocity > rightVelocity) {
			speedChangeLeft = rightVelocity / leftVelocity;
		} else if(rightVelocity > leftVelocity) {
			speedChangeRight = leftVelocity / rightVelocity;
		}
		
		velocity.put("leftVelocity", new Double(speedChangeLeft));
		velocity.put("rightVelocity", new Double(speedChangeRight));
		
		return velocity;
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void end() {
		_drive.tankDrive(0, 0);
	}
	
	@Override
	protected void interrupted() {
		end();
	}

}
