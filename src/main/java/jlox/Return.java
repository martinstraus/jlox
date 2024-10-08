package jlox;

public class Return extends RuntimeException {

    private final Object value;

    Return(Object value) {
        super(null, null, false, false);
        this.value = value;
    }

    public Object value() {
        return value;
    }

}
