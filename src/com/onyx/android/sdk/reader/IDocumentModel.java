/**
 * 
 */
package com.onyx.android.sdk.reader;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.onyx.android.sdk.data.cms.OnyxMetadata;

/**
 * @author joy
 *
 */
public interface IDocumentModel
{
    /**
     * interface to handle all callbacks
     * 
     * @author joy
     *
     */
    static interface DocumentCallbackListener {
        void onRequestDocumentPassword();
        
        /**
         * extensible callback interface
         * 
         * @param r
         */
        void onCallback(Runnable r);
    }
    void setDocumentCallbackListener(DocumentCallbackListener l);
    
    boolean canOpen(String path);
    
    boolean isOpened();
    /**
     * currently opened file path
     * @return
     */
    String getFilePath();
    
    boolean openFile(String path);
    boolean close();
    
    void setDocumentPassword(String password);
    
    /**
     * interrupt all task being processed until resume() being called
     */
    void interrupt();
    void resume();
    
    /**
     * return null if failed
     * 
     * @return
     */
    OnyxMetadata readMetadata();
    
    int getPageCount();
    
    DocPageLayout getPageLayout();
    DocPagingMode getPagingMode();
    boolean setPagingMode(DocPagingMode mode);
    
    double getPagePosition();
    double getPagePositionOfLocation(String location);
    boolean gotoPagePosition(double page);
    boolean gotoDocLocation(String location);
    
    /**
     * reflowable document can be navigated by screen
     * 
     * @return
     */
    boolean previousScreen();
    boolean nextScreen();

    int compareLocation(String loc1, String loc2);
    boolean isLocationInCurrentScreen(String location);
    String getScreenBeginningLocation();
    String getScreenEndLocation();
    
    Size getPageNaturalSize();
    Rect getPageContentArea();
    
    String getDocumentText(String locationBegin, String locationEnd);
    String getScreenText();
    
    /**
     * navigate the page using specified navigation arguments, but no rendering
     * 
     * @param zoom
     * @param scrollX
     * @param scrollY
     * @return
     */
    boolean navigatePage(double zoom, int scrollX, int scrollY);
        
    /**
     * return null when failed
     * 
     * @param page
     * @param zoom
     * @param left
     * @param top
     * @param width
     * @param height
     * @return
     */
    Bitmap renderPage(double zoom, int left, int top, int width, int height, Bitmap.Config conf, boolean isPrefetch);
    
    boolean hasTOC();
    /**
     * return null if failed
     * 
     * @return
     */
    TOCItem[] getTOC();
    
    boolean setFontSize(double size);
    
    boolean isGlyphEmboldenEnabled();
    boolean setGlyphEmboldenEnabled(boolean enable);
    
    DocTextSelection hitTestWord(int x, int y);
    DocTextSelection moveSelectionBegin(int x, int y);
    DocTextSelection moveSelectionEnd(int x, int y);
    
    DocTextSelection measureSelection(String locationBegin, String locationEnd);
    
    DocTextSelection hitTestSentence(String sentenceBegin);
    
    /**
     * return null if failed
     * 
     * @param pattern
     * @return
     */
    List<DocTextSelection> searchInCurrentScreen(String pattern);
    /**
     * return null if failed
     * 
     * @param pattern
     * @return
     */
    String searchForwardAfterCurrentScreen(String pattern);
    /**
     * return null if failed
     * 
     * @param pattern
     * @return
     */
    String searchBackwardBeforeCurrentScreen(String pattern);
    /**
     * return null if failed
     * 
     * @param pattern
     * @return
     */
    String[] searchAllInDocument(String pattern);
}
