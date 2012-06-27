#!/bin/bash
rm -f /root/*.php*
JAVA_PID=`ps -ef|grep renren.it_spilder.jar|grep -v grep|awk '{print $2}'`
if [ "$JAVA_PID" == "" ]; then
        echo "no java task.";
else
        echo "java pid $JAVA_PID";
        kill $JAVA_PID;
fi
wget http://www.renren.it/man/makehtml_archives_action_temp.php -T3600
wget http://www.renren.it/man/makehtml_homepage_temp.php -T3600
wget http://renren.it/man/makehtml_archives_action_temp.php -T3600
wget http://renren.it/man/makehtml_homepage_temp.php -T3600

wget http://www.renren.it/man/makehtml_archives_action_temp.php -T3600
wget http://www.renren.it/man/makehtml_homepage_temp.php -T3600
wget http://renren.it/man/makehtml_archives_action_temp.php -T3600
wget http://renren.it/man/makehtml_homepage_temp.php -T3600

wget http://www.renren.it/man/makehtml_archives_action_temp.php -T3600
wget http://www.renren.it/man/makehtml_homepage_temp.php -T3600
wget http://renren.it/man/makehtml_archives_action_temp.php -T3600
wget http://renren.it/man/makehtml_homepage_temp.php -T3600
