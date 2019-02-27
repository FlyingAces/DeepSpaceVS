package frc.robot.commands;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.Angle;

public class ZeroOutArmAnglesCommand extends MoveArmAnglesCommand {
	private Angle _angle;
	
	public ZeroOutArmAnglesCommand() {
		super(0.0, 0.0, 0.0);
		
		setTimeout(3);
	}
	
	public ZeroOutArmAnglesCommand(Angle angle) {
		super((angle == ArmSubsystem.Angle.SHOULDER)? 0.0 : MoveArmAnglesCommand.USE_CURRENT_ANGLE,
			  (angle == ArmSubsystem.Angle.ELBOW)? 0.0 : MoveArmAnglesCommand.USE_CURRENT_ANGLE,
			  (angle == ArmSubsystem.Angle.WRIST)? 0.0 : MoveArmAnglesCommand.USE_CURRENT_ANGLE);
		
		_angle = angle;
		
		setTimeout(3);
		
	}
	
	@Override
	protected boolean isFinished() {
		if(ArmSubsystem.getInstance().isAngleSwitchSet(ArmSubsystem.Angle.SHOULDER)) {
			ArmSubsystem.getInstance().zeroOutAnglePosition(ArmSubsystem.Angle.SHOULDER);
			_shoulderDir = 0.0;
		}
		
		if(ArmSubsystem.getInstance().isAngleSwitchSet(ArmSubsystem.Angle.ELBOW)) {
			ArmSubsystem.getInstance().zeroOutAnglePosition(ArmSubsystem.Angle.ELBOW);
			_elbowDir = 0.0;
		}
		
		if(ArmSubsystem.getInstance().isAngleSwitchSet(ArmSubsystem.Angle.WRIST)) {
			ArmSubsystem.getInstance().zeroOutAnglePosition(ArmSubsystem.Angle.WRIST);
			_wristDir = 0.0;
		}
		
		boolean isFinish = false;
		if(_angle != null)
			isFinish = super.isFinished();
		else
			isFinish = ((_shoulderDir == 0.0) && (_elbowDir == 0.0) && (_wristDir == 0.0));
		
		return isFinish || isTimedOut();
	}

}
