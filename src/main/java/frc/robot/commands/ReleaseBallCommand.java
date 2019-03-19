package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.ArmSubsystem;

public class ReleaseBallCommand extends Command {


    public ReleaseBallCommand(){
        super("ReleaseBallCommand");
    }

	@Override
	protected void initialize() {
        setTimeout(1);
		execute();
	}

	@Override
	protected void execute() {
        ArmSubsystem.getInstance().pushCylanoid();
    }
    
	@Override
	protected boolean isFinished() {
		return isTimedOut();
    }
    
    @Override
    protected void end(){
        ArmSubsystem.getInstance().releaseCylanoid();
    }

}