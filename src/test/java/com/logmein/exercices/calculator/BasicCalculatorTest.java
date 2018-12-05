package com.logmein.exercices.calculator;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class BasicCalculatorTest {

    private static WebDriver webDriver;


    @BeforeClass
    public static void prepareEnvironment() {
        String browser = System.getProperty("browser");
        if("chrome".equalsIgnoreCase(browser)){
            ChromeDriverManager.getInstance().version("2.53").setup();
            webDriver = new ChromeDriver();
        }
        if("firefox".equalsIgnoreCase(browser)){
            FirefoxDriverManager.getInstance().setup();
            webDriver = new FirefoxDriver();
        }

        webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        webDriver.get("http://output.jsbin.com/hudape/1/");
    }

    @Before
    public void resetPage(){
        resetExpressionField();
    }

    @Test
    public void anExpressionContainingOnlyAnOperatorShouldReturnAnError()  {
        evaluateExpression("-");
        assertEquals("ERR", getResult());
        deleteCharacter();
        evaluateExpression("+");
        assertEquals("ERR", getResult());
        deleteCharacter();
        evaluateExpression("×");
        assertEquals("ERR", getResult());
        deleteCharacter();
        evaluateExpression("/");
        assertEquals("ERR", getResult());
    }

    @Test
    public void anExpressionContainingOnlyAnOperandShouldReturnTheSameValue()  {
        evaluateExpression("1");
        assertEquals("1", getResult());
    }

    @Test
    public void aNegationAndOnlyOneNegation()  {
        evaluateExpression("-1");
        assertEquals("-1", getResult());
    }

    @Test
    public void aNegationInAnExpression()  {
    }

    @Test
    public void operatorsPriorityShouldBeRespected(){

        evaluateExpression("1+2×3");
        assertEquals("7", getResult());
        resetExpressionField();

        evaluateExpression("1+4/2");
        assertEquals("3", getResult());
        resetExpressionField();

        //Evaluate power
        evaluateExpression("2×5××2"); //Power before multiplication : Should give 2 * 25 (50) and not 10 ^2 (100)
        assertEquals("50", getResult());
        resetExpressionField();

        //Negation before anything else
        evaluateExpression("1--1");
        assertEquals("2", getResult());
    }

    @Test
    public void divisionByZeroShouldBeWrong(){
        //assuming it is not a 0+
        evaluateExpression("4/0");
        assertEquals("ERR", getResult());
    }

    @Test
    public void malformedExpressionsShouldBeWrong(){
        evaluateExpression("×4+5");
        assertEquals("ERR", getResult());
        resetExpressionField();

        evaluateExpression("4×××5");
        assertEquals("ERR", getResult());
        resetExpressionField();

        evaluateExpression("4++5");
        assertEquals("ERR", getResult());
        resetExpressionField();

        evaluateExpression("4-5/");
        assertEquals("ERR", getResult());
        resetExpressionField();

        evaluateExpression("4/////5");
        assertEquals("ERR", getResult());

    }

    @AfterClass
    public static void closeWebDriver(){
        webDriver.quit();
    }


    public void evaluateExpression(String expression){
        for (int i = 0; i < expression.length(); i++){
            char c = expression.charAt(i);

            if(Character.isDigit(c)){
                clickOnWebElement("numeric", c);
            }
            else if(characterIsOperator(c)){
                clickOnWebElement("operation", c);
            }
        }

        clickOnWebElement("operation", '=');
    }

    public boolean characterIsOperator(char c){
        return (('+' == c) || ('-' == c) || ('×' == c) || ('/' == c));
    }

    public void deleteCharacter(){
        webDriver.findElement(By.xpath("//*[@value='DEL']")).click();
    }

    public String getResult(){
        return webDriver.findElement(By.id("output")).getText();
    }

    public void resetExpressionField() {
        int lengthOfExpression = webDriver.findElement(By.id("expression")).getAttribute("value").length();
        while (lengthOfExpression != 0){
            deleteCharacter();
            lengthOfExpression --;
        }
    }

    public void clickOnWebElement(String className, char value){
        WebElement button = findWebElementByClassAndValue(className, value);
        button.click();
    }

    public WebElement findWebElementByClassAndValue(String className, char value){

        try{
           return webDriver.findElement(By.xpath("//*[@value='"+value+"' and @class='"+className+"']"));
        }
        catch(NoSuchElementException e){
            throw new NoSuchElementException("Invalid identifier or not existant element");
        }
    }
}