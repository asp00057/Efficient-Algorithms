public class CYKNaive {
    private final Grammar grammar;
    private long opCounter;

    public CYKNaive(Grammar grammar){
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
        return parseRec(startID, 0, string.length(), string);
    }

    public long getOpCounter(){
        return opCounter;
    }

    private boolean parseRec(int A, int i, int j, String s){
        opCounter ++;
        //if length = 1
        if(i==j-1){
            return grammar.isTerminalRule(A,s.charAt(i));
        }
        //if recursive
        for (int[] rule : grammar.getNoTerminalRules(A)){
            int B = rule[0];
            int C = rule[1];
            for (int k = i+1; k<j; k++){
                if(parseRec(B, i,k,s) && parseRec(C, k, j, s))
                    return true;
            }
        }

        return false;
    }
}