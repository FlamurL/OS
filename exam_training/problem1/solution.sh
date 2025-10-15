#!/bin/bash

if [ $# -eq 0 ]; then
        echo "No Argument has entered"
        exit 1
fi

file="$1"

if [ -f "$1" ]; then
        echo "File exists"
else
        echo "File not found"
fi
echo "Number of args: $#"
if [ $# -gt 0 ]; then
        echo "Arguments: "
        for arg in "$@"; do
                echo "$arg"
        done
fi


