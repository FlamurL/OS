#!/bin/bash

if[  $# -ne 2 ];then
    echo "you need two arguments"

    exit 1
fi

status=$1

if [ status = "INFO" || status = "ERROR" || status = "DEBUG"  ]; then
    echo "Good inputs"

else
    echo "Status is invalid"
    exit 1
fi

month=$2

awk -F',' -v status="$1" -v month="$2" '
BEGIN {
    if (month !~ /^[0-9]{4}-[0-9]{2}$/) {
        print "Error must be in format YYYY-MM"
        exit 1
    }
    count=0
}
{
    if ($1 == status && $2 == month) {
        count++
    }
}
END {
    if (NR == 0) {
        print "Error: Input file is empty" > "/dev/stderr"
        exit 1
    }
    # Output result
    if (count == 0) {
        print "No log lines found for status \047" status "\047 in month \047" month "\047"
    } else {
        print "Number of log lines with status \047" status "\047 in month \047" month "\047: " count
    }
}

 ' 'file.csv'
