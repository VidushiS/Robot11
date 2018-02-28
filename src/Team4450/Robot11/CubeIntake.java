package Team4450.Robot11;



import Team4450.Lib.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
public class CubeIntake {

	private Teleop teleop;
	public boolean isOut;
	public boolean isIntaking;
	public boolean isDepositing;
	public boolean isGrabberOpen;
	public void PowerCubeManipulator(Devices devices, Teleop teleop) {
		Util.consoleLog();
		this.teleop = teleop;
		
	}
	public void Dispose() {
		Util.consoleLog();
		if(Devices.LeftCubeIntakeMotor != null) Devices.LeftCubeIntakeMotor.free();
		if(Devices.RightCubeIntakeMotor != null) Devices.RightCubeIntakeMotor.free();
	}
	public void deposit() {
		Devices.LeftCubeIntakeMotor.set(.5);//TODO check to see if they are going opp.
		Devices.RightCubeIntakeMotor.set(-.5); //TODO test values
		
		Devices.gearOpen.SetA();
		Devices.gearOpen.SetB();
		Util.consoleLog("The cube is being deposited");
		
		isOut = true;
	}
	public void intake() {
		Devices.LeftCubeIntakeMotor.set(-.5); //TODO test
		Devices.RightCubeIntakeMotor.set(.5);// TODO test
		
		Devices.gearOpen.SetB();
		Devices.gearOpen.SetA();
		Util.consoleLog("The cube is being taken in to the robot");
		
		isOut = false;
	}
	
	public void stopCubeIntake(){
		Util.consoleLog();
		
		Devices.LeftCubeIntakeMotor.set(0); 
		Devices.RightCubeIntakeMotor.set(0);
		
	}
	
	public void WristIn() {
		Devices.gearWrist.SetA();
		
		isOut = false;
	}
	public void WristOut() {
		Devices.gearWrist.SetB();
		
		isOut = true;
	}
	public void MotorStartIntake() {
		Devices.LeftCubeIntakeMotor.set(.5);
		Devices.RightCubeIntakeMotor.set(-.5);
		
		isIntaking = true;
		isDepositing = false;
	}
	public void MotorStartDeposit() {
		Devices.LeftCubeIntakeMotor.set(-.5);
		Devices.RightCubeIntakeMotor.set(.5);
		
		isIntaking = false;
		isDepositing = true;
	}
	public void WristOpen() {
		Devices.gearOpen.SetA();
		
		isGrabberOpen = true;
	}
	public void WristClose() {
		Devices.gearOpen.SetB();
		
		isGrabberOpen = false;
	}
	
	public boolean isGrabberOpen() {
		return isGrabberOpen;
	}
	public boolean isIntaking() {
		return isIntaking;
	}
	public boolean isDepositing() {
		return isDepositing;
	}
	public boolean isOut() {
		return isOut;
	}
	
}
