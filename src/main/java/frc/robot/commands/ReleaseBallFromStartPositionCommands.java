package frc.robot.commands;

import frc.robot.config.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ReleaseBallFromStartPositionCommands extends CommandGroup {
	public ReleaseBallFromStartPositionCommands() {
        
        addSequential(new MoveArmAnglesCommand(MoveArmAnglesCommand.USE_CURRENT_ANGLE, MoveArmAnglesCommand.USE_CURRENT_ANGLE, 0.0));
        addSequential(new WaitCommand(.3));
        
        addSequential(new MoveArmAnglesCommand(MoveArmAnglesCommand.USE_CURRENT_ANGLE, -120.0, MoveArmAnglesCommand.USE_CURRENT_ANGLE));
        addSequential(new WaitCommand(.3));

        addSequential(new ReleaseBallCommand());
        addSequential(new WaitCommand(1.5));

        addSequential(new MoveArmAnglesCommand(MoveArmAnglesCommand.USE_CURRENT_ANGLE, 
                                               RobotMap.Angle.ELBOW_START_ANGLE.getAngle(), 
                                               147.0));
	}

}