#! /bin/bash

ANTTASK="clean-build"

JDK="default-jdk"
JOGL="libjogl2-java"
ANT="ant"
JUNIT="junit4"

INSTALLMSG="sudo apt-get install $JDK $JOGL $ANT $JUNIT"

# check if jdk is installed
if which javac >/dev/null
then
  echo "jdk seems to be installed... continue"
else
  echo -e "jdk seems to not be installed\nrun something like $INSTALLMSG"
  exit 1
fi

# check if jogl is installed
if  dpkg -l $JOGL 2>/dev/null | grep -q "^ii"
then
  echo "jogl seems to be installed... continue"
else
  echo -e "jogl seems to not be installed\nrun something like $INSTALLMSG"
  exit 2
fi

# check if ant is installed
if  dpkg -l $ANT 2>/dev/null | grep -q "^ii"
then
  echo "ant seems to be installed... continue"
else
  echo -e "ant seems to not be installed\nrun something like $INSTALLMSG"
  exit 3
fi

# check if junit is installed
if  dpkg -l $JUNIT 2>/dev/null | grep -q "^ii"
then
  echo "junit seems to be installed... continue"
else
  echo -e "junit seems to not be installed\nrun something like $INSTALLMSG"
  exit 4
fi

# run it
ant $ANTTASK "$@"

