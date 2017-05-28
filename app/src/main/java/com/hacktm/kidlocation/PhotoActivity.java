package com.hacktm.kidlocation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

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
    String filename;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImagineFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(photoFile != null) {

            Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("HACK_TAG", "SASAS");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(), filename);
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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        RetroFitService service = new Retrofit.Builder().baseUrl("http://192.168.43.92:8080/").client(client).build().create(RetroFitService.class);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename, reqFile);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String xCoord = sharedPreferences.getString("xCoord", "23.4");
        String yCoord = sharedPreferences.getString("yCoord", "65.4");

        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, xCoord, yCoord);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("HACK_TAG", "Sent");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("HACK_TAG", t.toString());

            }
        });
    }
}
