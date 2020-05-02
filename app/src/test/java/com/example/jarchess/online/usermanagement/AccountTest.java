package com.example.jarchess.online.usermanagement;

import android.util.Log;

import com.example.jarchess.online.JSONCompiler.JSONAccount;
import com.example.jarchess.online.networking.DataSender;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({Log.class})
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Parameterized.class)
public class AccountTest {
    private static final int VALID = 0;
    private static final int INVALID = 1;
    @Parameter
    public static String username;
    @Parameter(value = 1)
    public static String password;
    @Parameter(value = 2)
    public static boolean expectedResult;
    @Mock
    public DataSender mockDataSender = mock(DataSender.class);
    @Mock
    public JSONAccount mockJSONAccount = mock(JSONAccount.class);
    @Mock
    public JSONObject mockJSONObject = mock(JSONObject.class);
    private Account accountUnderTest;

    @Parameters(name = "{index} - username ={0}, password ={1}")
    public static Collection<Object[]> params() {
        Collection<Object[]> params = new LinkedList<>();
        params = new LinkedList();

        // valid username 3 long
        params.add(new Object[]{"tim", "aA3!5678", true}); // 8 long all types
        params.add(new Object[]{"tim", "aA345678", true}); // 8 long only missing symbol
        params.add(new Object[]{"tim", "a!345678", true}); // 8 long only missing upper
        params.add(new Object[]{"tim", "!A345678", true}); // 8 long only missing lower
        params.add(new Object[]{"tim", "aA!!!!!!", true}); // only missing letter
        params.add(new Object[]{"tim", "1234567890123456789012345678901234567890123456789012345678901!aA", true});// 64 long all types
        params.add(new Object[]{"tim", "aa345678", false});// lower number
        params.add(new Object[]{"tim", "AA345678", false});// upper number
        params.add(new Object[]{"tim", "!!345678", false});// symbol number
        params.add(new Object[]{"tim", "!SSSSSSS", false});// symbol upper
        params.add(new Object[]{"tim", "!aaaaaaa", false});// symbol lower
        params.add(new Object[]{"tim", "aAbbbbbb", false});// lower upper
        params.add(new Object[]{"tim", "1234aB!", false});// all types, too short (length 7)
        params.add(new Object[]{"tim", "1234567890123456789012345678901234567890123456789012345678901!aAA", false});// 65 long (too long) all types
        params.add(new Object[]{"tim", "", false});// empty password
        params.add(new Object[]{"tim", null, false});// null password

        // valid username 10 long
        params.add(new Object[]{"1234567890", "aA3!5678", true}); // 8 long all types
        params.add(new Object[]{"1234567890", "aA345678", true}); // 8 long only missing symbol
        params.add(new Object[]{"1234567890", "a!345678", true}); // 8 long only missing upper
        params.add(new Object[]{"1234567890", "!A345678", true}); // 8 long only missing lower
        params.add(new Object[]{"1234567890", "aA!!!!!!", true}); // only missing letter
        params.add(new Object[]{"1234567890", "1234567890123456789012345678901234567890123456789012345678901!aA", true});// 64 long all types
        params.add(new Object[]{"1234567890", "aa345678", false});// lower number
        params.add(new Object[]{"1234567890", "AA345678", false});// upper number
        params.add(new Object[]{"1234567890", "!!345678", false});// symbol number
        params.add(new Object[]{"1234567890", "!SSSSSSS", false});// symbol upper
        params.add(new Object[]{"1234567890", "!aaaaaaa", false});// symbol lower
        params.add(new Object[]{"1234567890", "aAbbbbbb", false});// lower upper
        params.add(new Object[]{"1234567890", "1234aB!", false});// all types, too short (length 7)
        params.add(new Object[]{"1234567890", "1234567890123456789012345678901234567890123456789012345678901!aAA", false});// 65 long (too long) all types
        params.add(new Object[]{"1234567890", "", false});// empty password
        params.add(new Object[]{"1234567890", null, false});// null password

        // invalid username 11 long
        params.add(new Object[]{"12345678901", "aA3!5678", false}); // 8 long all types
        params.add(new Object[]{"12345678901", "aA345678", false}); // 8 long only missing symbol
        params.add(new Object[]{"12345678901", "a!345678", false}); // 8 long only missing upper
        params.add(new Object[]{"12345678901", "!A345678", false}); // 8 long only missing lower
        params.add(new Object[]{"12345678901", "aA!!!!!!", false}); // only missing letter
        params.add(new Object[]{"12345678901", "1234567890123456789012345678901234567890123456789012345678901!aA", false});// 64 long all types
        params.add(new Object[]{"12345678901", "aa345678", false});// lower number
        params.add(new Object[]{"12345678901", "AA345678", false});// upper number
        params.add(new Object[]{"12345678901", "!!345678", false});// symbol number
        params.add(new Object[]{"12345678901", "!SSSSSSS", false});// symbol upper
        params.add(new Object[]{"12345678901", "!aaaaaaa", false});// symbol lower
        params.add(new Object[]{"12345678901", "aAbbbbbb", false});// lower upper
        params.add(new Object[]{"12345678901", "1234aB!", false});// all types, too short (length 7)
        params.add(new Object[]{"12345678901", "1234567890123456789012345678901234567890123456789012345678901!aAA", false});// 65 long (too long) all types
        params.add(new Object[]{"12345678901", "", false});// empty password
        params.add(new Object[]{"12345678901", null, false});// null password

        // invalid username 2 long
        params.add(new Object[]{"al", "aA3!5678", false}); // 8 long all types
        params.add(new Object[]{"al", "aA345678", false}); // 8 long only missing symbol
        params.add(new Object[]{"al", "a!345678", false}); // 8 long only missing upper
        params.add(new Object[]{"al", "!A345678", false}); // 8 long only missing lower
        params.add(new Object[]{"al", "aA!!!!!!", false}); // only missing letter
        params.add(new Object[]{"al", "1234567890123456789012345678901234567890123456789012345678901!aA", false});// 64 long all types
        params.add(new Object[]{"al", "aa345678", false});// lower number
        params.add(new Object[]{"al", "AA345678", false});// upper number
        params.add(new Object[]{"al", "!!345678", false});// symbol number
        params.add(new Object[]{"al", "!SSSSSSS", false});// symbol upper
        params.add(new Object[]{"al", "!aaaaaaa", false});// symbol lower
        params.add(new Object[]{"al", "aAbbbbbb", false});// lower upper
        params.add(new Object[]{"al", "1234aB!", false});// all types, too short (length 7)
        params.add(new Object[]{"al", "1234567890123456789012345678901234567890123456789012345678901!aAA", false});// 65 long (too long) all types
        params.add(new Object[]{"al", "", false});// empty password
        params.add(new Object[]{"al", null, false});// null password

        // invalid username empty
        params.add(new Object[]{"", "aA3!5678", false}); // 8 long all types
        params.add(new Object[]{"", "aA345678", false}); // 8 long only missing symbol
        params.add(new Object[]{"", "a!345678", false}); // 8 long only missing upper
        params.add(new Object[]{"", "!A345678", false}); // 8 long only missing lower
        params.add(new Object[]{"", "aA!!!!!!", false}); // only missing letter
        params.add(new Object[]{"", "1234567890123456789012345678901234567890123456789012345678901!aA", false});// 64 long all types
        params.add(new Object[]{"", "aa345678", false});// lower number
        params.add(new Object[]{"", "AA345678", false});// upper number
        params.add(new Object[]{"", "!!345678", false});// symbol number
        params.add(new Object[]{"", "!SSSSSSS", false});// symbol upper
        params.add(new Object[]{"", "!aaaaaaa", false});// symbol lower
        params.add(new Object[]{"", "aAbbbbbb", false});// lower upper
        params.add(new Object[]{"", "1234aB!", false});// all types, too short (length 7)
        params.add(new Object[]{"", "1234567890123456789012345678901234567890123456789012345678901!aAA", false});// 65 long (too long) all types
        params.add(new Object[]{"", "", false});// empty password
        params.add(new Object[]{"", null, false});// null password


        // invalid username null
        params.add(new Object[]{null, "aA3!5678", false}); // 8 long all types
        params.add(new Object[]{null, "aA345678", false}); // 8 long only missing symbol
        params.add(new Object[]{null, "a!345678", false}); // 8 long only missing upper
        params.add(new Object[]{null, "!A345678", false}); // 8 long only missing lower
        params.add(new Object[]{null, "aA!!!!!!", false}); // only missing letter
        params.add(new Object[]{null, "1234567890123456789012345678901234567890123456789012345678901!aA", false});// 64 long all types
        params.add(new Object[]{null, "aa345678", false});// lower number
        params.add(new Object[]{null, "AA345678", false});// upper number
        params.add(new Object[]{null, "!!345678", false});// symbol number
        params.add(new Object[]{null, "!SSSSSSS", false});// symbol upper
        params.add(new Object[]{null, "!aaaaaaa", false});// symbol lower
        params.add(new Object[]{null, "aAbbbbbb", false});// lower upper
        params.add(new Object[]{null, "1234aB!", false});// all types, too short (length 7)
        params.add(new Object[]{null, "1234567890123456789012345678901234567890123456789012345678901!aAA", false});// 65 long (too long) all types
        params.add(new Object[]{null, "", false});// empty password
        params.add(new Object[]{null, null, false});// null password


        return params;
    }


    @Test
    public void changePassword() throws IOException, JSONException {
        Assume.assumeTrue(username == "tim");
        when(mockJSONAccount.registerAccount(username, password)).thenReturn(null);
        when(mockDataSender.send(null)).thenReturn(mockJSONObject);
        when(mockJSONObject.get("status")).thenReturn("success");
        Assert.assertSame(expectedResult, accountUnderTest.registerAccount(username, password));
    }

    @Test
    public void registerAccount() throws IOException, JSONException {
        when(mockJSONAccount.registerAccount(username, password)).thenReturn(null);
        when(mockDataSender.send(null)).thenReturn(mockJSONObject);
        when(mockJSONObject.get("status")).thenReturn("success");
        Assert.assertSame(expectedResult, accountUnderTest.registerAccount(username, password));
    }

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        accountUnderTest = new Account(mockJSONAccount, mockDataSender);
    }
}