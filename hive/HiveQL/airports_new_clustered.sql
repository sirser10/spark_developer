-- Кластеризованный справочник аэропортов
-- Данные взяты из https://www.kaggle.com/datasets/tylerx/flights-and-airports-data
/*
airport_id - идентификатор аэропорта
city - город аэропорта
state - штат
name - навзание аэропорта
*/
drop table spark_developer.airports_new_c;
create table spark_developer.airports_new_c(
airport_id int
, city string
, state string
, name string
)
clustered by (airport_id) sorted by (airport_id) into 32 buckets
stored as orc
;

insert overwrite table spark_developer.airports_new_c
select * from spark_developer.airports_new;