package frc.robot.subsystems;

import frc.robot.config.RobotMap;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraSubsystem extends Subsystem {
	
	private static CameraSubsystem instance;
	
	private CameraSubsystem(){
		super("robotEyeSubsystem");

		UsbCamera cameraFront = CameraServer.getInstance().startAutomaticCapture("frontEye", RobotMap.CAMERA_FRONT);
		cameraFront.setResolution(RobotMap.CAMERA_IMG_WIDTH, RobotMap.CAMERA_IMG_HEIGHT);
		cameraFront.setBrightness(25);

		UsbCamera cameraBack = CameraServer.getInstance().startAutomaticCapture("backEye", RobotMap.CAMERA_BACK);
		cameraFront.setResolution(RobotMap.CAMERA_IMG_WIDTH, RobotMap.CAMERA_IMG_HEIGHT);
		cameraFront.setBrightness(25);
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static CameraSubsystem getInstance() {
		if(instance == null)
			instance = new CameraSubsystem();
		
		return instance;
	}

}
