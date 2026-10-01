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

        Grammar grammar = new Grammar();
        try {
            grammar.parseFromLines(lines);
        } catch (Exception e) {
            System.err.println("Error parsing grammar: " + e.getMessage());
            return;
        }

        CYKNaive parser;

        try {
            parser = new CYKNaive(grammar);
        } catch (Exception e) {
            System.err.println("Error initializing parser: " + e.getMessage());
            return;
        }

        try {
            while ((line = reader.readLine()) != null) {
                boolean result = parser.parse(line);
                //long ops = parser.getOpCounter();
                try {
                    System.out.println((result ? "yes" : "no") /*+ " (ops " + ops + ")"*/);
                } catch (Throwable t) {
                    System.out.println("no");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input strings: " + e.getMessage());
        }
    }
}