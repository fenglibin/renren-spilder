rem requestXmlFile:Set the file which include the request xml content.
rem soapAddress:Set the soap request address.
rem outFile:Give the file which used store the reponse string. This is optional, the result will output to the console if without this parameter.
rem wss:Give the webservice security config file, see the example file "wss.properties".This is optional, it will do normal webservice request without
                this option, it will do webservice security request.
java -Djavax.net.ssl.trustStore=D:\tools\jks\662key.jks -cp RENREN-IT-WEBSERVICE-CLIENT.jar it.renren.tools.wsclient.core.Run requestXmlFile=OnPersonPlacingOrder.xml soapAddress=https://ifop-app662.hkg.swissbank.com:9443/executionserver/services outFile=result.txt
pause