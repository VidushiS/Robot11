package Team4450.Robot11;

public class EncoderCalc {
	
	public int EncoderCounts(int feet){

		int inches = feet * 12;
		float revolutions = (float) (inches/(6*3.14159265358979)); 
		int encoderCounts = (int) revolutions * 20;

		return encoderCounts;
		}

}
