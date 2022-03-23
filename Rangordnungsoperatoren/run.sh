#! /bin/sh

# Shell script for starting MiToBo on Linux #
#############################################

# NOTE: to start this script from a different directory you need to
# set MITOBO_HOME properly, see below

## to start the script from a different directory
## set this variable to the directory where you unpacked the mitobo zip-file.
MITOBO_HOME=$PWD

# configure environent variables
. $MITOBO_HOME/setup-classpath.sh
export LD_LIBRARY_PATH

# Ok, let's start MiToBo!
java -cp "$CP" -Dlog4j.configuration=$MITOBO_HOME/plugins/jars/log4j.properties -Dalida.versionprovider_class=de.unihalle.informatik.MiToBo.core.operator.MTBVersionProviderReleaseFile ij.ImageJ -ijpath $MITOBO_HOME
