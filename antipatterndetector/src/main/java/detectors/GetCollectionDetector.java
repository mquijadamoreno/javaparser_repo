package detectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.sun.xml.internal.ws.util.StringUtils;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class GetCollectionDetector extends AbstractDetector {

	List<FieldDeclaration> fieldDeclarations;
	List<MethodDeclaration> methodDeclarations;

	public GetCollectionDetector(String file_path) throws FileNotFoundException {
		super(GetCollectionDetector.class.getName(), file_path);
		fieldDeclarations = new ArrayList<FieldDeclaration>();
		methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		parse();
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
			Optional<ObjectCreationExpr> creationExprOptional = method.findFirst(ObjectCreationExpr.class);

			if (creationExprOptional.isPresent() && creationExprOptional.get().getArgument(0).toString()
					.contains(StringUtils.decapitalize(method.getNameAsString().replace("get", "")))) {
				System.out.println(method);
				//this means the get Method is correctly used.
			}
			//TODO: build the option to recognise the call to unmodifableList.
		}
	}

	@Override
	protected void parse() {
		try {
			CombinedTypeSolver typeSolver = new CombinedTypeSolver();
			typeSolver.add(new JavaParserTypeSolver(new File(this.file_path)));
			typeSolver.add(new ReflectionTypeSolver());
			JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
			JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
			this.cu = JavaParser.parse(new FileInputStream(this.file_path + "TestClass.java"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
