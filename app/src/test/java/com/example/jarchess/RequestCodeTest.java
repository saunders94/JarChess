package com.example.jarchess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RequestCodeTest {
    @Parameterized.Parameter
    public int expectedInt;

    @Parameterized.Parameter(1)
    public RequestCode actualRequestCode;

    @Parameterized.Parameters(name = "{0}, {1}")
    public static Collection<Object[]> data() {
        List<Object[]> data = new LinkedList<>();
        data.add(new Object[]{0, RequestCode.FIND_RANDOM_OPPONENT_FOR_ONLINE_MATCH_REQUEST_CODE});
        data.add(new Object[]{1, RequestCode.FIND_FRIEND_OPPONENT_FOR_ONLINE_MATCH});
        data.add(new Object[]{2, RequestCode.START_MATCH});
        return data;
    }

    @Test
    public void getInt() {
        assertEquals(expectedInt, actualRequestCode.getInt());
    }
}