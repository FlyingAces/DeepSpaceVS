package frc.robot.subsystems;

import frc.robot.config.RobotMap;
import frc.robot.commands.ButtonConditionalCommands;
import frc.robot.commands.MoveArmAnglesCommand;
import frc.robot.commands.ReleaseBallCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ControllerSubsystem extends Subsystem {
	
	private Joystick _joystick;
	private JoystickButton _buttonTriggerRB;
	private JoystickButton _buttonTriggerLB;
	private JoystickButton _buttonX;
	private JoystickButton _buttonY;
	private JoystickButton _buttonB;
	private JoystickButton _buttonA;
	
	private static ControllerSubsystem _instance;
	
	private ControllerSubsystem(){
		_joystick = new Joystick(RobotMap.Controller.JOYSTICK_PORT.getChannel());
		
		_buttonTriggerRB = new JoystickButton(_joystick, RobotMap.Controller.TRIGGER_RB.getChannel());
		_buttonTriggerRB.whenPressed(new ButtonConditionalCommands(RobotMap.Controller.TRIGGER_RB));
		
		_buttonTriggerLB = new JoystickButton(_joystick, RobotMap.Controller.TRIGGER_LB.getChannel());
		_buttonTriggerLB.whenPressed(new ButtonConditionalCommands(RobotMap.Controller.TRIGGER_LB));
		
		_buttonX = new JoystickButton(_joystick, RobotMap.Controller.X_BUTTON.getChannel());
		_buttonX.whenPressed(new ButtonConditionalCommands(RobotMap.Controller.X_BUTTON));
		//_buttonX.whenPressed(new MoveArmAnglesCommand(MoveArmAnglesCommand.USE_CURRENT_ANGLE, MoveArmAnglesCommand.USE_CURRENT_ANGLE, 0.0));

		_buttonY = new JoystickButton(_joystick, RobotMap.Controller.Y_BUTTON.getChannel());
		_buttonY.whenPressed(new ButtonConditionalCommands(RobotMap.Controller.Y_BUTTON));
		
		_buttonB = new JoystickButton(_joystick, RobotMap.Controller.B_BUTTON.getChannel());
		_buttonB.whenPressed(new ButtonConditionalCommands(RobotMap.Controller.B_BUTTON));
		//_buttonB.whileHeld(new TestCommand(TestCommand.Direction.POS));

		_buttonA = new JoystickButton(_joystick, RobotMap.Controller.A_BUTTON.getChannel());
		_buttonA.whenPressed(new ButtonConditionalCommands(RobotMap.Controller.A_BUTTON));
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public static ControllerSubsystem getInstance(){
		if(_instance == null)
			_instance = new ControllerSubsystem();
		
		return _instance;
	}
	
	public Joystick getController(){
		return _joystick;
	}

}