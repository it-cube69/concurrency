package ru.itcube.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button download;
    private ProgressBar imageDownloadingProgress;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        download = findViewById(R.id.download);
        image = findViewById(R.id.image);
        imageDownloadingProgress = findViewById(R.id.imageDownloadingProgress);
        // скрываем прогресс бар
        imageDownloadingProgress.setVisibility(View.GONE);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // отображаем прогресс бар перед загрузкой картинки
                imageDownloadingProgress.setVisibility(View.VISIBLE);
                // запускаем процесс загрузки картинки в фоне
                new Thread(new ImageLoader()).start();
            }
        });
    }

    private class ImageUpdater implements Runnable {

        private Bitmap bitmap;

        public ImageUpdater(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            // перед отображением загруженной картинки скрываем прогресс бар
            imageDownloadingProgress.setVisibility(View.GONE);
            // отображаем загруженную картинку в ImageView
            image.setImageBitmap(bitmap);
        }
    }

    private class ImageLoader implements Runnable {

        @Override
        public void run() {
            try {
                URL url = new URL("https://lookw.ru/1/627/1402263545-nature-wallpapers-sunsets-89.jpg");
                InputStream in = new BufferedInputStream(url.openStream());
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                image.getHandler().post(new ImageUpdater(bitmap));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}