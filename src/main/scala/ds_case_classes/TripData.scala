package ds_case_classes

case class TripData(
                     VendorID: Int,
                     tpep_pickup_datetime: String,
                     tpep_dropoff_datetime: String,
                     passenger_count: Long,
                     trip_distance: Float,
                     RatecodeID: Int,
                     store_and_fwd_flag: String,
                     PULocationID: Int,
                     DOLocationID: Int,
                     payment_type: Int,
                     fare_amount: Float,
                     extra: Float,
                     mta_tax: Float,
                     tip_amount: Float ,
                     tolls_amount: Float,
                     improvement_surcharge: Float,
                     total_amount: Float,
                     congestion_surcharge: Float,
                     Airport_fee:Float
                   )

case class TaxiZoneLookup(
                           LocationID: Long,
                           Borough: String,
                           Zone: String,
                           service_zone: String
                         )

case class YellowTaxiJanuary(
                              VendorID: Long,
                              tpep_pickup_datetime: String,
                              tpep_dropoff_datetime: String,
                              passenger_count: Long,
                              trip_distance: Double,
                              RatecodeID: Int,
                              store_and_fwd_flag: String,
                              PULocationID: Long,
                              DOLocationID: Long,
                              payment_type: Long,
                              fare_amount: Double,
                              extra: Double,
                              mta_tax: Double,
                              tip_amount: Double ,
                              tolls_amount: Double,
                              improvement_surcharge: Double,
                              total_amount: Double,
                              )
