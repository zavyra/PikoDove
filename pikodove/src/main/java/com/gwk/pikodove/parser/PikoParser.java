package com.gwk.pikodove.parser;

import com.gwk.pikodove.annotation.PikoFixedLength;
import com.gwk.pikodove.annotation.PikoString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Zavyra on 2017-03-25.
 *
 */

public class PikoParser {

    public static Object fromPiko(byte[] bytesArray, PikoParserBlueprint blueprint) throws IllegalAccessException, InstantiationException, NoSuchFieldException, IOException {
        Object o = blueprint.getClazz().newInstance();
        ByteArrayInputStream buffer = new ByteArrayInputStream(bytesArray);
        byte[] indexes = new byte[(int) Math.ceil(blueprint.getIndexLength() / (float)Byte.SIZE)];
        buffer.read(indexes);

        int currentIndex = 0;
        for (byte index : indexes) {
            if(currentIndex >= blueprint.getIndexLength()) break;
            for ( int mask = 0x100; mask != 0x01; mask >>= 1 ) {
                boolean value = ( index & mask ) != 0;
                if(currentIndex >= blueprint.getIndexLength()) break;
                if(value) {
                    Field field = blueprint.getFields().get(currentIndex);
                    Class<?> fieldType = field.getType();
                    if (fieldType == Boolean.TYPE) {
                        field.setBoolean(o, true);
                    } else if (fieldType == Byte.TYPE) {
                        byte[] bytes = new byte[1];
                        buffer.read(bytes);
                        field.setByte(o, bytes[0]);
                    } else if (fieldType == Short.TYPE) {
                        byte[] bytes = new byte[Short.SIZE / Byte.SIZE];
                        buffer.read(bytes);
                        field.setShort(o, ByteBuffer.wrap(bytes).getShort());
                    } else if (fieldType == Integer.TYPE) {
                        byte[] bytes = new byte[Integer.SIZE / Byte.SIZE];
                        buffer.read(bytes);
                        field.setInt(o, ByteBuffer.wrap(bytes).getInt());
                    } else if (fieldType == Long.TYPE) {
                        byte[] bytes = new byte[Long.SIZE / Byte.SIZE];
                        buffer.read(bytes);
                        field.setLong(o, ByteBuffer.wrap(bytes).getLong());
                    } else if (fieldType == Character.TYPE) {
                        byte[] bytes = new byte[Character.SIZE / Byte.SIZE];
                        buffer.read(bytes);
                        field.setChar(o, ByteBuffer.wrap(bytes).getChar());
                    } else if (fieldType == Float.TYPE) {
                        byte[] bytes = new byte[Float.SIZE / Byte.SIZE];
                        buffer.read(bytes);
                        field.setFloat(o, ByteBuffer.wrap(bytes).getFloat());
                    } else if (fieldType == Double.TYPE) {
                        byte[] bytes = new byte[Double.SIZE / Byte.SIZE];
                        buffer.read(bytes);
                        field.setDouble(o, ByteBuffer.wrap(bytes).getDouble());
                    } else if (fieldType.equals(String.class)) {
                        // must have annotation
                        if (field.getAnnotation(PikoFixedLength.class) != null) {
                            int length = field.getAnnotation(PikoFixedLength.class).value();
                            byte[] bytes = new byte[length];
                            buffer.read(bytes);
                            field.set(o, new String(bytes, Charset.forName("UTF-8")));
                        } else if (field.getAnnotation(PikoString.class) != null) {
                            char terminator = field.getAnnotation(PikoString.class).value();
                            ByteArrayOutputStream temp = new ByteArrayOutputStream(2);
                            int c;
                            do {
                                c = buffer.read();
                                if(c == terminator || c == -1) break;
                                temp.write((char) c);
                            } while (true);
                            field.set(o, new String(temp.toByteArray(), Charset.forName("UTF-8")));
                        } else {
                            throw new IllegalArgumentException("String type must be either PikoFixedLength or PikoString");
                        }
                    }
                }
                currentIndex++;
            }
        }
        return o;
    }
    static String toBinary(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for( int i = 0; i < Byte.SIZE * bytes.length; i++ ) {
            if(i % 8 == 0 && i != 0) {
                sb.append(" ");
            }
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }
}
