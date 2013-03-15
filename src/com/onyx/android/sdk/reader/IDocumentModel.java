/**
 * 
 */
package com.onyx.android.sdk.reader;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.onyx.android.sdk.data.cms.OnyxMetadata;
import com.onyx.android.sdk.ui.data.TOCItem;

/**
 * @author joy
 *
 */
public interface IDocumentModel
{
    boolean canOpen(String path);
    boolean isOpened();
    /**
     * currently opened file path
     * @return
     */
    String getFilePath();
    boolean openFile(String path);
    boolean close();
    
    /**
     * return null if failed
     * 
     * @return
     */
    OnyxMetadata getMetadata();
    
    /**
     * interrupt all task being processed until resume() being called
     */
    void interrupt();
    void resume();
    
    DocPageLayout getPageLayout();
    DocPagingMode getPagingMode();
    boolean setPagingMode(DocPagingMode mode);
    int getPageCount();
    
    double getPagePosition();
    double getPagePositionOfLocation(String location);
    boolean gotoPagePosition(double page);
    boolean gotoDocLocation(String location);
    
    String getScreenText();
    
    /**
     * reflowable document can be navigated by screen
     * 
     * @return
     */
    boolean previousScreen();
    boolean nextScreen();
    
    boolean setFontSize(double size);

    Size getPageNaturalSize();
    Rect getPageContentArea();
    
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
    
    boolean isLocationInCurrentScreen(String location);
    String getScreenBeginningLocation();
    String getScreenEndLocation();
    
    boolean isGlyphEmboldenEnabled();
    boolean setGlyphEmboldenEnabled(boolean enable);
    
    DocTextSelection hitTestWord(int x, int y);
    DocTextSelection moveSelectionBegin(int x, int y);
    DocTextSelection moveSelectionEnd(int x, int y);
    
    DocTextSelection measureSelection(String locationBegin, String locationEnd);
    
    ArrayList<DocTextSelection> searchInCurrentScreen(String pattern);
    String searchForwardAfterCurrentScreen(String pattern);
    String searchBackwardBeforeCurrentScreen(String pattern);
    String[] searchAllInDocument(String pattern);
}
