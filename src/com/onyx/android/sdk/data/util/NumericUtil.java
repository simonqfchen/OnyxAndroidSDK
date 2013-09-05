/**
 * 
 */
package com.onyx.android.sdk.data.util;

/**
 * @author joy
 *
 */
public abstract class NumericUtil
{
    public static boolean equalsAlmost(double a, double b)
    {
        return Math.abs(a - b) <= 0.00001;
    }
}
