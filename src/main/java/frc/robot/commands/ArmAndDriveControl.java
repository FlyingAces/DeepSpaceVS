package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmAndDriveControl extends CommandGroup {
	public ArmAndDriveControl() {
		
		addParallel(new CommandByController());
		//addParallel(new CommandRobotArmWithController());
	}

}
