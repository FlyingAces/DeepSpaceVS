package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.util.RobotArmCalculations;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ButtonCommandsLocked extends CommandGroup {
	public ButtonCommandsLocked(RobotMap.Controller controller) {
		switch(controller) {
		case TRIGGER_LB:
			addSequential(
					new ConditionalCommand(
							new ChangeHandStateCommand(RobotArmCalculations.HandState.PLACE), 
							new WaitCommand(.01)) {

							@Override
							protected boolean condition() {
								return !ArmSubsystem.getInstance().isInverted();
							}
							
					});
			break;
		case TRIGGER_RB:
			addSequential(
					new ConditionalCommand(
							new ChangeHandStateCommand(RobotArmCalculations.HandState.PICK_UP), 
							new WaitCommand(.01)) {

							@Override
							protected boolean condition() {
								return !ArmSubsystem.getInstance().isInverted();
							}
							
					});
			break;
		case X_BUTTON:
			addSequential(
					new ConditionalCommand(
							new FlipCommand(FlipCommand.Direction.FORWARD), 
							new FlipCommand(FlipCommand.Direction.BACK)) {

							@Override
							protected boolean condition() {
								return ArmSubsystem.getInstance().isInverted();
							}
							
					});
			break;
		case Y_BUTTON:
			addSequential(new MoveArmAnglesCommand(RobotMap.Angle.SHOULDER_START_ANGLE.getAngle(), 
												   RobotMap.Angle.ELBOW_START_ANGLE.getAngle(), 
												   RobotMap.Angle.WRIST_START_ANGLE.getAngle()));
			break;
		case B_BUTTON:
			addSequential(new InitializeArmCommand());
			break;
		}
	}
}
