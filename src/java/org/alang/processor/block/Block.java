package org.alang.processor.block;

import org.alang.token.Token;

import java.util.ArrayList;
import java.util.List;

public class Block {

    private final String name;

    private final List<List<Token>> tokens;

    @SuppressWarnings("unchecked")
    public Block(String name, Token[] tokens) {
        this.name = name;

        final List<List<Token>> result = new ArrayList<>();
        ArrayList<Token> elements = new ArrayList<>();

        for (Token token : tokens) {
            if (elements.isEmpty() || token.getType() != Token.Type.COMMA) {
                elements.add(token);
                continue;
            }
            result.add((List<Token>) elements.clone());
            elements.clear();
        }

        if (!elements.isEmpty()) {
            result.add(elements);
        }
        this.tokens = result;
    }

    public String getName() {
        return name;
    }

    public List<List<Token>> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "block{" +
                "name='" + name + '\'' +
                ", node=" + tokens +
                '}';
    }
}
