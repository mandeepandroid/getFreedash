package com.getfreedash.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Bytenome-01 on 3/27/2018.
 */

public class FontManager  {
    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}