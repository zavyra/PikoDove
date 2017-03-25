package com.gwk.pikodove.parser;

import com.gwk.pikodove.annotation.PikoFixedLength;
import com.gwk.pikodove.annotation.PikoString;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Zavyra on 2017-03-25.
 */

public class PikoParserBlueprint {

    private final Class clazz;
    private ArrayList<Field> fields = new ArrayList<>();
    private int indexLength;

    public PikoParserBlueprint(Class clazz) throws IllegalArgumentException {
        this.clazz = clazz;
        this.indexLength = 0;
        ArrayList<Field> booleanType = new ArrayList<>();
        ArrayList<Field> byteType = new ArrayList<>();
        ArrayList<Field> shortType = new ArrayList<>();
        ArrayList<Field> intType = new ArrayList<>();
        ArrayList<Field> longType = new ArrayList<>();
        ArrayList<Field> charType = new ArrayList<>();
        ArrayList<Field> floatType = new ArrayList<>();
        ArrayList<Field> doubleType = new ArrayList<>();
        ArrayList<Field> fixedLengthType = new ArrayList<>();
        ArrayList<Field> dynamicStringType = new ArrayList<>();
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


        // boolean first
        for (Field field : booleanType) {
            fields.add(field);
        }
        // then byte type
        for (Field field : byteType) {
            fields.add(field);
        }
        // then short type
        for (Field field : shortType) {
            fields.add(field);
        }
        // then int type
        for (Field field : intType) {
            fields.add(field);
        }

        // then long type
        for (Field field : longType) {
            fields.add(field);
        }
        // then char type
        for (Field field : charType) {
            fields.add(field);
        }
        // then float type
        for (Field field : floatType) {
            fields.add(field);
        }
        // then double type
        for (Field field : doubleType) {
            fields.add(field);
        }

        // then fixed String type
        for (Field field : fixedLengthType) {
            fields.add(field);
        }
        // then for the last dynamic String type
        for (Field field : dynamicStringType) {
            fields.add(field);
        }
    }

    public Class getClazz() {
        return clazz;
    }

    public int getIndexLength() {
        return indexLength;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

}
