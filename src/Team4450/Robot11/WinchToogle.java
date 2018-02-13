package Team4450.Robot11;

import Team4450.Lib.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class WinchToogle {

	private final WPI_TalonSRX winchMotor = new WPI_TalonSRX(7);
	private Devices devices;
	private Teleop teleop;
	
	public WinchToogle(Devices devices, Teleop teleop) {
		Util.consoleLog();
		this.devices = devices;
		this.teleop = teleop;
		
		devices.InitializeCANTalon(winchMotor);
		winchMotor.setSafetyEnabled(true);
	}
	
	public void WinchMotorUp() {
		winchMotor.set(0.5);
	}
	public void WinchMotorDown() {
		winchMotor.set(-.5);
	}
	
}
