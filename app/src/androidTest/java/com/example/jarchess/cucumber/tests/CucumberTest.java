package com.example.jarchess.cucumber.tests;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import cucumber.api.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "features",
        glue = "com.example.jarchess.cucumber.steps",
        snippets = SnippetType.CAMELCASE
)
@SuppressWarnings(value = "all")
public class CucumberTest {
    // this class is only used for the annotations above.
    // You don't need to edit this
}
