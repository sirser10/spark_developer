-- Количество аэропортов по штатам
-- Данные взяты из https://www.kaggle.com/datasets/tylerx/flights-and-airports-data

drop table if exists spark_developer.count_airports_by_states
create table spark_developer.count_airports_by_states(
	count_airports int
)
partitioned by (state string)
stored as parquet

set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE spark_developer.count_airports_by_states
PARTITION (state)

SELECT
    COUNT(a.airport_id) AS count_airports,
    a.state
FROM spark_developer.flights f
LEFT JOIN spark_developer.airports_new_c a ON a.airport_id = f.originairportid
GROUP BY a.state
ORDER BY count_airports DESC;

select * from spark_developer.count_airports_by_states