-- Топ-10 аэропортов с самыми большими задержками за месяц
-- Данные взяты из https://www.kaggle.com/datasets/tylerx/flights-and-airports-data
/*
airport_id  - идентификатор аэропорта
, airport_name - название аэропорта
, depart_delays_sum  - сумма всех задержек при отправлении
, arrive_delays_sum int - сумма всех задержек по прилете
*/
drop table if exists spark_developer.top_10_most_delayed_airports_monthly
create table spark_developer.top_10_most_delayed_airports_monthly (
            airport_id int
        , airport_name string
        , depart_delays_sum int
        , arrive_delays_sum int
)
clustered by (airport_id) sorted by (airport_id) into 10 buckets
stored as orc

insert overwrite table spark_developer.top_10_most_delayed_airports_monthly
select
		f.originairportid as airport_id
		, a.name as airport_name
		, sum(depdelay ) as depart_delays_sum
		, sum(arrdelay) as arrive_delays_sum
from spark_developer.flights f
left join spark_developer.airports_new_c a on a.airport_id = f.originairportid
group by f.originairportid, a.name
order by depart_delays_sum desc
limit 10


select * from spark_developer.top_10_most_delayed_airports_monthly