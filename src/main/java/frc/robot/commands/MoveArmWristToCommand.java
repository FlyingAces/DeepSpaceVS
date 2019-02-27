package frc.robot.commands;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.RobotArmCalculations;
import frc.robot.util.RobotArmCalculations.HandState;

public class MoveArmWristToCommand extends MoveArmAnglesCommand {
	public static double USE_CURRENT_LOCATION = Double.NaN;
	
	protected double _wristTargetX;
	protected double _wristTargetY;
	
	private HandState _handState;
	
	public MoveArmWristToCommand(double wristTargetX, double wristTargetY) {
		this(wristTargetX, wristTargetY, null);
	}
	
	public MoveArmWristToCommand(HandState handState) {
		this(USE_CURRENT_LOCATION, USE_CURRENT_LOCATION, handState);
	}
	
	public MoveArmWristToCommand(double wristTargetX, double wristTargetY, HandState handState) {
		super(0.0, 0.0, 0.0);
		
		_wristTargetX = wristTargetX;
		_wristTargetY = wristTargetY;
		
		_handState = handState;
	}
	
	@Override
	protected void initialize() {
		RobotArmCalculations calculations = new RobotArmCalculations(_arm.getAngle(ArmSubsystem.Angle.SHOULDER),
												 _arm.getAngle(ArmSubsystem.Angle.ELBOW), 
												 _arm.getHandState());
		calculations.setWristAngle(_arm.getAngle(ArmSubsystem.Angle.WRIST));
		calculations.setInverted(_arm.isInverted());

		if(!Double.isNaN(_wristTargetX)) 
			calculations.setWristTargetX(_wristTargetX);
		if(!Double.isNaN(_wristTargetY))
			calculations.setWristTargetY(_wristTargetY);
		
		if(_handState != null)
			calculations.setHandState(_handState);
		
		_shoulderValueParam = calculations.getShoulderAngle();
		_elbowValueParam = calculations.getElbowAngle();
		_wristValueParam = calculations.getWristAngle();
		
		super.initialize();
	}

	@Override
	protected void end() {
		super.end();
		if(_handState != null) {
			_arm.setHandState(_handState);
			_feed.sendString("currentHandState",  ArmSubsystem.getInstance().getHandState().toString());
		}
	}

}
