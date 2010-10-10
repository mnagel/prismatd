#! /bin/bash

echo "known problems: does not remove cruft"
echo "does not validate filenames. filenames must not contain -"
echo "does not necessarily link all needed files"
echo ""

shopt -s extglob
for i in *[lrx]*([0-9]).png icon.png no_briefing.png
do
# put links in the android res directory
ln -s ./../../../gfx/$i ./../android/res/drawable/$i
done

echo ""
echo "DO NOT FORGET TO DELETE R.java in ECLIPLSE"
echo "DO NOT FORGET TO DELETE R.java in ECLIPLSE"
echo "DO NOT FORGET TO DELETE R.java in ECLIPLSE"
