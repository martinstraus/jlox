package jlox;

class RuntimeError extends RuntimeException {

    private final Token token;

    public RuntimeError(Token token, String error) {
        super(error);
        this.token = token;
    }

    public final int line() {
        return token.line();
    }
}
