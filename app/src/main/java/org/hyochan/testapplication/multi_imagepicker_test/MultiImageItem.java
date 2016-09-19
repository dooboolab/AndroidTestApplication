package org.hyochan.testapplication.multi_imagepicker_test;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.Serializable;

/**
 * Created by hyochan on 2016-08-24.
 */
public class MultiImageItem implements Serializable{
    private int id;
    private String thumbUri;
    private String strUri;
    private String imageName; // imageName가 없으면 intent로 부터 받아왔다는 이야기, 있으면 파일로부터 받아옴

    public MultiImageItem(String strUri, String imgName) {
        this.strUri = strUri;
        this.imageName = imgName;

        File thumbFile = new File(Environment.getExternalStorageDirectory(), "/TestApplication/thumb_" + imgName);
        this.thumbUri = Uri.fromFile(thumbFile).toString();
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public String getStrUri() {
        return strUri;
    }

    public void setStrUri(String strUri) {
        this.strUri = strUri;
    }

    public String getimageName() {
        return imageName;
    }

    public void setimageName(String imageName) {
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
