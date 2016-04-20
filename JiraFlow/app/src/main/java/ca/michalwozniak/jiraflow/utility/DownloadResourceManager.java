package ca.michalwozniak.jiraflow.utility;

import android.app.Activity;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 4/19/2016.
 */
public class DownloadResourceManager {

    private ThinDownloadManager thinDownloadManager;
    private List<Integer> downloadQueue;
    private Activity activity;
    private static final String TAG = "DOWNLOAD_MANAGER";
    private String username;
    private String password;
    private boolean basicAuthentication = false;

    public DownloadResourceManager(Activity activity) {
        this.thinDownloadManager = new ThinDownloadManager();
        this.activity = activity;
        downloadQueue = new ArrayList<>();
    }

    public DownloadResourceManager(Activity activity, String username, String password) {
        this.thinDownloadManager = new ThinDownloadManager();
        this.activity = activity;
        this.username = username;
        this.password = password;
        this.basicAuthentication = true;
        this.downloadQueue = new ArrayList<>();
    }


    public void add(String url, String destination) {
        if (!url.isEmpty() && !destination.isEmpty()) {
            Uri downloadUri = Uri.parse(url);
            Uri destinationUri = Uri.parse(activity.getFilesDir() + "/" + destination);
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri);

            if (basicAuthentication) {
                String credentials = "mv740" + ":" + "Wozm__06";
                final String basic =
                        "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                downloadRequest.addCustomHeader("Authorization", basic);
                downloadRequest.addCustomHeader("Accept", "application/json");
            }
            downloadRequest.setDestinationURI(destinationUri);
            downloadRequest.setRetryPolicy(new DefaultRetryPolicy());
            downloadRequest.setStatusListener(new DownloadStatusListener());

            downloadQueue.add(thinDownloadManager.add(downloadRequest));
        } else {
            Log.e(TAG, " url or destination is empty");
        }

    }


    public class DownloadStatusListener implements DownloadStatusListenerV1 {

        @Override
        public void onDownloadComplete(DownloadRequest downloadRequest) {
            Log.d(TAG, "complete : " + downloadRequest.getUri());
            downloadQueue.remove(Integer.valueOf(downloadRequest.getDownloadId()));
        }

        @Override
        public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
            Log.e(TAG, "failed " + downloadRequest.getUri() + ": " + errorMessage + "errorCode :" + errorCode);
        }

        @Override
        public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
            //Log.e(TAG, "progress " + downloadRequest.getUri() + ": " + downloadRequest.getUri() + "progress :" + progress);
        }
    }
    
}
