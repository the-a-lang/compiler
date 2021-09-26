package org.alang.data;

import org.alang.token.Token;

public class Data {
    public static final Token LPAREN = new Token(Token.Type.OPERATOR, '(');
    public static final Token RPAREN = new Token(Token.Type.OPERATOR, ')');

    public static final Token emptyToken = new Token(Token.Type.NUMBER, 0D);
    public static final Token plusToken = new Token(Token.Type.OPERATOR, '+');

    public static final Token commaToken = new Token(Token.Type.COMMA, ",");
}
