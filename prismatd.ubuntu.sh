#! /bin/bash

PRISMATD="towerdefence.jar"
JOGL="/usr/share/java/jogl.jar"
GLUEGEN="/usr/share/java/gluegen-rt.jar"

MAINCLASS="com.avona.games.towerdefence.awt.MainLoop"

# check if prismatd jar is installed
if [ -e $PRISMATD ]
then
  echo "prismatd jar seems to be installed... continue"
else
  echo "prismatd seems to not be installed\nbuild it somehow..."
  exit 3
fi

# check if jogl is installed
if [ -e $JOGL ]
then
  echo "jogl seems to be installed... continue"
else
  echo "jogl seems to not be installed\nrun something like 'sudo apt-get install libjogl-java' to install it"
  exit 1
fi

# check if gluegen is installed
if [ -e $GLUEGEN ]
then
  echo "gluegen seems to be installed... continue"
else
  echo "gluegen seems to not be installed\nrun something like 'sudo apt-get install libjogl-java' to install it"
  exit 2
fi

# run it
java -classpath $PRISMATD:$JOGL:$GLUEGEN $MAINCLASS

