CLASSPATH="$CLASSPATH"
CLASSPATH=$CLASSPATH:renren.it_spilder.jar
java  -Xmn16m -Xms64m -Xmx128m it.renren.spilder.util.wash.RedoContentType -classpath=$CLASSPATH
