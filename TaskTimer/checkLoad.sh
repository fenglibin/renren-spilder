#!/bin/bash
currentDate=`date`
cpuload=`w|grep load|awk -F"load" '{print $2}'|awk -F": " '{print $2}'|awk -F"," '{print $1}'`;
#if [ "$cpuload" -gt "3" ]; then
echo "$currentDate, Current load is :$cpuload<br>" >> /home/fenglibin/www/www.renren.it/load.txt;
if [ `echo "$cpuload > 3" | bc` -eq 1 ]; then
        echo "cpuload is great than 3, then restart apache" >> /home/fenglibin/www/www.renren.it/load.txt;
        /opt/lampp/lampp restartapache;
fi