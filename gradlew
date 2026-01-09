#!/usr/bin/env sh
​Intento de encontrar Java
​if [ -n "$JAVA_HOME" ] ; then
JAVACMD="$JAVA_HOME/bin/java"
else
JAVACMD="java"
fi
​Ejecutar el wrapper (simplificado para compilación en la nube)
​exec "$JAVACMD" JAVA_OPTS -classpath "gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "@"
