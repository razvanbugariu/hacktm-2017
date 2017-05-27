package com.hacktm.kidlocation;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by rbu on 5/27/17.
 */

public interface RetroFitService {
    @Multipart
    @POST("camera/{xx}/{yy}")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Path(value = "xx") String xx, @Path(value = "yy") String yy);
}
