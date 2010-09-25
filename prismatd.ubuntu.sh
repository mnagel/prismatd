#! /bin/bash

JOGL="/usr/share/java/jogl.jar"
GLUEGEN="/usr/share/java/gluegen-rt.jar"

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
java -classpath towerdefence.jar:$JOGL:$GLUEGEN com.avona.games.towerdefence.awt.MainLoop

