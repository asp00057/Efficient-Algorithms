\# CYK Algorithm - Linear Grammars

\## Language: java



\## Files



\- `LinearGrammar.java`: parses a linear context-free grammar in linear Chomsky Normal Form from plain text lines

&#x20; (rules `A -> a`, `A -> a B` and `A -> B a`). Translates nonterminals to integer IDs and stores the terminal rules

&#x20; and the two kinds of linear rules separately. A symbol is a nonterminal if it appears as the left-hand side of

&#x20; some rule; otherwise it is a terminal.

\- `CYKLinearBottomUp.java`: implements a bottom-up CYK parser specialized for linear grammars. Since the terminal

&#x20; of each rule is at one end of the substring, the split point is fixed and there is no loop over split points,

&#x20; so the time is O(|R| n^2) instead of O(|R| n^3).

\- `Main.java`: read a grammar followed by a blank line, then one test string per line. Prints `yes` or `no` for

&#x20; each string.

\- `compile.sh`, `run.sh`: compile and run the program.



\## Issues:

\- The table has size |V| \* n^2, so memory may be a problem for very long input strings.

\- The grammar must be in linear Chomsky Normal Form. A rule with two terminals or two nonterminals on the

&#x20; right-hand side is reported as an error.

