package frc.robot.util;

import frc.robot.config.RobotMap;

public class Conversions {
	public static double inchToEncoderPosition(double inches) {
		double circumference = RobotMap.Measurement.WHEEL_DIAMETER.getInches() * Math.PI;
		double encoderPosition = ((inches / circumference) * 4.0) * 1024.0;
		
		return encoderPosition;
	}
	
	public static double encoderPositionToInches(double encoderPosition) {
		double circumference = RobotMap.Measurement.WHEEL_DIAMETER.getInches() * Math.PI;
		double toInches = (encoderPosition / (4.0 * 1024.0)) * circumference;
		
		return toInches;
	}
	
	public static double angleToEncoderPosition(double angle) {
		double lengthOfArc = (angle / 360.0) * RobotMap.Measurement.ROBOT_WIDTH.getInches() * Math.PI;
		return inchToEncoderPosition(lengthOfArc);
	}
	
	public static double shoulderAndElbowEncoderPositionToAngle(double position) {
		return (position / (6.0 * 1024 * 4.0)) * 360.0 % 360.0;
	}
	
	public static double shoulderAndElbowAngleToEncoderPosition(double angle) {
		return ((angle / 360.0) * (6.0 * 1024 * 4.0));
	}
	
	public static double wristEncoderPositionToAngle(double encoderPosition) {
		return (encoderPosition / ( 250.0 * 4.0 )) * 360.0 % 360.0;
	}
	
	public static double wristAngleToEncoderPosition(double angle) {
		return ((angle / 360.0) * (250.0 * 4.0));
	}
	
	public static double radianToDegree(double radian) {
		return (radian * 180) / Math.PI;
	}
	
	public static double degreeToRadian(double degree) {
		return (degree * Math.PI) / 180;
	}
}
