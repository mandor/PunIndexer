# Simple shell script used to start punindexer in console mode.
# Arguments are just passed along to the Launcher.

cd "$( cd -P "$( dirname "$0" )" && pwd )"
java -server -Xmx32m -XX:MaxPermSize=32m -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -classpath "lib/*" net.mandor.pi.Launcher $@
