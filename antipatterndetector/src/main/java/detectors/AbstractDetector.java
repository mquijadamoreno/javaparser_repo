package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.github.javaparser.ast.CompilationUnit;

import visitors.VisitorFactory;

public abstract class AbstractDetector implements IDetector {

	public String name;
	public Collection<Ocurrence> ocurrences;
	public VisitorFactory visitorFactory;
	protected CompilationUnit cu;
	protected String file_path;
	
	
	public AbstractDetector(String name, String file_path) throws FileNotFoundException {
		this.name = name;
		this.ocurrences = new ArrayList<Ocurrence>();
		this.visitorFactory = new VisitorFactory();
		this.file_path = file_path;
	}
	
	public void addOcurrence(Ocurrence ocurrence) {
		this.ocurrences.add(ocurrence);
	}
	
	public Collection<Ocurrence> getOcurrences(){
		return Collections.unmodifiableCollection(ocurrences);
	}
	
	protected abstract void parse();
}
