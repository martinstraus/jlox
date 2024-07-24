package jlox;

import static java.util.Collections.emptyList;
import java.util.*;
import static jlox.TokenType.*;

class Scanner {

    private final String script;
    private final List<Token> tokens = new ArrayList<>();
    private int start, current, line;

    Scanner(String script) {
        this.script = script;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= script.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            default:
                Lox.error(line, String.format("Unexpected character '%c'", c));
                break;
        }
    }

    private char advance() {
        return script.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        var text = script.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
