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
or any other tool of your choice to execute post querries with.

## Thoughts on the format

the format is surprisingly verbose and clunky. Two main points come to mind:
- the time type which separates opening from closing types
- the notion that the closing time should be written on the next day,
  only because it takes place on the next day.

my format suggestion would look like this:
```json
{
  "monday" : [
    {
      "from": 36000,
      "to": 43200
    },
    {
      "from": 64800,
      "to": 3600
    }
  ]
}
```
This is the (json representation of the) format that I map the given format into.
Notice that each open period is now implicitly bound to be less than 24h,
otherwise the representation wouldn't be unique. But this seems like a
reasonable constraint.

## Other Notes
- I wanted to try something new (instead of Spring Boot), so I used [Ktor](https://ktor.io/),
  JetBrains own lib written in/for Kotlin to "create asynchronous client and server applications".
  I had some minor issues getting the serialisation wired up, but I'm happy with the
  lightweight feel of it.
- if I had used Spring, I'd have added a Swagger/OpenAPI configuration. I've checked the
  [Ktor documentation on OpenApi/Swagger](https://ktor.io/docs/openapi-swagger.html), but all it mentions is how to create
  a Ktor appplication from such documentation.
- according to the section 'Input Data' the time format is "max value is 86399 = 11.59:59 PM", so with a '.' as
  separator between hours and minutes. But according to the example output "Thursday: 10:30 AM - 6 PM", the separator is
  actually a ':'. I'm going with the second option, since that's the official output example.
- In Java every public class lives in its own file, but in Kotlin that's not necessary. And since Kotlin classes are very
  compact, I often put them together in a single file and name the file the way I'd have named the containing package
  in Java.