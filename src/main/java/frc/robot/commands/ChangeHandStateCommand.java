package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ChangeHandStateCommand extends CommandGroup {
	public ChangeHandStateCommand(RobotArmCalculations.HandState handState) {
		switch(handState) {
		case PICK_UP:
			addSequential(
				new ConditionalCommand(
					new MoveArmWristToCommand(RobotMap.PICK_UP_START_X, RobotMap.PICK_UP_MID_STEP_Y), 
					new WaitCommand(.01)) {

					@Override
					protected boolean condition() {
						return ArmSubsystem.getInstance().getWristTargetY() > RobotMap.PICK_UP_MID_STEP_Y;
					}
					
				});

			addSequential(new MoveArmWristToCommand(RobotMap.PICK_UP_START_X, RobotMap.PICK_UP_START_Y,RobotArmCalculations.HandState.PICK_UP));
			
			break;
		case PLACE_BALL:
			addSequential(
				new ConditionalCommand(
					new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.PLACE_MID_STEP_Y), 
					new WaitCommand(.01)) {

					@Override
					protected boolean condition() {
						return (ArmSubsystem.getInstance().getHandState() != RobotArmCalculations.HandState.PLACE_DISK) &&
							   (ArmSubsystem.getInstance().getWristTargetY() > RobotMap.PICK_UP_MID_STEP_Y);
					}
					
				});

			addSequential(new MoveArmWristToCommand(RobotArmCalculations.HandState.PLACE_BALL));
			break;
		case PLACE_DISK:
			addSequential(
				new ConditionalCommand(
					new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.PLACE_MID_STEP_Y), 
					new WaitCommand(.01)) {

					@Override
					protected boolean condition() {
						return (ArmSubsystem.getInstance().getHandState() != RobotArmCalculations.HandState.PLACE_BALL) &&
							   (ArmSubsystem.getInstance().getWristTargetY() > RobotMap.PICK_UP_MID_STEP_Y);
					}
					
				});
			addSequential(
				new ConditionalCommand(
					new MoveArmWristToCommand(RobotMap.PLACE_START_X, RobotMap.PLACE_START_Y), 
					new WaitCommand(.01)) {

					@Override
					protected boolean condition() {
						return ArmSubsystem.getInstance().getHandState() != RobotArmCalculations.HandState.PLACE_BALL;
					}
					
				});

			addSequential(new MoveArmWristToCommand(RobotArmCalculations.HandState.PLACE_DISK));
			break;
		case LOCKED:
			addSequential(new MoveArmWristToCommand(RobotArmCalculations.HandState.LOCKED));
			break;
		}
	}

}
