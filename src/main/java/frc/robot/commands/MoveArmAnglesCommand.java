package frc.robot.commands;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.Feed;
import frc.robot.util.Conversions;

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

	protected int _count;
	protected final int _maxCountOnTarget = 3;
	protected double[] _shoulderOnTarget;
	protected double[] _elbowOnTarget;
	protected double[] _wristOnTarget;

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

		_shoulderOnTarget = new double[_maxCountOnTarget];
		_elbowOnTarget = new double[_maxCountOnTarget];
		_wristOnTarget = new double[_maxCountOnTarget];

		for(int i = 0; i < _maxCountOnTarget; i++){
			_shoulderOnTarget[i] = i;
			_elbowOnTarget[i] = i;
			_wristOnTarget[i] = i;
		}

		_count = 0;

		_feed.sendAngleInfo("endAngles", _shoulderValue, _elbowValue, _wristValue);
	}
	
	@Override
	protected void execute() {
		if((_shoulderDir < 0)? _arm.getAngle(ArmSubsystem.Angle.SHOULDER) <= _shoulderValue : _arm.getAngle(ArmSubsystem.Angle.SHOULDER) >= _shoulderValue)
			_shoulderDir = 0.0;
		
		if((_elbowDir < 0)? _arm.getAngle(ArmSubsystem.Angle.ELBOW) <= _elbowValue : _arm.getAngle(ArmSubsystem.Angle.ELBOW) >= _elbowValue)
			_elbowDir = 0.0;
		
		if((_wristDir < 0)? _arm.getAngle(ArmSubsystem.Angle.WRIST) <= _wristValue : _arm.getAngle(ArmSubsystem.Angle.WRIST) >= _wristValue)
			_wristDir = 0.0;

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
		
		if(_shoulderDir != 0.0)
			_arm.setMotorSpeed(ArmSubsystem.Angle.SHOULDER, _shoulderDir * _speedMultiplier * shoulderSpeed);
		else
			_arm.setMotorPosition(ArmSubsystem.Angle.SHOULDER, Conversions.shoulderAndElbowAngleToEncoderPosition(_shoulderValue));
			
		if(_elbowDir != 0.0)
			_arm.setMotorSpeed(ArmSubsystem.Angle.ELBOW, _elbowDir * _speedMultiplier * elbowSpeed);
		else
			_arm.setMotorPosition(ArmSubsystem.Angle.ELBOW, Conversions.shoulderAndElbowAngleToEncoderPosition(_elbowValue));
			
		if(_wristDir != 0.0)
			_arm.setMotorSpeed(ArmSubsystem.Angle.WRIST, _wristDir * _speedMultiplier * wristSpeed);
		else
			_arm.setMotorPosition(ArmSubsystem.Angle.WRIST, Conversions.wristAngleToEncoderPosition(_wristValue));
		
	}

	@Override
	protected boolean isFinished() {
		_shoulderOnTarget[_count] = _arm.getAngle(ArmSubsystem.Angle.SHOULDER);
		_elbowOnTarget[_count] = _arm.getAngle(ArmSubsystem.Angle.ELBOW);
		_wristOnTarget[_count] = _arm.getAngle(ArmSubsystem.Angle.WRIST);

		_count = (_count + 1) % _maxCountOnTarget;

		for(int i = 1; i < _maxCountOnTarget; i++){
			if(_shoulderOnTarget[0] != _shoulderOnTarget[i])
				return false;

			if(_elbowOnTarget[0] != _elbowOnTarget[i])
				return false;

			if(_wristOnTarget[0] != _wristOnTarget[i])
				return false;
		}
		
		return true;

		/**
		if((_shoulderDir < 0)? _arm.getAngle(ArmSubsystem.Angle.SHOULDER) <= _shoulderValue : _arm.getAngle(ArmSubsystem.Angle.SHOULDER) >= _shoulderValue)
			_shoulderDir = 0.0;
		
		if((_elbowDir < 0)? _arm.getAngle(ArmSubsystem.Angle.ELBOW) <= _elbowValue : _arm.getAngle(ArmSubsystem.Angle.ELBOW) >= _elbowValue)
			_elbowDir = 0.0;
		
		if((_wristDir < 0)? _arm.getAngle(ArmSubsystem.Angle.WRIST) <= _wristValue : _arm.getAngle(ArmSubsystem.Angle.WRIST) >= _wristValue)
			_wristDir = 0.0;
			

		return 	((_shoulderDir == 0.0) && (_elbowDir == 0.0) && (_wristDir == 0.0));
		*/
	}

	
	@Override
	protected void end() {
		_arm.setMotorPosition(ArmSubsystem.Angle.SHOULDER,Conversions.shoulderAndElbowAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.SHOULDER)));
		_arm.setMotorPosition(ArmSubsystem.Angle.ELBOW,Conversions.shoulderAndElbowAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.ELBOW)));
		_arm.setMotorPosition(ArmSubsystem.Angle.WRIST, Conversions.wristAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.WRIST)));
	}
	
	
	public void setSpeedMultiplier(double value) {
		_speedMultiplier = value;
	}

}
