package ru.itcube.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button start;
    private Button stop;
    private TextView textView;
    private ImageView imageView;
//    private Integer balance = 0;
    private volatile boolean goOn = true;

//    public synchronized void charge(Integer value) {
//        balance = balance + value;
//    }
//
//    public synchronized void writeOff(Integer value) {
//        balance = balance - value;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        progressBar = findViewById(R.id.progressBar1);
        textView = findViewById(R.id.text1);
        imageView = findViewById(R.id.imageView2);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new LoadImage()).start();
                new Thread(new Start()).start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Stop()).start();
            }
        });
    }

    public static class Updater implements Runnable {
        private int progress;
        private ProgressBar progressBar;


        public int getProgress() {
            return progress;
        }

        public Updater(ProgressBar progressBar, int progress) {
            this.progressBar = progressBar;
            this.progress = progress;
        }

        @Override
        public void run() {
            this.progressBar.setProgress(getProgress());
        }
    }

    public static class UpdaterText implements Runnable {
        private int progress;
        private TextView textView;


        public int getProgress() {
            return progress;
        }

        public UpdaterText(TextView textView, int progress) {
            this.textView = textView;
            this.progress = progress;
        }

        @Override
        public void run() {
            this.textView.setText(String.format("Complete %d%%", getProgress()));
        }
    }

    public static class UpdaterImage implements Runnable {

        private ImageView imageView;
        private Bitmap bitmap;

        public UpdaterImage(ImageView imageView, Bitmap bitmap) {
            this.imageView = imageView;
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            imageView.setImageBitmap(bitmap);
        }
    }

    private class LoadImage implements Runnable {

        @Override
        public void run() {
            try {
                URL url = new URL("https://g.petango.com/photos/1963/ae27dfdd-2283-49cb-8121-0764097b50e9.jpg");
                InputStream in = new BufferedInputStream(url.openStream());
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                imageView.getHandler().post(new UpdaterImage(imageView, bitmap));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Start implements Runnable {

        @Override
        public void run() {
            int i = 0;
            if (!goOn) {
                goOn = true;
            }
            while (goOn) {
                if (i >= 100) {
                    break;
                }
                i++;
//                progressBar.setProgress(i);
//                textView.setText(String.format("Complete %d%%", i));

                progressBar.getHandler().post(new Updater(progressBar, i));
                textView.getHandler().post(new UpdaterText(textView, i));

//                progressBar.getHandler().post(new Updater(i) {
//                    @Override
//                    public void run() {
//                        progressBar.setProgress(getProgress());
//                    }
//                });
//
//                textView.getHandler().post(new Updater(i) {
//                    @Override
//                    public void run() {
//                        textView.setText(String.format("Complete %d%%", getProgress()));
//                    }
//                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Stop implements Runnable {

        @Override
        public void run() {
            if (goOn) {
                goOn = false;
            }
        }
    }

//    private class WriteOff implements Runnable {
//
//        @Override
//        public void run() {
//            for (int i = 0; i < 100; i++) {
//                writeOff(100);
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private class Charge implements Runnable {
//
//        @Override
//        public void run() {
//            for (int i = 0; i < 100; i++) {
//                charge(100);
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    private class Task extends AsyncTask<Void, Integer, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onCancelled(Void aVoid) {
//            super.onCancelled(aVoid);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            textView.setText("Выполнено " + 100 + "%");
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            progressBar.setProgress(values[0]);
//            textView.setText("Выполнено " + values[0] + "%");
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            for (int i = 1; i <= 100; i++) {
//                publishProgress(i);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
////            publishProgress(100);
//
//            return null;
//        }
//    }
}