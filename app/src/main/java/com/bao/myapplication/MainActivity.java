package com.bao.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bao.myapplication.Entry.Top250SubjectEntry;
import com.bao.myapplication.http.HttpMethods;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements
        FragmentContent1.OnFragmentInteractionListener,
        FragmentContent2.OnFragmentInteractionListener
{

    @BindView(R.id.id_viewPager)
    ViewPager viewPager;
    @BindView(R.id.id_tv1)
    TextView tv1;
    @BindView(R.id.id_tv2)
    TextView tv2;

    Fragment fragment1,fragment2;
    private DisposableObserver<Top250SubjectEntry.Subject> dispseableObserver;
    HttpMethods httpMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initImageLoad();
    }

    @OnClick({R.id.id_tv1,R.id.id_tv2})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.id_tv1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.id_tv2:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragment1 = new FragmentContent1();
        fragment2 = new FragmentContent2();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new MyViewPageradapter(fm,fragmentList));

        httpMethods = HttpMethods.getInstance();
        dispseableObserver = new DisposableObserver<Top250SubjectEntry.Subject>() {
            @Override
            public void onNext(Top250SubjectEntry.Subject subject) {
                //((FragmentContent1)fragment1).setTextView(top250SubjectEntry.getTitle());
                ((FragmentContent1)fragment1).addData(subject,false);
            }
            public void onError(Throwable e) {System.out.println(e.toString()); }
            public void onComplete() {System.out.println("onComplete"); }
        };
        getTopMovie250(false);
    }
    int start = 0;
    private void getTopMovie250(final boolean down)
    {
        dispseableObserver = new DisposableObserver<Top250SubjectEntry.Subject>() {
            @Override
            public void onNext(Top250SubjectEntry.Subject subject) {
                //((FragmentContent1)fragment1).setTextView(top250SubjectEntry.getTitle());
                ((FragmentContent1)fragment1).addData(subject,down);
            }
            public void onError(Throwable e) {System.out.println(e.toString()); }
            public void onComplete() {System.out.println("onComplete"); }
        };
        httpMethods.getTopMove250(dispseableObserver,start,10);
        start += 10;
    }

    void initImageLoad()
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if(uri.toString().contains("pull down"))
            getTopMovie250(true);
        else if(uri.toString().contains("pull up"))
            getTopMovie250(false);
    }


    private class MyViewPageradapter extends FragmentPagerAdapter {
        List<Fragment > mList=null;
        public MyViewPageradapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
