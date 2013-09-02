package com.onyx.android.sdk.ui.dialog;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.onyx.android.sdk.R;

import android.app.Dialog;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class BrightnessDialog extends Dialog
{

    private static final String TAG = "BrightnessDialog";

    private RatingBar mRatingBarLightSettings = null;

    /**
     * Brightness value for fully off
     */
    public static final int BRIGHTNESS_OFF = 0;

    /**
     * Brightness value for dim backlight
     */
    public static final int BRIGHTNESS_DIM = 20;

    /**
     * Brightness value for fully on
     */
    public static final int BRIGHTNESS_ON = 255;

    public static final int BRIGHTNESS_MINIMUM = BRIGHTNESS_OFF;
    public static final int BRIGHTNESS_MAXIMUM = BRIGHTNESS_ON;
    public static final int BRIGHTNESS_DEFAULT = BRIGHTNESS_MINIMUM + 20;

    private static final int LOW_BRIGHTNESS_INTERVAL = 2;
    // should be <= mRatingBarLightSettings.getNumStars()
    private static final int LOW_BRIGHTNESS_STEPS = 10;

    private static final int LOW_BRIGHTNESS_MAX = Math.min(BRIGHTNESS_MINIMUM + (LOW_BRIGHTNESS_INTERVAL * LOW_BRIGHTNESS_STEPS),
            BRIGHTNESS_MAXIMUM);

    private static final String mFrontLightFile = "/sys/devices/platform/rk29_backlight/backlight/rk28_bl/brightness";
    private static final int FRONT_LIGHT_OFF = 0;

    private Context mContext;



    public BrightnessDialog(Context context)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_brightness);

        mContext = context;

        mRatingBarLightSettings = (RatingBar) findViewById(R.id.ratingbar_light_settings);
        mRatingBarLightSettings.setFocusable(false);
        mRatingBarLightSettings.setOnRatingBarChangeListener(new OnRatingBarChangeListener()
        {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                setLightRatingBarProgress();
            }
        });
        assert(mRatingBarLightSettings.getNumStars() >= LOW_BRIGHTNESS_STEPS);

        setLightRatingBarDefaultProgress();

        ImageButton light_bar_down = (ImageButton) findViewById(R.id.imagebutton_light_down);
        light_bar_down.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mRatingBarLightSettings.setProgress(mRatingBarLightSettings.getProgress() - 1);
            }
        });

        ImageButton light_add = (ImageButton) findViewById(R.id.imagebutton_light_add);
        light_add.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mRatingBarLightSettings.getProgress() == mRatingBarLightSettings.getMax()) {
                    setLightRatingBarProgress();
                }
                else {
                    mRatingBarLightSettings.setProgress(mRatingBarLightSettings.getProgress() + 1);
                }
            }
        });
        
        this.setCanceledOnTouchOutside(true);

    }

    private void setLightRatingBarDefaultProgress()
    {
        int value = 0;
        try {
            value = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException snfe) {
            value = BRIGHTNESS_DEFAULT;
        }

        int rating = getRatingOfBrightness(value);
        Log.d(TAG, "rating : "+rating);
        mRatingBarLightSettings.setProgress(rating);
    }

    private void setLightRatingBarProgress()
    {
        int value = getBrightnessOfRating(mRatingBarLightSettings.getProgress());
        Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);

        setFrontLightValue(value);
    }

    private int getBrightnessOfRating(int value)
    {
        if (value <= LOW_BRIGHTNESS_STEPS) {
            return value * LOW_BRIGHTNESS_INTERVAL + BRIGHTNESS_MINIMUM;
        } else if (value == mRatingBarLightSettings.getMax()) {
            return BRIGHTNESS_ON;
        }
        else {
            int big_interval = (BRIGHTNESS_MAXIMUM - LOW_BRIGHTNESS_MAX) / (mRatingBarLightSettings.getNumStars() - LOW_BRIGHTNESS_STEPS);
            return big_interval * (value - LOW_BRIGHTNESS_STEPS) + LOW_BRIGHTNESS_MAX;
        }
    }

    private int getRatingOfBrightness(int value)
    {
        if (value <= LOW_BRIGHTNESS_MAX) {
            return (value - BRIGHTNESS_MINIMUM) / LOW_BRIGHTNESS_INTERVAL;
        }
        else {
            int big_interval = (BRIGHTNESS_MAXIMUM - LOW_BRIGHTNESS_MAX) / (mRatingBarLightSettings.getNumStars() - LOW_BRIGHTNESS_STEPS);
            return LOW_BRIGHTNESS_STEPS + (value - LOW_BRIGHTNESS_MAX) / big_interval;
        }
    }


    public int getFrontLightValue()
    {
        int value = readFrontLightFile();
        return value;
    }

    public void setFrontLightValue(int value)
    {
        writeFrontLightFile(value);
    }

    public boolean openFrontLight()
    {
        int value = getFrontLightValueFromProvider();
        if (value == FRONT_LIGHT_OFF) {
            value = BRIGHTNESS_DEFAULT;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
        }

        if(readFrontLightFile() == FRONT_LIGHT_OFF) {
            writeFrontLightFile(value);
            return true;
        } else {
            return false;
        }
    }

    public boolean closeFrontLight()
    {
        writeFrontLightFile(FRONT_LIGHT_OFF);
        return true;
    }

    private int readFrontLightFile()
    {
        int value = 0;
        try {
            File f = new File(mFrontLightFile);
            Scanner s = new Scanner(f);
            if(s.hasNextInt()) {
                value = s.nextInt();
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }

    private int getFrontLightValueFromProvider()
    {
        int res = 0;
        int light_value;
        try {
            light_value = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException snfe) {
            light_value = BRIGHTNESS_DEFAULT;
        }

        res = light_value;
        return res;
    }

    private void writeFrontLightFile(int value)
    {
        Log.d(TAG, "Set brightness: " + value);

        String message = Integer.toString(value);
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(mFrontLightFile);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if (fout != null) {
                try {
                    fout.close();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}

