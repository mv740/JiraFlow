package ca.michalwozniak.jiraflow.utility;

import android.app.Activity;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.HashMap;
import java.util.Map;

import ca.michalwozniak.jiraflow.helper.ImageIcon;

/**
 * Created by michal on 4/19/2016.
 */
public class DownloadResourceManager {

    private ThinDownloadManager thinDownloadManager;
    private Activity activity;
    private static final String TAG = "DOWNLOAD_MANAGER";
    private String _username;
    private String _password;
    private boolean basicAuthentication = false;
    Map<Integer,ImageIcon> downloadMap;
    private ImageIcon imageIcon;


    public DownloadResourceManager(Activity activity) {
        this.thinDownloadManager = new ThinDownloadManager();
        PreferenceManager preferenceManager = PreferenceManager.getInstance(activity);
        this._username = preferenceManager.getUsername();
        this._password = preferenceManager.getPassword();
        this.activity = activity;
        this.basicAuthentication = true;
        this.downloadMap = new HashMap<>();
    }


    public void add(String url, String destination, ImageIcon imageIcon) {
        if (!url.isEmpty() && !destination.isEmpty()) {
            Uri downloadUri = Uri.parse(url);
            Uri destinationUri = Uri.parse(activity.getFilesDir() + "/" + destination);

            DownloadRequest downloadRequest = new DownloadRequest(downloadUri);

            if (basicAuthentication) {
                String credentials = _username + ":" + _password;
                final String basic =
                        "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                downloadRequest.addCustomHeader("Authorization", basic);
                downloadRequest.addCustomHeader("Accept", "application/json");
            }
            downloadRequest.setDestinationURI(destinationUri);
            downloadRequest.setRetryPolicy(new DefaultRetryPolicy());
            downloadRequest.setStatusListener(new DownloadStatusListener());

            int downloadID = thinDownloadManager.add(downloadRequest);
            downloadMap.put(downloadID,imageIcon);

        } else {
            Log.e(TAG, " url or destination is empty");
        }

    }


    public class DownloadStatusListener implements DownloadStatusListenerV1 {

        @Override
        public void onDownloadComplete(DownloadRequest downloadRequest) {
            Log.d(TAG, "complete : " + downloadRequest.getUri());
            ImageIcon imageIcon = downloadMap.get(downloadRequest.getDownloadId());
            imageIcon.draw();
            downloadMap.remove(downloadRequest.getDownloadId());
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
