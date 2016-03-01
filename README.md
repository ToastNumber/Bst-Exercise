Bst Exercise
============

**Disclaimer: Read and follow the instructions on Canvas before submitting. The information here has been copied from Canvas, but do not rely on it for your final submission.**

Follow the instructions in the files in this repository. You should submit a zip file `abc123.zip`, where `abcde123` is your username.

Submission guidelines
---------------------
Auke Booij has written the following submission guidelines:

We will mark at least part of this assignment automatically. It is very important that you prepare your submission for this.
I have uploaded [`formatChecker.sh`](formatChecker.sh). This is a bash script that you should run against the zip file that you want to submit.

1. Do not put your classes in a Java package.
2. Make sure to only put one version of your code in the zip file, as the script might otherwise use old code that you included.
3. Place `formatChecker.sh` in the same directory as your zip file.
4. Open a terminal, go to the directory containing your zip file and this script, and execute: `chmod u+x formatChecker.sh` (to make it executable)
5. Run it against your zip file: `./formatChecker.sh myzipfile.zip`
6. Check that it generated a file `formatReport/myzipfile.txt`. This file should not contain any warnings, but it should contain the output from your test own code.

Marking scale
-------------
0. To get "third class" you should get everything before `delete()` right.
1. To get "lower second class" (2.2) you should also get `delete()` right.
2. To get "upper second class" (2.1) you should also get `saveInOrder()` right.
3. To get just "first class" you should also get `BstTable` right.
4. To get a comfortable "first class" you should get `balanced()` right.

This is assuming you have comments, Javadoc etc.

FAQ
---
If you want to add a question and answer here, please submit a pull request.

- **Is it ok if our BstTable.java doesn't compile but Fork.java and Empty.java do? Or is this still 0 marks?**
  Don't include it at all. If BstTable does not exist, a null implementation (that does nothing useful but at least compiles) is used.


