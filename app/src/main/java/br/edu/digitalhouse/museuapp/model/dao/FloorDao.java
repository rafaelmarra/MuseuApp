package br.edu.digitalhouse.museuapp.model.dao;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.edu.digitalhouse.museuapp.Interfaces.ServiceListener;
import br.edu.digitalhouse.museuapp.model.floorrequest.Gallery;
import br.edu.digitalhouse.museuapp.model.floorrequest.GalleryResponse;
import br.edu.digitalhouse.museuapp.model.dao.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloorDao {

    public void getGalleries(Context context, final ServiceListener listener, int floor) {

        List<Gallery> galleryList = new ArrayList<>();

        if (isConnected(context)) {

            getNetworkData(listener, floor, 100);

        } else {

            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
            /*getLocalData(context, listener, galleryList);*/
        }
    }

    private void getNetworkData(final ServiceListener listener, int floor, int recordsPerQuery) {

        Call<GalleryResponse> call = RetrofitService.getApiService().getFloor(floor, recordsPerQuery, RetrofitService.API_KEY);

        call.enqueue(new Callback<GalleryResponse>() {
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                if (response.body() != null) {
                    listener.onSucess(response.body());
                }
            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                listener.onSucess(t);

            }
        });

    }

    private void getLocalData(Context context, ServiceListener listener, List<Gallery> galleryList) {
        try {
            AssetManager manager = context.getAssets();
            InputStream galleriesJson = manager.open("galleries.json");
            BufferedReader bufferReaderIn = new BufferedReader(new InputStreamReader(galleriesJson));

            Gson gson = new Gson();

            Gallery[] galleryArray = gson.fromJson(bufferReaderIn, Gallery[].class);

            galleryList.addAll(Arrays.asList(galleryArray));

            listener.onSucess(galleryList);

        } catch (IOException exception) {
            Log.e("JSON", "ERRO AO LER ARQUIVO galleries.json");
            listener.onError(exception);
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;

        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected() &&
                    (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                            || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
        }
        return false;
    }

}
