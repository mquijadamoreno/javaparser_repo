package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.sun.xml.internal.ws.util.StringUtils;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class GetCollectionDetector extends AbstractDetector {

	List<FieldDeclaration> fieldDeclarations;
	List<MethodDeclaration> methodDeclarations;

	public GetCollectionDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(GetCollectionDetector.class.getName(), jcu);
		fieldDeclarations = new ArrayList<FieldDeclaration>();
		methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		retrieveFieldsAndMethodsData();
		fieldDeclarations = fieldDeclarations.stream()
				.filter(fd -> (fd.findFirst(ClassOrInterfaceType.class).get().getNameAsString().equals("List")
						|| fd.findFirst(ClassOrInterfaceType.class).get().getNameAsString().equals("Set")))
				.collect(Collectors.toList());
		List<String> fieldDeclarationsNames = new ArrayList<String>();
		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
			fieldDeclarationsNames.add(fieldDeclaration.findFirst(VariableDeclarator.class).get().getNameAsString());
		}

		methodDeclarations = methodDeclarations.stream()
				.filter(md -> (fieldDeclarationsNames
						.contains(StringUtils.decapitalize(md.getNameAsString().replace("get", "")))
						&& (md.findFirst(ClassOrInterfaceType.class).get().getNameAsString().equals("List")
								|| md.findFirst(ClassOrInterfaceType.class).get().getNameAsString().equals("Set"))))
				.collect(Collectors.toList());

		for (MethodDeclaration method : methodDeclarations) {

			/* Search for use of unmodifiableList */
			boolean found = false;
			List<MethodCallExpr> methodCallExpressions = method.findAll(MethodCallExpr.class);
			for (MethodCallExpr methodCallExpr : methodCallExpressions) {
				if ((methodCallExpr.getName().toString().equals("unmodifiableList")
						|| (methodCallExpr.getName().toString().equals("unmodifiableSet"))
								&& methodCallExpr.getArgument(0).toString().contains(
										StringUtils.decapitalize(method.getNameAsString().replace("get", ""))))) {
					found = true;
				}
			}
			
			/*Check if a copy or unmodifiable list are being called from the get method*/
			Optional<ObjectCreationExpr> creationExprOptional = method.findFirst(ObjectCreationExpr.class);
			if (!(creationExprOptional.isPresent() && (creationExprOptional.get().getArgument(0).toString()
					.contains(StringUtils.decapitalize(method.getNameAsString().replace("get", ""))))) || !found) {
				this.addOcurrence(new Ocurrence(method.getRange().get().toString(), this.reason, this.file_path));
			}

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
