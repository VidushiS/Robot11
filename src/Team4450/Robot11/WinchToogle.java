package Team4450.Robot11;

import Team4450.Lib.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class WinchToogle {

	private Teleop teleop;
	
	public WinchToogle(Devices devices, Teleop teleop) {
		Util.consoleLog();
		
	}
	
	public void WinchMotorUp() {
		Devices.winchMotor.set(0.5);
	}
	public void WinchMotorStop() {
		Devices.winchMotor.set(0);
	}
	public void WinchMotorDown() {
		Devices.winchMotor.set(-.5);
	}
	
}
