/**
 * 
 */
package com.onyx.android.sdk.device;

import java.io.File;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import com.fndroid.epdControlApi;
import com.onyx.android.sdk.device.EpdController.EPDMode;
import com.onyx.android.sdk.device.EpdController.UpdateMode;

/**
 * @author joy
 *
 */
public class F430Factory implements IDeviceFactory
{
    
    public static class F430Controller implements IDeviceController
    {
        
        @Override
        public File getExternalStorageDirectory()
        {
            return Environment.getExternalStorageDirectory();
        }

        @Override
        public File getRemovableSDCardDirectory()
        {
            return Environment.getExternalStorageDirectory();
        }

        @Override
        public boolean isFileOnRemovableSDCard(File file)
        {
            return file.getAbsolutePath().startsWith(getRemovableSDCardDirectory().getAbsolutePath());
        }

        @Override
        public TouchType getTouchType(Context context)
        {
            return TouchType.Capacitive;
        }

        @Override
        public boolean hasWifi(Context context)
        {
            return true;
        }

        @Override
        public boolean hasAudio(Context context)
        {
            return true;
        }

        @Override
        public boolean hasFrontLight(Context context)
        {
            return true;
        }

        @Override
        public boolean isEInkScreen()
        {
            return true;
        }

        @Override
        public EPDMode getEpdMode()
        {
            int mode = epdControlApi.epdGetwavmode();
            switch (mode) {
            case 1:
                return EPDMode.AUTO;
            case 2:
                return EPDMode.AUTO;
            case 3:
                return EPDMode.AUTO;
            case 4:
                return EPDMode.AUTO_A2;
            default:
                assert(false);
                return EPDMode.AUTO;
            }
        }

        @Override
        public boolean setEpdMode(Context context, EPDMode mode)
        {
            switch (mode) {
            case AUTO:
            case AUTO_PART:
            case TEXT:
                epdControlApi.epdScale16();
                break;
            case AUTO_A2:
            case AUTO_BLACK_WHITE:
                epdControlApi.epdScale2();
                break;
            case FULL:
                epdControlApi.epdRedraw();
                break;
            default:
                epdControlApi.epdScale2();
                break;
            }
            
            return true;
        }

        @Override
        public void invalidate(View view, UpdateMode mode)
        {
            switch (mode) {
            case GU:
            case GU_FAST:
                view.invalidate();
                break;
            case GC:
                epdControlApi.epdfulldraw();
                view.invalidate();
                break;
            case DW:
                view.invalidate();
                break;
            default:
                break;
            }
            
            return;
        }

        @Override
        public void postInvalidate(View view, UpdateMode mode)
        {
            switch (mode) {
            case GU:
            case GU_FAST:
                view.postInvalidate();
                break;
            case GC:
                view.postInvalidate();
                epdControlApi.epdRedraw();
                break;
            case DW:
                view.postInvalidate();
                break;
            default:
                break;
            }
            
            return;
        }

        @Override
        public UpdateMode getViewDefaultUpdateMode(View view)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean setViewDefaultUpdateMode(View view, UpdateMode mode)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public UpdateMode getSystemDefaultUpdateMode()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean setSystemDefaultUpdateMode(UpdateMode mode)
        {
            // TODO Auto-generated method stub
            return false;
        }
        
    }

    @Override
    public String name()
    {
        return "F430";
    }

    @Override
    public boolean isPresent()
    {
        return Build.HARDWARE.equalsIgnoreCase("SP6820A");
    }

    @Override
    public IDeviceController createController()
    {
        return new F430Controller();
    }

}
