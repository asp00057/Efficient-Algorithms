import java.util.*;

public class Grammar {
    private final Map<String, Integer> symbolToId = new HashMap<>();
    private final List<String> idToSymbol = new ArrayList<>();

    // A -> a
    private final List<Set<Character>> termRules = new ArrayList<>();
    // A -> B C
    private final List<List<int[]>> noTermRules = new ArrayList<>();

    private final Set<Integer> nullable = new HashSet<>();
    Set<Integer> rhsNonTerminals = new HashSet<>();
    private int firstLhsId = -1;

    public int getStartId() {
        return firstLhsId != -1 ? firstLhsId : 0;
    }

    public int getOrRegisterSymbol(String symbol) {
        if (!symbolToId.containsKey(symbol)) {
            int id = idToSymbol.size();
            symbolToId.put(symbol, id);
            idToSymbol.add(symbol);
            termRules.add(new HashSet<>());
            noTermRules.add(new ArrayList<>());
        }
        return symbolToId.get(symbol);
    }

    public void parseFromLines(List<String> lines) {
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.replace("->", " ").trim().split("\\s+");
            if (tokens.length < 1) continue;

            int lhsId = getOrRegisterSymbol(tokens[0]);
            if (firstLhsId == -1) {
                firstLhsId = lhsId;
            }
            if (tokens.length == 1) {
                nullable.add(lhsId);
            }else if (tokens.length == 2) {
                    String rhs = tokens[1];
                    if ((rhs.startsWith("\"") && rhs.endsWith("\"")) || (rhs.startsWith("'") && rhs.endsWith("'"))) {
                        if (rhs.length() >= 2) {
                            rhs = rhs.substring(1, rhs.length() - 1);
                        }
                    }
                    if (!rhs.isEmpty()) {
                        char terminal = rhs.charAt(0);
                        termRules.get(lhsId).add(terminal);
                    }
                } else if (tokens.length == 3) {
                    int bId = getOrRegisterSymbol(tokens[1]);
                    int cId = getOrRegisterSymbol(tokens[2]);
                    rhsNonTerminals.add(bId);
                    rhsNonTerminals.add(cId);
                    noTermRules.get(lhsId).add(new int[]{bId, cId});
                }
        }
    }

    public int getNumNonTerminals() {
        return idToSymbol.size();
    }

    public boolean isTerminalRule(int lhsId, char terminal) {
        if (lhsId < 0 || lhsId >= termRules.size()) return false;
        return termRules.get(lhsId).contains(terminal);
    }

    public List<int[]> getNoTerminalRules(int lhsId) {
        if (lhsId < 0 || lhsId >= noTermRules.size()) return Collections.emptyList();
        return noTermRules.get(lhsId);
    }
    public boolean isNullable(int lhsId) {
        return nullable.contains(lhsId);
    }
}