package com.gwk.pikodove.documentation;

import com.gwk.pikodove.annotation.PikoFixedLength;
import com.gwk.pikodove.annotation.PikoString;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Zavyra on 2017-03-25.
 */

public class PikoDocumentation {
    public static String fromClass(Class clazz) throws Exception{
        StringBuilder sb = new StringBuilder();

        // header
        sb.append("============================\n");
        sb.append(clazz.getName()).append("\n");
        sb.append("============================\n");

        // indexes
        sb.append("Index | Description\n");


        // make list type by priority :
        // 1. boolean
        // 2. short
        // 3. int
        // 4. long
        // 5. char
        // 6. float
        // 7. double
        // 8. fixed length string
        // 9. dynamic string

        ArrayList<Field> booleanType = new ArrayList<>();
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
                    throw new Exception("String type must be either PikoFixedLength or PikoString");
                }
            }
        }

        // generate list
        int index = 0;
        for (Field field : booleanType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (boolean) \n");
        }

        for (Field field : shortType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (short) \n");
        }

        for (Field field : intType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (integer) \n");
        }

        for (Field field : longType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (long) \n");
        }

        for (Field field : charType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (long) \n");
        }

        for (Field field : floatType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (float) \n");
        }

        for (Field field : doubleType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (double) \n");
        }

        for (Field field : fixedLengthType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (fixed length=")
                    .append(String.format("%x", field.getAnnotation(PikoFixedLength.class).value())).append(") \n");
        }

        for (Field field : dynamicStringType) {
            if((index + 1) % 8 == 0) index++;
            sb.append(String.format("%6d", index++)).append(" | ")
                    .append(field.getName()).append(" (termination=")
                    .append(String.format("%04x", (int) field.getAnnotation(PikoString.class).value())).append(") \n");
        }


        sb.append("============================\n");
        int closestByte = (int) Math.ceil(index / 7f);
        sb.append("Total Index : ").append(index).append(" bit -> ").append(closestByte).append(" bytes");
        return sb.toString();
    }
}
