package frc.robot.config;

public class RobotMap {
	
	public static enum Talon {
		SHOULDER_MOTOR(8),
		ELBOW_MOTOR(5),
		WRIST_MOTOR(7),
		CYLANOID(6),
		LEFT_FRONT(3),
		LEFT_BACK(4),
		RIGHT_FRONT(1),
		RIGHT_BACK(2),
		TEST(5);
		
		private int _channel;
		
		private Talon(int channel) {
			_channel = channel;
		}
		
		public int getChannel() {
			return _channel;
		}
	}
	
	public static enum Measurement {
		//width of inside of track
		ROBOT_WIDTH(29.5),
		ROBOT_LENGTH(31.75),
		ROBOT_HEIGHT(15.5),
		WHEEL_DIAMETER(6.00),
		BASE_SEGMENT_LENGTH(20.0),
		BASE_CONNECTION(8.0),
		SHOULDER_SEGMENT_LENGTH(25.6),
		ELBOW_SEGMENT_LENGTH(35.0),
		HAND_HEIGHT(20.0),
		HAND_WIDTH(12.0),
		MAX_EXTENSION(30.0),
		MAX_STEP_HEIGHT(19.0);
		
		private double _inches;
		
		private Measurement(double inches) {
			_inches = inches;
		}
		
		public double getInches() {
			return _inches;
		}
	}
	
	public static enum Angle {
		SHOULDER_START_ANGLE(125.0),
		SHOULDER_MAX_ANGLE(150.0),
		SHOULDER_MIN_ANGLE(-150.0),
		ELBOW_START_ANGLE(-170.0),
		ELBOW_MAX_ANGLE(175.0),
		ELBOW_MIN_ANGLE(-175.0),
		WRIST_START_ANGLE(170.0),
		WRIST_MAX_ANGLE(170.0),
		WRIST_MIN_ANGLE(-170.0);
		
		private double _angle;
		
		private Angle(double angle) {
			_angle = angle;
		}
		
		public double getAngle() {
			return _angle;
		}
	}
	
	public static enum Switch {
		SHOULDER_ZERO(0),
		ELBOW_ZERO(1),
		WRIST_ZERO(2);
				
		private int _channel;
		
		private Switch(int channel) {
			_channel = channel;
		}
		
		public int getChannel() {
			return _channel;
		}
	}
	
	public static enum Controller {
		JOYSTICK_PORT(0),
		AXIS_TRIGGER_LT(2),
		AXIS_TRIGGER_RT(3),
		TRIGGER_LB(5),
		TRIGGER_RB(6),
		AXIS_LEFT_X(0),
		AXIS_LEFT_Y(1),
		AXIS_RIGHT_X(4),
		AXIS_RIGHT_Y(5),
		A_BUTTON(1),
		B_BUTTON(2),
		X_BUTTON(3),
		Y_BUTTON(4);
		
		private int _channel;
		
		private Controller(int channel) {
			_channel = channel;
		}
		
		public int getChannel() {
			return _channel;
		}
		
	}
	
	public static final double ROBOT_FRONT_X = Measurement.ROBOT_LENGTH.getInches() - 
											   Measurement.BASE_CONNECTION.getInches();
	public static final double ROBOT_MAX_FRONT_X = ROBOT_FRONT_X +
												   Measurement.MAX_EXTENSION.getInches();
	public static final double ROBOT_BACK_X = -Measurement.BASE_CONNECTION.getInches();
	public static final double ROBOT_MIN_BACK_X = ROBOT_BACK_X -
												  Measurement.MAX_EXTENSION.getInches();
	
	public static final double GROUND_LEVEL_Y = -(Measurement.BASE_SEGMENT_LENGTH.getInches() + 
												  Measurement.ROBOT_HEIGHT.getInches());
	public static final double MAX_STEP_DOWN_Y = GROUND_LEVEL_Y - 
												 Measurement.MAX_STEP_HEIGHT.getInches();
	public static final double MAX_EXTENSION_HEIGHT_BACK_Y = Math.sqrt(((Measurement.SHOULDER_SEGMENT_LENGTH.getInches() + Measurement.ELBOW_SEGMENT_LENGTH.getInches() - 1.0) * 
			  															(Measurement.SHOULDER_SEGMENT_LENGTH.getInches() + Measurement.ELBOW_SEGMENT_LENGTH.getInches() - 1.0)) -
																	   ((ROBOT_MIN_BACK_X) * (ROBOT_MIN_BACK_X)));
	public static final double MAX_EXTENSION_HEIGHT_FRONT_Y = Math.sqrt(((Measurement.SHOULDER_SEGMENT_LENGTH.getInches() + Measurement.ELBOW_SEGMENT_LENGTH.getInches() - 1.0) * 
			  															(Measurement.SHOULDER_SEGMENT_LENGTH.getInches() + Measurement.ELBOW_SEGMENT_LENGTH.getInches() - 1.0)) -
																	   ((ROBOT_MAX_FRONT_X) * (ROBOT_MAX_FRONT_X)));

	public static final double PICK_UP_START_X = ROBOT_FRONT_X + 10.0;
	public static final double PICK_UP_START_Y = GROUND_LEVEL_Y + Measurement.HAND_WIDTH.getInches() + 15.0;
	public static final double PICK_UP_MAX_HEIGHT_Y = PICK_UP_START_Y;
	
	public static final double PLACE_START_X = RobotMap.ROBOT_FRONT_X;
	public static final double PLACE_START_Y = RobotMap.GROUND_LEVEL_Y + 27.5 - 
											   (RobotMap.Measurement.HAND_HEIGHT.getInches() - 6);
	public static final double PLACE_MAX_HEIGHT_Y = Math.sqrt(((Measurement.SHOULDER_SEGMENT_LENGTH.getInches() + Measurement.ELBOW_SEGMENT_LENGTH.getInches() - 1.0) * 
															   (Measurement.SHOULDER_SEGMENT_LENGTH.getInches() + Measurement.ELBOW_SEGMENT_LENGTH.getInches() - 1.0)) -
															  ((PLACE_START_X) * (PLACE_START_X)));
	
	public static final double DISK_LOW_POSITION_Y = RobotMap.PLACE_START_Y;
	public static final double DISK_MIDDLE_POSITION_Y = RobotMap.DISK_LOW_POSITION_Y + 28;
	public static final double DISK_HIGH_POSITION_Y = RobotMap.DISK_MIDDLE_POSITION_Y + 28;
	
	public static final double PICK_UP_MAX_EXTENSION_X = ROBOT_FRONT_X + 
														 Measurement.MAX_EXTENSION.getInches() - 
														 Measurement.HAND_HEIGHT.getInches();
	public static final double PICK_UP_GROUND_LEVEL_Y = GROUND_LEVEL_Y + Measurement.HAND_WIDTH.getInches() + 2.0;
	
	public static final double PLACE_MAX_EXTENSION_X = ROBOT_FRONT_X + 
			 										   Measurement.MAX_EXTENSION.getInches() - 
			 										   Measurement.HAND_WIDTH.getInches();
	
	public static final int CAMERA_FRONT = 0;
	//only can use 160x120, 320x240, 640x480
	public static final int CAMERA_IMG_WIDTH = 320;
	public static final int CAMERA_IMG_HEIGHT = 240;
}
