package com.gwk.pikodove.generator;

import com.gwk.pikodove.annotation.PikoFixedLength;
import com.gwk.pikodove.annotation.PikoString;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Zavyra on 2017-03-25.
 *
 */

public class PikoGeneratorBlueprint {

    // list all possible type of attributes
    private final Class clazz;
    private ArrayList<Field> booleanType = new ArrayList<>();
    private ArrayList<Field> byteType = new ArrayList<>();
    private ArrayList<Field> shortType = new ArrayList<>();
    private ArrayList<Field> intType = new ArrayList<>();
    private ArrayList<Field> longType = new ArrayList<>();
    private ArrayList<Field> charType = new ArrayList<>();
    private ArrayList<Field> floatType = new ArrayList<>();
    private ArrayList<Field> doubleType = new ArrayList<>();
    private ArrayList<Field> fixedLengthType = new ArrayList<>();
    private ArrayList<Field> dynamicStringType = new ArrayList<>();
    private int indexLength;

    public PikoGeneratorBlueprint(Class clazz) throws IllegalArgumentException {
        this.clazz = clazz;
        this.indexLength = 0;
        for (Field field : clazz.getDeclaredFields()) {

            // exception for these fields because android specifics
            if (field.getName().equals("serialVersionUID") ||
                    field.getName().equals("$change")) {
                continue;
            }
            Class<?> fieldType = field.getType();
            if (fieldType == Boolean.TYPE) {
                booleanType.add(field);
            } else if (fieldType == Byte.TYPE) {
                byteType.add(field);
            } else if (fieldType == Short.TYPE) {
                shortType.add(field);
            } else if (fieldType == Integer.TYPE) {
                intType.add(field);
            } else if (fieldType == Long.TYPE) {
                longType.add(field);
            } else if (fieldType == Character.TYPE) {
                charType.add(field);
            } else if (fieldType == Float.TYPE) {
                floatType.add(field);
            } else if (fieldType == Double.TYPE) {
                doubleType.add(field);
            } else if (fieldType == String.class) {
                // must have annotation
                if (field.getAnnotation(PikoFixedLength.class) != null) {
                    fixedLengthType.add(field);
                } else if (field.getAnnotation(PikoString.class) != null) {
                    dynamicStringType.add(field);
                } else {
                    throw new IllegalArgumentException("String type must be either PikoFixedLength or PikoString");
                }
            }
            indexLength++;
            // TODO : next version using tail bit
            //if(indexLength % 8 == 0) indexLength ++;
        }
    }

    public Class getClazz() {
        return clazz;
    }

    public int getIndexLength() {
        return indexLength;
    }

    public ArrayList<Field> getBooleanType() {
        return booleanType;
    }

    public ArrayList<Field> getByteType() {
        return byteType;
    }

    public ArrayList<Field> getShortType() {
        return shortType;
    }

    public ArrayList<Field> getIntType() {
        return intType;
    }

    public ArrayList<Field> getLongType() {
        return longType;
    }

    public ArrayList<Field> getCharType() {
        return charType;
    }

    public ArrayList<Field> getFloatType() {
        return floatType;
    }

    public ArrayList<Field> getDoubleType() {
        return doubleType;
    }

    public ArrayList<Field> getFixedLengthType() {
        return fixedLengthType;
    }

    public ArrayList<Field> getDynamicStringType() {
        return dynamicStringType;
    }
}
