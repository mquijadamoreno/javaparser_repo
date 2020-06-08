package detectors;

public class Ocurrence {
	
	private String range;
	private String reason;
	private String class_detected;
	
	public Ocurrence(String range, String reason, String class_detected) {
		this.range = range;
		this.reason = reason;
		this.class_detected = class_detected;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getClass_detected() {
		return class_detected;
	}
	public void setClass_detected(String class_detected) {
		this.class_detected = class_detected;
	}
	
	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
	
	@Override
	public String toString() {
		return this.reason + " \n Detected in: " + this.class_detected + ", range: " + this.range;
	}

	

}
