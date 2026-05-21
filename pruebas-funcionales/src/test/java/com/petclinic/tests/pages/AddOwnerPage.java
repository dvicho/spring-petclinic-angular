package com.petclinic.tests.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.io.*;
import java.nio.file.*;

/**
 * PAGE OBJECT — Formulario "Add Owner" de Pet-Clinic Angular
 *
 * Esta clase conoce CÓMO interactuar con la pantalla.
 * Los tests solo deciden si el resultado es correcto o no.
 *
 * URL del formulario: http://localhost:4200/petclinic/owners/add
 */
public class AddOwnerPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Selectores CSS de cada campo del formulario ──────────────────────────
    // Ajusta estos selectores si los nombres de los campos en tu HTML son distintos.
    // Para verlos: abre Chrome, pulsa F12, inspecciona el campo y copia el atributo
    // "name" o "id" que tenga.

    private final By campoFirstName  = By.cssSelector("input[name='firstName']");
    private final By campoLastName   = By.cssSelector("input[name='lastName']");
    private final By campoAddress    = By.cssSelector("input[name='address']");
    private final By campoCity       = By.cssSelector("input[name='city']");
    private final By campoTelephone  = By.cssSelector("input[name='telephone']");
    private final By botonGuardar    = By.cssSelector("button[type='submit']");
    private final By mensajeExito    = By.cssSelector(".alert-success, .alert.alert-success");
    private final By mensajeError    = By.cssSelector(".alert-danger, .help-block");

    // ── Constructor ───────────────────────────────────────────────────────────
    public AddOwnerPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Acciones ──────────────────────────────────────────────────────────────

    /** Navega al formulario de alta de propietario */
    public void navegarAlFormulario() {
        driver.get("http://localhost:4200/petclinic/owners/add");
        // Espera a que el botón de guardar esté visible antes de continuar
        wait.until(ExpectedConditions.visibilityOfElementLocated(botonGuardar));
    }

    public void escribirNombre(String nombre) {
        WebElement campo = driver.findElement(campoFirstName);
        campo.clear();
        campo.sendKeys(nombre);
    }

    public void escribirApellido(String apellido) {
        WebElement campo = driver.findElement(campoLastName);
        campo.clear();
        campo.sendKeys(apellido);
    }

    public void escribirDireccion(String direccion) {
        WebElement campo = driver.findElement(campoAddress);
        campo.clear();
        campo.sendKeys(direccion);
    }

    public void escribirCiudad(String ciudad) {
        WebElement campo = driver.findElement(campoCity);
        campo.clear();
        campo.sendKeys(ciudad);
    }

    public void escribirTelefono(String telefono) {
        WebElement campo = driver.findElement(campoTelephone);
        campo.clear();
        campo.sendKeys(telefono);
    }

    public void clickGuardar() {
        driver.findElement(botonGuardar).click();
    }

    /** Pulsa TAB en un campo para que Angular active la validación */
    public void activarValidacionCampo(By selector) {
        driver.findElement(selector).sendKeys(Keys.TAB);
    }

    // ── Consultas (lo que los tests comprueban) ───────────────────────────────

    /** Devuelve true si el campo tiene la clase ng-invalid (Angular lo marca en rojo) */
    public boolean esCampoInvalido(By selector) {
        String clases = driver.findElement(selector).getAttribute("class");
        return clases.contains("ng-invalid") && clases.contains("ng-touched");
    }

    /** Espera y devuelve el texto del mensaje de éxito */
    public String getMensajeExito() {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(mensajeExito)
        ).getText();
    }

    /** Devuelve el texto del primer mensaje de error visible */
    public String getMensajeError() {
        try {
            return driver.findElement(mensajeError).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    /** Devuelve la URL actual del navegador */
    public String getUrlActual() {
        return driver.getCurrentUrl();
    }

    // ── Utilidad: captura de pantalla ─────────────────────────────────────────

    /**
     * Guarda una captura de pantalla en target/screenshots/<nombre>.png
     * Llama a este método en cada test para tener evidencias.
     */
    public void captura(String nombre) {
        try {
            File src  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dest = Paths.get("target/screenshots/" + nombre + ".png");
            Files.createDirectories(dest.getParent());
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Captura guardada: " + dest.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("No se pudo guardar la captura: " + e.getMessage());
        }
    }
}