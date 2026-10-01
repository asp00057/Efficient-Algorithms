#!/bin/bash


input="$1"; prefix="$2"; shift 2
mkdir -p results
for v in "$@"; do
  echo "Running $v on $input ..."
  java -Xss256m ExperimentRunner"$v" < "$input" > "results/${prefix}_${v}.tsv"
done
echo "Done. Results in results/"
