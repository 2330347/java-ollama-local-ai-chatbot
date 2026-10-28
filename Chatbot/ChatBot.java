
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatBot {

    private static final String API_URL =
            "http://localhost:11434/api/chat";

    private static final String MODEL = "llama3.2";

    private static final String SYSTEM_PROMPT =
            "You are a friendly, helpful assistant. Keep answers concise.";

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private final List<String[]> history = new ArrayList<>();

    public String chat(String userMessage) throws Exception {

        history.add(new String[]{"user", userMessage});

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofMinutes(5))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            buildRequestBody()))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                history.remove(history.size() - 1);

                return "[Error " + response.statusCode()
                        + "] " + response.body();
            }

            String reply = extractContent(response.body());

            history.add(new String[]{"assistant", reply});

            return reply;

        } catch (Exception e) {
            history.remove(history.size() - 1);
            throw e;
        }
    }

    public void reset() {
        history.clear();
    }

    private String buildRequestBody() {

        StringBuilder sb = new StringBuilder();

        sb.append("{\"model\":\"")
                .append(MODEL)
                .append("\",");

        sb.append("\"stream\":false,");

        sb.append("\"messages\":[");

        // System prompt
        sb.append("{\"role\":\"system\",\"content\":\"")
                .append(escape(SYSTEM_PROMPT))
                .append("\"}");

        // Conversation history
        for (String[] message : history) {

            sb.append(",");

            sb.append("{\"role\":\"")
                    .append(message[0])
                    .append("\",");

            sb.append("\"content\":\"")
                    .append(escape(message[1]))
                    .append("\"}");
        }

        sb.append("]}");

        return sb.toString();
    }

    private static String escape(String s) {

        StringBuilder sb = new StringBuilder();

        for (char c : s.toCharArray()) {

            switch (c) {

                case '"':
                    sb.append("\\\"");
                    break;

                case '\\':
                    sb.append("\\\\");
                    break;

                case '\n':
                    sb.append("\\n");
                    break;

                case '\r':
                    sb.append("\\r");
                    break;

                case '\t':
                    sb.append("\\t");
                    break;

                default:

                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }

        return sb.toString();
    }

    private static String extractContent(String json) {

        String marker = "\"content\":\"";

        int start = json.indexOf(marker);

        if (start < 0) {
            return "[Could not parse Ollama response] " + json;
        }

        start += marker.length();

        StringBuilder out = new StringBuilder();

        for (int i = start; i < json.length(); i++) {

            char c = json.charAt(i);

            if (c == '"') {
                break;
            }

            if (c == '\\' && i + 1 < json.length()) {

                char n = json.charAt(++i);

                switch (n) {

                    case 'n':
                        out.append('\n');
                        break;

                    case 't':
                        out.append('\t');
                        break;

                    case 'r':
                        out.append('\r');
                        break;

                    case 'b':
                        out.append('\b');
                        break;

                    case 'f':
                        out.append('\f');
                        break;

                    case 'u':

                        if (i + 4 < json.length()) {
                            out.append((char) Integer.parseInt(
                                    json.substring(i + 1, i + 5), 16));

                            i += 4;
                        }

                        break;

                    default:
                        out.append(n);
                }

            } else {
                out.append(c);
            }
        }

        return out.toString();
    }

    public static void main(String[] args) throws Exception {

        ChatBot bot = new ChatBot();

        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("      FREE LOCAL AI CHATBOT");
        System.out.println("================================");

        System.out.println("Model: " + MODEL);
        System.out.println("Powered by Ollama + Llama 3.2");

        System.out.println();
        System.out.println("Type 'exit' to quit.");
        System.out.println("Type 'reset' to clear memory.");
        System.out.println();

        while (true) {

            System.out.print("You: ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (input.equalsIgnoreCase("reset")) {

                bot.reset();

                System.out.println("Bot: Memory cleared.\n");

                continue;
            }

            try {

                System.out.println("\nBot: " + bot.chat(input));

                System.out.println();

            } catch (Exception e) {

                System.out.println(
                        "Bot: [Request failed] " + e.getMessage());

                System.out.println();
            }
        }

        scanner.close();

        System.out.println("Goodbye!");
    }
}
