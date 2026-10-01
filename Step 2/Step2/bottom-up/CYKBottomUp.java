public class CYKBottomUp {

    private final Grammar grammar;
    private long opCounter;
    private boolean[][][] table;

    public CYKBottomUp(Grammar grammar){
        this.grammar = grammar;
        this.opCounter = 0;
    }

    public long getOpCounter(){
        return opCounter;
    }


    public boolean parse(String string) {
        if (string == null) {
            return false;
        }
        opCounter = 0;
        int startID = grammar.getStartId();
        if (startID < 0 || startID >= grammar.getNumNonTerminals()){
            return false;
        }
        if (string.isEmpty()){
            return grammar.isNullable(startID);
        }
        int n = string.length();
        table = new boolean[grammar.getNumNonTerminals()][n+1][n+1];

        //fill the table for lenght 1
        for(int i=0; i<n; i++){
            char c = string.charAt(i);
            for (int A=0; A< grammar.getNumNonTerminals(); A++){
                table[A][i][i+1] = grammar.isTerminalRule(A,c);
            }
        }
        //length 2 or more
        for(int len = 2; len<=n;len++){
            for(int i=0; i+len <=n;i++){
                int j = i+len;
                for(int A = 0; A < grammar.getNumNonTerminals(); A++){
                    boolean find = false;
                    for(int[] rule : grammar.getNoTerminalRules(A)){
                        int B = rule[0];
                        int C = rule[1];
                        for(int k = i+1; k<j;k++) {
                            opCounter++;
                            if (table[B][i][k] && table[C][k][j]) {
                                find = true;
                                break;
                            }
                        }
                        if (find) break;
                    }
                    table[A][i][j] = find;
                }
            }
        }
        return table[startID][0][n];
    }


}