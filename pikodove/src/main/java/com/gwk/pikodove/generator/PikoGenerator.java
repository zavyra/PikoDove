package com.gwk.pikodove.generator;

import com.gwk.pikodove.annotation.PikoString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Helper to generate Piko compatible data
 * Created by Zavyra on 2017-03-25.
 */

public class PikoGenerator {

    public static byte[] fromClass(Object object, PikoGeneratorBlueprint blueprint) throws NoSuchFieldException, IllegalAccessException {
        // buffer bytes
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        // start write indexing
        byte[] indexes = new byte[(int) Math.ceil(blueprint.getIndexLength() / 8f)];
        Class<?> clazz = object.getClass();
        int currentBit = 0;
        int currentByte = 0;

        // boolean first
        for (Field field : blueprint.getBooleanType()) {
            boolean value = clazz.getField(field.getName()).getBoolean(object);
            if(value) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit));
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        // then byte type
        for (Field field : blueprint.getByteType()) {
            byte value = clazz.getField(field.getName()).getByte(object);
            if(value != 0) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                buffer.write(value);
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }
        // then short type
        for (Field field : blueprint.getShortType()) {
            short value = clazz.getField(field.getName()).getShort(object);
            if(value != 0) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                int maxByte = Short.SIZE / Byte.SIZE;
                byte[] result = new byte[maxByte];
                for (int i = maxByte - 1; i >= 0; i--) {
                    result[i] = (byte)(value & 0xFF);
                    value >>= Byte.SIZE;
                }
                try {
                    buffer.write(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        // then int type
        for (Field field : blueprint.getIntType()) {
            int value = clazz.getField(field.getName()).getInt(object);
            if(value != 0) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                int maxByte = Integer.SIZE / Byte.SIZE;
                byte[] result = new byte[maxByte];
                for (int i = maxByte - 1; i >= 0; i--) {
                    result[i] = (byte)(value & 0xFF);
                    value >>= Byte.SIZE;
                }
                try {
                    buffer.write(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        // then long type
        for (Field field : blueprint.getLongType()) {
            long value = clazz.getField(field.getName()).getLong(object);
            if(value != 0) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                int maxByte = Long.SIZE / Byte.SIZE;
                byte[] result = new byte[maxByte];
                for (int i = maxByte - 1; i >= 0; i--) {
                    result[i] = (byte)(value & 0xFF);
                    value >>= Byte.SIZE;
                }
                try {
                    buffer.write(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        // then char type
        for (Field field : blueprint.getCharType()) {
            char value = clazz.getField(field.getName()).getChar(object);
            if(value != 0) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                int maxByte = Character.SIZE / Byte.SIZE;
                byte[] result = new byte[maxByte];
                for (int i = maxByte - 1; i >= 0; i--) {
                    result[i] = (byte)(value & 0xFF);
                    value >>= Byte.SIZE;
                }
                try {
                    buffer.write(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        // then float type
        for (Field field : blueprint.getFloatType()) {
            float value = clazz.getField(field.getName()).getFloat(object);
            if(value != 0) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                int maxByte = Float.SIZE / Byte.SIZE;
                try {
                    buffer.write(ByteBuffer.allocate(maxByte).putFloat(value).array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        // then double type
        for (Field field : blueprint.getDoubleType()) {
            double value = clazz.getField(field.getName()).getDouble(object);
            if(value != 0) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                int maxByte = Double.SIZE / Byte.SIZE;
                try {
                    buffer.write(ByteBuffer.allocate(maxByte).putDouble(value).array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        // then fixed String type
        for (Field field : blueprint.getFixedLengthType()) {
            String value = (String) clazz.getField(field.getName()).get(object);
            if(value != null && !value.isEmpty()) {
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                try {
                    buffer.write(value.getBytes(Charset.forName("UTF-8")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }
        // then for the last dynamic String type
        for (Field field : blueprint.getDynamicStringType()) {
            String value = (String) clazz.getField(field.getName()).get(object);
            if(value != null && !value.isEmpty()) {
                PikoString annotation = field.getAnnotation(PikoString.class);
                value += annotation.value();
                indexes[currentByte] |= ( 1 << ( 7 - currentBit ));
                try {
                    buffer.write(value.getBytes(Charset.forName("UTF-8")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // increment index
            currentBit++;
            if(currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }
        byte[] content = buffer.toByteArray();
        ByteBuffer wrap = ByteBuffer.allocate(indexes.length + content.length);
        wrap.put(indexes);
        wrap.put(content);
        return wrap.array();
    }



}
