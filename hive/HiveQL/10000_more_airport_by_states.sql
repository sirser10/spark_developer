-- Штаты, где больше 100000 аэропортов
-- Данные взяты из https://www.kaggle.com/datasets/tylerx/flights-and-airports-data

drop table spark_developer.10000_more_airport_by_states
CREATE TABLE spark_developer.10000_more_airport_by_states (
    count_airports INT
)
PARTITIONED BY (state STRING)
STORED AS PARQUET;

set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE spark_developer.10000_more_airport_by_states
PARTITION (state)

SELECT
    COUNT(a.airport_id) AS count_airports,
    a.state
FROM spark_developer.flights f
LEFT JOIN spark_developer.airports_new_c a ON a.airport_id = f.originairportid
GROUP BY a.state
HAVING COUNT(a.airport_id) > 100000;

select * from spark_developer.10000_more_airport_by_states