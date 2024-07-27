package jlox;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import static jlox.TokenType.*;

public class Lox {

    private static boolean hadError;

    static void error(Token token, String message) {
        if (token.type() == EOF) {
            report(token.line(), " at end", message);
        } else {
            report(token.line(), " at '" + token.lexeme() + "'", message);
        }
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
        return hadError ? 65 : 0;
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
        }
    }

    private static void interpret(String script) {
        List<Token> tokens = new Scanner(script).scanTokens();
        Expr expression = new Parser(tokens).parse();
        if (hadError) {
            return;
        }
        System.out.println(new AstPrinter().print(expression));
    }

}
