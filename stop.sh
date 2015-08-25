#!/bin/sh

ps -ef | grep DevServer | awk '{print $2}'| while read line;
do
    echo "kill task -> $line"
    kill -9 $line
done;