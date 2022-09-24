# Gson Validation
## _Validate objects before converting to json_

Gson is an annotation based  validation library used to validate java objects when converting to and from json strings using google's gson library.

- Use annotations to avoid boilerplate
- Simple and lightweight library
- Automatic validation in just one line 

## Installation

Add the following to your maven pom.xml file. Make sure to add the jitpack repository as well as the dependency or else it will not work

_You need this first part as well!_
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```xml

```xml
	<dependency>
	    <groupId>com.github.nickm980</groupId>
	    <artifactId>gson-validation</artifactId>
	    <version>v1.0.0</version>
	</dependency>
```

## Development

Start by adding validation annotations to java fields. 
```java
class BasicObject {
    @Required
    String field;
    @Email
    String email;
}
```

Then when validating the class, use the method GsonValidation#validate(Object obj);
```java
BasicObject obj = new BasicObject();
obj.field = "value";
obj.email = 'email@example.com';

GsonValidator validator = new GsonValidator(obj);
List<String> errors = validator.validate().catchErrors();
```


## Annotations

`@Email` - Validate an email.
`@Required` - Makes the field required. Field cannot be missing.
`@PhoneNumber` - Validate a phone number
`@Size(int min, int max)` - Limit the min and max size of a number or string. Defaults are lowest/highest value for that data type.
`@Regex(String regex)` - Run the validation tests against custom regex. Works on Strings only.

To make all fields required, enable `requiredFields` in the config

## Methods
```
ValidationBuilder GsonValidator#validate(Object); //validates object to json
String ValidationBuilder#toJson() throws ValidationException; //converts the data to json
List<String> ValidationBuilder#validate(String, Class); //validates json to object
```

## License

MIT
