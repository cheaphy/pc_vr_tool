echo off
java -classpath "target;target\devTool.jar" -Dorg.eclipse.jetty.server.Request.maxFormContentSize=1000000 com.shushanfx.DevServer