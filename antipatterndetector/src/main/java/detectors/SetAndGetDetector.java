package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class SetAndGetDetector extends AbstractDetector {

	private List<FieldDeclaration> fieldDeclarations;
	private List<String> attributeNames;
	private List<String> methodNames;

	public SetAndGetDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(SetAndGetDetector.class.getName(), jcu);
		fieldDeclarations = new ArrayList<FieldDeclaration>();
		attributeNames = new ArrayList<String>();
	}

	@Override
	public void detect() {
		retrieveFieldDeclarations();
		if (CollectionUtils.isNotEmpty(fieldDeclarations)) {
			parseAttributeNames();
			retrieveMethodNames();

			String setName, getName;
			for (String attribute : attributeNames) {
				setName = "set" + attribute;
				getName = "get" + attribute;
				if (!methodNames.contains(setName) || !methodNames.contains(getName)) {
					this.addOcurrence(new Ocurrence("undefined", this.reason,
							((ClassOrInterfaceDeclaration) this.fieldDeclarations.get(0).getParentNode().get())
									.getNameAsString()+".java"));
				}
			}
		}

	}


	private void parseAttributeNames() {
		String attribute;
		for (FieldDeclaration field : fieldDeclarations) {
			attribute = field.findAll(VariableDeclarator.class).toString().replace("[", "").replace("]", "");
			attributeNames.add(attribute.substring(0, 1).toUpperCase() + attribute.substring(1));
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
