//Package declaration
package frc.robot;

import frc.robot.commands.ArmAndDriveControl;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.util.Feed;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

//Class declaration
public class Robot extends IterativeRobot {
	
	//Command Objects
	private Command _autonomousCommand;
	private Command _teleopCommand;
	private Command _testCommand;
	
	private int _count;
	
	public Robot() {
	}
	
	public void robotInit() {
		_teleopCommand = new ArmAndDriveControl();
		_autonomousCommand = new ArmAndDriveControl();

		ArmSubsystem.getInstance().setEncodersToStart();
		
		Feed.getInstance().sendAngleInfo("currentAngles", ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.SHOULDER), 
														  ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.ELBOW), 
														  ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.WRIST));
		Feed.getInstance().sendAngleInfo("endAngles", ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.SHOULDER), 
												      ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.ELBOW), 
												      ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.WRIST));
		Feed.getInstance().sendString("currentHandState",  ArmSubsystem.getInstance().getHandState().toString());

		ControllerSubsystem.getInstance();
		CameraSubsystem.getInstance();
	}
	

	public void teleopInit() {
		if(_autonomousCommand != null) 
			_autonomousCommand.cancel();
		
		if(_teleopCommand != null)
			_teleopCommand.start();
	}
	
	public void teleopPeriodic() {
		Scheduler.getInstance().run(); 
		if(_count++ > 10){
			Feed.getInstance().sendAngleInfo("currentAngles", 
											ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.SHOULDER), 
											ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.ELBOW), 
											ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.WRIST));
			
			_count = 0;
		}
	}
	
	public void autonomousInit() {
		if(_teleopCommand != null)
			_teleopCommand.cancel();
		
		if(_autonomousCommand != null) 
			_autonomousCommand.start();
		
	}
	
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		if(_count++ > 10){
			Feed.getInstance().sendAngleInfo("currentAngles", 
											ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.SHOULDER), 
											ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.ELBOW), 
											ArmSubsystem.getInstance().getAngle(ArmSubsystem.Angle.WRIST));
			_count = 0;
		}
	}
	
	public void testInit() {
		System.out.println("Start Test");
	}
}