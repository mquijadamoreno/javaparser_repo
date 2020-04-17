package detectors;

public class Ocurrence {
	
	private Integer line;
	private String reason;
	private String class_detected;
	
	public Ocurrence(Integer line, String reason, String class_detected) {
		this.line = line;
		this.reason = reason;
		this.class_detected = class_detected;
	}
	
	
	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
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
	
	@Override
	public String toString() {
		return this.reason + " Detected in class: " + this.class_detected + ", line: " + this.line;
	}

}
