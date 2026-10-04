# CYK Algorithm - Naive
## Language: java

## Files

- `Grammar.java`: parses a context-free grammar in Chomsky Normal Form from plain text lines. Translates nonterminals to
  integer IDs and stores terminal rules and two-nonterminal rules.
- `CYKNaive.java`: implements the naive parser
- `Main.java`: read a grammar followed by a blank line, then one test string per line.

## Issues:
- Because of the time complexity of CYK, the naive implementation may not be efficient for large grammars or long 
input strings.
- Finally I was able to beat the error "Non-zero exit code, crash?", it was an stackoverflow error, so I gave
  -Xss256m to 'run.sh' to make the stack size bigger.