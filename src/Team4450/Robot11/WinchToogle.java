package Team4450.Robot11;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Timer;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class WinchToogle {
	private CubeIntake Block;
	private Thread WinchUp;
	private Thread WinchUpTeleOp;
	private final Robot robot;
	public WinchToogle(Robot robot) {
		Util.consoleLog();	
		this.robot = robot;
		
		Devices.footServo.set(1);
		Devices.forkliftServo.set(0);
	}
	public boolean LimitSwitch() {
		return Devices.winchLimitSwitch.get();
	}

	public void WinchMotorUp(int UpEncoder) {
		if(Devices.WinchEncoder.get() <= UpEncoder) {
			Devices.winchMotor.set(0.6);	
		}
		else if(Devices.WinchEncoder.get() > UpEncoder) {
			Devices.winchMotor.set(0);
		}
	}
	
	public void WinchMotorStop() {
		Devices.winchMotor.set(0);
	}
	//REVERSE THE BOOLEAN VALUES WHEN WITH THE CLONE BOT
	
	public void WinchMotorDown() {
		if(robot.isComp == true) {		
			if(LimitSwitch()) {
				Devices.winchMotor.set(-.6);
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
	//REVERSE THE BOOLEAN VALUES WHEN WITH THE CLONE BOT
	public void WinchMotorTeleOp(double power) {
		if (robot.isClone) {
			if(Devices.winchLimitSwitch.get() && power != 0){ //TODO double check this encoder values 
				Devices.winchMotor.set(-power);
			}
			else if (!Devices.winchLimitSwitch.get() == true || power == 0 || Devices.WinchEncoder.get() > 9000) {
				Devices.winchMotor.set(0);
			}
		}
		else {
			if(!Devices.winchLimitSwitch.get() && power != 0){ //TODO double check this encoder values 
				Devices.winchMotor.set(power);
			}
			else if (Devices.winchLimitSwitch.get() == true || power == 0 || Devices.WinchEncoder.get() > 9000) {
				Devices.winchMotor.set(0);
			}	
		}
		
	}
	public void deployForks() {
		
		Devices.forkliftServo.setAngle(60);	

	}
	public void deployFoot() {
		if(Devices.WinchEncoder.get() > 8900) {
			Devices.footServo.setAngle(60);	
		}
	}
	public void liftUp() {
		
		WinchUp = new WinchUp();
		WinchUp.start();
	}
	public void stopLift() {
		if(WinchUp!= null) {
			WinchUp.interrupt();
		}
	}
	
	private class WinchUp extends Thread{
		WinchUp(){
			this.setName("WinchUp");
		}
		public void run(){
			try {
				WinchMotorUp(Autonomous.SwitchEncoderUp);
	
				while (!isInterrupted()) {
					LCD.printLine(3, "Lift Encoder Counts", Devices.WinchEncoder.get());;
				}
				if(!interrupted()) {
					sleep(60);
				}
			}
			catch(InterruptedException e) {
				WinchMotorDown();
			}
			catch(Throwable e) {
				e.printStackTrace(Util.logPrintStream);
			}
			finally {
				WinchMotorDown();
			}
		}
		
		
	}
	
	
}
