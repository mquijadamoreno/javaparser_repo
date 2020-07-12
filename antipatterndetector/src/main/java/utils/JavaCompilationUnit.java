package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class JavaCompilationUnit {
	
	private CompilationUnit cu;
	private String file_path;
	private String className;
	
	public JavaCompilationUnit(String file_path, String className) {
		this.file_path = file_path;
		this.className = className;
	}
	
	public void buildCompilationUnit() {
		try {
			CombinedTypeSolver typeSolver = new CombinedTypeSolver();
			typeSolver.add(new JavaParserTypeSolver(new File(file_path)));
			typeSolver.add(new ReflectionTypeSolver());
			JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
			JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
			this.cu = JavaParser.parse(new FileInputStream(file_path + className));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public CompilationUnit getCompilationUnit() {
		return this.cu;
	}
	
	public String getFilePath() {
		return this.file_path + className;
	}

}
