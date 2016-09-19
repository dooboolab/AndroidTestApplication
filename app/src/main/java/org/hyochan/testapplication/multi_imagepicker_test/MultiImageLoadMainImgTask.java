package org.hyochan.testapplication.multi_imagepicker_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.hyochan.testapplication.utils.ImgCacheUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by hyochan on 2016-08-18.
 */
public class MultiImageLoadMainImgTask extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private Context context;
    private MultiImageItem multiImageItem;

    public MultiImageLoadMainImgTask(Context context, ImageView imageView, MultiImageItem multiImageItem) {
        this.context = context;
        imageViewReference = new WeakReference<>(imageView);
        this.multiImageItem = multiImageItem;
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;

        if(multiImageItem.getimageName() != null) bitmap = ImgCacheUtil.getInstance().getBitmap(multiImageItem.getimageName());
        if(bitmap != null) return bitmap;

        try{
            final InputStream imageStream = context.getContentResolver().openInputStream(Uri.parse(multiImageItem.getStrUri()));
            bitmap = BitmapFactory.decodeStream(imageStream);
            ImgCacheUtil.getInstance().addBitmap(multiImageItem.getimageName(), bitmap);
            // bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
        } catch (FileNotFoundException fn){
            Log.d("file not found", "exception : " + fn.getMessage());
        }
        return bitmap;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                // imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), drawable, null));
                // imageView.setImageDrawable(ContextCompat.getDrawable(context, drawable));
            }
        }
    }
}