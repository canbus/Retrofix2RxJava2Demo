package com.bao.myapplication.http;

import android.util.Log;

import com.bao.myapplication.Entry.Top250SubjectEntry;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.reactivex.schedulers.Schedulers.io;

public class HttpMethods {
    Retrofit retrofit = null;
    MoviceService moviceService = null;
    private static volatile HttpMethods instance = null;
    public static HttpMethods getInstance() { //创建单例
        if (instance == null) {
            synchronized (HttpMethods.class) {
                if (instance == null) {
                    instance = new HttpMethods();
                }
            }
        }
        return instance;
    }
    private HttpMethods(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        moviceService = retrofit.create(MoviceService.class);
    }

    //https://api.douban.com/v2/movie/top250?start=0&count=2
    public void getTopMove250(DisposableObserver<Top250SubjectEntry.Subject> disposableObserver,int start,int count){
        moviceService.getTop250(start,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//AndroidSchedulers.mainThread())
                .map(new Function<Top250SubjectEntry, List<Top250SubjectEntry.Subject>>() {
                    @Override
                    public List<Top250SubjectEntry.Subject> apply(Top250SubjectEntry top250SubjectEntry) throws Exception {
                        return top250SubjectEntry.getSubjects();
                    }
                })
                .flatMap(new Function<List<Top250SubjectEntry.Subject>, ObservableSource<Top250SubjectEntry.Subject>>() {
                    @Override
                    public ObservableSource<Top250SubjectEntry.Subject> apply(List<Top250SubjectEntry.Subject> subjects) throws Exception {
                        return Observable.fromIterable(subjects);
                    }
                })
                .subscribe(disposableObserver);

    }
}
