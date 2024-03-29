package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class CommandByController extends Command{
	private DriveTrainSubsystem _drive;
	private ControllerSubsystem _controller; 
	
	public CommandByController() {
		super("CommandByController");
		
		_drive = DriveTrainSubsystem.getInstance();
		requires(_drive);
		
		_controller = ControllerSubsystem.getInstance();
		
	}

	@Override
	protected void initialize() {
		System.out.println("CommandByController initialized");
    }
	
	@Override
	protected void execute() {
    	double driveSpeed = _controller.getController().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_RT.getChannel()) -
    			_controller.getController().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_LT.getChannel());
		double driveAngle = _controller.getController().getRawAxis(RobotMap.Controller.AXIS_LEFT_X.getChannel());

			_drive.arcadeDrive(driveSpeed, driveAngle);
    }
	
	@Override
	protected boolean isFinished() {
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
