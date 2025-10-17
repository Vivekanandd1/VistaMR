package Testcases;

import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import io.qameta.allure.testng.AllureTestNg;

@Listeners({AllureTestNg.class})
public class DemoTest {
    @Test
    public void sampleTest() {
        System.out.println("Hello from TestNG + Allure");
    }
}
