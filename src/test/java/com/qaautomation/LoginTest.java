package com.qaautomation;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Login")
public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://automationpratice.com.br/login");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void fazerLogin(String email, String senha) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user"))).clear();
        driver.findElement(By.id("user")).sendKeys(email);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(senha);
        driver.findElement(By.id("btnLogin")).click();
    }

    private void assertMensagemErro(String mensagem) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(normalize-space(.), '" + mensagem + "')]")));
    }

    private void assertLoginRealizado() {
        wait.until(ExpectedConditions.urlContains("/my-account"));
        assertTrue(driver.getPageSource().contains("Login realizado") ||
                driver.getPageSource().contains("Olá, teste@teste.com"));
    }

    @Test
    @DisplayName("login com sucesso")
    void loginComSucesso() {
        fazerLogin("teste@teste.com", "12345678");
        assertLoginRealizado();
    }

    @Test
    @DisplayName("login com email vazio")
    void loginComEmailVazio() {
        fazerLogin("", "12345678");
        assertMensagemErro("E-mail inválido.");
    }

    @Test
    @DisplayName("login com senha vazia")
    void loginComSenhaVazia() {
        fazerLogin("teste@teste.com", "");
        assertMensagemErro("Senha inválida.");
    }

    @Test
    @DisplayName("login com email e senha vazios")
    void loginComEmailESenhaVazios() {
        fazerLogin("", "");
        assertMensagemErro("E-mail inválido.");
    }

    @Test
    @DisplayName("login com email inválido")
    void loginComEmailInvalido() {
        fazerLogin("email_invalido", "12345678");
        assertMensagemErro("E-mail inválido.");
    }

    @Test
    @DisplayName("login com senha inválida")
    void loginComSenhaInvalida() {
        fazerLogin("teste@teste.com", "123");
        assertMensagemErro("Senha inválida.");
    }

    @Test
    @DisplayName("login com email válido e senha válida")
    void loginComEmailValidoESenhaValida() {
        fazerLogin("teste@teste.com", "12345678");
        assertLoginRealizado();
    }

    @Test
    @DisplayName("login com email válido e senha inválida")
    void loginComEmailValidoESenhaInvalida() {
        fazerLogin("teste@teste.com", "123");
        assertMensagemErro("Senha inválida.");
    }

    @Test
    @DisplayName("login com email inválido e senha válida")
    void loginComEmailInvalidoESenhaValida() {
        fazerLogin("email_invalido", "12345678");
        assertMensagemErro("E-mail inválido.");
    }

    @Test
    @DisplayName("login com email válido e senha vazia")
    void loginComEmailValidoESenhaVazia() {
        fazerLogin("teste@teste.com", "");
        assertMensagemErro("Senha inválida.");
    }

    @Test
    @DisplayName("login com email vazio e senha válida")
    void loginComEmailVazioESenhaValida() {
        fazerLogin("", "12345678");
        assertMensagemErro("E-mail inválido.");
    }

    @Test
    @DisplayName("login com todos os campos preenchidos corretamente")
    void loginComTodosOsCamposPreenchidosCorretamente() {
        fazerLogin("teste@teste.com", "12345678");
        assertLoginRealizado();
    }

}
