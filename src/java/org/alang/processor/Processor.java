package org.alang.processor;

import org.alang.processor.block.RecursiveParser;
import org.alang.token.Token;

import java.util.ArrayList;
import java.util.List;

public class Processor {

    public static Token[] processNode(Object[] objects) {
        return parse(objects);
    }

    private static Token[] parse(Object[] objects) {
        int open = 0;
        final int len = objects.length;

        final List<List<Token>> blocks = new ArrayList<>();

        final int div = len / 2;
        final ArrayList<Integer> positions = new ArrayList<>(div);
        final ArrayList<Integer> positionsR = new ArrayList<>(div);

        for (int i = 0; i < len; i++) {
            final Token token = (Token) objects[i];
            if (isWhat(token, "(") && ++open == 1) {
                positions.add(i);
                blocks.add(extracted(i, objects, positionsR));
            } else if (isWhat(token, ")")) {
                open--;
            }
        }
        checkZero(open);

        final RecursiveParser recursiveParser = new RecursiveParser(blocks);

        boolean isOpen = false;

        for (int i = 0; i < objects.length; i++) {
            final Token token = (Token) objects[i];

            if (isWhat(token, "(") && positions.contains(i)) {
                recursiveParser.putBlock();
                isOpen = true;
            } else if (isWhat(token, ")") && positionsR.contains(i)) {
                isOpen = false;
            } else if (!isOpen) {
                recursiveParser.put(token);
            }
        }
        return recursiveParser.getTokens();
    }

    private static List<Token> extracted(int pos, Object[] objects, List<Integer> positionsR) {
        final List<Token> elements = new ArrayList<>();

        int openBraces = 0;
        while (pos < objects.length) {
            final Token token = (Token) objects[pos++];

            if (isWhat(token, "(") && openBraces++ == 0) {
                continue;
            } else if (isWhat(token, ")") && openBraces-- == 1) {
                positionsR.add(pos - 1);
                break;
            }
            elements.add(token);
        }
        checkZero(openBraces);
        return elements;
    }

    public static boolean isWhat(Token token, String value) {
        return token.getType() == Token.Type.SYMBOL
                && token.getValue().equals(value);
    }

    private static void checkZero(int num) {
        if (num == 0)
            return;
        throw new IllegalArgumentException();
    }
}
