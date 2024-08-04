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
            return "final %s %s;".formatted(type, name);
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
        List<ASTType> types = Arrays.asList(
            new ASTType("Assign", new Field("Token", "name"), new Field("Expr", "value")),
            new ASTType(
                "Binary",
                new Field("Expr", "left"),
                new Field("Token", "operator"),
                new Field("Expr", "right")
            ),
            new ASTType("Grouping", new Field("Expr", "expression")),
            new ASTType("Literal", new Field("Object", "value")),
            new ASTType(
                "Logical",
                new Field("Expr", "left"),
                new Field("Token", "operator"),
                new Field("Expr", "right")
            ),
            new ASTType(
                "Unary",
                new Field("Token", "operator"),
                new Field("Expr", "expression")
            ),
            new ASTType("Variable", new Field("Token", "name"))
        );
        generateAST(outputDir, "Expr", types);
        generateAST(
            outputDir,
            "Stmt",
            Arrays.asList(
                new ASTType(
                    "If", 
                    new Field("Expr", "condition"),
                    new Field("Stmt", "thenBranch"),
                    new Field("Stmt", "elseBranch")
                ),
                new ASTType("Block", new Field("List<Stmt>", "statements")),
                new ASTType("Expression", new Field("Expr", "expression")),
                new ASTType("Print", new Field("Expr", "expression")),
                new ASTType(
                    "Var",
                    new Field("Token", "name"),
                    new Field("Expr", "initializer")
                ),
                new ASTType(
                    "While", 
                    new Field("Expr", "condition"), 
                    new Field("Stmt", "body")
                )
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
        writer.println("");

        // Types.
        for (ASTType type : types) {
            defineType(writer, baseName, type);
        }

        generateVisitor(writer, baseName, types);
        writer.println("");

        // Abstract method for visitor.
        writer.println("    abstract <T> T accept(Visitor<T> visitor);");
        writer.println("");

        // End of class.
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

        // Visitor method.
        writer.println("        @Override");
        writer.println("        <T> T accept(Visitor<T> visitor) {");
        writer.printf("            return visitor.visit%s%s(this);\n", type.name(), baseName);
        writer.println("        }");

        // End of class.
        writer.println("    }");
        writer.println("");
    }

    private static void generateVisitor(PrintWriter writer, String baseName, List<ASTType> types) throws IOException {
        writer.println("    static interface Visitor<T> {");
        writer.println();
        for (ASTType type : types) {
            writer.printf("        T visit%2$s%1$s(%1$s.%2$s %3$s);\n", baseName, type.name(), baseName.toLowerCase());
        }
        writer.println();
        writer.println("    }");
    }
}
