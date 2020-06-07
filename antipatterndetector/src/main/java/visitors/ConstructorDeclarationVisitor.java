package visitors;

import java.util.List;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConstructorDeclarationVisitor extends VoidVisitorAdapter<List<ConstructorDeclaration>> {
	
	@Override
	public void visit(ConstructorDeclaration n, List<ConstructorDeclaration> list) {
		super.visit(n, list);
		list.add(n);
	}

}
