# PikoDove

PikoDove is a library using bitwise data format. Make data transfer efficient and efective.

For more information read [Wiki](https://github.com/zavyra/PikoDove/wiki)

## Install

Add jitpack repository
```gradle
repositories {
    // another repositories
    maven { url 'https://jitpack.io' }
}
```

Add dependencies in build.gradle file
```gradle

dependencies {
    // another dependencies
    compile 'com.github.zavyra:PikoDove:0.1.3'
}
```

## Usage

### Supported Data Types:
1. boolean
2. byte
3. short
4. int
5. long
6. char
7. float
8. double
9. String (fixed length)
10. String (dynamic length)
11. *Array of primitive type (coming soon)*
12. *Object (coming soon)*
13. *Array of object (coming soon)*

### Generate Contract Documentation

<p>Your POJO</p>

```java
public class MyCustomClass() {
    // attributes
    public boolean booleanAttribute;
    public byte byteAttribute;
    public short shortAttribute;
    public int intAttribute;
    public long longAttribute;
    public char charAttribute;
    public float floatAttribute;
    public double doubleAttribute;
    @PikoFixedLength(6) public String fixedLengthStringString;
    @PikoString('\n') public String stringWithNewLineTerminator;
    @PikoString public String stringWithDefaultTerminator;
}
```

```java
PikoDocumentation.fromClass(MyCustomClass.class)
```

### Generate Piko Format (for testing purpose)

```java
PikoGenerator.fromClass(myCustomClass, new PikoGeneratorBlueprint(MyCustomClass.class))
```

### Parse Piko to POJO

<p>Read Piko Contract first, then create method with Piko object as parameter</p>

```java
public void fromPiko(Piko piko) {
  // assign all attribute here. Order matters! Follow generated documentation
}
```

Available methods :
1. public boolean readNextBoolean()
2. public byte readNextByte()
3. public short readNextShort()
4. public int readNextInt()
5. public long readNextLong()
6. public char readNextChar()
7. public float readNextFloat()
8. public double readNextDouble()
9. public String readNextFixedString(int length)
10. public String readNextString(char terminator)
11. public String readNextString()

<p>Call that method from anywhere to convert Piko format to POJO</p>

```java
myCustomClass.fromPiko(new Piko(byteArray))
```

## Future Work

1. Support array data type
2. Support object data type
3. Support array of object data type
4. Make more faster parser
5. Using pre-compile auto-generated class using annotation
6. Porting to another language (php/javascript for server side)
