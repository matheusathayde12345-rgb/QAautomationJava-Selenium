package com.qaautomation;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Cadastro de usuário")
public class CadastroUsuarioTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get("https://automationpratice.com.br/register");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("input"), 2));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void preencherCadastro(String nome, String email, String senha) {
        WebElement campoNome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']")));
        campoNome.clear();
        campoNome.sendKeys(nome);

        WebElement campoEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
        campoEmail.clear();
        campoEmail.sendKeys(email);

        WebElement campoSenha = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='password']")));
        campoSenha.clear();
        campoSenha.sendKeys(senha);
    }

    private void clicarCadastrar() {
        By botaoCadastro = By.xpath("//button[normalize-space()='CADASTRAR' or normalize-space()='Cadastrar']");
        WebElement botao = wait.until(ExpectedConditions.presenceOfElementLocated(botaoCadastro));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", botao);
        wait.until(ExpectedConditions.elementToBeClickable(botao));

        try {
            botao.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botao);
        }
    }

    private void validarQueNaoFoiCadastrado() {
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }

    private void validarQueFoiCadastrado() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/my-account"),
                ExpectedConditions.urlContains("/login")
        ));
        assertTrue(driver.getPageSource().contains("Cadastro") || driver.getPageSource().contains("Login"));
    }

    @Test
    @DisplayName("cadastro com sucesso")
    void cadastroComSucesso() {
        preencherCadastro("Joao Silva", "joao@email.com", "12345678");
        clicarCadastrar();
        validarQueFoiCadastrado();
    }

    @Test
    @DisplayName("cadastro com nome vazio")
    void cadastroComNomeVazio() {
        preencherCadastro("", "joao@email.com", "12345678");
        clicarCadastrar();
        validarQueNaoFoiCadastrado();
    }

    @Test
    @DisplayName("cadastro com email vazio")
    void cadastroComEmailVazio() {
        preencherCadastro("Joao Silva", "", "12345678");
        clicarCadastrar();
        validarQueNaoFoiCadastrado();
    }

    @Test
    @DisplayName("cadastro com email já cadastrado")
    void cadastroComEmailJaCadastrado() {
        preencherCadastro("Joao Silva", "teste@teste.com", "12345678");
        clicarCadastrar();
        validarQueNaoFoiCadastrado();
    }

    @Test
    @DisplayName("cadastro com email em maiúsculas")
    void cadastroComEmailEmMaiusculas() {
        preencherCadastro("Joao Silva", "JOAO@EMAIL.COM", "12345678");
        clicarCadastrar();
        validarQueFoiCadastrado();
    }

    @Test
    @DisplayName("cadastro com senha vazia")
    void cadastroComSenhaVazia() {
        preencherCadastro("Joao Silva", "joao@email.com", "");
        clicarCadastrar();
        validarQueNaoFoiCadastrado();
    }

    @Test
    @DisplayName("cadastro com campos vazios")
    void cadastroComCamposVazios() {
        preencherCadastro("", "", "");
        clicarCadastrar();
        validarQueNaoFoiCadastrado();
    }

}
