@echo off
@REM 不能被直接执行
if (%1%) == () goto close
@REM 获取帮助
if (%1%) == (help) goto help

md %1%
cd ./%1%
md app
md docs
md requirements
md scripts
md tests

cd ./app
md dependencies
md %1%
md libs
md managers
md middlewares
md responses
md routes
md schemas
md utils
md config
md log

cd ./log
md logconfig

:close
echo It can not be executed directly.Please run it with cmd or powershell
pause

:help
echo fast_build [name]
echo name : the name of fast project you want.