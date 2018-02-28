package Team4450.Robot11;

import Team4450.Lib.*;

public class RobotSpeedShifter {
	
	public void slowSpeed() {
		Devices.speedShift.SetA();
		Util.consoleLog("Slow Speed Mode enabled");
	}
	public void fastSpeed() {
		Devices.speedShift.SetB();
		Util.consoleLog("Fast Speed Mode enabled");
	}

}
