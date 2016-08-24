package org.hyochan.testapplication.pinchzoom_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.hyochan.testapplication.utils.ImgCacheUtil;

import java.lang.ref.WeakReference;

/**
 * Created by hyochan on 2016-08-18.
 */
public class PinchLoadThumbImgTask extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private Context context;

    public PinchLoadThumbImgTask(Context context, ImageView imageView) {
        this.context = context;
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(Integer... drawable) {
        // drawable을 bitmap으로 변환

        Bitmap bitmap = ImgCacheUtil.getInstance().getBitmap(drawable[0].toString());
        if(bitmap == null){
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), drawable[0]);
            bitmap = ThumbnailUtils.extractThumbnail(icon, 1280, 800);
            ImgCacheUtil.getInstance().addBitmap(drawable[0].toString(), bitmap);
        }

        return bitmap;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                // imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), drawable, null));
                // imageView.setImageDrawable(ContextCompat.getDrawable(context, drawable));
            }
        }
    }
}