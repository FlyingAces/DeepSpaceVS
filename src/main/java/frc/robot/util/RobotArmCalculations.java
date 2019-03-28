package frc.robot.util;


import frc.robot.config.RobotMap;

public class RobotArmCalculations {

	public static enum HandState {
		PICK_UP, PLACE_BALL, PLACE_DISK, LOCKED;
	}

	private double _shoulderAngle;
	private double _elbowAngle;
	private double _wristAngle;

	private double _wristTargetX;
	private double _wristTargetY;

	private HandState _handState;

	private boolean _isInverted;

	public RobotArmCalculations(double shoulderAngle, double elbowAngle, HandState handState) {
		_shoulderAngle = limitShoulderAngle(shoulderAngle);
		_elbowAngle = limitElbowAngle(elbowAngle);
		_wristAngle = 0.0;
		_isInverted = (_elbowAngle < 0);
		_handState = handState;

		calculateWristTargetAndWristAngle();
	}

	private void calculateAllAngles() {
		double distance = distance(_wristTargetX, _wristTargetY);
		double d1 = relativeAtan2(_wristTargetY, _wristTargetX);
		double d2 = lawOfCosines(distance, RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches(),
				RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches());
		if (Double.isNaN(d2))
			d2 = 0.0;

		_shoulderAngle = (!_isInverted) ? (d1 - d2) : (d1 + d2);
		if (_shoulderAngle <= -180.0) {
			_shoulderAngle += 360.0;
		} else if (_shoulderAngle > 180.0) {
			_shoulderAngle -= 360.0;
		}

		_elbowAngle = (!_isInverted)
				? 180.0 - lawOfCosines(RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches(),
						RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches(), distance)
				: -(180.0 - lawOfCosines(RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches(),
						RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches(), distance));

		if (Double.isNaN(_elbowAngle))
			_elbowAngle = 0.0;

		if (_shoulderAngle != limitShoulderAngle(_shoulderAngle) || _elbowAngle != limitElbowAngle(_elbowAngle)) {
			_shoulderAngle = limitShoulderAngle(_shoulderAngle);
			_elbowAngle = limitElbowAngle(_elbowAngle);
			calculateWristTargetAndWristAngle();
		} else
			calculateWristAngle();

	}

	private void calculateWristTargetAndWristAngle() {

		double shoulderPolarAngle = Conversions.degreeToRadian(90.0 - _shoulderAngle);
		double shoulderX = RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches() * Math.cos(shoulderPolarAngle);
		double shoulderY = RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches() * Math.sin(shoulderPolarAngle);

		double elbowPolarAngle = Conversions.degreeToRadian(90.0 - _shoulderAngle - _elbowAngle);
		double elbowX = RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches() * Math.cos(elbowPolarAngle);
		double elbowY = RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches() * Math.sin(elbowPolarAngle);

		_wristTargetX = shoulderX + elbowX;
		_wristTargetY = shoulderY + elbowY;

		calculateWristAngle();

	}

	private void calculateWristAngle() {
		switch (_handState) {
		case PICK_UP:
			_wristAngle = 90.0 - (_shoulderAngle + _elbowAngle);
			break;
		case PLACE_BALL:
		case PLACE_DISK:
			_wristAngle = -(_shoulderAngle + _elbowAngle);
			break;
		default:
			_wristAngle = _wristAngle;
			break;
		}
		_wristAngle = limitWristAngle(_wristAngle);
	}

	private double relativeAtan2(double y, double x) {
		double angle = -Conversions.radianToDegree(Math.atan2(y, x));
		if (angle < 0) {
			angle += 360.0;
		}
		angle = (angle + 90.0) % 360.0;
		if (angle > 180.0) {
			angle -= 360.0;
		}
		return angle;
	}

	private double lawOfCosines(double a, double b, double c) {
		return Conversions.radianToDegree(Math.acos((a * a + b * b - c * c) / (2 * a * b)));
	}

	private double distance(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	private double limitShoulderAngle(double angle) {
		return (angle < 0) ? Math.max(RobotMap.Angle.SHOULDER_MIN_ANGLE.getAngle(), angle)
				: Math.min(RobotMap.Angle.SHOULDER_MAX_ANGLE.getAngle(), angle);
	}

	private double limitElbowAngle(double angle) {
		return (angle < 0) ? Math.max(RobotMap.Angle.ELBOW_MIN_ANGLE.getAngle(), angle)
				: Math.min(RobotMap.Angle.ELBOW_MAX_ANGLE.getAngle(), angle);
	}

	private double limitWristAngle(double angle) {
		return (angle < 0) ? Math.max(RobotMap.Angle.WRIST_MIN_ANGLE.getAngle(), angle)
				: Math.min(RobotMap.Angle.WRIST_MAX_ANGLE.getAngle(), angle);
	}

	public double getWristTargetX() {
		return _wristTargetX;
	}

	public double getWristTargetY() {
		return _wristTargetY;
	}

	public void setWristTargetX(double x) {
		if (((x * x)
				+ (getWristTargetY() * getWristTargetY())) > ((RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches()
						+ RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches())
						* (RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches()
								+ RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches()))) {

			_elbowAngle = 0.0;
			setShoulderAngle(relativeAtan2(getWristTargetY(), x));

		} else {
			if(distance(x, getWristTargetY()) > 
			Math.abs(RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches() - RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches())) {
				_wristTargetX = x;
				calculateAllAngles();
			}
		}
	}

	public void setWristTargetY(double y) {
		if (((y * y)
				+ (getWristTargetX() * getWristTargetX())) > ((RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches()
						+ RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches())
						* (RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches()
								+ RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches()))) {

			_elbowAngle = 0.0;
			setShoulderAngle(relativeAtan2(y, getWristTargetX()));
		} else {
			if(distance(getWristTargetX(), y) > 
			Math.abs(RobotMap.Measurement.SHOULDER_SEGMENT_LENGTH.getInches() - RobotMap.Measurement.ELBOW_SEGMENT_LENGTH.getInches())) {
				_wristTargetY = y;
				calculateAllAngles();
			}
		}
	}

	public double getShoulderAngle() {
		return _shoulderAngle;
	}

	public void setShoulderAngle(double angle) {
		_shoulderAngle = limitShoulderAngle(angle);
		calculateWristTargetAndWristAngle();
	}

	public double getElbowAngle() {
		return _elbowAngle;
	}

	public void setElbowAngle(double angle) {
		_elbowAngle = limitElbowAngle(angle);
		_isInverted = _elbowAngle < 0.0;
		calculateWristTargetAndWristAngle();
	}

	public double getWristAngle() {
		return _wristAngle;
	}

	public void setWristAngle(double angle) {
		if (_handState != HandState.LOCKED)
			return;
		_wristAngle = limitWristAngle(angle);

	}

	public HandState getHandState() {
		return _handState;

	}

	public void setHandState(HandState handState) {
		_handState = handState;
		calculateWristAngle();
	}

	public boolean isInverted() {
		return _isInverted;
	}

	public void setInverted(boolean isInverted) {
		_isInverted = isInverted;
		calculateAllAngles();
	}

}