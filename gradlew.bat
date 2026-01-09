@rem Script para Windows
@echo off
if "%JAVA_HOME%" == "" (set JAVA_EXE=java) else (set JAVA_EXE="%JAVA_HOME%\bin\java.exe")
%JAVA_EXE% -classpath "gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
