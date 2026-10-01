#!/bin/bash
#Usage: ./run_alls.sh <input_file> <output_prefix> <variant> [<variant> ...]
#   input_file    file with the grammar, an empty line, and then one test string per line
#   output_prefix prefix for the result files
#   variant       one or more of: Naive, TopDown, BottomUp

input="$1"; prefix="$2"; shift 2
mkdir -p results
for v in "$@"; do
  echo "Running $v on $input ..."
  java -Xss256m ExperimentRunner"$v" < "$input" > "results/${prefix}_${v}.tsv"
done
echo "Done. Results in results/"
