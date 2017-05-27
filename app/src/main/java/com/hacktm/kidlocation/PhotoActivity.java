package com.hacktm.kidlocation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

    int TAKE_PHOTO_CODE = 0;

    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageView = (ImageView) findViewById(R.id.imageViewPhoto);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(imageBitmap);
        Log.d("TASK_TAG", imageBitmap.getNinePatchChunk() + " " + imageBitmap.getByteCount());
//        imageView.get
        sendPhoto(imageBitmap);
    }

    private void sendPhoto(Bitmap bitmap) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        RetroFitService service = new Retrofit.Builder().baseUrl("http://192.168.43.92:8080/").client(client).build().create(RetroFitService.class);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Log.d("HACK_TAG", byteArray.toString());

        RequestBody reqFile = RequestBody.create(MediaType.parse("image"), byteArray);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "file", reqFile);

        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body);
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
