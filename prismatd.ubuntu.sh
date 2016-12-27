#! /bin/bash

PRISMATD="build/jar/PrismaTD.jar"
JOGL="/usr/share/java/jogl2.jar"
GLUEGEN="/usr/share/java/gluegen2-rt.jar"

LIBRARY="/usr/lib/jni/"

MAINCLASS="com.avona.games.towerdefence.awt.MainLoop"

# check if prismatd jar is installed
if [ -e $PRISMATD ]
then
  echo "prismatd jar seems to be installed... continue"
else
  echo -e "prismatd seems to not be installed\nbuild it by running ./prismatd-build.ubuntu.sh"
  exit 3
fi

# check if jogl is installed
if [ -e $JOGL ]
then
  echo "jogl seems to be installed... continue"
else
  echo -e "jogl seems to not be installed\nrun something like 'sudo apt-get install libjogl-java' to install it"
  exit 1
fi

# check if gluegen is installed
if [ -e $GLUEGEN ]
then
  echo "gluegen seems to be installed... continue"
else
  echo -e "gluegen seems to not be installed\nrun something like 'sudo apt-get install libjogl-java' to install it"
  exit 2
fi

# run it -- sun java (on debian squeeze) fails badly without library path
java -Djava.library.path=$LIBRARY -classpath $PRISMATD:$JOGL:$GLUEGEN $MAINCLASS "$@"
