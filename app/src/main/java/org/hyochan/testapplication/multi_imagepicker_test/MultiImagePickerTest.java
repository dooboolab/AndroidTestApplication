package org.hyochan.testapplication.multi_imagepicker_test;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.hyochan.testapplication.BuildConfig;
import org.hyochan.testapplication.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MultiImagePickerTest extends AppCompatActivity implements View.OnClickListener{

    private final String TAG= "MultiImagePickerTest";


    private final int cameraReq = 100;
    private final int imgReq = 200;

    TextView txtTitle;

    Button btnCamera;
    Button btnImage;
    GridView gridView;

    private ArrayList<MultiImageItem> arrayList;
    private MyGridAdapter myGridAdapter;

    // lists saved with camera photo taken
    ArrayList<String> cameraTakenLists = new ArrayList<>();

    // permission
    private final int PERMISSION_REQ_BEFORE_TAKING_PHOTO = 111;

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

        arrayList = new ArrayList<>();

        // TEST : 저장된 사진 불러오기
        File dirFile = new File(Environment.getExternalStorageDirectory(), "/");
        File files[] = dirFile.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i=0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

        for(File file : files){
            if (!file.isDirectory()) {
                if(file.getName().startsWith("tmp") && file.getName().endsWith(".jpg")){
                    Log.d("Parsed Files", "FileName:" + file.getName());
                    cameraTakenLists.add(file.getName());
                    arrayList.add(new MultiImageItem(Uri.fromFile(file).toString(), null));
                }
            }
        }
        myGridAdapter = new MyGridAdapter(arrayList);
        gridView.setAdapter(myGridAdapter);

    }

    private class MyGridAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private ArrayList<MultiImageItem> arrayList;
        private ViewHolder viewHolder;

        private class ViewHolder {
            public ImageView imgView;
            public Button btnClose;
        }

        public MyGridAdapter(ArrayList<MultiImageItem> arrayList) {
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        public void addItem(MultiImageItem item){
            arrayList.add(item);
        }

        @Override
        public MultiImageItem getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view = layoutInflater.inflate(R.layout.multiimage_grid_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.btnClose = (Button) view.findViewById(R.id.btn_close);
                viewHolder.imgView = (ImageView) view.findViewById(R.id.img);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            new MultiImageLoadTumbImgTask(getApplicationContext(), viewHolder.imgView, arrayList.get(i)).execute();

            viewHolder.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayList.remove(i);
                    notifyDataSetChanged();
                }
            });

            viewHolder.imgView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(getApplicationContext(), MultiImageZoomActivity.class);
                    // intent.putExtra("drawable", arrayList.get(i).getDrawable());
                    intent.putExtra("myitems", arrayList);
                    intent.putExtra("position", i);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_image:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(photoPickerIntent, imgReq);
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
                    startCameraActivityForResult();
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
                    String fileName1 = "tmp" + cameraTakenLists.size() + ".jpg";
                    cameraTakenLists.add(fileName1);
                    File cameraPath = new File(Environment.getExternalStorageDirectory(), fileName1);
                    Uri uri1 = Uri.fromFile(cameraPath);
                    if(uri1 != null) myGridAdapter.addItem(new MultiImageItem(uri1.toString(), null));
                    break;
                case imgReq:
                    final ClipData clipData = data.getClipData();
                    if(clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++){
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri2 = item.getUri();
                            // TODO : 아래 주석 지우고 밑에줄 주석 처리
                            myGridAdapter.addItem(new MultiImageItem(uri2.toString(), null));

                            String fileName2 = "tmp" + cameraTakenLists.size() + ".jpg";
                            File galleryPath = new File(Environment.getExternalStorageDirectory(), fileName2);

                            try{
                                ParcelFileDescriptor parcelFileDescriptor =
                                        getApplicationContext().getContentResolver().openFileDescriptor(uri2, "r");
                                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                                parcelFileDescriptor.close();

                                FileOutputStream fos = null;
                                fos = new FileOutputStream(galleryPath);
                                // Use the compress method on the BitMap object to write image to the OutputStream
                                image.compress(Bitmap.CompressFormat.PNG, 100, fos);

                                cameraTakenLists.add(fileName2);
                                // myGridAdapter.addItem(new MultiImageItem(Uri.fromFile(galleryPath).toString(), null));

                            } catch (FileNotFoundException fe){
                                Log.d(TAG, "file not found : " + fe.getMessage());
                            } catch (IOException ie){
                                Log.d(TAG, "IOException : " + ie.getMessage());
                            } catch (NullPointerException ne){
                                Log.d(TAG, "NullPointerException for fos : " + ne.getMessage());
                            }


                            // In case you need image's absolute path
                            // String path= getRealPathFromURI(getActivity(), uri)
                        }
                    }
                    myGridAdapter.notifyDataSetChanged();

/*
                    final Uri imageUri = data.getData();
                    if(imageUri != null){
                        myGridAdapter.addItem(new MultiImageItem(imageUri, null));
                        myGridAdapter.notifyDataSetChanged();
                    }
*/
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_BEFORE_TAKING_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivityForResult();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "사진 저장 외부 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void startCameraActivityForResult(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String fileName = "tmp" + cameraTakenLists.size() + ".jpg";
        File photoPath = new File(Environment.getExternalStorageDirectory(), fileName);
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoPath);
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, cameraReq);
    }
}
