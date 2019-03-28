package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class ButtonConditionalCommands extends ConditionalCommand {
	public ButtonConditionalCommands(RobotMap.Controller controller) {
		super(new ButtonCommandsLocked(controller), 
				new ConditionalCommand( new ButtonCommandsPickUp(controller),
										new ConditionalCommand(new ButtonCommandsPlaceBall(controller), 
															   new ButtonCommandsPlaceDisk(controller))
										{
											@Override
											protected boolean condition() {
												return ArmSubsystem.getInstance().getHandState() == RobotArmCalculations.HandState.PLACE_BALL;
											}
										})
				{
										
					@Override
					protected boolean condition() {
						return ArmSubsystem.getInstance().getHandState() == RobotArmCalculations.HandState.PICK_UP;
					}
				});
	}

	@Override
	protected boolean condition() {
		return ArmSubsystem.getInstance().getHandState() == RobotArmCalculations.HandState.LOCKED;
	}
}
