package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ButtonCommandsPlace extends CommandGroup {
	public ButtonCommandsPlace(RobotMap.Controller controller) {
		switch(controller) {
		case TRIGGER_LB:
			addSequential(new ChangeHandStateCommand(RobotArmCalculations.HandState.PICK_UP));
			break;
		case TRIGGER_RB:
			addSequential(new ChangeHandStateCommand(RobotArmCalculations.HandState.LOCKED));
			break;
		case X_BUTTON:
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.PLACE_START_X + 1.0));
			addSequential(new WaitCommand(0.5));
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.PLACE_START_X));
			break;
		case Y_BUTTON:
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.DISK_HIGH_POSITION_Y));
			break;
		case B_BUTTON:
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.DISK_MIDDLE_POSITION_Y));
			break;
		case A_BUTTON:
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.DISK_LOW_POSITION_Y));
			break;
		
		}
	}

}
