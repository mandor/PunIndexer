# Simple shell script used to start punindexer in console mode.
# Arguments are just passed along to the Launcher.

cd "$( cd -P "$( dirname "$0" )" && pwd )"
java -server -classpath "lib/*" net.mandor.pi.Launcher $@
