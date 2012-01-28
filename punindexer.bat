@echo off
rem Simple batch script used to start punindexer in console mode.
rem Arguments are just passed along to the Launcher.

set _=%CD%
cd %~dp0
java -server -Xmx48m -XX:MaxPermSize=32m -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -classpath "lib/*" net.mandor.pi.Launcher %*
cd %_%
