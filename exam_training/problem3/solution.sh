#!/bin/bash

awk -F';' '
{
    groups[$4]++
}
END {
    max_count = 0
    most_common = ""
    for (group in groups) {
        if (groups[group] > max_count) {
            max_count = groups[group]
            most_common = group
        }
    }
    print "Most common group: " most_common " (" max_count " times)"
}' file.csv


awk 'BEGIN{count=0} /22/ {count++} END {print count}' file.csv


awk -F';' '{print $3}' file.csv | sort -u -t':' -k1,1nr -k2,2nr