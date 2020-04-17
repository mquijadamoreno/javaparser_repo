package visitors;

import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodDeclarationVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
	
	@Override
	public void visit(MethodDeclaration methodDeclaration, List<MethodDeclaration> collector) {
		super.visit(methodDeclaration, collector);
		collector.add(methodDeclaration);
	}
	

}
