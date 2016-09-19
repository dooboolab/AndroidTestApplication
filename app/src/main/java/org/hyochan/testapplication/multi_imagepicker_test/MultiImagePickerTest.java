package org.hyochan.testapplication.multi_imagepicker_test;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.hyochan.testapplication.BuildConfig;
import org.hyochan.testapplication.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MultiImagePickerTest extends AppCompatActivity implements View.OnClickListener{

    private final String TAG= "MultiImagePickerTest";


    private final int cameraReq = 100;
    private final int imgReq = 200;

    private String cameraFileName;
    private Uri cameraUri;

    TextView txtTitle;

    Button btnCamera;
    Button btnImage;
    GridView gridView;

    private ArrayList<MultiImageItem> mainItems;
    private MultiImageGridAdapter multiImageGridAdapter;

    // lists saved with camera photo taken
    private ArrayList<String> savedImgsList = new ArrayList<>();

    // permission
    private final int PERMISSION_REQ_BEFORE_TAKING_PHOTO = 111;
    private final int PERMISSION_REQ_BEFORE_CHOOSING_GALLERY = 222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_picker_test);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("멀티 이미지 픽커 테스트");

        btnImage = (Button) findViewById(R.id.btn_image);
        btnImage.setOnClickListener(this);
        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.grid_view);

        mainItems = new ArrayList<>();

        // TEST : 저장된 사진 불러오기
        File dirFile = new File(Environment.getExternalStorageDirectory(), "/TestApplication");
        File files[] = dirFile.listFiles();
        if(files != null){
            Log.d("Files", "Size: "+ files.length);
            for(File file : files){
                if (!file.isDirectory()) {

                    // PROBLEM : item이 다르게 들어간다. sorting을 하던지 dir path를 불러와서 같이 adding을 하던지 해야함.
/*
                    if(file.getName().startsWith("tmp") && file.getName().endsWith(".png")){
                        Log.d("Parsed Files", "FileName:" + file.getName());
                        savedImgsList.add(file.getName());
                        mainItems.add(new MultiImageItem(Uri.fromFile(file).toString(), file.getName()));
                     } else if(file.getName().startsWith("thumb_tmp") && file.getName().endsWith(".png")){
                        thumbItems.add(new MultiImageItem(Uri.fromFile(file).toString(), file.getName()));
                    }
*/
                    // 위 문제 해결 1번 째 방법
/*
                    if(file.getName().startsWith("tmp") && file.getName().endsWith(".png")){
                        Log.d("Parsed Files", "FileName:" + file.getName());

                        // 1. 메인 아이템 추가
                        savedImgsList.add(file.getName());
                        mainItems.add(new MultiImageItem(Uri.fromFile(file).toString(), file.getName()));

                        // 2. 썸네일 추가
                        Environment.getExternalStorageDirectory(), "/TestApplication"
                        File thumbFile = new File(dirFile, "/thumb_" + file.getName());
                        thumbItems.add(new MultiImageItem(Uri.fromFile(thumbFile).toString(), "thumb_" + file.getName()));
                    }
                }
*/
                    // 위 문제 해결 2번 째 방법 : MultiImageItem 생성자에서 직접 해줌
                    if (file.getName().startsWith("tmp_") && file.getName().endsWith(".png")) {
                        Log.d("Parsed Files", "FileName:" + file.getName());

                        savedImgsList.add(file.getName());
                        mainItems.add(new MultiImageItem(Uri.fromFile(file).toString(), file.getName()));
                    }
                }
            }
        }
        multiImageGridAdapter = new MultiImageGridAdapter(getApplicationContext(), mainItems);
        gridView.setAdapter(multiImageGridAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_image:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQ_BEFORE_CHOOSING_GALLERY);
                } else {
                    startMyActivityForResult(imgReq);
                }
                break;
            case R.id.btn_camera:
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                Log.d(TAG, "external_storage_permission : " + permissionCheck);
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQ_BEFORE_TAKING_PHOTO);
                } else {
                    startMyActivityForResult(cameraReq);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "reqCode : " + requestCode + ", resultCode : " + resultCode);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case cameraReq:
                    if(cameraUri != null && cameraFileName != null) {
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cameraUri);
                            Bitmap thumbBitmap = ThumbnailUtils.extractThumbnail(bitmap, 400, 400);
                            // 2. save thumbnail
                            File thumbPath = new File(Environment.getExternalStorageDirectory(), "/TestApplication/thumb_" + cameraFileName);
                            thumbPath.createNewFile();
                            FileOutputStream fos = new FileOutputStream(thumbPath);
                            thumbBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                            savedImgsList.add(cameraFileName);
                            multiImageGridAdapter.addItem(new MultiImageItem(cameraUri.toString(), cameraFileName));
                            multiImageGridAdapter.notifyDataSetChanged();

                        } catch (FileNotFoundException ex){
                            Log.d(TAG, "not found : " + ex.getMessage());
                        } catch (IOException ex){
                            Log.d(TAG, "io exception : " + ex.getMessage());
                        }
                    }
                    break;
                case imgReq:
                    final ClipData clipData = data.getClipData();
                    if(clipData != null) {
                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(MultiImagePickerTest.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMessage("로딩중입니다...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        for (int i = 0; i < clipData.getItemCount(); i++){
                            final String imgFileName = "tmp_" + UUID.randomUUID().toString().replaceAll("-", "") + ".png";
                            new MultiImageCopyFileTask(getApplicationContext(), imgFileName, i, clipData.getItemAt(i), new MultiImageCopyFileTask.OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(MultiImageItem multiImageItem, int numTask) {
                                    Log.d(TAG, "onTaskCompleted : " + numTask);
                                    multiImageGridAdapter.addItem(multiImageItem);
                                    savedImgsList.add(imgFileName);
                                    multiImageGridAdapter.notifyDataSetChanged();
                                    if (progressDialog != null && progressDialog.isShowing()){
                                        int percentage = (((numTask+1) * 100)/clipData.getItemCount());
                                        Log.d(TAG, "percentage : " + percentage);
                                        progressDialog.setProgress(percentage);
                                        if(numTask+1 == clipData.getItemCount()) {
                                            Log.d(TAG, "task done");
                                            progressDialog.dismiss();
                                        }
                                    }
                                }
                            }).execute();

                            // In case you need image's absolute path
                            // String path= getRealPathFromURI(getActivity(), uri)
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_BEFORE_CHOOSING_GALLERY:
            case PERMISSION_REQ_BEFORE_TAKING_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File file = new File(Environment.getExternalStorageDirectory(), "/TestApplication");
                    if (!file.exists()) {
                        if (!file.mkdirs()) {
                            Log.e(TAG, "Problem creating Image folder");
                        }
                    }
                    startMyActivityForResult(requestCode);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "사진 저장 외부 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void startMyActivityForResult(int requestCode){

        switch (requestCode){
            case cameraReq:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                cameraFileName = "tmp_" + UUID.randomUUID().toString().replaceAll("-", "") + ".png";
                File cameraPath = new File(Environment.getExternalStorageDirectory(), "/TestApplication/" + cameraFileName);
                cameraUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", cameraPath);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                startActivityForResult(cameraIntent, cameraReq);
                break;
            case imgReq:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(photoPickerIntent, imgReq);
                break;
        }
    }

}
