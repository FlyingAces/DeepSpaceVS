package frc.robot.commands;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.Feed;

import edu.wpi.first.wpilibj.command.Command;

public class MoveArmAnglesCommand extends Command {
	public static double USE_CURRENT_ANGLE = Double.NaN;
	
	protected ArmSubsystem _arm;
	protected Feed _feed;
	
	protected double _shoulderValueParam;
	protected double _elbowValueParam;
	protected double _wristValueParam;
	
	protected double _shoulderValue;
	protected double _elbowValue;
	protected double _wristValue;
	
	protected double _shoulderDir;
	protected double _elbowDir;
	protected double _wristDir;
	
	protected double _speedMultiplier;
	
	public MoveArmAnglesCommand(double shoulderValue, double elbowValue, double wristValue) {
		super("MoveArmAnglesCommand");
		
		_arm = ArmSubsystem.getInstance();
		requires(_arm);
		
		_feed = Feed.getInstance();
		
		_shoulderValueParam = shoulderValue;
		_elbowValueParam = elbowValue;
		_wristValueParam = wristValue;
		
		_speedMultiplier = 1.0;
	}
	
	@Override
	protected void initialize() {
		_shoulderValue = (Double.isNaN(_shoulderValueParam))? _arm.getAngle(ArmSubsystem.Angle.SHOULDER) : _shoulderValueParam;
		_elbowValue = (Double.isNaN(_elbowValueParam))? _arm.getAngle(ArmSubsystem.Angle.ELBOW) : _elbowValueParam;
		_wristValue = (Double.isNaN(_wristValueParam))? _arm.getAngle(ArmSubsystem.Angle.WRIST) : _wristValueParam;
		
		_shoulderDir = (_shoulderValue == _arm.getAngle(ArmSubsystem.Angle.SHOULDER))? 0.0 : 
					   (_shoulderValue > _arm.getAngle(ArmSubsystem.Angle.SHOULDER))? 1.0 : -1.0;
		_elbowDir = (_elbowValue == _arm.getAngle(ArmSubsystem.Angle.ELBOW))? 0.0 :
					(_elbowValue > _arm.getAngle(ArmSubsystem.Angle.ELBOW))? 1.0 : -1.0;
		_wristDir = (_wristValue == _arm.getAngle(ArmSubsystem.Angle.WRIST)) ? 0.0 : 
					(_wristValue > _arm.getAngle(ArmSubsystem.Angle.WRIST)) ? 1.0 : -1.0;
		
		_feed.sendAngleInfo("endAngles", _shoulderValue, _elbowValue, _wristValue);
	}
	
	@Override
	protected void execute() {
		double diffShoulderAngle = Math.abs(_shoulderValue - _arm.getAngle(ArmSubsystem.Angle.SHOULDER));
		double diffElbowAngle = Math.abs(_elbowValue - _arm.getAngle(ArmSubsystem.Angle.ELBOW));
		double diffWristAngle = Math.abs(_wristValue - _arm.getAngle(ArmSubsystem.Angle.WRIST));
		
		double shoulderSpeed = 1.0;
		double elbowSpeed = 1.0;
		double wristSpeed = 1.0;
		
		if(diffElbowAngle < diffShoulderAngle && diffWristAngle < diffShoulderAngle) {
			elbowSpeed = diffElbowAngle / diffShoulderAngle;
			wristSpeed = diffWristAngle / diffShoulderAngle;
		} else if(diffShoulderAngle < diffElbowAngle && diffWristAngle < diffElbowAngle) {
			shoulderSpeed = diffShoulderAngle / diffElbowAngle;
			wristSpeed = diffWristAngle / diffElbowAngle;
		} else if(diffShoulderAngle < diffWristAngle && diffElbowAngle < diffWristAngle){
			shoulderSpeed = diffShoulderAngle / diffWristAngle;
			elbowSpeed = diffElbowAngle / diffWristAngle;
		}
		
		_arm.setMotorSpeeds(_shoulderDir * _speedMultiplier * shoulderSpeed, 
						    _elbowDir * _speedMultiplier * elbowSpeed, 
				            _wristDir * _speedMultiplier * wristSpeed);
		    
		_feed.sendAngleInfo("currentAngles", _arm.getAngle(ArmSubsystem.Angle.SHOULDER), _arm.getAngle(ArmSubsystem.Angle.ELBOW), _arm.getAngle(ArmSubsystem.Angle.WRIST));
	}

	@Override
	protected boolean isFinished() {
		if((_shoulderDir < 0)? _arm.getAngle(ArmSubsystem.Angle.SHOULDER) <= _shoulderValue : _arm.getAngle(ArmSubsystem.Angle.SHOULDER) >= _shoulderValue)
			_shoulderDir = 0.0;
		
		if((_elbowDir < 0)? _arm.getAngle(ArmSubsystem.Angle.ELBOW) <= _elbowValue : _arm.getAngle(ArmSubsystem.Angle.ELBOW) >= _elbowValue)
			_elbowDir = 0.0;
		
		if((_wristDir < 0)? _arm.getAngle(ArmSubsystem.Angle.WRIST) <= _wristValue : _arm.getAngle(ArmSubsystem.Angle.WRIST) >= _wristValue)
			_wristDir = 0.0;
			
		return 	(_shoulderDir == 0.0) && (_elbowDir == 0.0) && (_wristDir == 0.0);
	}
	
	@Override
	protected void end() {
		_arm.setMotorSpeeds(0.0, 0.0, 0.0);
		
		_feed.sendAngleInfo("currentAngles", _arm.getAngle(ArmSubsystem.Angle.SHOULDER), 
											 _arm.getAngle(ArmSubsystem.Angle.ELBOW), 
											 _arm.getAngle(ArmSubsystem.Angle.WRIST));
	}
	
	public void setSpeedMultiplier(double value) {
		_speedMultiplier = value;
	}

}
