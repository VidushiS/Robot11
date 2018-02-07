package Team4450.Robot11;



import Team4450.Lib.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
public class CubeIntake {
	
	private Devices devices;
	private Teleop teleop;
	
	private final WPI_TalonSRX	LeftCubeIntakeMotor = new WPI_TalonSRX(0); //TODO check assignments
	private final WPI_TalonSRX RightCubeIntakeMotor = new WPI_TalonSRX(1); //TODO check assignments
	
	private final ValveDA gearOpen = new ValveDA(1);
	private final ValveDA gearWrist = new ValveDA(2);

	public void PowerCubeManipulator(Devices devices, Teleop teleop) {
		Util.consoleLog();
		this.devices = devices;
		this.teleop = teleop;
		
		devices.InitializeCANTalon(LeftCubeIntakeMotor);
		LeftCubeIntakeMotor.setSafetyEnabled(true);
		devices.InitializeCANTalon(RightCubeIntakeMotor);
		RightCubeIntakeMotor.setSafetyEnabled(true);
	}
	public void Dispose() {
		Util.consoleLog();
		if(LeftCubeIntakeMotor != null) LeftCubeIntakeMotor.free();
		if(RightCubeIntakeMotor != null) RightCubeIntakeMotor.free();
	}
	public void deposit() {
	//	LeftCubeIntakeMotor.set(.5);//TODO check to see if they are going opp.
		//RightCubeIntakeMotor.set(.5);
		
		//gearOpen.SetA();
		//gearWrist.SetB();
		Util.consoleLog("The cube is being deposited");
	}
	public void intake() {
		//LeftCubeIntakeMotor.set(-.5);
		//RightCubeIntakeMotor.set(-.5);
		
		//gearOpen.SetB();
		//gearWrist.SetA();
		Util.consoleLog("The cube is being taken in to the robot");
	}
	public void stopCubeIntake(){
		Util.consoleLog();
		
		//LeftCubeIntakeMotor.set(0);
		//RightCubeIntakeMotor.set(0);
		
	}
}
