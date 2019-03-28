package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.util.Conversions;

public class MoveArmJointsByButtonCommand extends Command {
	private ArmSubsystem _arm;
    
    private ArmSubsystem.Angle _joint;
    private RobotMap.Controller _controller;
	
	public MoveArmJointsByButtonCommand(ArmSubsystem.Angle joint, RobotMap.Controller controller) {
        super("MoveArmJointsByButtonCommand");
        
        _arm = ArmSubsystem.getInstance();
        requires(_arm);

        _joint = joint;
        _controller = controller;
	}
	
	@Override
	protected void initialize() {
		execute();
	}

	@Override
	protected void execute() {
        int dir = 0;

        //dpad left
        if(ControllerSubsystem.getInstance().getController().getPOV() > 225 && 
           ControllerSubsystem.getInstance().getController().getPOV() < 315)
           dir = -1; 
        
        //dpad right   
        if(ControllerSubsystem.getInstance().getController().getPOV() > 45 && 
           ControllerSubsystem.getInstance().getController().getPOV() < 135)
           dir = 1; 

		_arm.setMotorSpeed(_joint, dir);
	}

	@Override
	protected boolean isFinished() {
		return ControllerSubsystem.getInstance().getController().getRawButtonReleased(_controller.getChannel());
    }
    
	@Override
	protected void end() {
		_arm.setMotorPosition(ArmSubsystem.Angle.SHOULDER, Conversions.shoulderAndElbowAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.SHOULDER)));
		_arm.setMotorPosition(ArmSubsystem.Angle.ELBOW, Conversions.shoulderAndElbowAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.ELBOW)));
		_arm.setMotorPosition(ArmSubsystem.Angle.WRIST, Conversions.wristAngleToEncoderPosition(_arm.getAngle(ArmSubsystem.Angle.WRIST)));
	}

}
