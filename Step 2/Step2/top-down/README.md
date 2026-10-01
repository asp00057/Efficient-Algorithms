# CYK Algorithm - Top Down 
## Language: java

## Overview

Implementation of the **top-dowm** variant of the CYK algorithm: the same recursive descent as the naive , but 
augmented with a memoization table to avoid redundant computations.

## Files

- `Grammar.java`: parses a context-free grammar in Chomsky Normal Form from plain text lines. Translates nonterminals to 
integer IDs and stores terminal rules and two-nonterminal rules.
- `CYKTopDown.java`: implements the top-down parser
- `Main.java`: read a grammar followed by a blank line, then one test string per line.

