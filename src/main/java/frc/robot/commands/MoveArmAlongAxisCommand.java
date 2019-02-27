package frc.robot.commands;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.RobotArmCalculations;

public class MoveArmAlongAxisCommand extends MoveArmAnglesCommand {
	public static enum Axis {
		X, Y;
	}
	
	protected Axis _axis;
	protected double _endLoc;
	protected double _dir;
	
	public MoveArmAlongAxisCommand(Axis axis, double endLoc) {
		super(0.0, 0.0, 0.0);
		
		_axis = axis;
		_endLoc = endLoc;
		_dir = 0.0;
	}
	
	private double updateToNextStep(double curr) {
		curr += _dir;
		if((_dir < 0.0)? curr < _endLoc : curr > _endLoc)
			curr = _endLoc;
		
		return curr;
	}
	
	@Override
	protected void initialize() {
		RobotArmCalculations calculations = new RobotArmCalculations(_arm.getAngle(ArmSubsystem.Angle.SHOULDER),
																	 _arm.getAngle(ArmSubsystem.Angle.ELBOW), 
																	 _arm.getHandState());
		calculations.setWristAngle(_arm.getAngle(ArmSubsystem.Angle.WRIST));
		calculations.setInverted(_arm.isInverted());
		
		switch(_axis) {
		case X:
			_dir = (_endLoc == _arm.getWristTargetX())? 0.0 : 
		   		   (_endLoc > _arm.getWristTargetX())? 1.0 : -1.0;
			calculations.setWristTargetX(updateToNextStep(_arm.getWristTargetX()));
			break;
		case Y:
			_dir = (_endLoc == _arm.getWristTargetY())? 0.0 : 
		   		   (_endLoc > _arm.getWristTargetY())? 1.0 : -1.0;
			calculations.setWristTargetY(updateToNextStep(_arm.getWristTargetY()));
			break;
		}

		_shoulderValueParam = calculations.getShoulderAngle();
		_elbowValueParam = calculations.getElbowAngle();
		_wristValueParam = calculations.getWristAngle();

		super.initialize();
	}
	
	@Override
	protected boolean isFinished() {
		switch(_axis) {
		case X:
			if(super.isFinished() && ((_dir < 0)? _arm.getWristTargetX() <= _endLoc : _arm.getWristTargetX() >= _endLoc))
				return true;
			else if(super.isFinished()) {
				initialize();
			}
			break;
		case Y:
			if(super.isFinished() && ((_dir < 0)? _arm.getWristTargetY() <= _endLoc : _arm.getWristTargetY() >= _endLoc))
				return true;
			else if(super.isFinished()) {
				initialize();
			}
			break;
		}
		
		return false; 
	}

}
