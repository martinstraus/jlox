package jlox;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

public class Lox {

    public static void main(String[] args) throws IOException {
        System.exit(new Lox().run(args));
    }
    
    private boolean hadError;

    public int run(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage:\n\tjlox [script]");
            return 64;
        } else if (args.length == 1) {
            return interpretFile(args[0]);
        } else {
            repl();
            return 0;
        }
    }

    private int interpretFile(String filename) throws IOException {
        interpret(
            new String(
                Files.readAllBytes(Paths.get(filename)),
                Charset.defaultCharset()
            )
        );
        return hadError ? 65 : 0;
    }

    private void repl() throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Lox");
        while(true) {
            System.out.print("> ");
            var line = reader.readLine();
            if (line == null) break;
            interpret(line);
            hadError = false;
        }
    }

    private void interpret(String script) {
        var scanner = new Scanner(script);
        List<Token> tokens = scanner.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
    
    private void error(int line, String message) {
        report(line, "", message);
    }
    
    private void report(int line, String where, String message) {
        System.err.printf("[line %d] Error %s: message", line, where);
        hadError = true;
    }
}
