package com.hacktm.kidlocation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoActivity extends AppCompatActivity {

    int TAKE_PHOTO_CODE = 1;
    String mCurrentPhotoPath;
    String filename;

    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageView = (ImageView) findViewById(R.id.imageViewPhoto);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImagineFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(photoFile != null) {
            Log.d("HACK_TAG", Uri.fromFile(photoFile).toString());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            Log.d("HACK_TAG", "ASASASASASASASASASASASA");
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            Log.d("HACK_TAG", "ASASASASASASASASASASASA");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("HACK_TAG", "ASASASASASASASASASASASA");
//        Bundle bundle = data.getExtras();
//        Bitmap imageBitmap = (Bitmap) bundle.get("data");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(), filename);
//        imageView.setImageBitmap(imageBitmap);
        Log.d("TASK_TAG", file.getName());
        sendPhoto(file);
    }

    private File createImagineFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG" + timestamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        filename = imageFileName + ".jpg";
        File image = new File(storageDir, filename);
        return image;
    }

    private void sendPhoto(File file) {
        Log.d("HACK_TAG", "ASASASASASASASASASASASA");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        RetroFitService service = new Retrofit.Builder().baseUrl("http://192.168.43.92:8080/").client(client).build().create(RetroFitService.class);

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, file);
//        byte[] byteArray = stream.toByteArray();


        RequestBody reqFile = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename, reqFile);

        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, "23.4", "56.3");
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("HACK_TAG", "YES");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("HACK_TAG", t.toString());

            }
        });
    }
}
