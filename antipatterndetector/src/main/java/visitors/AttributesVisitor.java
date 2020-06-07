package visitors;

import java.util.List;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AttributesVisitor extends VoidVisitorAdapter<List<FieldDeclaration>> {
	
	@Override
	public void visit(FieldDeclaration n, List<FieldDeclaration> list) {
		super.visit(n, list);
		list.add(n);
	}

}
