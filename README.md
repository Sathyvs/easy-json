# easy-json
[maven-central:1.1](https://central.sonatype.com/search?q=easy-json&namespace=io.github.sathyvs)

EasyJson is a very handy Java library that makes working with Json data very easy. This library 
marshals and un-marshals the Json data string into Maps and Lists type objects, and uses Jackson 
library under the hood to process json Strings.

## Motivation
Why create another library? All existing libraries either allows us handle Json using plain util 
java maps and lists, or Java Pojos. While Pojos can be fluent, Java maps and lists or not. 
EasyJson solves that problem. In Json data heavy projects where we do not need to create Pojos for 
every single Json format, and for highly nested structures, easy json is the perfect library to use.

---
To include in your project:

Gradle
```
implementation group: 'io.github.sathyvs', name: 'easy-json', version: '1.1'
```
Maven
```
<dependency>
    <groupId>io.github.sathyvs</groupId>
    <artifactId>easy-json</artifactId>
    <version>1.1</version>
</dependency>
```
---
## Creating a Simple Json payload
Creating a Json payload is very simple using easy json. Just create a new JsonObject with the keys 
and values as shown below,
```
JsonObject customerData = new JsonObject()
    .put("customer", new JsonObject()
        .put("name", "John Doe")
        .put("address", new JsonObject()
            .put("street", "1010 225th Dr SE")
            .put("city", "Bothell")
            .put("state", "Washington")));

System.out.println(customerData.encodePrettily());
```
The `encodePrettily` will marshal and pretty print the Json payload as below. Yup !! It's that simple.
```
{
  "customer" : {
    "name" : "John Doe",
    "address" : {
      "street" : "1010 225th Dr SE",
      "city" : "Bothell",
      "state" : "Washington"
    }
  } 
}
```
## Working with Json arrays 
Adding list of items is very similar that we use JsonArray class. The below code will add a list of 
emails and phone numbers to the same customer object
```
customerData.getJsonObject("customer")
    .put("email", new JsonArray().add("abc@gmail.com").add("xyz@gmail.com"))
    .put("phone", new JsonArray()
        .add(new JsonObject().put("type", "cell").put("number", "0000000000"))
        .add(new JsonObject().put("type", "home").put("number", "9999999999")));
```
and the json payload will be
```
{
  "customer" : {
    "name" : "John Doe",
    "address" : {
      "street" : "1010 225th Dr SE",
      "city" : "Bothell",
      "state" : "Washington"
    },
    "email" : [ "abc@gmail.com", "xyz@gmail.com" ],
    "phone" : [ {
      "type" : "cell",
      "number" : "0000000000"
    }, {
      "type" : "home",
      "number" : "9999999999"
    } ]
  }
}
```
As you can notice we can add a list of primitive types just as String or add Complex objects using 
JsonObject class.

The JsonObject class has a getMap() method that returns a basic Java Map<String, Object> and the 
JsonArray class has a getList() method that returns a list of Objects if the application needs the 
data in java util maps and lists.

## Unmarshal from a Json String
Unmarshalling the Json String to JsonObject is simple as well. 
```
JsonObject unmarshalledObj = new JsonObject(jsonString);
```
The above code gives the JsonObject which can then be used to access any or all of the data in the 
payload. 
```
System.out.println(unmarshalledObj.getJsonObject("customer").getJsonArray("phone"));
```
will print just the phone numbers added as below
```
[{"type":"cell","number":"0000000000"},{"type":"home","number":"9999999999"}]
```

That's it !!! Enjoy coding with easy-json 
