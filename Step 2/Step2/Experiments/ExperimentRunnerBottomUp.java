import java.io.*;
import java.util.*;

public class ExperimentRunnerBottomUp {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> grammarLines = new ArrayList<>();
        String line;

        // Same as in Main.java: it reads the grammar until the empty line
        try {
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    if (!grammarLines.isEmpty()) {
                        break;
                    }
                    continue;
                }
                grammarLines.add(trimmed);
            }
        } catch (IOException e) {
            System.err.println("Error reading grammar: " + e.getMessage());
            return;
        }

        // Here we save all the strings at first, because we need to know which
        // one is the longest before starting to measure (for the warm-up).
        List<String> testStrings = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                testStrings.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading test strings: " + e.getMessage());
            return;
        }

        if (testStrings.isEmpty()) {
            System.err.println("No test strings provided.");
            return;
        }

        Grammar grammar = new Grammar();
        grammar.parseFromLines(grammarLines);
        CYKBottomUp parser = new CYKBottomUp(grammar);

        // We run it with the longest string before measuring anything
        // so the JIT is able to optimize the code when we start

        String longest = testStrings.get(0);
        for (String s : testStrings) {
            if (s.length() > longest.length()) {
                longest = s;
            }
        }
        parser.parse(longest);

        //Here we measure, each string 10 times and discarding the first and last time to avoid outliers

        final int REPETITIONS = 10;
        final int DISCARD = 1;

        System.out.println("length\ttime_ns\toperations\tresult");

        for (String s : testStrings) {
            long[] times = new long[REPETITIONS];
            boolean result = false;
            long ops = 0;

            for (int r = 0; r < REPETITIONS; r++) {
                System.gc();
                long start = System.nanoTime();
                result = parser.parse(s);
                long end = System.nanoTime();
                times[r] = end - start;
                ops = parser.getOpCounter();
            }

            Arrays.sort(times);
            long sum = 0;
            int count = 0;
            for (int i = DISCARD; i < REPETITIONS - DISCARD; i++) {
                sum += times[i];
                count++;
            }
            long avgTime = sum / count;

            System.out.println(s.length() + "\t" + avgTime + "\t" + ops + "\t" + (result ? "yes" : "no"));
        }
    }
}