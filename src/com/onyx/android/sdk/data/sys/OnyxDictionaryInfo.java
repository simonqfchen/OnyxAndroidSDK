/**
 * 
 */
package com.onyx.android.sdk.data.sys;

import android.content.Intent;

/**
 * @author joy
 *
 */
public class OnyxDictionaryInfo
{
    public static final OnyxDictionaryInfo PREDEFINED_DICTS[] = {
        new OnyxDictionaryInfo("QuickDic", "QuickDic Dictionary", "com.hughes.android.dictionary", "com.hughes.android.dictionary.DictionaryManagerActivity", Intent.ACTION_SEARCH, 0),
        new OnyxDictionaryInfo("ColorDict", "ColorDict", "com.socialnmobile.colordict", "com.socialnmobile.colordict.activity.Main", Intent.ACTION_SEARCH, 0),
        new OnyxDictionaryInfo("Fora", "Fora Dictionary", "com.ngc.fora", "com.ngc.fora.ForaDictionary", Intent.ACTION_SEARCH, 0),
        new OnyxDictionaryInfo("FreeDictionary.org", "Free Dictionary.org","org.freedictionary", "org.freedictionary.MainActivity", Intent.ACTION_VIEW, 0),
    };
    
    public final String id;
    public final String name;
    public final String packageName;
    public final String className;
    public final String action;
    public final Integer internal;
    
    private OnyxDictionaryInfo(String id, String name, String packageName, String className, String action, Integer internal ) 
    {
        this.id = id;
        this.name = name;
        this.packageName = packageName;
        this.className = className;
        this.action = action;
        this.internal = internal;
    }
    
    /**
     * return null if not found
     * 
     * @param dictId
     * @return
     */
    public static OnyxDictionaryInfo findDict(String dictId)
    {
        for (OnyxDictionaryInfo di : PREDEFINED_DICTS) {
            if (di.id.equalsIgnoreCase(dictId)) {
                return di;
            }
        }
        
        return null;
    }
}
