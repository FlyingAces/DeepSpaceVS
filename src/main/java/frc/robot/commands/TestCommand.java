package frc.robot.commands;

import frc.robot.subsystems.TestSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class TestCommand extends Command {
	public static enum Direction {
		POS, NEG;
	}
	
	private TestSubsystem _test;
	
	private double _dir;
	
	public TestCommand(Direction dir) {
		super("TestCommand");
		_test = TestSubsystem.getInstance();
		requires(_test);
		
		_dir = (dir == Direction.POS)? 1.0 : -1.0;
	}
	
	@Override
	protected void initialize() {
		execute();
	}
	
	@Override
	protected void execute() {
		_test.setMotorSpeed(_dir);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		System.out.println("Encoder Pos: " + _test.getEncoderPosition());
		return false;
	}
	@Override
	protected void end() {
		_test.setMotorSpeed(0.0);
	}

}
