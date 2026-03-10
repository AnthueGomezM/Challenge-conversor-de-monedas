# 💱 Currency Converter — Conversor de Monedas

Aplicación de consola desarrollada en **Java** que permite convertir entre múltiples monedas en tiempo real utilizando la API de [ExchangeRate-API](https://www.exchangerate-api.com/).

---

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Uso](#uso)
- [Monedas Soportadas](#monedas-soportadas)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [API Reference](#api-reference)
- [Capturas de Pantalla](#capturas-de-pantalla)
- [Posibles Mejoras](#posibles-mejoras)

---

## Descripción

**Currency Converter** es un proyecto educativo que demuestra cómo consumir una API REST desde Java puro, sin dependencias externas. El programa consulta tasas de cambio actualizadas y realiza conversiones precisas entre 15 monedas internacionales, todo desde la terminal.

Fue desarrollado como parte del programa académico de la **Universidad Cooperativa de Colombia (UCC)**.

---

## Características

- ✅ Conversión en tiempo real entre 15 monedas
- ✅ Consulta de tasas de cambio actualizadas vía API REST
- ✅ Tabla de todas las tasas de cambio respecto a una moneda base
- ✅ Parseo manual de JSON (sin librerías externas como Gson o Jackson)
- ✅ Interfaz de consola con formato visual usando caracteres de caja Unicode
- ✅ Validación de entradas del usuario (moneda, cantidad)
- ✅ Manejo de errores de conexión y timeouts
- ✅ Cero dependencias externas — solo Java estándar (JDK)

---

## Tecnologías

| Componente       | Tecnología                     |
|------------------|--------------------------------|
| Lenguaje         | Java 8+                        |
| IDE recomendado  | IntelliJ IDEA                  |
| API              | ExchangeRate-API v6 (REST/JSON)|
| HTTP Client      | `java.net.HttpURLConnection`   |
| Build            | Compilación directa con `javac`|

---

## Requisitos Previos

- **Java JDK 8** o superior instalado ([Descargar JDK](https://www.oracle.com/java/technologies/downloads/))
- **IntelliJ IDEA** (Community o Ultimate) — [Descargar](https://www.jetbrains.com/idea/download/)
- Conexión a internet (para consultar la API)

Verifica tu instalación de Java:

```bash
java -version
javac -version
```

---

## Instalación y Ejecución

### Opción 1: IntelliJ IDEA (Recomendado)

1. Abre IntelliJ IDEA
2. Selecciona **File → New → Project**
3. Escoge **Java** como tipo de proyecto y configura el JDK
4. Dale un nombre al proyecto (ej. `CurrencyConverter`)
5. En la carpeta `src/`, crea una nueva clase Java llamada `CurrencyConverter`
6. Copia y pega el contenido del archivo `CurrencyConverter.java`
7. Haz clic derecho sobre el archivo → **Run 'CurrencyConverter.main()'**

### Opción 2: Línea de comandos

```bash
# Clonar o descargar el proyecto
cd CurrencyConverter/src

# Compilar
javac CurrencyConverter.java

# Ejecutar
java CurrencyConverter
```

---

## Uso

Al iniciar la aplicación, se presenta un menú interactivo con 4 opciones:

### 1. Convertir moneda

```
  Moneda de origen (ej: USD, COP, EUR): USD
  Moneda de destino (ej: COP, EUR, USD): COP
  Cantidad a convertir: 100

  ┌──────────────────────────────────────────┐
  │  100.00 USD (Dólar estadounidense)
  │  = 423,850.00 COP (Peso colombiano)
  │
  │  Tasa: 1 USD = 4238.500000 COP
  └──────────────────────────────────────────┘
```

### 2. Ver monedas disponibles

Muestra una tabla con los códigos y nombres de las 15 monedas configuradas.

### 3. Ver tasas de cambio

Muestra todas las tasas de cambio respecto a una moneda base seleccionada.

### 4. Salir

Cierra la aplicación.

---

## Monedas Soportadas

| Código | Moneda                  |
|--------|-------------------------|
| USD    | Dólar estadounidense    |
| EUR    | Euro                    |
| GBP    | Libra esterlina         |
| JPY    | Yen japonés             |
| COP    | Peso colombiano         |
| ARS    | Peso argentino          |
| BRL    | Real brasileño          |
| MXN    | Peso mexicano           |
| CLP    | Peso chileno            |
| PEN    | Sol peruano             |
| CAD    | Dólar canadiense        |
| CHF    | Franco suizo            |
| CNY    | Yuan chino              |
| KRW    | Won surcoreano          |
| BOB    | Boliviano               |

> Para agregar más monedas, edita el bloque `static` de la clase `CurrencyConverter` y añade entradas al `Map<String, String> CURRENCIES`.

---

## Arquitectura del Proyecto

```
CurrencyConverter/
├── src/
│   └── CurrencyConverter.java    # Clase principal (punto de entrada)
└── README.md
```

### Estructura interna de la clase

El programa sigue una arquitectura modular dentro de un solo archivo:

| Método                  | Responsabilidad                                    |
|-------------------------|----------------------------------------------------|
| `main()`                | Bucle principal del menú interactivo                |
| `convertCurrency()`     | Lógica de conversión entre dos monedas              |
| `showAvailableCurrencies()` | Muestra la tabla de monedas disponibles         |
| `showExchangeRates()`   | Consulta y muestra tasas desde una moneda base      |
| `fetchFromApi()`        | Conexión HTTP GET a ExchangeRate-API                |
| `extractRate()`         | Parseo manual del JSON para extraer tasas           |

### Flujo de datos

```
Usuario → Selección de monedas → fetchFromApi() → JSON Response
                                                       ↓
                                              extractRate() → Tasa
                                                       ↓
                                              Cálculo → Resultado en consola
```

---

## API Reference

Este proyecto utiliza la **ExchangeRate-API v6**.

| Elemento     | Detalle                                                                |
|--------------|------------------------------------------------------------------------|
| Base URL     | `https://v6.exchangerate-api.com/v6/{API_KEY}/latest/{CURRENCY}`       |
| Método       | `GET`                                                                  |
| Autenticación| API Key incluida en la URL                                             |
| Formato      | JSON                                                                   |
| Límite (free)| 1,500 solicitudes/mes                                                  |

### Ejemplo de respuesta

```json
{
  "result": "success",
  "base_code": "USD",
  "conversion_rates": {
    "USD": 1,
    "EUR": 0.9245,
    "COP": 4238.50,
    "..."
  }
}
```

> Si necesitas tu propia API Key, regístrate gratis en [exchangerate-api.com](https://www.exchangerate-api.com/).

---

## Capturas de Pantalla

### Menú principal

```
  ╔══════════════════════════════════════════╗
  ║       CONVERSOR DE MONEDAS v1.0         ║
  ║       Exchange Rate API - Java          ║
  ╚══════════════════════════════════════════╝
  ┌──────────────────────────────────────────┐
  │  1. Convertir moneda                    │
  │  2. Ver monedas disponibles             │
  │  3. Ver tasas de cambio de una moneda   │
  │  4. Salir                               │
  └──────────────────────────────────────────┘
  Selecciona una opción: _
```

---

## Posibles Mejoras

- [ ] Agregar historial de conversiones en archivo `.csv`
- [ ] Implementar caché de tasas para reducir llamadas a la API
- [ ] Agregar soporte para todas las monedas de la API (~160)
- [ ] Migrar a interfaz gráfica con JavaFX o Swing
- [ ] Agregar pruebas unitarias con JUnit
- [ ] Implementar patrón MVC para separar responsabilidades
- [ ] Usar Gson o Jackson para parseo robusto de JSON
