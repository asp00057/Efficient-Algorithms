public class CYKTopDown {

    private static final byte UNKNOWN = 0;
    private static final byte TRUE = 1;
    private static final byte FALSE = 2;

    private final Grammar grammar;
    private long opCounter;
    private byte[][][] table;

    public CYKTopDown(Grammar grammar){
        this.grammar = grammar;
        this.opCounter = 0;
    }

    public boolean parse(String string){
        if (string == null) {
            return false;
        }
        opCounter = 0;
        int startID = grammar.getStartId();
        if(startID <0 || startID >= grammar.getNumNonTerminals()){
            return false;
        }
        if (string.isEmpty()){
            return grammar.isNullable(startID);
        }
        int n = string.length();
        table = new byte[grammar.getNumNonTerminals()][n+1][n+1];
        return parseRec(startID, 0, string.length(), string);
    }

    public long getOpCounter(){
        return opCounter;
    }

    private boolean parseRec(int A, int i, int j, String s){
        opCounter ++;
        if (table[A][i][j] != UNKNOWN){
            return table[A][i][j] == TRUE;
        }
        //New variable to save result on the table
        boolean result;
        //if length = 1
        if(i==j-1){
            result =  grammar.isTerminalRule(A,s.charAt(i));
        }else{
            //if recursive
            result = false;
            for (int[] rule : grammar.getNoTerminalRules(A)){
                int B = rule[0];
                int C = rule[1];
                for (int k = i+1; k<j; k++){
                    if(parseRec(B, i,k,s) && parseRec(C, k, j, s)){
                        result = true;
                        break;
                    }
                }
                if (result) break;
            }
        }
        table[A][i][j] = result ? TRUE : FALSE;
        return result;
    }
}