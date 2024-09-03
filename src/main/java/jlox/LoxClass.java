package jlox;

import java.util.List;

public class LoxClass implements LoxCallable {
    private final String name;
    
    LoxClass(String name) {
        this.name = name;
    }
    
    String name() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return new LoxInstance(this);
    }

    @Override
    public int arity() {
        return 0;
    }
}
