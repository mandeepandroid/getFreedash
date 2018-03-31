package com.getfreedash.Utils;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Bytenome-01 on 3/13/2018.
 */

public class Toast  {
    Activity context;
    public Toast( Activity context){
        this.context=context;
    }
    public void taost(Activity context,String msg){
        android.widget.Toast.makeText(context,msg, android.widget.Toast.LENGTH_SHORT).show();

    }
}
