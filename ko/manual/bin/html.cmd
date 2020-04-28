@ECHO OFF
IF "%1"=="" GOTO USAGE
IF "%1"=="all" GOTO ALL

ant -f "%~d0%~p0..\build.xml" -Dtargetdocs=%1 html
GOTO END

:USAGE
ECHO Usage: %~n0.cmd { all ^| document... }
GOTO END

:ALL
    ant -f "%~d0%~p0..\build.xml" html

:END