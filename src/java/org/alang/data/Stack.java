package org.alang.data;

import java.util.Arrays;

import static java.lang.Math.min;
import static java.lang.System.arraycopy;

public class Stack<T> {

    private T[] objects;
    private final int initialLen;
    private final boolean avoidNull;
    public int pos = 0;

    @SuppressWarnings("unchecked")
    public Stack(int initialLen, boolean avoidNull) {
        objects = (T[]) new Object[initialLen];
        this.initialLen = initialLen;
        this.avoidNull = avoidNull;
    }
    
    public void push(T object) {
        if (object == null && avoidNull)
            return;
        if (pos == objects.length)
            objects = resize(objects.length * 2);
        objects[pos++] = object;
    }

    public void pushLast(T object) {
        objects[pos - 1] = object;
    }

    public T getLast() {
        final int last = pos - 1;
        if (last < 0)
            return null;
        return objects[pos - 1];
    }

    @SuppressWarnings("unchecked")
    private T[] resize(int to) {
        final T[] copy = (T[]) new Object[to];
        arraycopy(objects, 0, copy, 0, min(objects.length, to));
        return copy;
    }

    public T[] getObjects() {
        return resize(pos);
    }
}
