package com.gwk.pikodove.parser;

import android.util.Pair;

import com.gwk.pikodove.annotation.PikoFixedLength;
import com.gwk.pikodove.annotation.PikoString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Zavyra on 2017-03-25.
 */

public class Piko {

    private final byte[] indexes;
    private final ByteArrayInputStream buffer;
    private int currentIndex;
    private int currentByte;
    private int currentMask;

    public Piko(byte[] bytesArray, PikoParserBlueprint blueprint) throws IOException {
        buffer = new ByteArrayInputStream(bytesArray);
        indexes = new byte[(int) Math.ceil(blueprint.getIndexLength() / (float)Byte.SIZE)];
        buffer.read(indexes);
        currentIndex = 0;
        currentByte = 0;
        currentMask = 0x100;
    }

    public boolean readNextBoolean() {
        return isSet();
    }

    public byte readNextByte() {
        if(isSet()) {
            byte[] bytes = new byte[1];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bytes[0];
        }
        return 0;
    }

    public short readNextShort() {
        if(isSet()) {
            byte[] bytes = new byte[2];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ByteBuffer.wrap(bytes).getShort();
        }
        return 0;
    }

    public int readNextInt() {
        if(isSet()) {
            byte[] bytes = new byte[4];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ByteBuffer.wrap(bytes).getInt();
        }
        return 0;
    }

    public long readNextLong() {
        if(isSet()) {
            byte[] bytes = new byte[8];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ByteBuffer.wrap(bytes).getLong();
        }
        return 0;
    }

    public char readNextChar() {
        if(isSet()) {
            byte[] bytes = new byte[2];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ByteBuffer.wrap(bytes).getChar();
        }
        return 0;
    }

    public float readNextFloat() {
        if(isSet()) {
            byte[] bytes = new byte[4];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ByteBuffer.wrap(bytes).getFloat();
        }
        return 0;
    }

    public double readNextDouble() {
        if(isSet()) {
            byte[] bytes = new byte[8];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ByteBuffer.wrap(bytes).getDouble();
        }
        return 0;
    }

    public String readNextFixedString(int length) {
        if(isSet()) {
            byte[] bytes = new byte[length];
            try {
                buffer.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(bytes, Charset.forName("UTF-8"));
        }
        return null;
    }

    public String readNextString(char terminator) {
        ByteArrayOutputStream temp = new ByteArrayOutputStream(2);
        int c;
        do {
            c = buffer.read();
            if(c == terminator || c == -1) break;
            temp.write((char) c);
        } while (true);
        return new String(temp.toByteArray(), Charset.forName("UTF-8"));
    }

    public String readNextString() {
        return readNextString((char) 0x00);
    }

    private boolean isSet() {
        boolean b = (indexes[currentByte] & currentMask) != 0;
        incrementPointer();
        return b;
    }

    private void incrementPointer() {
        currentIndex++;
        currentMask >>= 1;
        if(currentIndex == 8) {
            currentIndex = 0;
            currentByte++;
            currentMask = 0x100;
        }
    }
}
