
@echo off


@REM 创建日志文件夹
md log


set meta=./metaconfig.txt

@REM 如果不存在元配置文件，则创建一个新的
if not  exist %meta% (
    type nul > %meta%
)

echo #local_path = ./config/sample/sample.txt > %meta%

@REM 接下来创建config及基础配置文件
if not exist ./config (
    md config
    cd ./config
    md sample
    cd ./sample
    echo #sample = sample > sample.txt
)


