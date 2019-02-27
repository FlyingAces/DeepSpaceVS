package frc.robot.util;

import frc.robot.config.RobotMap;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Feed {
	
	private static Feed _instance;
	private NetworkTable _feed;
	
	private Feed() {
		NetworkTableInstance.getDefault().startClientTeam(4711);
		NetworkTableInstance.getDefault().startDSClient();
		_feed = NetworkTableInstance.getDefault().getTable("robotArmFeed");
		
		sendConstantInfo();
	}

	public static Feed getInstance() {
		if(_instance == null) {
			_instance = new Feed();
		}
		return _instance;
	}
	
	public void sendConstantInfo() {
		NetworkTableEntry entry = _feed.getEntry("measurements");
		double[] measurementsArray = { RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches(),
				RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches(),
				RobotMap.Measurement.HAND_HEIGHT.getInches() };
		entry.setDoubleArray(measurementsArray);
	}
	
	public void sendAngleInfo(String armName, double shoulder, double elbow, double wrist) {
		NetworkTableEntry entry = _feed.getEntry(armName);
		double[] angleArray = { shoulder, elbow, wrist};
		entry.setDoubleArray(angleArray);
	}
	
	public void sendButtonMap(String yCom, String xCom, String aCom, String bCom) {
		NetworkTableEntry entry = _feed.getEntry("buttonMapping");
		String[] buttonArray = { "y = " + yCom, "x = " + xCom, "a = " + aCom, "b = " + bCom };
		entry.setStringArray(buttonArray);

	}
	
	public void sendString(String stringName, String value) {
		NetworkTableEntry entry = _feed.getEntry(stringName);
		entry.setString(value);
	}
}
