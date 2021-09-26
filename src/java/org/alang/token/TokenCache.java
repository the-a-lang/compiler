package org.alang.token;

import java.util.WeakHashMap;

public class TokenCache {
    private static final WeakHashMap<Object, Token> numbersTokensCache = new WeakHashMap<>();

    public static Token getNumber(final Object value) {
        Token token = numbersTokensCache.get(value);
        if (token == null)
            numbersTokensCache.put(value,
                    token = new Token(Token.Type.NUMBER, value));
        return token;
    }

    private static final WeakHashMap<Object, Token> symbolTokensCache = new WeakHashMap<>();

    public static Token getSymbol(final Object value) {
        Token token = symbolTokensCache.get(value);
        if (token == null)
            symbolTokensCache.put(value,
                    token = new Token(Token.Type.SYMBOL, value));
        return token;
    }
}
