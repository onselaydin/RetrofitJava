package com.example.retrofitjava.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.retrofitjava.R;
import com.example.retrofitjava.adapter.RecycleViewAdapter;
import com.example.retrofitjava.model.CryptoModel;
import com.example.retrofitjava.service.CryptoAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<CryptoModel> cryptoModels;
    private String BASE_URL = "https://api.nomics.com/v1/";
    Retrofit retrofit;
    RecyclerView recyclerView;
    RecycleViewAdapter recycleViewAdapter;
    CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);


        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        loadData();

    }

    private void loadData(){
        final CryptoAPI cryptoAPI = retrofit.create(CryptoAPI.class);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(cryptoAPI.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse));

        //old code used call
        /*
        Call<List<CryptoModel>> call = cryptoAPI.getData();

        call.enqueue(new Callback<List<CryptoModel>>() { //new Call diyince otomatik onResponse ve onFailure metodları oluşuyor
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                if(response.isSuccessful()){
                    List<CryptoModel> responseList = response.body();
                    cryptoModels = new ArrayList<>(responseList);

                    //RecycleView
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recycleViewAdapter = new RecycleViewAdapter(cryptoModels);
                    recyclerView.setAdapter(recycleViewAdapter);


                    for(CryptoModel cryptoModel : cryptoModels){
                        System.out.println(cryptoModel.currency);
                        System.out.println(cryptoModel.price);
                    }


                }
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                System.out.println("Hata var...");
                t.printStackTrace();
            }
        });
        */

    }

    private void handleResponse(List<CryptoModel> cryptoModelList){

        cryptoModels = new ArrayList<>(cryptoModelList);

        //RecycleView
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recycleViewAdapter = new RecycleViewAdapter(cryptoModels);
        recyclerView.setAdapter(recycleViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}