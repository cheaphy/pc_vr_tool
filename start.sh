#!/bin/sh
nohup "/usr/lib/jvm/java-1.8.0-oracle-1.8.0.45.x86_64/bin/java" -classpath "target:target/devTool.jar" -Dorg.eclipse.jetty.server.Request.maxFormContentSize=1000000 com.shushanfx.DevServer>> devTool.log &
