import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "1e38e6d222297ec86d39e61e";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    // Monedas disponibles con nombres descriptivos
    private static final Map<String, String> CURRENCIES = new LinkedHashMap<>();

    static {
        CURRENCIES.put("USD", "Dólar estadounidense");
        CURRENCIES.put("EUR", "Euro");
        CURRENCIES.put("GBP", "Libra esterlina");
        CURRENCIES.put("JPY", "Yen japonés");
        CURRENCIES.put("COP", "Peso colombiano");
        CURRENCIES.put("ARS", "Peso argentino");
        CURRENCIES.put("BRL", "Real brasileño");
        CURRENCIES.put("MXN", "Peso mexicano");
        CURRENCIES.put("CLP", "Peso chileno");
        CURRENCIES.put("PEN", "Sol peruano");
        CURRENCIES.put("CAD", "Dólar canadiense");
        CURRENCIES.put("CHF", "Franco suizo");
        CURRENCIES.put("CNY", "Yuan chino");
        CURRENCIES.put("KRW", "Won surcoreano");
        CURRENCIES.put("BOB", "Boliviano");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printWelcome();

        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    convertCurrency(scanner);
                    break;
                case "2":
                    showAvailableCurrencies();
                    break;
                case "3":
                    showExchangeRates(scanner);
                    break;
                case "4":
                    running = false;
                    System.out.println("\n  ¡Gracias por usar el Conversor de Monedas! Hasta pronto.\n");
                    break;
                default:
                    System.out.println("\n  ⚠ Opción no válida. Intenta de nuevo.\n");
            }
        }

        scanner.close();
    }

    // ─── Interfaz de usuario ────────────────────────────────────────

    private static void printWelcome() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║       CONVERSOR DE MONEDAS v1.0         ║");
        System.out.println("  ║       Exchange Rate API - Java          ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    private static void printMenu() {
        System.out.println("  ┌──────────────────────────────────────────┐");
        System.out.println("  │  1. Convertir moneda                    │");
        System.out.println("  │  2. Ver monedas disponibles             │");
        System.out.println("  │  3. Ver tasas de cambio de una moneda   │");
        System.out.println("  │  4. Salir                               │");
        System.out.println("  └──────────────────────────────────────────┘");
        System.out.print("  Selecciona una opción: ");
    }

    // ─── Opción 1: Convertir moneda ────────────────────────────────

    private static void convertCurrency(Scanner scanner) {
        System.out.println("\n  ── CONVERTIR MONEDA ──\n");

        // Moneda origen
        System.out.print("  Moneda de origen (ej: USD, COP, EUR): ");
        String from = scanner.nextLine().trim().toUpperCase();
        if (!CURRENCIES.containsKey(from)) {
            System.out.println("  ⚠ Moneda '" + from + "' no está en la lista. Usa opción 2 para ver las disponibles.\n");
            return;
        }

        // Moneda destino
        System.out.print("  Moneda de destino (ej: COP, EUR, USD): ");
        String to = scanner.nextLine().trim().toUpperCase();
        if (!CURRENCIES.containsKey(to)) {
            System.out.println("  ⚠ Moneda '" + to + "' no está en la lista.\n");
            return;
        }

        // Cantidad
        System.out.print("  Cantidad a convertir: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount <= 0) {
                System.out.println("  ⚠ La cantidad debe ser mayor a 0.\n");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("  ⚠ Cantidad no válida.\n");
            return;
        }

        // Llamar a la API
        System.out.println("\n  Consultando tasa de cambio...");
        String jsonResponse = fetchFromApi(from);

        if (jsonResponse == null) {
            System.out.println("  ✖ Error al conectar con la API. Verifica tu conexión a internet.\n");
            return;
        }

        Double rate = extractRate(jsonResponse, to);
        if (rate == null) {
            System.out.println("  ✖ No se encontró la tasa de cambio para " + to + ".\n");
            return;
        }

        double result = amount * rate;

        // Mostrar resultado
        System.out.println();
        System.out.println("  ┌──────────────────────────────────────────┐");
        System.out.printf("  │  %.2f %s (%s)%n", amount, from, CURRENCIES.get(from));
        System.out.printf("  │  = %.2f %s (%s)%n", result, to, CURRENCIES.get(to));
        System.out.println("  │");
        System.out.printf("  │  Tasa: 1 %s = %.6f %s%n", from, rate, to);
        System.out.println("  └──────────────────────────────────────────┘");
        System.out.println();
    }

    // ─── Opción 2: Mostrar monedas disponibles ─────────────────────

    private static void showAvailableCurrencies() {
        System.out.println("\n  ── MONEDAS DISPONIBLES ──\n");
        System.out.println("  ┌────────┬───────────────────────────────┐");
        System.out.println("  │ Código │ Nombre                        │");
        System.out.println("  ├────────┼───────────────────────────────┤");

        for (Map.Entry<String, String> entry : CURRENCIES.entrySet()) {
            System.out.printf("  │  %-4s  │ %-29s │%n", entry.getKey(), entry.getValue());
        }

        System.out.println("  └────────┴───────────────────────────────┘\n");
    }

    // ─── Opción 3: Ver tasas de cambio ─────────────────────────────

    private static void showExchangeRates(Scanner scanner) {
        System.out.println("\n  ── TASAS DE CAMBIO ──\n");

        System.out.print("  Moneda base (ej: USD): ");
        String base = scanner.nextLine().trim().toUpperCase();

        if (!CURRENCIES.containsKey(base)) {
            System.out.println("  ⚠ Moneda '" + base + "' no está en la lista.\n");
            return;
        }

        System.out.println("\n  Consultando tasas de cambio para " + base + "...");
        String jsonResponse = fetchFromApi(base);

        if (jsonResponse == null) {
            System.out.println("  ✖ Error al conectar con la API.\n");
            return;
        }

        System.out.println();
        System.out.println("  ┌────────┬──────────────────┐");
        System.out.printf("  │  BASE  │  1 %-14s │%n", base);
        System.out.println("  ├────────┼──────────────────┤");

        for (String code : CURRENCIES.keySet()) {
            if (code.equals(base)) continue;
            Double rate = extractRate(jsonResponse, code);
            if (rate != null) {
                System.out.printf("  │  %-4s  │  %14.6f │%n", code, rate);
            }
        }

        System.out.println("  └────────┴──────────────────┘\n");
    }

    // ─── Conexión con la API ────────────────────────────────────────

    private static String fetchFromApi(String baseCurrency) {
        try {
            URL url = new URL(BASE_URL + baseCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("  ✖ La API respondió con código: " + responseCode);
                return null;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            return response.toString();

        } catch (Exception e) {
            System.out.println("  ✖ Error de conexión: " + e.getMessage());
            return null;
        }
    }

    // ─── Parseo manual del JSON (sin librerías externas) ────────────

    /**
     * Extrae la tasa de cambio para una moneda específica del JSON de respuesta.
     * Se hace parseo manual para evitar dependencias externas (Gson, Jackson, etc.)
     */
    private static Double extractRate(String json, String targetCurrency) {
        try {
            // Buscar el bloque "conversion_rates":{...}
            String key = "\"conversion_rates\"";
            int ratesStart = json.indexOf(key);
            if (ratesStart == -1) return null;

            // Buscar la moneda objetivo dentro del bloque
            String searchKey = "\"" + targetCurrency + "\"";
            int keyIndex = json.indexOf(searchKey, ratesStart);
            if (keyIndex == -1) return null;

            // Extraer el valor numérico después de los dos puntos
            int colonIndex = json.indexOf(":", keyIndex);
            if (colonIndex == -1) return null;

            int valueStart = colonIndex + 1;

            // Encontrar el fin del valor (coma o llave de cierre)
            int valueEnd = valueStart;
            while (valueEnd < json.length()) {
                char c = json.charAt(valueEnd);
                if (c == ',' || c == '}') break;
                valueEnd++;
            }

            String valueStr = json.substring(valueStart, valueEnd).trim();
            return Double.parseDouble(valueStr);

        } catch (Exception e) {
            return null;
        }
    }
}