package Team4450.Robot11;

import Team4450.Lib.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class WinchToogle {
	private CubeIntake Block;
	public WinchToogle(Devices devices, Teleop teleop) {
		Util.consoleLog();
		
	}
	
	public void WinchMotorUp(int UpEncoder) {
		if(Devices.WinchEncoder.get() <= UpEncoder) {
			Devices.winchMotor.set(0.5);	
			//Devices.winchSlacker.set(-0.4);
		}
		else if(Devices.WinchEncoder.get() > UpEncoder) {
			Devices.winchMotor.set(0);
			//Devices.winchSlacker.set(0);
		}
			}
	public void WinchMotorStop() {
		Devices.winchMotor.set(0);
	}
	public void WinchMotorDown(int DownEncoder) {
		if(Devices.WinchEncoder.get() >= DownEncoder) {
			Devices.winchMotor.set(-.5);
		//	Devices.winchSlacker.set(0.4);
		}
		else if(Devices.WinchEncoder.get() < DownEncoder) {
			Devices.winchMotor.set(0);
		//	Devices.winchSlacker.set(0);
		}
		
	}
	
	public void WinchScoreSwitch(int UpEncoder, int DownEncoder) {
		WinchMotorUp(UpEncoder);
		//auto.autoDrive(.3, 400, true); //TODO check encodercounts drive a bit forward to the scale
		Block.deposit();
		Block.stopCubeIntake();
		Block.WristIn();
		//auto.autoDrive(-.3, -400, true);//TODO check encoder counts
		WinchMotorDown(DownEncoder);
		
	}
	
	public void WinchScoreScale(int UpEncoderScale, int DownEncoderScale) {
		WinchMotorUp(UpEncoderScale);
		//auto.autoDrive(.3, 400, true); // TODO check encodercounts drive a bit forward to the scale
		Block.deposit();
		Block.stopCubeIntake();
		Block.WristIn();
		//auto.autoDrive(-.3, -400, true);// TODO check encoder counts drive backwards
		WinchMotorDown(DownEncoderScale);
	}
	
	
}
