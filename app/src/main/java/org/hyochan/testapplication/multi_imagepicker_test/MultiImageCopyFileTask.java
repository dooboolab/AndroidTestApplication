package org.hyochan.testapplication.multi_imagepicker_test;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hyochan on 2016-08-25.
 */
public class MultiImageCopyFileTask extends AsyncTask<Void, Void, MultiImageItem> {
    private final String TAG = "MultiImageCopyFileTask";
    private Context context;
    private String fileName;
    private ClipData.Item clipItem;
    private OnTaskCompleted listener;
    private int numTask;
    private File thumbPath;

    public interface OnTaskCompleted{
        void onTaskCompleted(MultiImageItem multiImageItem, int numTask);
    }

    public MultiImageCopyFileTask(Context context, String fileName, int numTask, ClipData.Item clipItem, OnTaskCompleted listener) {
        this.context = context;
        this.fileName = fileName;
        this.clipItem = clipItem;
        this.listener = listener;
        this.numTask = numTask;

        Log.d(TAG, "constructor fileName : "+ fileName);
    }

    @Override
    // Actual download method, run in the task thread
    protected MultiImageItem doInBackground(Void... params) {
        Log.d(TAG, "doInBackground : "+ fileName);
        Uri uri = clipItem.getUri();
        try{

            // 1. copy gallery file
            File galleryPath = new File(Environment.getExternalStorageDirectory(), "/TestApplication/" + fileName);
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap imgPath = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();

            FileOutputStream fos1 = new FileOutputStream(galleryPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            imgPath.compress(Bitmap.CompressFormat.PNG, 100, fos1);


            // 2. save thumbnail
            Bitmap thumbBitmap = ThumbnailUtils.extractThumbnail(imgPath, 400, 400);
            thumbPath = new File(Environment.getExternalStorageDirectory(), "/TestApplication/thumb_" + fileName);
            thumbPath.createNewFile();
            FileOutputStream fos2 = new FileOutputStream(thumbPath);
            thumbBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos2);


            Log.d(TAG, "fileName 2 : " + fileName + " is added");

            return new MultiImageItem(Uri.fromFile(galleryPath).toString(), fileName);

        } catch (FileNotFoundException fe){
            Log.d(TAG, "file not found : " + fe.getMessage());
        } catch (IOException ie){
            Log.d(TAG, "IOException : " + ie.getMessage());
        } catch (NullPointerException ne){
            Log.d(TAG, "NullPointerException for fos : " + ne.getMessage());
        }


        return null;
    }

    @Override
    protected void onPostExecute(MultiImageItem multiImageItem) {
        Log.d(TAG, "onTaskCompleted");
        listener.onTaskCompleted(multiImageItem, numTask);

        super.onPostExecute(multiImageItem);
    }
}