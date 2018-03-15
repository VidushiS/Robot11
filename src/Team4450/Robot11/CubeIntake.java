package Team4450.Robot11;



import Team4450.Lib.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
public class CubeIntake {

	private Robot robot;
	public boolean isOut;
	public boolean isIntaking;
	public boolean isDepositing;
	public boolean isGrabberOpen;
	public boolean isIntakeDeposit;
	public boolean isIntakeIntaking;
	public boolean ISAUTOINTAKERUNNING;
	private Thread AutoIntakeThread;
	
	public CubeIntake(Robot robot) {
		Util.consoleLog();
		this.robot = robot;
		
		if (robot.isAutonomous()) {
			WristIn();
			WristOpen();
		}
		
	}
	public void dispose() {
		Util.consoleLog();
		if(Devices.LeftCubeIntakeMotor != null) Devices.LeftCubeIntakeMotor.free();
		if(Devices.RightCubeIntakeMotor != null) Devices.RightCubeIntakeMotor.free();
	}
	public void deposit() {
		Devices.LeftCubeIntakeMotor.set(.5);//TODO check to see if they are going opp.
		Devices.RightCubeIntakeMotor.set(-.5); //TODO test values
		
		Devices.gearOpen.SetB();
		Devices.gearOpen.SetA();
		
		
		isIntakeDeposit = true;
		isIntakeIntaking = false;
	}
	public void intake() {
		Devices.LeftCubeIntakeMotor.set(-.5); //TODO test
		Devices.RightCubeIntakeMotor.set(.5);// TODO test
		
		Devices.gearOpen.SetA();
		Devices.gearOpen.SetB();
		Util.consoleLog("The cube is being taken in to the robot");
		
		isIntakeDeposit = false;
		isIntakeIntaking = true;
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
	
	public void scoreSwitch() {
		deposit();
		stopCubeIntake();
		WristIn();
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
	public boolean isIntakeDeposit() {
		return isIntakeDeposit();
	}
	public boolean isIntakeIntaking() {
		return isIntakeIntaking;
	}
	
	public void AutoIntakeStart() {
		
		if (AutoIntakeThread != null) return;
		AutoIntakeThread = new AutoIntake();
		AutoIntakeThread.start();
		ISAUTOINTAKERUNNING = true;
	}
	public void AutoIntakeStop() {
		if (AutoIntakeThread != null) {
			AutoIntakeThread.interrupt();
		}
		AutoIntakeThread = null;
		ISAUTOINTAKERUNNING = false;
	}
	
	private class AutoIntake extends Thread{
		
		AutoIntake(){
			this.setName("AutoIntake");	
		}
		public void run(){
			double currentLimit;
			
			if(robot.isComp) {
				currentLimit = 15.0;
			}
			else currentLimit = 20.0;
			
			
			try 
			{
				Devices.gearOpen.SetA();
				Devices.LeftCubeIntakeMotor.set(.5);//TODO check to see if they are going opp.
				Devices.RightCubeIntakeMotor.set(-.5); //TODO test values
			
			while(!isInterrupted() && Devices.LeftCubeIntakeMotor.getOutputCurrent() < currentLimit) {
				LCD.printLine(9, "cube motor current=%f", Devices.LeftCubeIntakeMotor.getOutputCurrent());
	            sleep(50);
			}
			if(!interrupted()) {
				Util.consoleLog(" Cube detected");
				sleep(500);
			}
			}
			catch (InterruptedException e) {
				stopCubeIntake();
			}
			catch (Throwable e) { e.printStackTrace(Util.logPrintStream); }
			finally {stopCubeIntake();}
			
			AutoIntakeThread = null;
		}
	}
	
	
}
