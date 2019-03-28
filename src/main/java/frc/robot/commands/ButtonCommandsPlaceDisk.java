package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ButtonCommandsPlaceDisk extends CommandGroup {
	public ButtonCommandsPlaceDisk(RobotMap.Controller controller) {
		switch(controller) {
		case TRIGGER_LB:
			addSequential(new ChangeHandStateCommand(RobotArmCalculations.HandState.PLACE_BALL));
			break;
		case TRIGGER_RB:
            addSequential(new ChangeHandStateCommand(RobotArmCalculations.HandState.LOCKED));
			break;
		case X_BUTTON:
			addSequential(new MoveArmWristToCommand(RobotMap.PLACE_START_X + 5.0, MoveArmWristToCommand.USE_CURRENT_LOCATION));
			addSequential(new ReleaseBallCommand());
			addSequential(new MoveArmWristToCommand(RobotMap.PLACE_START_X, MoveArmWristToCommand.USE_CURRENT_LOCATION));

			break;
		case Y_BUTTON:
			addSequential(
				new ConditionalCommand(
					new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.PLACE_MID_STEP_Y), 
					new WaitCommand(.01)) {

					@Override
					protected boolean condition() {
						return ArmSubsystem.getInstance().getWristTargetY() < RobotMap.PICK_UP_MID_STEP_Y;
					}
					
				});

			addSequential(new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.DISK_HIGH_POSITION_Y));

			break;
		case B_BUTTON:
			addSequential(
				new ConditionalCommand(
					new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.PLACE_MID_STEP_Y), 
					new WaitCommand(.01)) {

					@Override
					protected boolean condition() {
						return ((ArmSubsystem.getInstance().getWristTargetY() < RobotMap.PICK_UP_MID_STEP_Y) && 
								(RobotMap.DISK_MIDDLE_POSITION_Y > RobotMap.PICK_UP_MID_STEP_Y)) ||
							   ((ArmSubsystem.getInstance().getWristTargetY() > RobotMap.PICK_UP_MID_STEP_Y) && 
								(RobotMap.DISK_MIDDLE_POSITION_Y < RobotMap.PICK_UP_MID_STEP_Y));
					}
					
				});

			addSequential(new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.DISK_MIDDLE_POSITION_Y));
			break;
		case A_BUTTON:
			addSequential(
				new ConditionalCommand(
					new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.PLACE_MID_STEP_Y), 
					new WaitCommand(.01)) {

					@Override
					protected boolean condition() {
						return ArmSubsystem.getInstance().getWristTargetY() > RobotMap.PICK_UP_MID_STEP_Y;
					}
					
				});

			addSequential(new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.DISK_LOW_POSITION_Y));
			break;
		
		}
	}

}
