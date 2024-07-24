package jlox;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

public class Lox {

    private static boolean hadError;

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
        var scanner = new Scanner(script);
        List<Token> tokens = scanner.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

}
