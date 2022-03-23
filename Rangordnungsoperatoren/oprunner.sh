#! /bin/sh

# Shell script for starting MiToBo's command line oprunner on Linux #
#####################################################################

# NOTE: to start this script from a different directory you need to
# set MITOBO_HOME properly, see below

# examples: 
#	sh oprunner.sh -r '.\*'
#	sh oprunner.sh GaussFilter inputImg=yourimage.tif resultImg=result.tif
#	sh oprunner.sh ImgThresh inputImage=yourimage.tif  threshold=100 resultImage=binary.tif

## to start the script from a different directory
## set this variable to the directory where you unpacked the mitobo zip-file.
MITOBO_HOME=$PWD

# configure environent variables
. $MITOBO_HOME/setup-classpath.sh
export LD_LIBRARY_PATH

# Ok, let's start the generic commandline interface of MiToBo
java -cp "$CP" de/unihalle/informatik/Alida/tools/ALDOpRunner $*
