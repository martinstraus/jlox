package jlox;

class Token {

    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return type + " \"" + lexeme + "\" " + literal;
    }

    public String lexeme() {
        return lexeme;
    }

    public TokenType type() {
        return type;
    }

    public Object literal() {
        return literal;
    }
    
    public int line() {
        return line;
    }
}
