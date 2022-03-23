#! /bin/sh

# NOTE: to start this script from a different directory you need to
# set MITOBO_HOME properly, see below

# usage:
#	sh compile.sh {java-source-file}*
#
# examples: 
#	cd src/main/java
#	sh compile.sh einfBV/demo/MiToBoDemoOperator.java

## to start the script from a different directory
## set this variable to the directory where you unpacked the mitobo zip-file.
MITOBO_HOME=$PWD

mkdir -p $MITOBO_HOME/plugins/classes

# configure environent variables
. $MITOBO_HOME/setup-classpath.sh
export LD_LIBRARY_PATH

# now compile specified files
javac -cp "$CP" -d $MITOBO_HOME/plugins/classes $*
