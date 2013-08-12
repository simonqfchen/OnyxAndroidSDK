/**
 *
 */
package com.onyx.android.sdk.tts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.onyx.android.sdk.data.util.ProfileUtil;

/**
 * @author dxwts
 *
 */
public class OnyxTtsSpeaker implements TextToSpeech.OnUtteranceCompletedListener
{

    private static final String TAG = "OnyxTtsSpeaker";

    public static interface OnSpeakerCompletionListener
    {
        void onSpeakerCompletion();
    }
    private OnSpeakerCompletionListener mOnSpeakerCompletionListener = null;
    public void setOnSpeakerCompletionListener(OnSpeakerCompletionListener l)
    {
        mOnSpeakerCompletionListener = l;
    }
    private void notifyOnSpeakerCompletionListener()
    {
        if (mOnSpeakerCompletionListener != null) {
            mOnSpeakerCompletionListener.onSpeakerCompletion();
        }
    }

    private static final String UTTERANCE_ID = TAG;

    private Context mContext = null;
    private TextToSpeech mTtsService = null;
    private MediaPlayer mPlayer = new MediaPlayer();

    private Object mTtsLocker = new Object();
    private volatile PowerManager.WakeLock mWakeLock;
    private boolean mIsActive = false;
    private boolean mTtsSpeaking = false;
    private boolean mTtsPaused = false;

    public OnyxTtsSpeaker(Context context)
    {
        mContext = context;
        mTtsService = new TextToSpeech(mContext, new TextToSpeech.OnInitListener()
        {

            @Override
            public void onInit(int status)
            {
                onInitializationCompleted();
            }
        });
    }

    private void onInitializationCompleted()
    {
        Log.d(TAG, "onInitializationCompleted");

        mTtsService.setOnUtteranceCompletedListener(this);

        Locale locale = null;
        final String languageCode = mContext.getResources().getConfiguration().locale.getISO3Language();
        Log.v(TAG,"languageCode = " + languageCode);
        if ("other".equals(languageCode)) {
            locale = Locale.getDefault();
            if (mTtsService.isLanguageAvailable(locale) < 0) {
                locale = Locale.ENGLISH;
            }
        }
        else {
            try {
                locale = new Locale(languageCode);
            }
            catch (Exception e) {
            }
            if (locale == null || mTtsService.isLanguageAvailable(locale) < 0) {
                locale = Locale.getDefault();
                if (mTtsService.isLanguageAvailable(locale) < 0) {
                    locale = Locale.ENGLISH;
                }
            }
        }
        mTtsService.setLanguage(locale);
    }

    private void onPlayerCompletion()
    {
        synchronized (mTtsLocker) {
            mTtsSpeaking = false;
            OnyxTtsSpeaker.this.notifyOnSpeakerCompletionListener();
        }
    }

    @Override
    public void onUtteranceCompleted(String uttId)
    {
        Log.d(TAG, "onUtteranceCompleted");

        synchronized (mTtsLocker) {
            Log.d(TAG, "after synchronized");
            if (mIsActive && UTTERANCE_ID.equals(uttId)) {
                try {
//                    String wave_file = this.getTempWaveFile().getAbsolutePath();

                    if(mPlayer != null) {
                        mPlayer.release();
                    }
                    Log.d(TAG, "after second if");

                    mPlayer = new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                    {

                        @Override
                        public void onCompletion(MediaPlayer mp)
                        {
                            onPlayerCompletion();
                        }
                    });

                    Log.d(TAG, "ready to go");
                    ParcelFileDescriptor fileDescriptor;
                    try {
                        Log.d(TAG, "first line");
                        fileDescriptor = mTtsService.getSynthesizedFileDescriptor();
                        Log.d(TAG, "second line, descriptor null? "+(fileDescriptor == null));
                        Log.i(TAG, "file size by parcel file descriptor: "+fileDescriptor.getStatSize());
                        Log.d(TAG, "after get stat size");
                        mPlayer.setDataSource(fileDescriptor.getFileDescriptor());
                        mPlayer.prepare();
                        Log.d(TAG, "after prepare");
                    }
                    catch (RemoteException e1) {
                        Log.e(TAG, "jim remote exception occurred.");
                        e1.printStackTrace();
                        onPlayerCompletion();
                    }
                    catch (IOException e) {
                        Log.e(TAG, "jim exception 2", e);
                        onPlayerCompletion();
                    }
                    catch (IllegalStateException e) {
                        Log.e(TAG, "jim exception 3", e);
                        onPlayerCompletion();
                    }
                    catch (IllegalArgumentException  e) {
                        Log.e(TAG, "jim exception 4", e);
                        onPlayerCompletion();
                    }
                    Log.d(TAG, "after second try");

                    if (!mTtsPaused) {
                        mPlayer.start();
                    }
                }
                catch (IllegalArgumentException e) {
                    Log.e(TAG, "exception", e);
                }
                catch (IllegalStateException e) {
                    Log.e(TAG, "exception", e);
                }
            }
        }
    }

    public boolean isOpened()
    {
        return this.isActive() || this.isPaused();
    }

    /**
     * both stopped and paused will cause active to be false
     *
     * @return
     */
    public boolean isActive()
    {
        synchronized (mTtsLocker) {
            return mIsActive;
        }
    }

    @SuppressLint("Wakelock")
    private void setActive(boolean active)
    {
        Log.d(TAG, "setActive: " + active);
        synchronized (mTtsLocker) {
            mIsActive = active;

            if (active) {
                if (mWakeLock == null) {
                    PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE); 
                    mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
                    mWakeLock.acquire();
                }
            }
            else {
                if (mWakeLock != null) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        }
    }

    public boolean isPaused()
    {
        synchronized (mTtsLocker) {
            return mTtsPaused;
        }
    }

    /**
     * stop or wait current playing finished to start a new play
     *
     * @param text
     */
    public void startTts(String text)
    {
        synchronized (mTtsLocker) {
            if (mTtsSpeaking) {
                return;
            }

            if (text == null || text.trim().length() == 0) {
                return;
            }

            setActive(true);

            HashMap<String, String> callbackMap = new HashMap<String, String>();
            callbackMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);

            String wave_file = this.getTempWaveFile().getAbsolutePath();

            ProfileUtil.start(TAG, "synthesizeToFile");
            int res = mTtsService.synthesizeToFile(text, callbackMap, wave_file);
            ProfileUtil.end(TAG, "synthesizeToFile");

            if (res == TextToSpeech.ERROR) {
                Log.w(TAG, "TTS synthesize failed");
                this.setActive(false);
            }
            else {
                mTtsSpeaking = true;
            }
            mTtsPaused = false;
        }
    }

    public void pause()
    {
        synchronized (mTtsLocker) {
            setActive(false);
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
            mTtsPaused = true;
        }
    }

    public void resume()
    {
        synchronized (mTtsLocker) {
            setActive(true);
            if (mTtsPaused) {
                mPlayer.start();
                mTtsPaused = false;
            }
        }
    }

    public void stop()
    {
        synchronized (mTtsLocker) {
            setActive(false);
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.reset();
                mPlayer.release();
                mPlayer.setOnCompletionListener(null);
                mPlayer = null;

                mTtsPaused = false;
            }
            mTtsService.stop();
            mTtsSpeaking = false;
            mTtsPaused = false;
        }
    }

    public void shutdown()
    {
        assert(mTtsService != null);
        this.stop();
        mTtsService.shutdown();
    }

    private File getTempWaveFile()
    {
        File cache_folder = new File("/mnt/ramdisk");
        if (!cache_folder.exists()) {
            cache_folder = mContext.getExternalCacheDir();
        }

        return new File(cache_folder, "tts_temp.wav");
    }
}
