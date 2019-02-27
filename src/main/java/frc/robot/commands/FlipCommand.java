package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class FlipCommand extends CommandGroup {
	public static enum Direction {
		FORWARD, BACK;
	}
	
	public FlipCommand(Direction direction) {
		switch(direction) {
		case FORWARD:
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.MAX_EXTENSION_HEIGHT_BACK_Y));
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.ROBOT_BACK_X));
			
			addSequential(new MoveArmAnglesCommand(0.0, 45.0, RobotMap.Angle.WRIST_MIN_ANGLE.getAngle()));
			
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.PICK_UP_START_X));
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.GROUND_LEVEL_Y + 20.0));
			break;
		case BACK:
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.MAX_EXTENSION_HEIGHT_FRONT_Y ));
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.ROBOT_FRONT_X));
		
			addSequential(new MoveArmAnglesCommand(0.0, -35.0, RobotMap.Angle.WRIST_MAX_ANGLE.getAngle()));
			
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.ROBOT_BACK_X - (RobotMap.Measurement.MAX_EXTENSION.getInches() * .5)));
			addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.GROUND_LEVEL_Y + 20.0));
			break;
		}
			
	}
}