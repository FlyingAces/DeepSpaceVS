package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.util.RobotArmCalculations;

public class CommandRobotArmWithController extends MoveArmAnglesCommand {
	private ControllerSubsystem _controller; 
	
	public CommandRobotArmWithController() {
		super(0.0, 0.0, 0.0);
		_controller = ControllerSubsystem.getInstance();
	}
	
	@Override
	protected void initialize() {
		RobotArmCalculations calculations = new RobotArmCalculations(_arm.getAngle(ArmSubsystem.Angle.SHOULDER),
						 											 _arm.getAngle(ArmSubsystem.Angle.ELBOW), 
						 											 _arm.getHandState());
		calculations.setWristAngle(_arm.getAngle(ArmSubsystem.Angle.WRIST));
		
		double rightX = _controller.getController().getRawAxis(RobotMap.Controller.AXIS_RIGHT_X.getChannel());
		double rightY = _controller.getController().getRawAxis(RobotMap.Controller.AXIS_RIGHT_Y.getChannel());
		
		if(rightX > -.1 && rightX < .1)
			rightX = 0.0;
		
		if(rightY > -.1 && rightY < .1)
			rightY = 0.0;
		
		switch (ArmSubsystem.getInstance().getHandState()) {
		case LOCKED:
			if (rightX > 0 &&
					((calculations.isInverted())? calculations.getWristTargetX() < RobotMap.ROBOT_BACK_X : 
												  calculations.getWristTargetX() < RobotMap.ROBOT_MAX_FRONT_X))
				calculations.setWristTargetX(calculations.getWristTargetX() + 1);
			else if (rightX < 0 &&
					((calculations.isInverted())? calculations.getWristTargetX() > RobotMap.ROBOT_MIN_BACK_X : 
						  						  calculations.getWristTargetX() > RobotMap.ROBOT_FRONT_X))
				calculations.setWristTargetX(calculations.getWristTargetX() - 1);

			if (rightY < 0 &&
					((calculations.isInverted())? calculations.getWristTargetY() < RobotMap.MAX_EXTENSION_HEIGHT_BACK_Y : 
											      calculations.getWristTargetY() < RobotMap.MAX_EXTENSION_HEIGHT_FRONT_Y))
				calculations.setWristTargetY(calculations.getWristTargetY() + 1);
			else if (rightY > 0 && 
					(calculations.getWristTargetY() > RobotMap.MAX_STEP_DOWN_Y))
				calculations.setWristTargetY(calculations.getWristTargetY() - 1);
			break;
		case PICK_UP:
			if (rightY < 0 &&
					calculations.getWristTargetY() < RobotMap.PICK_UP_MAX_HEIGHT_Y)
				calculations.setWristTargetY(calculations.getWristTargetY() + 1);
			else if (rightY > 0 && 
					calculations.getWristTargetY() > RobotMap.PICK_UP_GROUND_LEVEL_Y)
				calculations.setWristTargetY(calculations.getWristTargetY() - 1);
			break;
		case PLACE_BALL:
		case PLACE_DISK:
			if (rightY < 0 &&
					calculations.getWristTargetY() < RobotMap.PLACE_MAX_HEIGHT_Y) 
				calculations.setWristTargetY(calculations.getWristTargetY() + 1);
			else if (rightY > 0 && 
					calculations.getWristTargetY() > RobotMap.PLACE_START_Y)
				calculations.setWristTargetY(calculations.getWristTargetY() - 1);
			break;
		}
		
		_shoulderValueParam = calculations.getShoulderAngle();
		_elbowValueParam = calculations.getElbowAngle();
		_wristValueParam = calculations.getWristAngle();
		
		super.initialize();
	}
	
	@Override
	protected void execute() {
		double valueX = Math.abs(_controller.getController().getRawAxis(RobotMap.Controller.AXIS_RIGHT_X.getChannel()));
		double valueY = Math.abs(_controller.getController().getRawAxis(RobotMap.Controller.AXIS_RIGHT_Y.getChannel()));
		
		if(valueX > -.1 && valueX < .1)
			valueX = 0.1;
		
		if(valueY > -.1 && valueY < .1)
			valueY = 0.1;
			
		_speedMultiplier = (valueX > valueY)? valueX : valueY;
		
		super.execute();
	}
	
	@Override
	protected boolean isFinished() {
		if(super.isFinished())
				initialize();
			
		return false; 
	}
}