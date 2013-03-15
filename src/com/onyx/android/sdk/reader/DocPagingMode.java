/**
 * 
 */
package com.onyx.android.sdk.reader;

/**
 * @author joy
 *
 */
public enum DocPagingMode
{
    None, 
    Hard_Pages, // single-page-based view that only shows a single page at a time 
    Hard_Pages_2Up, // double-page-based view that shows 2 pages at a time
    Flow_Pages, //  a paginated view, where a screen takes up the whole viewport and the content is reflowed
    Scroll_Pages, // scrollable page-based view showing a sequence of pages
    Scroll, // HTML-browser-like view that can be scrolled and does not have pages
}
