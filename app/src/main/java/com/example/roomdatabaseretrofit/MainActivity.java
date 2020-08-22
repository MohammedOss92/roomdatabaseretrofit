package com.example.roomdatabaseretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.roomdatabaseretrofit.Adapter.ActorAdapter;
import com.example.roomdatabaseretrofit.Modal.Actor;
import com.example.roomdatabaseretrofit.Network.Api;
import com.example.roomdatabaseretrofit.Network.ApiClient;
import com.example.roomdatabaseretrofit.Repository.ActorRespository;
import com.example.roomdatabaseretrofit.ViewModal.ActoreViewModal;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActoreViewModal actoreViewModal;
    private RecyclerView recyclerView;
    private ActorAdapter actorAdapter;
    private List<Actor> actorList;

    private static final String URL_DATA = "http://www.codingwithjks.tech/data.php/";
    private ActorRespository actorRespository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        actorRespository = new ActorRespository(getApplication());
        actorList = new ArrayList<>();
        actorAdapter = new ActorAdapter(this,actorList);

        actoreViewModal = new ViewModelProvider(this).get(ActoreViewModal.class);

        actoreViewModal.getAllActor().observe(this, new Observer<List<Actor>>() {
            @Override
            public void onChanged(List<Actor> actorList) {
//                Toast.makeText(MainActivity.this, "working fine", Toast.LENGTH_SHORT).show();
                actorAdapter.getAllActors(actorList);
                recyclerView.setAdapter(actorAdapter);
                Log.d("main","onChanged: "+actorList);
            }
        });

        networkRequest();
    }

    private void networkRequest() {

        Call<List<Actor>> call = ApiClient
                .getInstance()
                .getApiInterface()
                .getAllActors();
        call.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {

                if (response.isSuccessful()){
                    actorRespository.insert(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "something went wrong ....", Toast.LENGTH_SHORT).show();
                Log.d("onFailure","onFailure: "+call);
                Log.d("onFailure",MainActivity.this+"t");
                Log.d("onFailure",t.getMessage());
            }
        });

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL_DATA)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Api api = retrofit.create(Api.class);
//        Call<List<Actor>> call = api.getAllActors();
//        call.enqueue(new Callback<List<Actor>>() {
//            @Override
//            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
//                if (response.isSuccessful()){
//                    actorRespository.insert(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Actor>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "something went wrong ....", Toast.LENGTH_SHORT).show();
//
//                Log.d("onFailure","onFailure: "+call);
//            }
//        });
    }
}
