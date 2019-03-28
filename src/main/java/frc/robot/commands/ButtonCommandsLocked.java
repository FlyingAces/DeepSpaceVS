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
							new ChangeHandStateCommand(RobotArmCalculations.HandState.PLACE_DISK), 
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
							new InitializeArmCommand(InitializeArmCommand.Position.PICK_UP), 
							new InitializeArmCommand(InitializeArmCommand.Position.START)) {
							
							private boolean _toggle = false;
							
							@Override
							protected boolean condition() {
								_toggle = !_toggle;
								return _toggle;
							}
							
					});
			break;
		case Y_BUTTON:
			addSequential(new MoveArmJointsByButtonCommand(ArmSubsystem.Angle.WRIST, controller));
			break;
		case A_BUTTON:
			addSequential(new MoveArmJointsByButtonCommand(ArmSubsystem.Angle.ELBOW, controller));
			break;
		case B_BUTTON:
			addSequential(new MoveArmJointsByButtonCommand(ArmSubsystem.Angle.SHOULDER, controller));
			break;
		}
	}
}
