#!/bin/bash

base=$(pwd)

# first argument is the path containing the submissions. this takes the absolute path of that.
if [ -z $1 ]
then
    SUBMISSION_DIR=$(pwd)
else
    SUBMISSION_DIR=$(cd $(dirname $1) && pwd -P)
fi

myLocation=$(cd $(dirname $0) && pwd -P)

ZIP_FILES=`find ${SUBMISSION_DIR} | grep "/[a-zA-Z0-9_-]*.zip"`
for f in ${ZIP_FILES}; do
	submission_dir=$(dirname $f)
	cd $submission_dir
	echo "Grading $f..."
	${myLocation}/autograder.sh $(basename $f) ${base}/grades
done

echo "Cya!..."
