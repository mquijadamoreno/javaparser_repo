package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class FinalAttributeSetDetector extends AbstractDetector {

	private List<FieldDeclaration> fieldDeclarations;
	List<MethodDeclaration> methodDeclarations;

	public FinalAttributeSetDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(FinalAttributeSetDetector.class.getName(), jcu);
		this.fieldDeclarations = new ArrayList<FieldDeclaration>();
		this.methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		retrieveFieldsAndMethodsData();
		List<String> fieldDeclarationNames = new ArrayList<String>();
		String name = "";
		boolean found;
		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
			
			if(!fieldDeclaration.hasModifier(Keyword.FINAL)){
				name = fieldDeclaration.findFirst(VariableDeclarator.class).get().getNameAsString();
				found = false;
				for (MethodDeclaration methodDeclaration : methodDeclarations) {
					if(methodDeclaration.getName().toString().startsWith("set") && methodDeclaration.getName().toString().toLowerCase().equals("set" + name.toLowerCase())) {
						found = true;
						break;
					}
				}
				
				if(!found) {
					this.addOcurrence(new Ocurrence(fieldDeclaration.getRange().get().toString(), this.reason, this.file_path));
				}
				
			}
						
			
			
			fieldDeclarationNames.add(fieldDeclaration.findFirst(VariableDeclarator.class).get().getNameAsString());
		}


	}

	private void retrieveFieldsAndMethodsData() {
		VoidVisitor<List<FieldDeclaration>> visitor = (VoidVisitor<List<FieldDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.ATTRIBUTES_VISITOR);
		visitor.visit(this.cu, fieldDeclarations);

		VoidVisitor<List<MethodDeclaration>> visitor2 = (VoidVisitor<List<MethodDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_NAME_VISITOR);
		visitor2.visit(this.cu, methodDeclarations);
	}

}
