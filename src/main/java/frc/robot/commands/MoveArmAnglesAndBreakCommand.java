package frc.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import frc.robot.subsystems.ArmSubsystem;


public class MoveArmAnglesAndBreakCommand extends MoveArmAnglesCommand {

    protected PIDController _shoulderPID;
    protected PIDController _elbowPID;
    protected PIDController _wristPID;

    public MoveArmAnglesAndBreakCommand(double shoulderValue, double elbowValue, double wristValue) {
        super(shoulderValue, elbowValue, wristValue);

        // P, I, D, F, source
        _shoulderPID = new PIDController(0.0, 0.0, 0.0, 0.0, 
        new PIDSource(){
        
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                
            }
        
            @Override
            public double pidGet() {
                return _arm.getAngle(ArmSubsystem.Angle.SHOULDER);
            }
        
            @Override
            public PIDSourceType getPIDSourceType() {
                return null;
            }
        }, 
        new PIDOutput(){
        
            @Override
            public void pidWrite(double output) {
                _arm.setMotorSpeed(ArmSubsystem.Angle.SHOULDER, output);
            }
        });

        _elbowPID = new PIDController(0.0, 0.0, 0.0, 0.0, new PIDSource(){
        
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                
            }
        
            @Override
            public double pidGet() {
                return _arm.getAngle(ArmSubsystem.Angle.ELBOW);
            }
        
            @Override
            public PIDSourceType getPIDSourceType() {
                return null;
            }
        },
        new PIDOutput(){
        
            @Override
            public void pidWrite(double output) {
                _arm.setMotorSpeed(ArmSubsystem.Angle.ELBOW, output);
            }
        });
        _wristPID = new PIDController(0.0, 0.0, 0.0, 0.0, new PIDSource(){
        
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                
            }
        
            @Override
            public double pidGet() {
                return _arm.getAngle(ArmSubsystem.Angle.WRIST);
            }
        
            @Override
            public PIDSourceType getPIDSourceType() {
                return null;
            }
        },
            new PIDOutput(){
        
                @Override
                public void pidWrite(double output) {
                    _arm.setMotorSpeed(ArmSubsystem.Angle.WRIST, output);
                }
            });
        
    }

    @Override
	protected void initialize() {
        super.initialize();
        
        if(_shoulderPID.isEnabled())
            _shoulderPID.disable();
            
        if(_elbowPID.isEnabled())
            _elbowPID.disable();
        
        if(_wristPID.isEnabled())
            _wristPID.disable();
    }

    @Override
    protected boolean isFinished() {
        if(super.isFinished() && 
        !_shoulderPID.isEnabled() &&
        !_elbowPID.isEnabled() &&
        !_wristPID.isEnabled()) {
            _shoulderPID.setSetpoint(_shoulderValue);
            _elbowPID.setSetpoint(_elbowValue);
            _wristPID.setSetpoint(_wristValue);

            _shoulderPID.enable();
            _elbowPID.enable();
            _wristPID.enable();
        }

        return false;
    }
}