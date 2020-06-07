package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class ConstructorOverloadDetector extends AbstractDetector {

	private List<ConstructorDeclaration> constructorDeclarations;

	public ConstructorOverloadDetector(String file_path) throws FileNotFoundException {
		super(MethodOverloadDetector.class.getName(), file_path);
		this.constructorDeclarations = new ArrayList<ConstructorDeclaration>();
	}

	@Override
	public void detect() {
		parse();
		retrieveMethodNames();
		analyzeConstructors();
	}
	
	private void analyzeConstructors() {
		if (constructorDeclarations.size() > 1) {
			for (ConstructorDeclaration ocurrence : constructorDeclarations) {
				if (CollectionUtils.isNotEmpty(ocurrence.getBody().findAll(ExplicitConstructorInvocationStmt.class))) {
					System.out.println(
							"Calling \"this()\" from an overload constructor. \n This ocurrence should happen at least "
									+ (constructorDeclarations.size() - 1) + " times");
				}

			}
		}
	}

	private void retrieveMethodNames() {
		VoidVisitor<List<ConstructorDeclaration>> visitor = (VoidVisitor<List<ConstructorDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.CONSTRUCTOR_VISITOR);
		visitor.visit(this.cu, constructorDeclarations);
	}

	@Override
	protected void parse() {
		try {
			this.cu = JavaParser.parse(new FileInputStream(file_path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
