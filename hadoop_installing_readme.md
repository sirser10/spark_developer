# Демонстрация HDFS
#### Cсылка на гитхаб https://github.com/big-data-europe/docker-hadoop
## Запускаем HDFS
$ docker compose up -d
$ docker compose ps -a

## Веб-интерфейс
http://localhost:9870

##  Подключаемся к Namenode
$ docker exec -ti namenode /bin/bash

### Посмотрим на команды hdfs
hdfs

### Посмотрим корневую структуру папок hdfs
hdfs dfs -ls /

### Создадим локальный файл:
cat > a.txt
aaa bbb ccc
^D (Control+D)

### Создадим директорию
hdfs dfs -mkdir -p /user/eas011/

### Загрузим локальный a.txt в HDFS и проверим загрузку
hdfs dfs -put a.txt /user/eas011/
hdfs dfs -ls /user/eas011/
hdfs dfs -cat /user/eas011/a.txt

### Изменим файлик локально
cat > a.txt

fff
ggg
hhh
^D (Control+D)
cat a.txt

### Перезапишем его в hdfs
hdfs dfs -put a.txt /user/eas011/   // File already exists!
hdfs dfs -put -f a.txt /user/eas011/ // Force file rewrite
hdfs dfs -cat /user/eas011/a.txt

## Посмотрим где лежит файлик
hdfs fsck /user/eas011/a.txt -files -blocks -locations
/user/eas011/a.txt 7 bytes, replicated: replication=3, 1 block(s):  Under replicated BP-54015084-172.18.0.6-1629173599639:blk_1073741835_1011

. BP-1115798734-172.18.0.2-1745351129227:blk_1073741826_1002 len=23 Live_repl=3  [DatanodeInfoWithStorage[172.18.0.5:9866,DS-e1dfeeb1-618d-46fc-85b4-04af85e9f388,DISK], DatanodeInfoWithStorage[172.18.0.4:9866,DS-3a668e8f-e7ce-4464-8fde-d0c4b0965bf3,DISK], DatanodeInfoWithStorage[172.18.0.3:9866,DS-504dc9b1-da15-4937-82c9-15503eb359b5,DISK]]

##  Подключаемся на Datanoded
l

$ docker exec -ti datanode1 /bin/bash

### Поищем где лежит файлик
find . -name blk_1073741835
cd <найденный_путь>
cat blk_1073741835
