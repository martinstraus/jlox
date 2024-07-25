package tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import static java.util.stream.Collectors.joining;

public class ASTGenerator {

    private static record ASTType(String name, Field... fields) {

    }

    private static record Field(String type, String name) {

        @Override
        public String toString() {
            return "%s %s".formatted(type, name);
        }
        
        public String declaration() {
            return "private final %s %s;".formatted(type, name);
        }
        
        public String assignment() {
            return "this.%1$s = %1$s;".formatted(name);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage:\n\tast_generator [output directory]");
            System.exit(64);
        }
        Path outputDir = Paths.get(args[0]);
        generateAST(
            outputDir,
            "Expr",
            Arrays.asList(
                new ASTType(
                    "Binary",
                    new Field("Expr", "left"),
                    new Field("Token", "Operator"),
                    new Field("Expr", "right")
                ),
                new ASTType("Grouping", new Field("Expr", "expression")),
                new ASTType("Literal", new Field("Object", "value")),
                new ASTType("Unary", new Field("Token", "operator"))
            )
        );
    }

    private static void generateAST(Path outputDir, String baseName, List<ASTType> types) throws IOException {
        Path file = outputDir.resolve(baseName + ".java");
        PrintWriter writer = new PrintWriter(file.toString(), "UTF-8");
        writer.println("package jlox;");
        writer.println("");
        writer.println("import java.util.List;");
        writer.println("");
        writer.printf("abstract class %s {\n", baseName);
        for (ASTType type : types) {
            defineType(writer, baseName, type);
        }
        writer.println("}");
        writer.close();
    }
    
    private static void defineType(PrintWriter writer, String baseName, ASTType type) {
        writer.printf("    static class %s extends %s {\n", type.name(), baseName);
        
        for (Field field : type.fields()) {
            writer.println("        " + field.declaration());
        }
        
        // Constructor.
        String arguments = Arrays
            .asList(type.fields())
            .stream()
            .map(Field::toString)
            .collect(joining(","));
        writer.printf("        %s(%s) {\n", type.name(), arguments);
        for (Field field : type.fields()) {
            writer.println("            " + field.assignment());
        }
        writer.println("        }");
        
        writer.println("    }");
    }
}
