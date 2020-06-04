package com.example.retrofitjava.service;

import com.example.retrofitjava.model.CryptoModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface CryptoAPI {
    //e1452e8c069e47bbcb3aabaac5bab75c
    //https://api.nomics.com/v1/prices?key=e1452e8c069e47bbcb3aabaac5bab75c

    @GET("prices?key=e1452e8c069e47bbcb3aabaac5bab75c")
    Observable<List<CryptoModel>> getData();

}
