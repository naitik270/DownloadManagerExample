package com.gadgetsaint.downloadmanagerexample;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    RecyclerView rv;
    ArrayList<Long> list = new ArrayList<>();
    List<Model> video_list = new ArrayList<>();
    AdapterVideoList adapterVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        video_list.add(new Model("Video 1", "https://commonsware.com/misc/test.mp4"));
        video_list.add(new Model("Video 2", "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"));
        video_list.add(new Model("Video 3", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
        video_list.add(new Model("Video 4", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"));
        video_list.add(new Model("Video 5", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4"));
//        video_list.add(new Model("Video 5", "https://www.youtube.com/watch?v=vJCRg8JDaSI"));

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


//        Download_Uri = Uri.parse("http://www.gadgetsaint.com/wp-content/uploads/2016/11/cropped-web_hi_res_512.png");
        Download_Uri = Uri.parse("https://commonsware.com/misc/test.mp4");

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapterVideoList = new AdapterVideoList(MainActivity.this, video_list);
        rv.setAdapter(adapterVideoList);
        adapterVideoList.setListener((model, mode) -> {

            if (mode.equalsIgnoreCase("download")) {
                SingleVideoDownload(model.getUrl(),model.getTitle());
            } else if (mode.equalsIgnoreCase("download all")) {
                MultipleVideoDownload();
            }
        });


//        TextView btnSingle = (TextView) findViewById(R.id.single);
//
//        TextView btnMultiple = (TextView) findViewById(R.id.multiple);
//
//
//        if (!isStoragePermissionGranted()) {
//
//
//        }
//
//
//        btnMultiple.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                list.clear();
//
//                for (int i = 0; i < 2; i++) {
//                    DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
//                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//                    request.setAllowedOverRoaming(false);
//                    request.setTitle("Downloading " + "Sample_" + i + ".mp4");
//                    request.setDescription("Downloading " + "Sample_" + i + ".mp4");
//                    request.setVisibleInDownloadsUi(true);
////                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample_" + i + ".png");
////                    request.setDestinationInExternalPublicDir(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)),"/GadgetSaint/"  + "/" + "Sample_" + i + ".png");
////                    request.allowScanningByMediaScanner();
//
//                    // You Download will be in this path
//                    // Internal Storage: Android/data/ YOUR APP PACKAGE NAME/files/Pictures/ YOUR FiLE NAME.mp4
//                    Uri uri = Uri.parse("file://"
//                            + getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + "Sample_" + i + ".mp4");
//
//                    request.setDestinationUri(uri);
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    refid = downloadManager.enqueue(request);
//
//
//                    Log.e("OUTNM", "" + refid);
//
//                    list.add(refid);
//
//                }
//
//
//            }
//        });
//
//
//        btnSingle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                list.clear();
//
//                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
//                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//                request.setAllowedOverRoaming(false);
//                request.setTitle("Downloading " + "Sample" + ".png");
//                request.setDescription("Downloading " + "Sample" + ".png");
//                request.setVisibleInDownloadsUi(true);
////                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample" + ".png");
////                request.setDestinationInExternalPublicDir(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)), "/GadgetSaint/" + "/" + "Sample.png");
////                request.allowScanningByMediaScanner();
//                // You Download will be in this path
//                // Internal Storage: Android/data/ YOUR APP PACKAGE NAME/files/Pictures/ YOUR FiLE NAME.mp4
//                Uri uri = Uri.parse("file://"
//                        + getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + "Sample_.mp4");
//
//                request.setDestinationUri(uri);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                refid = downloadManager.enqueue(request);
//
//
//                Log.e("OUT", "" + refid);
//
//                list.add(refid);
//
//            }
//        });


    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {


            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


            Log.e("IN", "" + referenceId);

            list.remove(referenceId);


//            if (list.isEmpty())
//            {
//
//
//                Log.e("INSIDE", "" + referenceId);
//                NotificationCompat.Builder mBuilder =
//                        new NotificationCompat.Builder(MainActivity.this)
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setContentTitle("GadgetSaint")
//                                .setContentText("All Download completed");
//
//
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(455, mBuilder.build());
//
//
//            }

        }
    };


    public void SingleVideoDownload(String url,String title) {
        list.clear();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading " + title + ".mp4");
        request.setDescription("Downloading " + title + ".mp4");
        request.setVisibleInDownloadsUi(true);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample" + ".png");
//                request.setDestinationInExternalPublicDir(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)), "/GadgetSaint/" + "/" + "Sample.png");
//                request.allowScanningByMediaScanner();
        // You Download will be in this path

        // Internal Storage: Android/data/ YOUR APP PACKAGE NAME/files/Pictures/ YOUR FiLE NAME.mp4
        Uri uri = Uri.parse("file://"
                + getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +title + ".mp4");

        request.setDestinationUri(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        refid = downloadManager.enqueue(request);

//
    }

    public void MultipleVideoDownload() {
        list.clear();
        for(Model model : video_list){
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getUrl()));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                    | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("Downloading " +  model.getTitle() + ".mp4");
            request.setDescription("Downloading " + model.getTitle() + ".mp4");
            request.setVisibleInDownloadsUi(true);
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample_" + i + ".png");
//                    request.setDestinationInExternalPublicDir(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)),"/GadgetSaint/"  + "/" + "Sample_" + i + ".png");
//                    request.allowScanningByMediaScanner();

            // You Download will be in this path
            // Internal Storage: Android/data/ YOUR APP PACKAGE NAME/files/Pictures/ YOUR FiLE NAME.mp4
            Uri uri = Uri.parse("file://"
                    + getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + model.getTitle()+ ".mp4");

            request.setDestinationUri(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            refid = downloadManager.enqueue(request);


            Log.e("OUTNM", "" + refid);

            list.add(refid);
        }


    }


    @Override
    protected void onDestroy() {


        super.onDestroy();

        unregisterReceiver(onComplete);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            // permission granted

        }
    }
}
