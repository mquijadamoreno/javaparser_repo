package testpackage;

public class TestClass {
	
	private String att1;
	private Integer att2;
	
	public TestClass() {
		this.att1 = "testClass att1";
		this.att2 = 2;
	}

	public String getAtt1() {
		return att1;
	}

	public void setAtt1(String att1) {
		this.att1 = att1;
	}

	public Integer getAtt2() {
		return att2;
	}

	public void setAtt2(Integer att2) {
		this.att2 = att2;
	}
	
	public void method1() {
		this.att1 = "a";
	}
	
	public void method1(String string) {
		this.method1();
		this.att1 = "b";
		String lul = "string";
		if(this.att1==lul) {
			System.out.println("lul");
		}
	}
	
	public void method1(Integer integer) {
		this.method1();
		this.att1 = "c";
	}

}
