package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class SetAndGetDetector extends AbstractDetector {

	private List<FieldDeclaration> fieldDeclarations;
	private List<String> attributeNames;
	private List<String> methodNames;

	public SetAndGetDetector(String file_path) throws FileNotFoundException {
		super(SetAndGetDetector.class.getName(), file_path);
		fieldDeclarations = new ArrayList<FieldDeclaration>();
		attributeNames = new ArrayList<String>();
	}

	@Override
	public void detect() {
		parse();
		retrieveFieldDeclarations();
		parseAttributeNames();
		retrieveMethodNames();

		String setName, getName;
		for (String attribute : attributeNames) {
			setName = "set"  + attribute;
			getName = "get" + attribute;
			System.out.println("Searching for methods : " + setName + " and " + getName + "...");
			if (!methodNames.contains(setName) || !methodNames.contains(getName)) {
				System.out.println("[" + attribute + "] set or get method not found!");
				System.out.println("Setters and getters methods should be defined for every attribute");
			} else {
				System.out.println("Methods found!");
			}
		}
	}

	@Override
	protected void parse() {
		try {
			this.cu = JavaParser.parse(new FileInputStream(file_path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void parseAttributeNames() {
		String attribute;
		for (FieldDeclaration field : fieldDeclarations) {
			attribute = field.findAll(VariableDeclarator.class).toString().replace("[", "").replace("]", "");
			attributeNames.add(attribute.substring(0,1).toUpperCase() + attribute.substring(1));
		}
	}

	private void retrieveFieldDeclarations() {
		VoidVisitor<List<FieldDeclaration>> visitor = (VoidVisitor<List<FieldDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.ATTRIBUTES_VISITOR);
		visitor.visit(this.cu, fieldDeclarations);
	}

	private void retrieveMethodNames() {
		List<MethodDeclaration> methodDeclarations = new ArrayList<MethodDeclaration>();
		VoidVisitor<List<MethodDeclaration>> visitor = (VoidVisitor<List<MethodDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_NAME_VISITOR);
		visitor.visit(this.cu, methodDeclarations);
		methodNames = Collections.unmodifiableList(
				methodDeclarations.stream().map(MethodDeclaration::getNameAsString).collect(Collectors.toList()));
	}
	

}
