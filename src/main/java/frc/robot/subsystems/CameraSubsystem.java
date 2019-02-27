package frc.robot.subsystems;

//import org.opencv.core.Core;
import org.opencv.core.Mat;
//import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import frc.robot.config.RobotMap;
//import org.usfirst.frc.team4711.robot.vision.GripPipeline;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;

public class CameraSubsystem extends Subsystem {

	private CvSink cvSink;
	
	private static CameraSubsystem instance;
	
	private CameraSubsystem(){
		super("robotEyeSubsystem");

		UsbCamera cameraFront = CameraServer.getInstance().startAutomaticCapture("cam0", RobotMap.CAMERA_FRONT);
		cameraFront.setResolution(RobotMap.CAMERA_IMG_WIDTH, RobotMap.CAMERA_IMG_HEIGHT);
		cameraFront.setBrightness(25);
		
		
		cvSink = CameraServer.getInstance().getVideo(cameraFront);
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static CameraSubsystem getInstance() {
		if(instance == null)
			instance = new CameraSubsystem();
		
		return instance;
	}
	
	public Mat getFrontCameraPicture() {
		Mat image = new Mat();
		cvSink.grabFrame(image);
		return image;
	}

}
