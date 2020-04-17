package visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassOrInterfaceDeclarationVisitor extends VoidVisitorAdapter<Object> {
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		  super.visit(n, arg);
          System.out.println(" * " + n.getName());
	}
	
	
}
