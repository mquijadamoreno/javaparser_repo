package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class PublicCloneDetector extends AbstractDetector {

	List<MethodDeclaration> cloneMethods;

	public PublicCloneDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(PublicCloneDetector.class.getName(), jcu);
		cloneMethods = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		retrieveCloneMethods();
		for (MethodDeclaration method : cloneMethods) {
			if (method.getParentNode().isPresent()) {
				ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) method.getParentNode().get();
				if (!cid.getImplementedTypes().toString().contains("Cloneable")) {
					this.addOcurrence(new Ocurrence(method.getRange().get().toString(), this.reason, cid.getNameAsString()));
				}
			}
		}
	}

	private void retrieveCloneMethods() {
		List<MethodDeclaration> methodDeclarations = new ArrayList<MethodDeclaration>();
		VoidVisitor<List<MethodDeclaration>> visitor = (VoidVisitor<List<MethodDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_NAME_VISITOR);
		visitor.visit(this.cu, methodDeclarations);
		this.cloneMethods = methodDeclarations.stream()
				.filter(m -> (m.getNameAsString().equals("clone") && m.getModifiers().toString().contains("public")))
				.collect(Collectors.toList());
	}

}
