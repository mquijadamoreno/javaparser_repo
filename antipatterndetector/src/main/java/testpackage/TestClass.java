package testpackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TestClass extends TestClassA {
	
	private String att1;
	private Integer att2;
	private List<String> stringList;
	private LinkedList<String> stringList2;
	
	public TestClass() {
		this.att1 = "testClass att1";
		this.att2 = 2;
	}
	
	public TestClass(Integer num) {
		this.att2 = num;
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
		this.stringList2 = new LinkedList<String>();
		stringList2.get(0);
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
		this.att1 = "c";
	}
	
	public void method3() {
		List<String> arrayList = new ArrayList<String>();
		stringList = new LinkedList<String>();
		arrayList.add("string 1");
		arrayList.add("string 2");
		arrayList.add("string 3");
		for(int i = 0; i < arrayList.size(); i++) {
			stringList.add(new String(arrayList.get(i)));
			//System.out.println("pepega");
		}
		for (String string : arrayList) {
			stringList.add(new String(string));
		}
	}
	
	@Override
	@Deprecated
	public int methodA(Integer num) {
		Integer b = super.methodB(num);
		return super.methodB(b);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public List<String> getStringList() {
		return this.stringList;
	}

	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

}
