#!/bin/bash

awk '$6 ~ /^error/ {print}' file.csv


awk '{print $2, $4}' file.csv

awk 'BEGIN { sum=0; count=0 }
{
        split($3, time_parts,":")
        minutes=time_parts[1]
        seconds=time_parts[2]
        total_seconds=minutes*60+seconds
        sum+= total_seconds
        count++
}
END {
if (count>0) {
        avg_seconds = sum / count
        avg_minutes = int(avg_seconds / 60)
        remaining_seconds = int(avg_seconds % 60)
        printf "Average: %02d:%02d\n", avg_minutes, remaining_seconds
}
}

' file.csv


awk '{gsub(/not_submitted/, "not submitted"); print}' file.csv

awk '/^21/ {print}' file.csv