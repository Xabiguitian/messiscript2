package es.udc.paproject.e2etests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AppTest {

    private WebDriver driver;
    private final String baseUrl = "http://localhost:5173";

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login(String userName, String password) {
        driver.get(baseUrl + "/users/login");
        
        driver.findElement(By.id("userName")).sendKeys(userName);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Wait for login to complete and show user dropdown
        WebElement userDropdown = driver.findElement(By.id("user-dropdown"));
        assertTrue(userDropdown.getText().contains(userName));
    }

    @Test
    public void testLogin() {
        login("viewer", "pa2526");
    }

    @Test
    public void testViewSessionDetails() {
        login("viewer", "pa2526");

        // 1. Select tomorrow in DateSelector
        WebElement dateSelector = driver.findElement(By.id("date-selector"));
        dateSelector.click();
        WebElement tomorrowOption = driver.findElement(By.xpath("//option[2]")); // Second option is tomorrow
        tomorrowOption.click();

        // 2. Get first movie title
        WebElement movieLink = driver.findElement(By.className("movie-link"));
        String movieTitle = movieLink.getText();

        // 3. Get first session time
        WebElement sessionLink = driver.findElement(By.className("session-link"));
        String sessionTime = sessionLink.getText();

        // 4. Click session link
        sessionLink.click();

        // 5. Verify details
        WebElement titleInDetails = driver.findElement(By.id("movie-title-link"));
        assertTrue(titleInDetails.getText().equals(movieTitle));

        WebElement timeInDetails = driver.findElement(By.id("session-date-time"));
        assertTrue(timeInDetails.getText().contains(sessionTime));

        // Check other fields are present
        assertTrue(driver.findElement(By.id("room-name")).isDisplayed());
        assertTrue(driver.findElement(By.id("price")).isDisplayed());
        assertTrue(driver.findElement(By.id("empty-seats")).isDisplayed());
        
        // Check buy tickets link is present (as we are logged in as testviewer)
        assertTrue(driver.findElement(By.id("buy-tickets-link")).isDisplayed());
    }

    @Test
    public void testBuyTickets() {
        login("viewer", "pa2526");

        // 1. Acceder a la URL de la información detallada de la sesión (ej. Sesión 4)
        driver.get(baseUrl + "/catalog/sessions/4");

        // 2. Guardar el nombre de la película
        WebElement titleElement = driver.findElement(By.id("movie-title-link"));
        String movieTitle = titleElement.getText();

        // 3. Click buy tickets
        driver.findElement(By.id("buy-tickets-link")).click();

        // 4. Fill form
        WebElement quantityInput = driver.findElement(By.id("quantity"));
        quantityInput.clear();
        quantityInput.sendKeys("2");

        WebElement creditCardInput = driver.findElement(By.id("creditCard"));
        creditCardInput.clear();
        creditCardInput.sendKeys("1234567890123456");

        // 5. Submit form
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Esperamos a que la redirección a '/shopping/purchase-completed' nos confirme que la compra terminó
        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(5))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.urlToBe(baseUrl + "/shopping/purchase-completed"));

        // 6. Obtener ID de compra
        WebElement purchaseIdElement = driver.findElement(By.id("purchase-id"));
        String purchaseId = purchaseIdElement.getText();

        // 7. Navigate to Purchase History
        driver.findElement(By.id("user-dropdown")).click();
        driver.findElement(By.id("purchase-history-link")).click();

        // 8. Verify the first row matches our purchase
        WebElement firstPurchaseRow = driver.findElement(By.className("purchase-row"));
        WebElement firstPurchaseId = firstPurchaseRow.findElement(By.xpath("./td[1]"));
        WebElement firstPurchaseMovie = firstPurchaseRow.findElement(By.className("purchase-movie"));
        WebElement firstPurchaseTickets = firstPurchaseRow.findElement(By.className("purchase-tickets"));

        assertTrue(firstPurchaseId.getText().equals(purchaseId));
        assertTrue(firstPurchaseMovie.getText().equals(movieTitle));
        assertTrue(firstPurchaseTickets.getText().equals("2"));
    }
    @Test
    public void testDeliverTickets() {
        // 1. First, a viewer buys a ticket to have a fresh purchase ID
        login("viewer", "pa2526");
        driver.get(baseUrl + "/catalog/sessions/4"); // Future session
        String movieTitle = driver.findElement(By.id("movie-title-link")).getText();
        driver.findElement(By.id("buy-tickets-link")).click();
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("1");
        driver.findElement(By.id("creditCard")).clear();
        driver.findElement(By.id("creditCard")).sendKeys("9999999999999999");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.urlToBe(baseUrl + "/shopping/purchase-completed"));
        
        String purchaseId = driver.findElement(By.id("purchase-id")).getText().trim();

        // 2. Logout and Login as ticketseller
        driver.findElement(By.id("user-dropdown")).click();
        driver.findElement(By.xpath("//a[contains(@href, '/users/logout')]")).click();
        login("ticketseller", "pa2526");

        // 3. Go to Deliver Tickets and use the new ID
        driver.get(baseUrl + "/shopping/deliver-tickets");
        driver.findElement(By.id("purchaseId")).sendKeys(purchaseId);
        driver.findElement(By.id("creditCard")).sendKeys("9999999999999999");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // 4. Verify success message
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
        assertTrue(driver.findElement(By.className("alert-success")).getText().contains(movieTitle));

        // 5. Try again with same data
        driver.findElement(By.id("purchaseId")).clear();
        driver.findElement(By.id("purchaseId")).sendKeys(purchaseId);
        driver.findElement(By.id("creditCard")).clear();
        driver.findElement(By.id("creditCard")).sendKeys("9999999999999999");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // 6. Verify error message (TicketsAlreadyDeliveredException)
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.className("alert-danger")));
        assertTrue(driver.findElement(By.className("alert-danger")).isDisplayed());
    }

}
