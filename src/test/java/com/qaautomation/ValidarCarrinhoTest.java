package com.qaautomation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@DisplayName("Validar carrinho")
public class ValidarCarrinhoTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void realizarLogin() {
        driver.get("https://automationpratice.com.br/login");

        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user")));
        email.clear();
        email.sendKeys("teste@teste.com");

        WebElement senha = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        senha.clear();
        senha.sendKeys("12345678");

        WebElement botaoLogin = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnLogin")));
        botaoLogin.click();

        wait.until(ExpectedConditions.urlContains("/my-account"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Login realizado') or contains(text(),'Olá, teste@teste.com')]")));

        WebElement botaoOk = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='OK']")));
        botaoOk.click();
    }

    private void abrirShopListView() {
        driver.get("https://automationpratice.com.br/shopList");
        wait.until(ExpectedConditions.urlContains("/shopList"));
    }

    private void fecharPopupSucesso() {
        By popupOk = By.xpath("//button[normalize-space()='OK' or normalize-space()='Continue Shopping' or normalize-space()='VIEW CART']");
        By popup = By.cssSelector(".swal2-popup");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(popup));
            WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(popupOk));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", botao);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botao);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(popup));
        } catch (Exception e) {
            // Popup pode não aparecer em alguns cenários; nesse caso, o fluxo continua.
        }
    }

    private int contarProdutosNoCarrinho() {
        driver.get("https://automationpratice.com.br/cart");
        wait.until(ExpectedConditions.urlContains("/cart"));

        By produtosCarrinho = By.xpath("//a[contains(@href, '/product-details-one/')] ");
        return driver.findElements(produtosCarrinho).size();
    }

    private void adicionarPrimeirosProdutos(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            By botoesAddToCart = By.xpath("//a[normalize-space()='Add to cart']");
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(botoesAddToCart, i));

            List<WebElement> produtos = driver.findElements(botoesAddToCart);
            WebElement botao = produtos.get(i);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", botao);
            wait.until(ExpectedConditions.elementToBeClickable(botao));
            botao.click();
            fecharPopupSucesso();
        }
    }

    @Test
    @DisplayName("deve adicionar os dois primeiros produtos do shop list view ao carrinho")
    void deveAdicionarOsDoisPrimeirosProdutosAoCarrinho() {
        driver.get("https://automationpratice.com.br/");
        realizarLogin();
        abrirShopListView();

        int quantidadeInicial = contarProdutosNoCarrinho();
        adicionarPrimeirosProdutos(2);
        int quantidadeFinal = contarProdutosNoCarrinho();

        assertTrue(quantidadeFinal >= quantidadeInicial + 2,
                "Produtos adicionados. Inicial=" + quantidadeInicial + ", Final=" + quantidadeFinal);
    }
}

