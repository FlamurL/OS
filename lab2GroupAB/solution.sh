#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: $0 <csv_file>"
    exit 1
fi

FILE="$1"

if [ ! -f "$FILE" ]; then
    echo "Error: File does not exist."
    exit 1
fi

if [ ! -r "$FILE" ]; then
    echo "Error: File is not readable."
    exit 1
fi

DATA=$(mktemp)
tail -n +2 "$FILE" > "$DATA"

TOTAL_STUDENTS=$(wc -l < "$DATA")

SUBJECTS=("Math" "Science" "English" "History")

echo "Exam Scores Analysis"
echo "---------------------"
echo "Total Number of Students: $TOTAL_STUDENTS"
echo ""
echo "Subject Averages:"

for i in {3..6}; do
    SUBJECT=${SUBJECTS[$((i-3))]}
    AVG=$(awk -F',' -v col="$i" '{ sum+=$col } END { printf "%.2f", sum/NR }' "$DATA")
    printf "  %-8s: %s\n" "$SUBJECT" "$AVG"
done

echo ""
echo "Subject Extreme Performers:"

for i in {3..6}; do
    SUBJECT=${SUBJECTS[$((i-3))]}

    HIGHEST_LINE=$(awk -F',' -v col="$i" 'BEGIN{max=-1} { if($col>max){max=$col; name=$2} } END {print name, max}' "$DATA")
    HIGHEST_NAME=$(echo "$HIGHEST_LINE" | awk '{ $NF=""; print $0 }' | sed 's/ *$//')
    HIGHEST_SCORE=$(echo "$HIGHEST_LINE" | awk '{ print $NF }')

    LOWEST_LINE=$(awk -F',' -v col="$i" 'BEGIN{min=101} { if($col<min){min=$col; name=$2} } END {print name, min}' "$DATA")
    LOWEST_NAME=$(echo "$LOWEST_LINE" | awk '{ $NF=""; print $0 }' | sed 's/ *$//')
    LOWEST_SCORE=$(echo "$LOWEST_LINE" | awk '{ print $NF }')

    echo "  $SUBJECT - Highest: $HIGHEST_NAME (Score: $HIGHEST_SCORE), Lowest: $LOWEST_NAME (Score: $LOWEST_SCORE)"
done

rm "$DATA"
