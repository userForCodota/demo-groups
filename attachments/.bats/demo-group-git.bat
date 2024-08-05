@echo off
set originPath=%cd%
:allstart
cd %originPath%
echo currently located in %cd%
echo -----------------------------------------------------------------------------------------------------------------------------------------
git status
echo -----------------------------------------------------------------------------------------------------------------------------------------
echo [1] git pull
echo [2] git add and git commit and git push
echo [3] exit
echo ------------------------------------------------------------------------------
choice /C 123 /M "please enter your selection"
echo ------------------------------------------------------------------------------
if errorlevel 3 goto endscript
if errorlevel 2 goto add_commit_push
if errorlevel 1 goto git_pull
goto allstart

:add_commit_push
call :getDatetimeFormat
set datetimeStr=%return%
cd ../
cd ../
git add .
git commit -m %datetimeStr%
git push origin develop
goto allstart

:git_pull
git pull
goto allstart

:endscript
echo exit
pause




:getDatetimeFormat
for /f "tokens=2 delims==" %%a in ('wmic os get localdatetime /value') do set datetime=%%a
set "year=%datetime:~0,4%"
set "month=%datetime:~4,2%"
set "day=%datetime:~6,2%"
set "hour=%datetime:~8,2%"
set "min=%datetime:~10,2%"
set "sec=%datetime:~12,2%"
if "%month:~0,1%"==" " set "month=0%month:~1%"
if "%day:~0,1%"==" " set "day=0%day:~1%"
if "%hour:~0,1%"==" " set "hour=0%hour:~1%"
if "%min:~0,1%"==" " set "min=0%min:~1%"
if "%sec:~0,1%"==" " set "sec=0%sec:~1%"
set datetimeStr=%year%%month%%day%_%hour%%min%%sec%
set return=%datetimeStr%
goto:eof