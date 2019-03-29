package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ButtonCommandsPickUp extends CommandGroup {
	public ButtonCommandsPickUp(RobotMap.Controller controller) {
		switch(controller) {
		case TRIGGER_LB:
			addSequential(new ChangeHandStateCommand(RobotArmCalculations.HandState.LOCKED));
			break;
		case TRIGGER_RB:
			addSequential(new ChangeHandStateCommand(RobotArmCalculations.HandState.PLACE_BALL));
			break;
		case X_BUTTON:
			addSequential(new MoveArmWristToCommand(RobotMap.PICK_UP_START_X, RobotMap.PICK_UP_GROUND_LEVEL_Y));
			addSequential(new WaitCommand(0.25));
			addSequential(new MoveArmWristToCommand(RobotMap.PICK_UP_START_X, RobotMap.PICK_UP_START_Y));
			
			break;
		}
	}
}