JAVA_OPTION="-Xmn16m -Xms64m -Xmx128m"
JAVA_OPTION=" $JAVA_OPTION -Xdebug -Xrunjdwp:transport=dt_socket,address=8888,server=y,suspend=n"
nohup java -jar $JAVA_OPTION renren.it_spilder.jar -deconfig -springebeans.xml -tablePrefixe_ -p&
#nohup java -jar -Xmn16m -Xms64m -Xmx128m renren.it_spilder.jar -deconfig -springebeans.xml -p&
