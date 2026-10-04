# CYK Algorithm - Bottom Up
## Language: java

## Overview

Implementation of the **bottom-up** variant of the CYK algorithm: Unlike the naive & top-down variants, 
this vairant works in the opposite direction, starts by resolving single characters an builds up to longer ones reusing
the resulst already stored in a table.

## Files

- `Grammar.java`: parses a context-free grammar in Chomsky Normal Form from plain text lines. Translates nonterminals to 
integer IDs and stores terminal rules and two-nonterminal rules.
- `CYKTopDown.java`: implements the bottom-up parser
- `Main.java`: read a grammar followed by a blank line, then one test string per line.

