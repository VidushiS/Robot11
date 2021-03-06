package Team4450.Robot11;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import Team4450.Robot11.Devices;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PIDController;

public class WinchToogle {
	private CubeIntake Block;
	private final Robot robot;
	public boolean FixedPosition;
	private static PIDController winchPID;
	
	public WinchToogle(Robot robot) {
		Util.consoleLog();	
		this.robot = robot;
		
		
		Devices.footServo.set(1);
		Devices.winchStop.SetA();
	
		
		winchPID = new PIDController(0.0, 0.0, 0.0, Devices.WinchEncoder, Devices.winchMotor);
		
		Devices.WinchEncoder.reset();
		winchActive();
		FixedPosition = false;
		
		
	}
	public void dispose(){
		Util.consoleLog();
		
		winchPID.disable();
		winchPID.free();
	}
	
	public boolean LimitSwitch() {
		return Devices.winchLimitSwitch.get();
	}
	
	public void WinchMotorDown() {
		if(robot.isComp == true) {		
			if(LimitSwitch()) {
				Devices.winchMotor.set(0.6);
			}
			else if(!LimitSwitch()) {
				Devices.winchMotor.set(0);
			}	
		}
		else {
			if(!LimitSwitch()) {
				Devices.winchMotor.set(-.6);
			}
			else if(LimitSwitch()) {
				Devices.winchMotor.set(0);
			}	
		}
		
	}
	public void WinchMotorTeleOp(double power) {
		if(!isFixedPosition()) {
			if (robot.isClone) {
				if((Devices.WinchEncoder.get() < 11000 && power > 0) || (power < 0 && Devices.winchLimitSwitch.get())){ 
						Devices.winchMotor.set(power);	
				}
				else {
						if (!Devices.winchLimitSwitch.get()) { 
							Devices.WinchEncoder.reset();
						}
						Devices.winchMotor.set(0);			
				}
					
				
			}
			else {
				if((!Devices.winchLimitSwitch.get() && power < 0) || (power > 0 && Devices.WinchEncoder.get() < 11000)){ //TODO double check this encoder values 
					Devices.winchMotor.set(power);
				}
				else { 
					if (Devices.winchLimitSwitch.get()) {
						Devices.WinchEncoder.reset();
					}
					Devices.winchMotor.set(0);
				}
		}
			
			}
		else {
			
		}
		
	}
	
	public void winchStop() {
		Devices.winchStop.SetB();
	}
	public void winchActive() {
		Devices.winchStop.SetA();
	}
	public void winchSetPosition(int encoderCounts){
		
		FixedPosition = true;
		winchPID.setPID(0.0003, 0.00001, 0.0003, 0.0);
		
		//Remember in the constructor, the output is the motor, so we are restricting the motor values to -1 and 1
		winchPID.setOutputRange(-1, 1);
		//the PID value is trying to get to the encoder count inputted in to this method
		winchPID.setSetpoint(encoderCounts);
		//If we are within 1 percent of the encoder target then this program should stop, otherwise the robot will try to get the perfect encoder count
		winchPID.setPercentTolerance(1);
		//Start up the PID
		winchPID.enable();
		Util.consoleLog();
		
	}
	public void DisablePID() {
		winchPID.disable();
		Util.consoleLog();
		FixedPosition = false;
	}
	public boolean isFixedPosition() {
		return FixedPosition;
	}
	
	public void DeployBar() {
		Devices.BarServo.setPosition(0.2);
	}
	public void HoldBar() {
		
		Devices.BarServo.setPosition(0.5);	

	}
/*	public void deployFoot() {
		if(Devices.WinchEncoder.get() > 8900) {
			Devices.footServo.setAngle(60);	
		}
	}*/
	
	
	
	
		
	
	
	
}
