package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;

import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class InitializeArmCommand extends CommandGroup {
	public InitializeArmCommand() {

		addSequential(new MoveArmAnglesCommand(0.0, 45.0, -45.0));
		addSequential(new WaitCommand(.5));
		addSequential(new MoveArmWristToCommand(RobotMap.PICK_UP_START_X, RobotMap.PICK_UP_START_Y, RobotArmCalculations.HandState.PICK_UP));
			
		//addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.PICK_UP_START_X));
		//addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.GROUND_LEVEL_Y + 20.0));
		
		/*addSequential(new MoveArmWristToCommand(0.0, 
				 RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches() + (RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches()*.75)));

		addSequential(new ZeroOutArmAnglesCommand());
		
		addSequential(new MoveArmAnglesCommand(MoveArmAnglesCommand.USE_CURRENT_ANGLE, -35.0, 170));
		
		addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.X, RobotMap.ROBOT_BACK_X - 10.0));
		addSequential(new MoveArmAlongAxisCommand(MoveArmAlongAxisCommand.Axis.Y, RobotMap.GROUND_LEVEL_Y + 5.0));*/
	}

}
