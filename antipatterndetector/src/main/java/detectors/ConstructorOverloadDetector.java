package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class ConstructorOverloadDetector extends AbstractDetector {

	private List<ConstructorDeclaration> constructorDeclarations;

	public ConstructorOverloadDetector(String file_path) throws FileNotFoundException {
		super(ConstructorOverloadDetector.class.getName(), file_path);
		this.constructorDeclarations = new ArrayList<ConstructorDeclaration>();
	}

	@Override
	public void detect() {
		parse();
		retrieveMethodNames();
		analyzeConstructors();
	}

	private void analyzeConstructors() {
		Integer numOfConstructorDeclarations = constructorDeclarations.size();
		Integer numOfTimesThisCallExpr = 0;
		if (numOfConstructorDeclarations > 1) {

			ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) constructorDeclarations.get(0)
					.getParentNode().get();

			for (ConstructorDeclaration ocurrence : constructorDeclarations) {
				if (CollectionUtils.isNotEmpty(ocurrence.getBody().findAll(ExplicitConstructorInvocationStmt.class))) {
					numOfTimesThisCallExpr++;
				}

			}

			if (numOfTimesThisCallExpr != (numOfConstructorDeclarations - 1)) {
				this.addOcurrence(new Ocurrence("undefined", this.reason, cid.getNameAsString()+".java"));
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
