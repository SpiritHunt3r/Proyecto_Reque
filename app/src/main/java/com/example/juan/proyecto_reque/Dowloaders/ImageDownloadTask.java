package com.example.juan.proyecto_reque.Dowloaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageDownloadTask extends AsyncTask<String,Void,Bitmap> {
    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            Bitmap resultado = null;
            URL url = new URL(urls[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            resultado = BitmapFactory.decodeStream(inputStream);

            return resultado;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
