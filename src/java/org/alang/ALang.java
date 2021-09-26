package org.alang;

import org.alang.lex.Lex;
import org.alang.processor.Processor;
import org.alang.token.Token;

import static org.alang.token.Token.Type.OPERATOR;

public class ALang {

    private static final char[] operators = {'+', '-', '/', '*'};
    public static final char[] precedenceOperators = {'*', '/'};

    public static final Token[] operatorTokens;

    static {
        final int len = operators.length;

        operatorTokens = new Token[len];
        for (int i = 0; i < len; i++) {
            operatorTokens[i] = new Token(OPERATOR, operators[i]);
        }
    }

    public Token[] process(final String text) {
        return Processor.processNode(lex(text));
    }

    public Object[] lex(final String text) {
        return new Lex(this).lex(text);
    }

    public Token tokenFor(final char operator) {
        return operatorTokens[operatorIndexOf(operator)];
    }

    public int operatorIndexOf(char aChar) {
        for (int i = 0; i < operators.length; i++) {
            char operator = operators[i];
            if (aChar == operator)
                return i;
        }
        return -1;
    }
}
