package com.example.jarchess.cucumber.steps;

import org.junit.Assert;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;


public class ExampleSteps {

    @When("converted to lowercase")
    public void convertedToLowercase() {
        out = systemUnderTest.toLowercase(in);
    }

    // This is just for an example of a system under test
    private class SystemUnderTest {
        String toUppercase(String input) {
//            return ""; //failing version
            return input.toUpperCase(); //passing version
        }

        String toLowercase(String in) {
//            return ""; //failing version
            return in.toLowerCase(); //passing version
        }
    }

    private SystemUnderTest systemUnderTest;

    @Before
    public void setupSystemUnderTest() {
        systemUnderTest = new SystemUnderTest();
    }

    String in, out;

    @Given("\"([^\\\"]*)\" is the original string")
    public void inIsTheOriginalString(String s) {
        in = s;
    }

    @When("converted to uppercase")
    public void convertedToUppercase() {
        out = systemUnderTest.toUppercase(in);
    }

    @Then("the returned result should be \"([^\\\"]*)\"")
    public void theReturnedResultShouldBeOut(String expected) {
        Assert.assertEquals("systemUnderTest.toUpper(\""+in+"\")", expected, out);
    }
}