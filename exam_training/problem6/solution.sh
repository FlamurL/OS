#!/bin/bash


if [ ! -f "words.txt" ]; then
    echo "Error: words.txt file not found!"
    exit 1
fi


sort words.txt | uniq
echo


sort words.txt | uniq -c | sort -nr | while read count word; do
    echo "$word: $count"
done