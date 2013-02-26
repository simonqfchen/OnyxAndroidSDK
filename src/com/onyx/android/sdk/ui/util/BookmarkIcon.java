package com.onyx.android.sdk.ui.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

public class BookmarkIcon
{
    public static Bitmap drawTriangle(boolean isBookmark) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        if (isBookmark) {
            paint.setStyle(Style.FILL);
        }
        else {
            paint.setStyle(Style.STROKE);
        }

        Bitmap bitmap = Bitmap.createBitmap(120, 120, Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);

        Path path = new Path();
        path.moveTo(100, 0);
        path.lineTo(120, 20);
        path.lineTo(120, 0);
        path.lineTo(100, 0);
        c.drawPath(path, paint);

        return bitmap;
    }
}
