package com.example.jarchess;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class JZStringBuilderTest {
    @Parameterized.Parameter()
    public static String expected;
    @Parameterized.Parameter(value = 1)
    public static Object[] args;
    private static Collection<Object[]> data = new LinkedList<>();
    private JZStringBuilder jzStringBuilder;
    private String actual;

    static private Object[] buildDataRow(String expectedString, Object... args) {
        List<Object> list = new LinkedList<>();

        list.add(expectedString);
        list.add(args == null ? new Object[]{} : args);
        data.add(list.toArray());

        return list.toArray();
    }

    @Parameterized.Parameters(name = "{0}")

    public static Collection<Object[]> data() {

        buildDataRow("hi 123 12.3", "hi", ' ', 12, 3, " ", 12.3);
        buildDataRow("hi null123 12.3", "hi", ' ', null, 12, 3, " ", 12.3);
        buildDataRow("", "");
        buildDataRow("");
        return data;
    }

    @Test
    public void buildIsCorrect() {
        buildIsCorrectSetup();
        assertEquals(expected, actual);
    }

    private void buildIsCorrectSetup() {
        System.out.println(args);
        switch (args.length) {
            case 0:
                actual = JZStringBuilder.build();
                break;

            case 1:
                actual = JZStringBuilder.build(args[0]);
                break;

            case 2:
                actual = JZStringBuilder.build(args[0], args[1]);
                break;

            case 3:
                actual = JZStringBuilder.build(args[0], args[1], args[2]);
                break;

            case 4:
                actual = JZStringBuilder.build(args[0], args[1], args[2], args[3]);
                break;

            case 5:
                actual = JZStringBuilder.build(args[0], args[1], args[2], args[3], args[4]);
                break;

            case 6:
                actual = JZStringBuilder.build(args[0], args[1], args[2], args[3], args[4], args[5]);
                break;

            case 7:
                actual = JZStringBuilder.build(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + args.length);
        }
    }

    @Test
    public void constructorAndToStringIsCorrect() {
        constructorAndToStringIsCorrectSetup();

        assertEquals(expected, jzStringBuilder.toString());
    }

    private void constructorAndToStringIsCorrectSetup() {
        switch (args.length) {
            case 0:
                jzStringBuilder = new JZStringBuilder();
                break;

            case 1:
                jzStringBuilder = new JZStringBuilder(args[0]);
                break;

            case 2:
                jzStringBuilder = new JZStringBuilder(args[0], args[1]);
                break;

            case 3:
                jzStringBuilder = new JZStringBuilder(args[0], args[1], args[2]);
                break;

            case 4:
                jzStringBuilder = new JZStringBuilder(args[0], args[1], args[2], args[3]);
                break;

            case 5:
                jzStringBuilder = new JZStringBuilder(args[0], args[1], args[2], args[3], args[4]);
                break;

            case 6:
                jzStringBuilder = new JZStringBuilder(args[0], args[1], args[2], args[3], args[4], args[5]);
                break;

            case 7:
                jzStringBuilder = new JZStringBuilder(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + args.length);
        }
    }

    @Before
    public void setUp() throws Exception {


    }
}