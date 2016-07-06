package com.deepdownstudios.util;

import android.support.annotation.NonNull;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple decorator covers InputStream the way DataInputStream does... but reads Little-endian byte order.
 * It doesn't override DataInputStream as 1) there is no point and 2) most relevant methods
 * in DataInputStream are declared final.
 *
 * Basically lifted from here:
 * https://raw.githubusercontent.com/janinko/ultimasdk/master/src/main/java/eu/janinko/Andaria/ultimasdk/utils/LittleEndianDataInputStream.java
 */
public class LittleEndianDataInputStream extends InputStream implements DataInput {

    public LittleEndianDataInputStream(InputStream in) {
        this.in = in;
        this.d = new DataInputStream(in);
        w = new byte[8];
    }

    @Override
    public int available() throws IOException {
        return d.available();
    }


    public final short readShort() throws IOException
    {
        d.readFully(w, 0, 2);
        return (short)(
                (w[1]&0xff) << 8 |
                        (w[0]&0xff));
    }

    /**
     * Note, returns int even though it reads a short.
     */
    public final int readUnsignedShort() throws IOException
    {
        d.readFully(w, 0, 2);
        return (
                (w[1]&0xff) << 8 |
                        (w[0]&0xff));
    }

    /**
     * like DataInputStream.readChar except little endian.
     * This must read UTF-16.  Reading UTF-16 in little-endian is pretty worthless
     * since Java is the most likely origin of UTF-16 and big-endian is the native Java
     * format.  So... if you are using this, its probably a mistake.
     */
    public final char readChar() throws IOException
    {
        d.readFully(w, 0, 2);
        return (char) (
                (w[1]&0xff) << 8 |
                        (w[0]&0xff));
    }

    /**
     * like DataInputStream.readInt except little endian.
     */
    public final int readInt() throws IOException
    {
        d.readFully(w, 0, 4);
        return
                (w[3])      << 24 |
                        (w[2]&0xff) << 16 |
                        (w[1]&0xff) <<  8 |
                        (w[0]&0xff);
    }

    /**
     * like DataInputStream.readLong except little endian.
     */
    public final long readLong() throws IOException
    {
        d.readFully(w, 0, 8);
        return
                (long)(w[7])      << 56 |
                        (long)(w[6]&0xff) << 48 |
                        (long)(w[5]&0xff) << 40 |
                        (long)(w[4]&0xff) << 32 |
                        (long)(w[3]&0xff) << 24 |
                        (long)(w[2]&0xff) << 16 |
                        (long)(w[1]&0xff) <<  8 |
                        (long)(w[0]&0xff);
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final int read(@NonNull byte b[], int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public final void readFully(@NonNull byte b[]) throws IOException {
        d.readFully(b, 0, b.length);
    }

    public final void readFully(@NonNull byte b[], int off, int len) throws IOException {
        d.readFully(b, off, len);
    }

    public final int skipBytes(int n) throws IOException {
        return d.skipBytes(n);
    }

    public final boolean readBoolean() throws IOException {
        return d.readBoolean();
    }

    public final byte readByte() throws IOException {
        return d.readByte();
    }

    public int read() throws IOException {
        return in.read();
    }

    public final int readUnsignedByte() throws IOException {
        return d.readUnsignedByte();
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public final String readLine() throws IOException {
        return d.readLine();
    }

    public final String readUTF() throws IOException {
        return d.readUTF();
    }

    @Override
    public final void close() throws IOException {
        d.close();
    }

    private DataInputStream d; // to get at high level readFully methods of DataInputStream
    protected InputStream in; // to get at the low-level read methods of InputStream
    private byte w[]; // work array for buffering input
}
