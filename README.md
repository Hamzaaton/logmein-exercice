# logmein-exercice
This project was created as part of a test for LogMeIn - Jive.

## Running your tests
To start testing simply run:

    mvn clean test -Dbrowser=chrome 

Only two types of browsers have been configured chrome and firefox. The project will automatically download a webdriver, set the environment variable and launch the browser.

A plugin for reprot generation has been used to generate better looking report under /tags/surefire-reports


## Found bugs

I have been able to find only 3 bugs :

1 - Negation priority : If a negation is being used in the middle of an operation, it will not be taken in consideration : 1 * -1 will result in an error and not -1
2 - Division by zero : Assuming we are talking about limits and it is a 0 not a 0+ or 0-, division by zero should not be allowed and should result in an error
3 - Malformed expressions : A expression such as 3 /////// 4 results as 3 and it should be an error.
