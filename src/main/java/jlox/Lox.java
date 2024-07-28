package jlox;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import static jlox.TokenType.*;

public class Lox {

    private static boolean hadError;
    private static boolean hadRuntimeError;
    private static final Interpreter interpreter = new Interpreter();

    static void error(Token token, String message) {
        if (token.type() == EOF) {
            report(token.line(), " at end", message);
        } else {
            report(token.line(), " at '" + token.lexeme() + "'", message);
        }
    }

    static void runtimeError(RuntimeError error) {
        hadRuntimeError = true;
        System.err.printf("[line %d] %s\n", error.line(), error.getMessage());
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void report(int line, String where, String message) {
        System.err.printf("[line %d] Error %s: %s\n", line, where, message);
        hadError = true;
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage:\n\tjlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            System.exit(interpretFile(args[0]));
        } else {
            repl();
        }
    }

    private static int interpretFile(String filename) throws IOException {
        interpret(
            new String(
                Files.readAllBytes(Paths.get(filename)),
                Charset.defaultCharset()
            )
        );
        if (hadError) {
            return 66;
        }
        if (hadRuntimeError) {
            return 70;
        }
        return 0;
    }

    private static void repl() throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Lox");
        while (true) {
            System.out.print("> ");
            var line = reader.readLine();
            if (line == null) {
                break;
            }
            interpret(line);
            hadError = false;
            hadRuntimeError = false;
        }
    }

    private static void interpret(String script) {
        List<Token> tokens = new Scanner(script).scanTokens();
        List<Stmt> statements = new Parser(tokens).parse();
        if (hadError) {
            return;
        }
        interpreter.interpret(statements);
    }

    static String stringify(Object object) {
        if (object == null) {
            return "nil";
        }
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }

}
