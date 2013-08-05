/**
 * 
 */
package com.onyx.android.sdk.data.util;

import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;

/**
 * @author joy
 *
 */
public class MemoryFileUtil {
    private static final Method sMethodGetParcelFileDescriptor;
    private static final Method sMethodGetFileDescriptor;
    static {
        sMethodGetParcelFileDescriptor = get("getParcelFileDescriptor");
        sMethodGetFileDescriptor = get("getFileDescriptor");
    }

    public static ParcelFileDescriptor getParcelFileDescriptor(MemoryFile file) {
        try {
            return (ParcelFileDescriptor) sMethodGetParcelFileDescriptor.invoke(file);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileDescriptor getFileDescriptor(MemoryFile file) {
        try {
            return (FileDescriptor) sMethodGetFileDescriptor.invoke(file);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Method get(String name) {
        try {
            return MemoryFile.class.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}