package visitors;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ForLoopVisitor extends VoidVisitorAdapter<Void> {
	
	private List<ForStmt> listForStmt;
	private List<ForEachStmt> listForEachStmt;
	
	public ForLoopVisitor() {
		this.listForStmt = new ArrayList<ForStmt>();
		this.listForEachStmt = new ArrayList<ForEachStmt>();
	}
	
	public void visit(CompilationUnit cu) {
		ForEachVisitor forEachVisitor = new ForEachVisitor();
		ForVisitor forVisitor = new ForVisitor();
		forEachVisitor.visit(cu,listForEachStmt);
		forVisitor.visit(cu,listForStmt);
	}

}
