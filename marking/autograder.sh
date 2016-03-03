#!/bin/bash
shopt -s expand_aliases

#########################
##   WARNING: DO NOT   ##
##   DISTRIBUTE THIS   ##
##     VERSION TO      ##
##      STUDENTS       ##
#########################

#####  WAIT #####

# we need to decide how students should submit their code:
# currently this unzips the file by flattening the directory structure
# but this might not work fine for some students
# better require a certain dir structure

# (but this allows us to use martin's sample solutions as-is)




checkstyleJarName="checkstyle-6.15-all.jar"
checkstyleConfigFile="style_checks.xml"

# tests to get a third
THIRD_TESTS="PropertiesIsEmpty PropertiesSmallerBigger PropertiesHas PropertiesFind PropertiesPut Grade3rd"
# tests to get a lower second
LOWER_TESTS="PropertiesDelete GradeLower2nd"
UPPER_TESTS="PropertiesSaveInOrder PropertiesSmallestLargest PropertiesDeleteSmallestLargest PropertiesSize PropertiesHeight GradeUpper2nd"
FIRST_TESTS="GradeLowerFirst"
OTHER_TESTS="PropertiesBalanced GradeUpperFirst"


# locate the directory that contains this script
myLocation=$(cd $(dirname $0) && pwd -P)

checkstyleJar=${myLocation}/${checkstyleJarName}
checkstyleConfig=${myLocation}/${checkstyleConfigFile}

# execute java code in a sandbox
alias sandjava="java -Djava.security.manager"

tests_src=${myLocation}/bst-tests

base=$(pwd)

gradedir=$2
if [ -z $2 ]
then
    gradedir="${base}/grades"
fi

testsDir=${myLocation}/bst-tests
ourInterfaces="${myLocation}/src-override/Bst.java ${myLocation}/src-override/Table.java"

junit="${myLocation}/junit-4.12.jar"
hamcrest="${myLocation}/core-1.3.jar"
javaRuntype="${myLocation}/javaruntype-1.2.jar"
quickcheckCore="${myLocation}/junit-quickcheck-core-0.6-beta-1.jar"
quickcheckGenerators="${myLocation}/junit-quickcheck-generators-0.6-beta-1.jar"
slf4j="${myLocation}/slf4j-api-1.7.18.jar"
slf4jSimple="${myLocation}/slf4j-simple-1.7.18.jar"
generics="${myLocation}/generics-resolver-2.0.1.jar"
antlr="${myLocation}/antlr-4.5.2-complete.jar"
ognl="${myLocation}/ognl-3.1.2.jar"


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

unzip -n -j -q $zipfile -d $zipdir || die "Could not read zipfile ${zipfile}"
# Deleting annoying files generated on OSX
cd ${zipdir}
rm ./._* -f 2> /dev/null
rm -r ./._* -f 2> /dev/null

echo "Extracted ${zipfile}" | mark
echo | mark

## START file sanitation
# Replace the interface files with our own to make sure students did not modify them.
cp $ourInterfaces $zipdir
# Insert a null BstTable if the students did not write any, to avoid compilation errors.
if [ ! -f ${zipdir}/BstTable.java ]
then
    cp "${myLocation}/src-override/BstTable.java" "${zipdir}/BstTable.java"
fi
## END file sanitation

## START compilation
# This gathers all the files and compiles them.
# TODO: replace students' interface files with our own
echo "Compiling..." | mark

lineTop
build_dir=${zipdir}/build
mkdir -p $build_dir
classpath=${build_dir}:${junit}:${hamcrest}:${javaRuntype}:${slf4j}:${generics}:${antlr}:${ognl}:${quickcheckCore}:${quickcheckGenerators}:${slf4jSimple}
compilation_output=$((javac -cp $classpath -Xlint -d $build_dir ${zipdir}/*.java ${testsDir}/*.java) 2>&1)
echo "$compilation_output" | mark
lineBot
## END compilation


# marking code goes here. note that the *.java and *.class files are in the pwd at this point
# use sandjava to execute classes in a sandbox (to prevent students from setting your cat on fire and launching the missiles)


## START SuppressWarnings scan
# The students are allowed to use a couple of @SuppressWarnings pragmas in the balanced() method.
# However, they can be abused to hide compiler warnings that we /do/ want to see.
# So this picks them out, including the surrounding source code.
echo "Grepping for SuppressWarnings..." | mark

lineTop
grep SuppressWarnings -RHn *.java -B3 -A3 | mark
num_suppresses=$(cat *.java | grep -c SuppressWarnings -c)
lineBot
## END SuppressWarnings scan


## START style checks
# The following checks code style. Mainly indentation.
# The configuration of "checkstyle" are in style_checks.xml
# Currently it's mainly javadocs.
echo "Checking code style..." | mark

lineTop
style=$(java -jar $checkstyleJar -c $checkstyleConfig BstTable.java)
echo "$style" | mark
lineBot
## END style checks


## START junit tests
run_tests()
{
    if [ -z "$1" ]
    then
        my_tests=$TESTS
    else
        my_tests="$1"
    fi
    echo Running the following tests: | mark
    echo $my_tests | mark
    tests_passed="0"
    tests_ran=0
    for test in $my_tests
    do
        output=$(java -cp $classpath org.junit.runner.JUnitCore $test)
        scores=$((echo " = award 0" && echo "${output}") | grep " = award [.0-9]*$" | sed -r 's/.* award ([.0-9]*)$/\1/' | paste -sd+ | bc)
        tests_passed=$(echo $tests_passed + $scores | bc)
        # if [[ $? == 0 ]]
        # then
        #     tests_passed=$((tests_passed+1))
        # fi
        tests_ran=$((tests_ran+1))
        echo "$output" | mark
    done
    echo $tests_passed
}

echo "Running unit tests..." | mark
lineTop
crashtest=$(java -cp $classpath org.junit.runner.JUnitCore CrashTest)
if [[ $? == 0 ]]
then
    echo "Crash test passed." | mark
    echo | mark
    crashed=0
else
    echo "WARNING: Crash test failed!" | mark
    echo "$crashtest" | mark
    crashed=1
fi
third_score=$( run_tests "$THIRD_TESTS" )
lower_score=$( run_tests "$LOWER_TESTS" )
upper_score=$( run_tests "$UPPER_TESTS" )
first_score=$( run_tests "$FIRST_TESTS" )
other_score=$( run_tests "$OTHER_TESTS" )
lineBot
## END junit tests


### START final report
echo | mark
echo "------------" | mark
echo "Final report" | mark
echo "------------" | mark

if [[ $crashed == 0 ]]
then
    echo "Crash test passed." | mark
else
    echo "WARNING: Crash test failed!" | mark
fi

# Length of compilation output is an indication of code quality
echo "Compilation output size: $(echo -n "$compilation_output" | wc -l) lines (expected 0)" | mark

# number of @SuppressWarnings pragmas
echo "SuppressWarnings count: ${num_suppresses} (expected 3)" | mark

# Check if Bst is referenced in BstTable (sanity check)
if [ "$(grep "Bst[^T]" "${zipdir}/BstTable.java" | wc -l)" -eq 0 ]
then
    echo "WARNING: Bst<> not referenced in BstTable.java!" | mark
fi

# style report
if [ "$(echo "$style" | tail -n1)" = "Audit done." ]
then
    numErrors=0
else
    numErrors=$(echo -n "$style" | tail -1 | sed 's/[^0-9]//g')
fi
echo "Style: ${numErrors} errors" | mark

count_tests()
{
    echo "$1" | wc -w | cat
}


# number of passed tests
echo "Methods up to delete: ${third_score} (out of 40)" | mark
echo "Delete method: ${lower_score} (out of 10)" | mark
echo "Other methods: ${upper_score} (out of 30)" | mark
echo "BstTable: ${first_score} (out of 15)" | mark
echo "Balancing: ${other_score} (out of 15)" | mark
echo "Total: $(echo $third_score + $lower_score + $upper_score + $first_score + $other_score | bc)" | mark
# echo "Third class: ${third_score} passed (out of $(count_tests "${THIRD_TESTS}"))" | mark
# echo "Lower second class: ${lower_score} passed (out of $(count_tests "${LOWER_TESTS}"))" | mark
# echo "Upper second class: ${upper_score} passed (out of $(count_tests "${UPPER_TESTS}"))" | mark
# echo "First class: ${first_score} passed (out of $(count_tests "${FIRST_TESTS}"))" | mark
# echo "Extra points: ${other_score} passed (out of $(count_tests "${OTHER_TESTS}"))" | mark
### END final report

cd ..

rm -r ${zipdir}
