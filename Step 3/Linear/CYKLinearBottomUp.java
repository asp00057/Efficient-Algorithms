/**
 * Bottom-up CYK specialized for linear grammars in linear Chomsky normal
 * form (rules A -> a, A -> a B, A -> B a). Because every rule has the
 * terminal glued to one end, the split point between the two symbols of a
 * substring is fixed by that end, not free to range over all of (i, j) as
 * in the general CYK. This removes the O(n) loop over split points, so
 * each table entry costs O(|R|) instead of O(|R| n), and the total time
 * drops from O(|V| |R| n^3) to O(|V| |R| n^2).
 */
public class CYKLinearBottomUp {

    private final LinearGrammar grammar;
    private long opCounter;
    private boolean[][][] table;

    public CYKLinearBottomUp(LinearGrammar grammar) {
        this.grammar = grammar;
        this.opCounter = 0;
    }

    public long getOpCounter() {
        return opCounter;
    }

    public boolean parse(String string) {
        if (string == null) return false;
        opCounter = 0;
        int startID = grammar.getStartId();
        if (startID < 0 || startID >= grammar.getNumNonTerminals()) return false;
        if (string.isEmpty()) return grammar.isNullable(startID);

        int n = string.length();
        table = new boolean[grammar.getNumNonTerminals()][n + 1][n + 1];

        // Length 1: same as the general algorithm.
        for (int i = 0; i < n; i++) {
            char c = string.charAt(i);
            for (int A = 0; A < grammar.getNumNonTerminals(); A++) {
                table[A][i][i + 1] = grammar.isTerminalRule(A, c);
            }
        }

        // Length 2 or more: no loop over split points k. For A -> a B the
        // split is fixed right after position i; for A -> B a it is fixed
        // right before position j.
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len;
                for (int A = 0; A < grammar.getNumNonTerminals(); A++) {
                    boolean find = false;

                    char first = string.charAt(i);
                    for (Object[] rule : grammar.getLeftRules(A)) {
                        opCounter++;
                        char a = (Character) rule[0];
                        int B = (Integer) rule[1];
                        if (a == first && table[B][i + 1][j]) {
                            find = true;
                            break;
                        }
                    }

                    if (!find) {
                        char last = string.charAt(j - 1);
                        for (Object[] rule : grammar.getRightRules(A)) {
                            opCounter++;
                            int B = (Integer) rule[0];
                            char a = (Character) rule[1];
                            if (a == last && table[B][i][j - 1]) {
                                find = true;
                                break;
                            }
                        }
                    }

                    table[A][i][j] = find;
                }
            }
        }
        return table[startID][0][n];
    }
}
