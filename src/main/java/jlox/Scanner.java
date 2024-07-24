package jlox;

import static java.util.Collections.emptyList;
import java.util.List;

class Scanner {

    private final String script;

    public Scanner(String script) {
        this.script = script;
    }

    public List<Token> scanTokens() {
        return emptyList();
    }
}
