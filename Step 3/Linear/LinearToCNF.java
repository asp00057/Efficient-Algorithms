import java.io.*;
import java.util.*;

// Method 1: converts a linear-CNF grammar to ordinary CNF (A -> X_a B / B X_a,
// with X_a -> a), output in Grammar.java's format.
// Usage: java LinearToCNF < linear_grammar.txt > cnf_grammar.txt
public class LinearToCNF {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                if (!lines.isEmpty()) break;
                continue;
            }
            lines.add(trimmed);
        }

        LinearGrammar lg = new LinearGrammar();
        lg.parseFromLines(lines);

        Map<Character, String> terminalNonTerminal = new LinkedHashMap<>();
        List<String> output = new ArrayList<>();

        for (int A = 0; A < lg.getNumNonTerminals(); A++) {
            String aName = lg.getSymbol(A);

            for (Object[] rule : lg.getLeftRules(A)) {
                char a = (Character) rule[0];
                int B = (Integer) rule[1];
                String xa = terminalNonTerminal.computeIfAbsent(a,
                        c -> "X_" + c);
                output.add(aName + " -> " + xa + " " + lg.getSymbol(B));
            }
            for (Object[] rule : lg.getRightRules(A)) {
                int B = (Integer) rule[0];
                char a = (Character) rule[1];
                String xa = terminalNonTerminal.computeIfAbsent(a,
                        c -> "X_" + c);
                output.add(aName + " -> " + lg.getSymbol(B) + " " + xa);
            }
        }

        // Plain terminal rules A -> a are copied over unchanged.
        for (String raw : lines) {
            String[] tokens = raw.replace("->", " ").trim().split("\\s+");
            if (tokens.length == 2) {
                output.add(tokens[0] + " -> " + tokens[1]);
            }
        }

        for (Map.Entry<Character, String> e : terminalNonTerminal.entrySet()) {
            output.add(e.getValue() + " -> " + e.getKey());
        }

        for (String l : output) {
            System.out.println(l);
        }
    }
}
