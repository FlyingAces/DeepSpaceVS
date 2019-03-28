package frc.robot.commands;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.TestSubsystem;
import frc.robot.util.Conversions;
import frc.robot.config.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class TestCommand extends Command {
	public static enum Direction {
		POS, NEG;
	}
	
	private TestSubsystem _test;
	
	private double _dir;
	
	public TestCommand(Direction dir) {
		super("TestCommand");
		//_test = TestSubsystem.getInstance();
		//requires(_test);
		
		_dir = (dir == Direction.POS)? 1.0 : -1.0;
	}
	
	@Override
	protected void initialize() {
		execute();
	}

	@Override
	protected void execute() {
		_test.setMotorSpeed(_dir * 	ControllerSubsystem.getInstance().getController().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_RT.getChannel()));
		//_test.setMotorPosition(Conversions.shoulderAndElbowAngleToEncoderPosition(90.0));
		//ArmSubsystem.getInstance().setMotorSpeed(ArmSubsystem.Angle.WRIST, _dir * ControllerSubsystem.getInstance().getController().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_RT.getChannel()));
		//ArmSubsystem.getInstance().setMotorSpeed(ArmSubsystem.Angle.ELBOW, _dir * 	ControllerSubsystem.getInstance().getController().getRawAxis(RobotMap.Controller.AXIS_TRIGGER_RT.getChannel()));
		//System.out.println("Encoder curr: " + _test.getCurrent());
		//System.out.println("Encoder curr: " + _test.getCurrent() + " Encoder position: " +  _test.getEncoderPosition());
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		//System.out.println("Encoder Pos: " + _test.getEncoderPosition());
		return false;
	}
	@Override
	protected void end() {
		_test.setMotorSpeed(0.0);
		//ArmSubsystem.getInstance().setMotorSpeed(ArmSubsystem.Angle.WRIST, 0.0);
		//ArmSubsystem.getInstance().setMotorSpeed(ArmSubsystem.Angle.ELBOW, 0.0);
	}

}
