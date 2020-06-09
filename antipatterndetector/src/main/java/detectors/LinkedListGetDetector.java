package detectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class LinkedListGetDetector extends AbstractDetector {

	List<MethodCallExpr> methodCallExprs;
	
	public LinkedListGetDetector(String file_path) throws FileNotFoundException {
		super(LinkedListGetDetector.class.getName(), file_path);
		this.methodCallExprs = new ArrayList<MethodCallExpr>();
	}

	@Override
	public void detect() {
		parse();
		retrieveMethodCalls();
		methodCallExprs = methodCallExprs.stream().filter(mce -> mce.getNameAsString().equals("get")).collect(Collectors.toList());
		for (MethodCallExpr methodCall : methodCallExprs) {
			if(methodCall.getScope().isPresent()) {
				ResolvedType exprType = methodCall.getScope().get().calculateResolvedType();
				if(exprType.isReferenceType()) {
					try {
						Class clazz = Class.forName(exprType.asReferenceType().getTypeDeclaration().getQualifiedName());
						System.out.println(clazz.isInstance(List.class));
						//TODO: ask how to retrieve concrete type.
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void retrieveMethodCalls() {
		VoidVisitor<List<MethodCallExpr>> visitor = (VoidVisitor<List<MethodCallExpr>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_CALL_EXPR_VISITOR);
		visitor.visit(this.cu, methodCallExprs);
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

}
