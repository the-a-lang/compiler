package org.alang.lex;

import org.alang.ALang;
import org.alang.data.Stack;
import org.alang.token.Token;

import static java.lang.Character.toChars;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static org.alang.data.Data.commaToken;
import static org.alang.token.TokenCache.getNumber;
import static org.alang.token.TokenCache.getSymbol;

public class Lex {

    private final StringBuilder numbers = new StringBuilder();
    private final StringBuilder texts = new StringBuilder();

    private final ALang aLang;

    private Stack<Token> stack;

    public Lex(final ALang aLang) {
        this.aLang = aLang;
    }

    public Object[] lex(final String text) {
        char[] chars = text.toCharArray();
        final int len = chars.length;

        stack = new Stack<>(len, true);

        boolean inside = false;
        for (int i = 0; i < len; i++) {
            char aChar = chars[i];

            if (aChar == '"') {
                inside = !inside;
            } else if (inside) {
                if (aChar == '\\') {
                    if ((++i >= chars.length)) {
                        throw new IllegalArgumentException();
                    }
                    switch (aChar = chars[i]) {
                        case 't':
                           aChar = '\t';
                           break;
                        case 'n':
                            aChar = '\n';
                            break;
                        case 's':
                            aChar = ' ';
                            break;
                        case '0':
                            aChar = '\0';
                            break;
                        case '\'':
                        case '\"':
                        case '\\':
                            break;
                        case 'u': {
                            if (--i >= len - 5) {
                                throw new IllegalArgumentException();
                            }
                            texts.append(toChars(parseInt(valueOf(chars[i + 2]) +
                                    chars[i + 3] + chars[i + 4] + chars[i + 5], 16)));
                            i += 5;
                            continue;
                        }
                        default:
                            throw new IllegalArgumentException("Illegal escape character in string literal");
                    }
                }
                texts.append(aChar);
            } else if (aChar == ',') {
                push();
                stack.push(commaToken);
            } else if (aLang.operatorIndexOf(aChar) >= 0) {
                push();
                stack.push(aLang.tokenFor(aChar));
            } else {
                final boolean isBlank = isBlank(aChar);

                if (isBlank || isBlock(aChar)) {
                    push();
                    if (!isBlank)
                        stack.push(new Token(
                                Token.Type.SYMBOL, valueOf(aChar)));
                } else if (isNumber(aChar)) {
                    numbers.append(aChar);
                } else {
                    push();
                    final Token lastPushToken = stack.getLast();
                    final Object last = lastPushToken == null ?
                            null : lastPushToken.getValue();
                    boolean isLetter = isLetter(aChar);

                    if (isLetter && last instanceof String
                            && areLetters(valueOf(last)) && this.isLetter(chars[i - 1])) {
                        stack.pushLast(new Token(Token.Type.SYMBOL,
                                last + valueOf(aChar)));
                    } else {
                        if (isLetter && lastPushToken != null &&
                                lastPushToken.getType() == Token.Type.NUMBER)
                            throw new IllegalArgumentException("What do you mean by '" +
                                    last + aChar + "'?");
                        stack.push(getSymbol(valueOf(aChar)));
                    }
                }
            }
        }
        if (inside) {
            throw new IllegalArgumentException();
        }
        push();
        return stack.getObjects();
    }

    private boolean isBlock(char aChar) {
        return aChar == '(' || aChar == ')';
    }

    private boolean areLetters(final String text) {
        for (char aChar : text.toCharArray())
            if (!isLetter(aChar))
                return false;
        return !text.isEmpty();
    }

    private boolean isLetter(final char aChar) {
        return inRange('a', 'z', aChar) ||
                inRange('A', 'Z', aChar);
    }

    private boolean inRange(final char from, final char to,
                            final char aChar) {
        return aChar >= from && aChar <= to;
    }

    private boolean isNumber(final char aChar) {
        return aChar == '.' || isDigit(aChar)
                || aChar == 'E' || aChar == 'e';
    }

    private boolean isDigit(final char aChar) {
        return inRange('0', '9', aChar);
    }

    private void push() {
        final String number = buildNReset(numbers);
        final Token previous = stack.getLast();
        final String previousValue = valueOf(previous == null ?
                "" : previous.getValue());
        if (number != null) {
            if (previous != null && previous.getType() == Token.Type.SYMBOL &&
                    areLetters(previousValue) && onlyNumbers(number)) {
                stack.pushLast(new Token(Token.Type.SYMBOL,
                        previousValue.concat(number)));
            } else stack.push(getNumber(number));
        }

        final String text = buildNReset(texts);
        if (text != null)
            stack.push(new Token(Token.Type.STRING, text));
    }

    private boolean onlyNumbers(String number) {
        for (char aChar : number.toCharArray())
            if (!isDigit(aChar))
                return false;
        return true;
    }

    private String buildNReset(StringBuilder builder) {
        final String build = builder.toString();
        builder.setLength(0);
        return build.isEmpty() ? null : build;
    }

    private boolean isBlank(char aChar) {
        return aChar == ' '
                || aChar == '\t'
                || aChar == '\n';
    }
}
