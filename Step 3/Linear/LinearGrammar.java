import java.util.*;

/**
 * Represents a linear context-free grammar in linear Chomsky normal form:
 * every right-hand side is either a single terminal (A -> a), a terminal
 * followed by a nonterminal (A -> a B), or a nonterminal followed by a
 * terminal (A -> B a).
 *
 * Input format: one rule per line, "LHS -> s1 s2", same convention as
 * Grammar.java (symbols separated by whitespace, terminals optionally
 * quoted). A symbol is treated as a nonterminal if it appears as the
 * left-hand side of some rule; otherwise it is treated as a terminal.
 */
public class LinearGrammar {

    private final Map<String, Integer> symbolToId = new HashMap<>();
    private final List<String> idToSymbol = new ArrayList<>();

    // A -> a
    private final List<Set<Character>> termRules = new ArrayList<>();
    // A -> a B   (terminal on the left, nonterminal B on the right)
    private final List<List<Object[]>> leftRules = new ArrayList<>();
    // A -> B a   (nonterminal B on the left, terminal on the right)
    private final List<List<Object[]>> rightRules = new ArrayList<>();

    private final Set<Integer> nullable = new HashSet<>();
    private int firstLhsId = -1;

    public int getStartId() {
        return firstLhsId != -1 ? firstLhsId : 0;
    }

    public int getNumNonTerminals() {
        return idToSymbol.size();
    }

    private int getOrRegisterSymbol(String symbol) {
        if (!symbolToId.containsKey(symbol)) {
            int id = idToSymbol.size();
            symbolToId.put(symbol, id);
            idToSymbol.add(symbol);
            termRules.add(new HashSet<>());
            leftRules.add(new ArrayList<>());
            rightRules.add(new ArrayList<>());
        }
        return symbolToId.get(symbol);
    }

    private static String stripQuotes(String tok) {
        if (tok.length() >= 2 &&
                ((tok.startsWith("\"") && tok.endsWith("\"")) ||
                        (tok.startsWith("'") && tok.endsWith("'")))) {
            return tok.substring(1, tok.length() - 1);
        }
        return tok;
    }

    public void parseFromLines(List<String> lines) {
        List<String[]> parsed = new ArrayList<>();
        Set<String> nonTerminalSymbols = new HashSet<>();

        // find every symbol that appears as a left-hand side
        // those are the nonterminals, everything else is a terminal
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.replace("->", " ").trim().split("\\s+");
            if (tokens.length < 1) continue;
            parsed.add(tokens);
            nonTerminalSymbols.add(tokens[0]);
        }

        // register symbols and classify each rule
        for (String[] tokens : parsed) {
            int lhsId = getOrRegisterSymbol(tokens[0]);
            if (firstLhsId == -1) firstLhsId = lhsId;

            if (tokens.length == 1) {
                nullable.add(lhsId);

            } else if (tokens.length == 2) {
                // A -> a  (single terminal)
                String rhs = stripQuotes(tokens[1]);
                if (!rhs.isEmpty()) {
                    termRules.get(lhsId).add(rhs.charAt(0));
                }

            } else if (tokens.length == 3) {
                String t1 = tokens[1], t2 = tokens[2];
                boolean t1NT = nonTerminalSymbols.contains(t1);
                boolean t2NT = nonTerminalSymbols.contains(t2);

                if (!t1NT && t2NT) {
                    // A -> a B
                    char a = stripQuotes(t1).charAt(0);
                    int bId = getOrRegisterSymbol(t2);
                    leftRules.get(lhsId).add(new Object[]{a, bId});
                } else if (t1NT && !t2NT) {
                    // A -> B a
                    int bId = getOrRegisterSymbol(t1);
                    char a = stripQuotes(t2).charAt(0);
                    rightRules.get(lhsId).add(new Object[]{bId, a});
                } else {
                    throw new IllegalArgumentException(
                            "Not a linear CNF rule (needs exactly one terminal "
                                    + "and one nonterminal on the right-hand side): "
                                    + String.join(" ", tokens));
                }
            }
        }
    }

    public boolean isTerminalRule(int lhsId, char terminal) {
        if (lhsId < 0 || lhsId >= termRules.size()) return false;
        return termRules.get(lhsId).contains(terminal);
    }

    /** Rules A -> a B for this A, as {Character a, Integer bId} pairs. */
    public List<Object[]> getLeftRules(int lhsId) {
        if (lhsId < 0 || lhsId >= leftRules.size()) return Collections.emptyList();
        return leftRules.get(lhsId);
    }

    /** Rules A -> B a for this A, as {Integer bId, Character a} pairs. */
    public List<Object[]> getRightRules(int lhsId) {
        if (lhsId < 0 || lhsId >= rightRules.size()) return Collections.emptyList();
        return rightRules.get(lhsId);
    }

    public boolean isNullable(int lhsId) {
        return nullable.contains(lhsId);
    }

    public String getSymbol(int id) {
        return idToSymbol.get(id);
    }
}
