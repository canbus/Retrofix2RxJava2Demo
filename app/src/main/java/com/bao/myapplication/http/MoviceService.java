package com.bao.myapplication.http;

import com.bao.myapplication.Entry.Top250SubjectEntry;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MoviceService {
    @GET("top250")
    Observable<Top250SubjectEntry> getTop250(@Query("start")int start, @Query("count")int count);
}
