import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    if (!lines.isEmpty()) {
                        break;
                    }
                    continue;
                }
                lines.add(trimmed);
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            return;
        }

        LinearGrammar grammar = new LinearGrammar();
        try {
            grammar.parseFromLines(lines);
        } catch (Exception e) {
            System.err.println("Error parsing grammar: " + e.getMessage());
            return;
        }

        CYKLinearBottomUp parser;
        try {
            parser = new CYKLinearBottomUp(grammar);
        } catch (Exception e) {
            System.err.println("Error initializing parser: " + e.getMessage());
            return;
        }

        try {
            while ((line = reader.readLine()) != null) {
                boolean result = parser.parse(line);
                try {
                    System.out.println(result ? "yes" : "no");
                } catch (Throwable t) {
                    System.out.println("no");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input strings: " + e.getMessage());
        }
    }
}
