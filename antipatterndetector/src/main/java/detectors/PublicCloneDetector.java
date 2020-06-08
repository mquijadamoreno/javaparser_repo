package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class PublicCloneDetector extends AbstractDetector {

	List<MethodDeclaration> cloneMethods;

	public PublicCloneDetector(String file_path) throws FileNotFoundException {
		super(PublicCloneDetector.class.getName(), file_path);
		cloneMethods = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		parse();
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

	@Override
	protected void parse() {
		try {
			this.cu = JavaParser.parse(new FileInputStream(file_path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
