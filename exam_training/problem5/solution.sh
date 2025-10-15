#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Please add the file name"
    exit 1

fi

if [ ! -f $1 ]; then
    echo "File doesnt exist"
    exit 1

fi

math=$(awk -F"," '
BEGIN{
count=0
total=0
}
{
count++
total+=$2

}
END{
print total/count
}
' "muli.txt")

science=$(awk -F"," '
BEGIN{
count=0
total=0
}
{
count++
total+=$3

}
END{
print total/count
}
' "muli.txt")

english=$(awk -F"," '
BEGIN{
count=0
total=0
}
{
count++
total+=$4

}
END{
print total/count
}
' "muli.txt")

echo "Average for math $math"
echo "Average for science $science"
echo "Average for english $english"
