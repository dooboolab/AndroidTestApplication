package org.hyochan.testapplication.pinchzoom_test;

import java.io.Serializable;

/**
 * Created by hyochan on 2016-08-23.
 */
public class MyItem implements Serializable {
    private int drawable;
    private String txt;

    public MyItem(int drawable, String txt) {
        this.drawable = drawable;
        this.txt = txt;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
