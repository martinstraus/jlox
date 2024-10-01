package jlox;

import java.util.List;
import java.util.Map;

public class LoxClass implements LoxCallable {
    private final String name;
    private final Map<String, LoxFunction> methods;
    
    LoxClass(String name, Map<String, LoxFunction> methods) {
        this.name = name;
        this.methods = methods;
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
    
    LoxFunction findMethod(String name) {
        return (methods.containsKey(name)) 
            ? methods.get(name)
            : null;
    }
}
