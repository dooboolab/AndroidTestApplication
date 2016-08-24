package org.hyochan.testapplication.multi_imagepicker_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import org.hyochan.testapplication.utils.ImgCacheUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by hyochan on 2016-08-18.
 */
public class MultiImageLoadTumbImgTask extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private Context context;
    private MultiImageItem multiImageItem;

    public MultiImageLoadTumbImgTask(Context context, ImageView imageView, MultiImageItem multiImageItem) {
        this.context = context;
        imageViewReference = new WeakReference<>(imageView);
        this.multiImageItem = multiImageItem;
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;

        // path가 없으면 uri로 불러옴
        if(multiImageItem.getimageName() == null || multiImageItem.getimageName().equals("")){
            try{
                final InputStream imageStream = context.getContentResolver().openInputStream(Uri.parse(multiImageItem.getStrUri()));
                bitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException fn){
                Log.d("file not found", "exception : " + fn.getMessage());
            }
        }
        // path 불러옴
        else {
            bitmap = ImgCacheUtil.getInstance().getBitmap(multiImageItem.getimageName());
            if(bitmap != null) return bitmap;

            File tmpPath = new File(Environment.getExternalStorageDirectory(), "/TestApplication" );
            File myPath = new File(tmpPath, multiImageItem.getimageName());
            if (myPath.exists()) {
                Log.d("MultiImage", "imgExists : " + myPath);
                // 파일이 있으면 imgView에 띄우기
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(myPath.getAbsolutePath(), bmOptions);
            }
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 800, 800);
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