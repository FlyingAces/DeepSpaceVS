package frc.robot.util;

public class Gains {
    private double _kp;
    private double _ki;
    private double _kd;
    private double _kf;
    private int _izone;
    private double _peak;

    public Gains(double kp, double ki, double kd, double kf, int izone, double peak){
        _kp = kp;
        _ki = ki;
        _kd = kd;
        _kf = kf;
        _izone = izone;
        _peak = peak;
    }

    public double getKp(){
        return _kp;
    }

    public double getKi(){ 
        return _ki;
    }

    public double getKd(){
        return _kd;
    }

    public double getKf(){
        return _kf;
    }

    public int getIzone(){
        return _izone;
    }

    public double getPeak(){
        return _peak;
    }
}