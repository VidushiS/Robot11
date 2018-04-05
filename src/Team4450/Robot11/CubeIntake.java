package Team4450.Robot11;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CubeIntake {

	private Robot robot;
	public boolean isOut, isIntaking, isDepositing, isGrabberOpen, isIntakeDeposit, isIntakeIntaking, ISAUTOINTAKERUNNING;
	private Thread AutoIntakeThread;
	
	public CubeIntake(Robot robot) {
		Util.consoleLog();
		this.robot = robot;
		
		if (robot.isAutonomous()) {
			SmartDashboard.putBoolean("Intaking Cube", isIntakeIntaking());
			SmartDashboard.putBoolean("Depositing cube", isIntakeDeposit());
		}
	
	}
	public void dispose() {
		Util.consoleLog();
		if(Devices.LeftCubeIntakeMotor != null) Devices.LeftCubeIntakeMotor.free();
		if(Devices.RightCubeIntakeMotor != null) Devices.RightCubeIntakeMotor.free();
	}
	public void deposit() {
		WristOpen();
		MotorStartDeposit();
		Timer.delay(.7);
		stopCubeIntake();
		WristClose();
		
		
		isIntakeDeposit = true;
		isIntakeIntaking = false;
	}
	public void intake() {
		
		//WristOpen();
		MotorStartIntake();
		Timer.delay(.5);
		WristClose();
		stopCubeIntake();
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
		if(robot.isClone) {
			Devices.gearWrist.SetA();
		}
		else {
			Devices.gearWrist.SetB();
		}
		
		isOut = false;
	}
	public void WristOut() {
		if(robot.isClone) {
			Devices.gearWrist.SetB();
		}
		else {
			Devices.gearWrist.SetA();
		}
		
		isOut = true;
	}
	public void MotorStartIntake() {
		Devices.LeftCubeIntakeMotor.set(-.5);
		Devices.RightCubeIntakeMotor.set(-.5);
		
		isIntaking = true;
		isDepositing = false;
	}
	public void MotorStartDeposit() {
		Devices.LeftCubeIntakeMotor.set(.5);
		Devices.RightCubeIntakeMotor.set(.5);
		
		isIntaking = false;
		isDepositing = true;
	}
	public void WristOpen() {
		if(robot.isClone) {
		Devices.gearOpen.SetA();
		}
		else {
			Devices.gearOpen.SetB();
		}
		isGrabberOpen = true;
	}
	public void WristClose() {
		if(robot.isClone) {
			Devices.gearOpen.SetB();	
		}
		else{
			Devices.gearOpen.SetA();
			}
		
		
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
	public boolean isIntakeDeposit() {
		return isIntakeDeposit;
	}
	public boolean isIntakeIntaking() {
		return isIntakeIntaking;
	}
	public boolean ISAUTOINTAKERUNNING() {
		return ISAUTOINTAKERUNNING;
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
				Devices.LeftCubeIntakeMotor.set(-.5);//TODO check to see if they are going opp.
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
