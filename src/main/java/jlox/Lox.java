package jlox;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;

public class Lox {

    public static void main(String[] args) throws IOException {
        System.exit(new Lox().run(args));
    }

    public int run(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage:\n\tjlox [script]");
            return 64;
        } else if (args.length == 1) {
            interpretFile(args[0]);
        } else {
            repl();
        }
        return 0;
    }

    private void interpretFile(String filename) throws IOException {
        interpret(
            new String(
                Files.readAllBytes(Paths.get(filename)),
                Charset.defaultCharset()
            )
        );
    }

    private void repl() throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Lox");
        while(true) {
            System.out.print("> ");
            var line = reader.readLine();
            if (line == null) break;
            interpret(line);
        }
    }

    private void interpret(String script) {

    }
}
