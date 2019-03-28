package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;

import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class InitializeArmCommand extends CommandGroup {
	public static enum Position {
		START, PICK_UP;
	}

	public InitializeArmCommand(Position position) {
		switch(position){
			case START:
				addSequential(
					new ConditionalCommand(
						new MoveArmWristToCommand(RobotMap.ROBOT_FRONT_X, RobotMap.ROBOT_FRONT_MID_STEP_Y), 
						new WaitCommand(.01)) {

						@Override
						protected boolean condition() {
							return ArmSubsystem.getInstance().getWristTargetY() < RobotMap.ROBOT_FRONT_MID_STEP_Y;
						}
						
					});
				
				addSequential(new MoveArmAnglesCommand(RobotMap.Angle.SHOULDER_START_ANGLE.getAngle(), 
													RobotMap.Angle.ELBOW_START_ANGLE.getAngle(), 
													RobotMap.Angle.WRIST_START_ANGLE.getAngle()));
			break;
			case PICK_UP:
				addSequential(new MoveArmWristToCommand(RobotMap.ROBOT_BACK_X, RobotMap.ROBOT_BACK_MID_STEP_Y));
				addSequential(new MoveArmAnglesCommand(0.0, 45.0, MoveArmAnglesCommand.USE_CURRENT_ANGLE));
				addSequential(new MoveArmWristToCommand(RobotMap.ROBOT_FRONT_X, RobotMap.ROBOT_FRONT_MID_STEP_Y));
				addSequential(new ChangeHandStateCommand(RobotArmCalculations.HandState.PICK_UP));
			break;
		}
	}

}
