/**
 * 
 */
package com.onyx.android.sdk.ui.dialog.data;

import android.widget.LinearLayout;

/**
 * @author joy
 *
 */
public interface IReaderMenuHandler
{
    static enum LineSpacingProperty {normal, big, small, decreases, enlarge};
    static enum RotationScreenProperty {rotation_0, rotation_90, rotation_180, rotation_270};
    static enum FontSizeProperty {increase, decrease};

    int getPageIndex();
    int getPageCount();

    void nextPage();
    void previousPage();
    void gotoPage(int i);

    void updateCurrentPage(LinearLayout l);

    void increaseFontSize();
    void decreaseFontSize();
    void changeFontsize(FontSizeProperty property);

    void toggleFontEmbolden();

    void showSetFontView();
    public String getFontFace();
    void setFontFace();

    void rotationScreen(int i);
    void changeRotationScreen(int orientation);

    void showLineSpacingView();
    void setLineSpacing(LineSpacingProperty property);

    void showTOC();
    void showBookMarks();
    void showTTsView();
    void showAnnotation();

    void searchContent();
    void startDictionary();
    void showGoToPageDialog();

    void zoomToPage();
    void zoomToWidth();
    void zoomToHeight();
    void zoomBySelection();
    void zoomByTwoPoints();
    void zoomByValue(double z);
    void zoomIn();
    void zoomOut();

    void toggleFullscreen();
    boolean showZoomSettings();
    boolean showSpacingSettings();
    boolean canChangeFontFace();
    boolean isFullscreen();
    void setScreenRefresh();
    void showReaderSettings();

    boolean ttsIsSpeaking();
    void ttsInit();
    void ttsSpeak();
    void ttsPause();
    void ttsStop();
}
