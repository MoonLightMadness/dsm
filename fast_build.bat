@echo off

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
