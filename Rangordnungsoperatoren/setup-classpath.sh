# setup the classpath in CP and LD_LIBRARY_PATH as well als ARCH

CP="$MITOBO_HOME/plugins/Mi_To_Bo.jar:$MITOBO_HOME/plugins/classes"
for jar in $MITOBO_HOME/plugins/jars/*jar ; do
        CP="$CP:$jar"
done

