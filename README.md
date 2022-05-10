# Ktor Serialisation Demo

Simple project (opening times formatter) demonstrating the use of Ktor with kotlinx.serialization. 

## build, run und test
build the app via `./gradlew build` then start it via
```shell
java -jar build/libs/ktor_serialization_demo-0.0.1-all.jar
```
Now you can test it by using curl
```shell
curl --request POST 'http://localhost:8080/format' --header 'Content-Type: application/json' --data-raw '{
"monday" : [],
"tuesday" : [
  {
    "type" : "open",
    "value" : 36000
  },
  {
    "type" : "close",
    "value" : 37800
  }
]
}'
```
The result should be
```text
Monday: Closed
Tuesday: 10 AM - 10:30 AM
```