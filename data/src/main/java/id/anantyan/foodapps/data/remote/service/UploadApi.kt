package id.anantyan.foodapps.data.remote.service

import id.anantyan.foodapps.data.BuildConfig
import id.anantyan.foodapps.data.remote.model.PhotoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UploadApi {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Query("key") apiKey: String? = BuildConfig.API_KEY_UPLOAD,
        @Part image: MultipartBody.Part
    ): Response<PhotoResponse>
}