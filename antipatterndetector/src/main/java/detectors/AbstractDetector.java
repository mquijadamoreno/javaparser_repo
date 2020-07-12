package detectors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import com.github.javaparser.ast.CompilationUnit;

import utils.JavaCompilationUnit;
import visitors.VisitorFactory;

public abstract class AbstractDetector implements IDetector {

	public String name;
	public Collection<Ocurrence> ocurrences;
	public VisitorFactory visitorFactory;
	protected CompilationUnit cu;
	protected String file_path;
	protected Properties detectorProperties;
	protected String reason;

	public AbstractDetector(String name, JavaCompilationUnit jcu) throws FileNotFoundException {
		this.name = name;
		this.cu = jcu.getCompilationUnit();
		this.file_path = jcu.getFilePath();
		this.ocurrences = new ArrayList<Ocurrence>();
		this.visitorFactory = new VisitorFactory();
		this.detectorProperties = new Properties();
		try {
			detectorProperties.load(this.getClass().getResourceAsStream("/resources/detectorsDescription.properties"));
			this.reason = detectorProperties.getProperty(this.name);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addOcurrence(Ocurrence ocurrence) {
		this.ocurrences.add(ocurrence);
	}

	public Collection<Ocurrence> getOcurrences() {
		return Collections.unmodifiableCollection(ocurrences);
	}

}
