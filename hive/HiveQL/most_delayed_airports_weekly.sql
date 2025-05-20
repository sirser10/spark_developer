-- Аэропорты с самыми долгими задержками на каждый день недели
-- Данные взяты из https://www.kaggle.com/datasets/tylerx/flights-and-airports-data
drop table if exists spark_developer.most_delayed_airports_weekly;

create table spark_developer.most_delayed_airports_weekly(
	originairportid string
	, depart_delays_sum int
	, arrive_delays_sum int
)
partitioned by (dayofweek int)
stored as orc;

set hive.exec.dynamic.partition.mode=nonstrict;

insert overwrite table spark_developer.most_delayed_airports_weekly
partition(dayofweek)
select
	originairportid
	, sum(depdelay ) as depart_delays_sum
	, sum(arrdelay) as arrive_delays_sum
	,  dayofweek
from spark_developer.flights f
group by
	dayofweek
	, originairportid