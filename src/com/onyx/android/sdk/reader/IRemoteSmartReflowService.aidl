package com.onyx.android.sdk.reader;

import android.os.ParcelFileDescriptor;

/**
 * @author joy
 *
 */
 interface IRemoteSmartReflowService
 {
     ParcelFileDescriptor reflowPage(in ParcelFileDescriptor bmp);
 }