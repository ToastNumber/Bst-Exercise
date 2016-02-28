#!/bin/bash
shopt -s expand_aliases

## Student safe autograder ##

# locate the directory that contains this script
myLocation=$(cd $(dirname $0) && pwd -P)

# execute java code in a sandbox
alias sandjava="java -Djava.security.manager"

base=$(pwd)

gradedir=$2
if [ -z $2 ]
then
    gradedir="${base}/formatReport"
fi

mkdir -p $gradedir

zipfile=$1

if [ -z $1 ]
then
    defaultZip=$(ls *.zip|head -1)
    read -p "No zipfile supplied. Use default '${defaultZip}'? [Y/n]: " useDefaultZip
    case $useDefaultZip in
  n) exit;;
  *) zipfile=$defaultZip;;
    esac
fi

if [[ "$zipfile" =~ [^a-zA-Z0-9._-] ]]; then
    echo "INVALID filename, please restrict to [a-zA-Z0-9._-]. In particular, don't use spaces in your filename."
    exit
fi

zipdir="${base}/.${zipfile}.tmp"

gradefile="${gradedir}/${zipfile%.zip}.txt"

# Clean gradefile
rm $gradefile 2> /dev/null

# Clean existing tmp dir
rm -r $zipdir 2> /dev/null

alias mark="cat >> '$gradefile'"
die() { echo "ERROR: $@" | tee -a $gradefile ; exit 1; }
lineTop() { echo "=================================================================" | mark ; }
lineBot() { lineTop ; echo | mark ; }


echo "Unzipping..." | mark

unzip -j -q $zipfile -d $zipdir || die "Could not read zipfile ${zipfile}"
# Deleting annoying files generated on OSX
cd ${zipdir}
rm ./._* -f 2> /dev/null
rm -r ./._* -f 2> /dev/null

echo "Extracted ${zipfile}" | mark
echo | mark

## START compilation
# This gathers all the files and compiles them.
# TODO: replace students' interface files with our own
echo "Compiling..." | mark

lineTop
build_dir=${zipdir}/build
mkdir -p $build_dir
classpath=${build_dir}
javac -cp $classpath -Xlint -d $build_dir ${zipdir}/*.java 2>&1 | mark
lineBot
## END compilation


# marking code goes here. note that the *.java and *.class files are in the pwd at this point
# use sandjava to execute classes in a sandbox


## START junit tests
echo "Running test code..." | mark
lineTop
sandjava -cp $classpath BstTests | mark
lineBot
## END junit tests

cd ..

rm -r ${zipdir}
