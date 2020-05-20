package com.tamagotchi.tamagotchiserverprotocol.routers;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IFilesApiService {
    @GET
    Single<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
