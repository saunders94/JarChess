Feature: String Conversion using system under test

  Scenario Outline: Convert to uppercase
    Given <in> is the original string
    When converted to uppercase
    Then the returned result is <out>

    Examples:

      | in    | out   |
      | "a"   | "A"   |
      | "A1"  | "A1"  |
      | "aB2" | "AB2" |
      | ""    | ""    |

  Scenario Outline: Convert to lowercase
    #noinspection CucumberUndefinedStep
    Given <in> is the original string
    When converted to lowercase
    #noinspection CucumberUndefinedStep
    Then the returned result is <out>

    Examples:

      | in    | out   |
      | "a"   | "a"   |
      | "A1"  | "a1"  |
      | "aB2" | "ab2" |
      | ""    | ""    |