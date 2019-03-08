package frc.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.Conversions;
import frc.robot.util.Feed;

import edu.wpi.first.wpilibj.command.Command;

public class MoveArmAnglesAndBreakCommand extends Command {

    protected ArmSubsystem _arm;
    protected Feed _feed;
    
    protected double _shoulderValueParam;
	protected double _elbowValueParam;
    protected double _wristValueParam;
    
    protected int _shoulderValue;
	protected int _elbowValue;
    protected int _wristValue;
    
    protected int _count;

    public MoveArmAnglesAndBreakCommand(double shoulderValue, double elbowValue, double wristValue) {
        super("PIDshoulderMove");

        _arm = ArmSubsystem.getInstance();
		requires(_arm);
		
		_feed = Feed.getInstance();
		
		_shoulderValueParam = shoulderValue;
		_elbowValueParam = elbowValue;
        _wristValueParam = wristValue;
    }

    @Override
	protected void initialize() {
        _shoulderValue = (Double.isNaN(_shoulderValueParam))? 
            Conversions.shoulderAndElbowAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.SHOULDER)) : 
            Conversions.shoulderAndElbowAngleToEncoderPosition(_shoulderValueParam);
        _elbowValue = (Double.isNaN(_elbowValueParam))? 
            Conversions.shoulderAndElbowAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.ELBOW)) : 
            Conversions.shoulderAndElbowAngleToEncoderPosition(_elbowValueParam);
        _wristValue = (Double.isNaN(_wristValueParam))? 
            Conversions.shoulderAndElbowAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.WRIST)) : 
            Conversions.shoulderAndElbowAngleToEncoderPosition(_wristValueParam);

        _feed.sendAngleInfo("endAngles", _shoulderValueParam, _elbowValueParam, _wristValueParam);
    }

    @Override
	protected void execute() {
		_arm.setMotorPosition(_shoulderValue, _elbowValue, _wristValue);
        
        if(_count++ > 30){
            System.out.println("shoulderValue : " + _shoulderValue + ", encoderValue : " + _arm.getEncoder());
            _feed.sendAngleInfo("currentAngles", _arm.getAngle(ArmSubsystem.Angle.SHOULDER), _arm.getAngle(ArmSubsystem.Angle.ELBOW), _arm.getAngle(ArmSubsystem.Angle.WRIST));
            _count = 0;
        }
	}


    @Override
    protected boolean isFinished() {
        return false;
    }
}