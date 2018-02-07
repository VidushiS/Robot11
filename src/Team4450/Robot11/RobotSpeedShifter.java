package Team4450.Robot11;

import Team4450.Lib.*;

public class RobotSpeedShifter {
	
	private final ValveDA speedShifter = new ValveDA(0);
	
	public void slowSpeed() {
		speedShifter.SetA();
		Util.consoleLog("Slow Speed Mode enabled");
	}
	public void fastSpeed() {
		speedShifter.SetB();
		Util.consoleLog("Fast Speed Mode enabled");
	}

}
