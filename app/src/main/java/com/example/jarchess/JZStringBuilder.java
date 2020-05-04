package com.example.jarchess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class JZStringBuilder {
    private final StringBuilder stringBuilder;

    public JZStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    //
//    public JZStringBuilder(String str) {
//        stringBuilder = new StringBuilder(str);
//    }

    public JZStringBuilder(Object obj) {
        if (obj == null) {
            stringBuilder = new StringBuilder();
        } else {
            stringBuilder = new StringBuilder(obj.toString());
        }
    }

//    public JZStringBuilder(String str, String... otherStrings) {
//
//        stringBuilder = new StringBuilder(str);
//
//        for(String s : otherStrings){
//            stringBuilder.append(s);
//        }
//    }

    public JZStringBuilder(Object... objects) {

        this();

        for (Object o : objects) {
            append(o);
        }
    }

    public JZStringBuilder() {
        stringBuilder = new StringBuilder();
    }

    public static String build(Object... objects) {
        return new JZStringBuilder(objects).toString();
    }


    public StringBuilder append(@Nullable Object... args) {
        for (Object o : args) {
            stringBuilder.append(o);
        }
        return stringBuilder;
    }

    public StringBuilder append(@Nullable Object obj) {
        return stringBuilder.append(obj);
    }


    public StringBuilder append(@Nullable String str) {
        return stringBuilder.append(str);
    }


    public StringBuilder append(@Nullable StringBuffer sb) {
        return stringBuilder.append(sb);
    }


    public StringBuilder append(@Nullable CharSequence s) {
        return stringBuilder.append(s);
    }


    public StringBuilder append(@Nullable CharSequence s, int start, int end) {
        return stringBuilder.append(s, start, end);
    }


    public StringBuilder append(char[] str) {
        return stringBuilder.append(str);
    }


    public StringBuilder append(char[] str, int offset, int len) {
        return stringBuilder.append(str, offset, len);
    }


    public StringBuilder append(boolean b) {
        return stringBuilder.append(b);
    }


    public StringBuilder append(char c) {
        return stringBuilder.append(c);
    }


    public StringBuilder append(int i) {
        return stringBuilder.append(i);
    }


    public StringBuilder append(long lng) {
        return stringBuilder.append(lng);
    }


    public StringBuilder append(float f) {
        return stringBuilder.append(f);
    }


    public StringBuilder append(double d) {
        return stringBuilder.append(d);
    }


    public StringBuilder appendCodePoint(int codePoint) {
        return stringBuilder.appendCodePoint(codePoint);
    }

    public int capacity() {
        return stringBuilder.capacity();
    }

    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }

    public int codePointAt(int index) {
        return stringBuilder.codePointAt(index);
    }

    public int codePointBefore(int index) {
        return stringBuilder.codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return stringBuilder.codePointCount(beginIndex, endIndex);
    }

    public StringBuilder delete(int start, int end) {
        return stringBuilder.delete(start, end);
    }

    public StringBuilder deleteCharAt(int index) {
        return stringBuilder.deleteCharAt(index);
    }

    public void ensureCapacity(int minimumCapacity) {
        stringBuilder.ensureCapacity(minimumCapacity);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        stringBuilder.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public int indexOf(@NonNull String str) {
        return stringBuilder.indexOf(str);
    }

    public int indexOf(@NonNull String str, int fromIndex) {
        return stringBuilder.indexOf(str, fromIndex);
    }

    public StringBuilder insert(int index, char[] str, int offset, int len) {
        return stringBuilder.insert(index, str, offset, len);
    }

    public StringBuilder insert(int offset, @Nullable Object obj) {
        return stringBuilder.insert(offset, obj);
    }

    public StringBuilder insert(int offset, @Nullable String str) {
        return stringBuilder.insert(offset, str);
    }

    public StringBuilder insert(int offset, char[] str) {
        return stringBuilder.insert(offset, str);
    }

    public StringBuilder insert(int dstOffset, @Nullable CharSequence s) {
        return stringBuilder.insert(dstOffset, s);
    }

    public StringBuilder insert(int dstOffset, @Nullable CharSequence s, int start, int end) {
        return stringBuilder.insert(dstOffset, s, start, end);
    }

    public StringBuilder insert(int offset, boolean b) {
        return stringBuilder.insert(offset, b);
    }

    public StringBuilder insert(int offset, char c) {
        return stringBuilder.insert(offset, c);
    }

    public StringBuilder insert(int offset, int i) {
        return stringBuilder.insert(offset, i);
    }

    public StringBuilder insert(int offset, long l) {
        return stringBuilder.insert(offset, l);
    }

    public StringBuilder insert(int offset, float f) {
        return stringBuilder.insert(offset, f);
    }

    public StringBuilder insert(int offset, double d) {
        return stringBuilder.insert(offset, d);
    }

    public int lastIndexOf(@NonNull String str) {
        return stringBuilder.lastIndexOf(str);
    }

    public int lastIndexOf(@NonNull String str, int fromIndex) {
        return stringBuilder.lastIndexOf(str, fromIndex);
    }

    public int length() {
        return stringBuilder.length();
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return stringBuilder.offsetByCodePoints(index, codePointOffset);
    }

    public StringBuilder replace(int start, int end, @NonNull String str) {
        return stringBuilder.replace(start, end, str);
    }

    public StringBuilder reverse() {
        return stringBuilder.reverse();
    }

    public void setCharAt(int index, char ch) {
        stringBuilder.setCharAt(index, ch);
    }

    public void setLength(int newLength) {
        stringBuilder.setLength(newLength);
    }

    public CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }

    public String substring(int start) {
        return stringBuilder.substring(start);
    }

    public String substring(int start, int end) {
        return stringBuilder.substring(start, end);
    }

    @NonNull
    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public void trimToSize() {
        stringBuilder.trimToSize();
    }


}
