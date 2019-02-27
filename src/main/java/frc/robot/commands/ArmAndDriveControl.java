package frc.robot.commands;

import frc.robot.config.RobotMap;
import frc.robot.subsystems.ArmSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ArmAndDriveControl extends CommandGroup {
	public ArmAndDriveControl() {
		/*
		addSequential(
				new ConditionalCommand(
						new InitializeArmCommand(), 
						new WaitCommand(.01)) {

						@Override
						protected boolean condition() {
							return (ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.SHOULDER) == RobotMap.Angle.SHOULDER_START_ANGLE.getAngle()) &&
								   (ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.ELBOW) == RobotMap.Angle.ELBOW_START_ANGLE.getAngle()) &&
								   (ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.WRIST) == RobotMap.Angle.WRIST_START_ANGLE.getAngle());
						}
						
				});
		*/
		addParallel(new CommandByController());
		addParallel(new CommandRobotArmWithController());
	}

}
