package com.onyx.android.sdk.reader;

import android.os.ParcelFileDescriptor;

import com.onyx.android.sdk.reader.DocPagingMode;
import com.onyx.android.sdk.reader.RenderParameter;

/**
 * @author joy
 *
 */
interface IRemoteRenderService
{
    boolean openFile(String path, String password,
            String deviceName, String deviceSerial, String appStorage);
    void close();

    ParcelFileDescriptor renderPage(in DocPagingMode pagingMode, in RenderParameter param,
            boolean isPrecache, out RenderParameter resultParam);
            
    /**
     * because page content area need to be calculated at runtime by rendering,
     * so the value is cached as the side effect of page rendering for further use
     */
    Rect getPageContentArea(double pos);
}
