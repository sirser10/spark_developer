### How to clone only particular folder from github

git clone --depth 1 --filter=blob:none --sparse https://github.com/OtusTeam/SparkDeveloper.git
cd SparkDeveloper

git sparse-checkout set lesson-12/HW/yellow_taxi_jan_25_2018