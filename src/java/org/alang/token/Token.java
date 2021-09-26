package org.alang.token;

public class Token {

    private final Type type;
    private final Object value;

    public enum Type {
        NUMBER,
        SYMBOL,
        STRING,
        OPERATOR,
        COMMA,
        METHOD
    }

    public Token(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
