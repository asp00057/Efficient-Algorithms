#!/bin/bash
# Usage: ./gen_input.sh <grammar_file> <pattern> <from> <to> <step>
# Patterns:
#   nested       ((( ... )))        n/2 opening, then n/2 closing
#   pairs        ()()()...          n/2 repetitions of "()"
#   close_first  )()()...           pairs with an extra ")" at the start
#   open_last    ()()...(           pairs with an extra "(" at the end
#   a            aaa...a            n times 'a'
#   s            sss...s            n times 's'
#   ba           baaa...a           'b' followed by n-1 times 'a'
grammar="$1"; pattern="$2"; from="$3"; to="$4"; step="$5"

rep() { # rep <string> <times>
  local s="" i
  for ((i=0; i<$2; i++)); do s+="$1"; done
  printf '%s' "$s"
}

grep -v '^$' "$grammar"
echo
for ((n=from; n<=to; n+=step)); do
  h=$((n/2))
  case "$pattern" in
    nested)      echo "$(rep '(' $h)$(rep ')' $h)" ;;
    pairs)       echo "$(rep '()' $h)" ;;
    close_first) echo ")$(rep '()' $h)" ;;
    open_last)   echo "$(rep '()' $h)(" ;;
    a)           echo "$(rep 'a' $n)" ;;
    s)           echo "$(rep 's' $n)" ;;
    ba)          echo "b$(rep 'a' $((n-1)))" ;;
    *) echo "unknown pattern: $pattern" >&2; exit 1 ;;
  esac
done