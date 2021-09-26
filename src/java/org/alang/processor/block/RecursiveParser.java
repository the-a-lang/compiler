package org.alang.processor.block;

import org.alang.token.Token;

import java.util.ArrayList;
import java.util.List;

public class RecursiveParser {

    private final List<List<Token>> blocks;

    private final ArrayList<Token> elements = new ArrayList<>();

    private Object previous = null;
    private int blockIndex = 0;

    public RecursiveParser(final List<List<Token>> blocks) {
        this.blocks = blocks;
    }

    public void put(final Object element) {
        elements.add((Token) element);
        previous = element;
    }

    public void putBlock() {
        processBlock();
        previous = null;
    }

    public Token[] getTokens() {
        return elements.toArray(new Token[0]);
    }

    private void processBlock() {
        if (previous instanceof Token && isFunctionName((Token) previous)) {
            final Token[] tokens = blocks.get(blockIndex++)
                    .toArray(new Token[0]);

            elements.set(elements.size() - 1, new Token(
                            Token.Type.METHOD,
                            new Block((String) ((Token) previous).getValue(), tokens)
                    )
            );
        }
    }

    private boolean isFunctionName(Token previous) {
        return previous.getType() == Token.Type.SYMBOL &&
                previous.getValue().equals("max");
    }
}
