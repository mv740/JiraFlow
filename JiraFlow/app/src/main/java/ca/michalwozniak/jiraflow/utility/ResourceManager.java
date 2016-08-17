package ca.michalwozniak.jiraflow.utility;

import android.content.Context;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.utility.svg.SvgDecoder;
import ca.michalwozniak.jiraflow.utility.svg.SvgDrawableTranscoder;
import ca.michalwozniak.jiraflow.utility.svg.SvgSoftwareLayerSetter;

/**
 * Created by michal on 4/19/2016.
 */
public class ResourceManager {

    private final static String TAG = "RESOURCE_MANAGER";

    private ResourceManager() {
    }

    public static String getStringFromFile(String url) {
        String ret = "";
        File fl = new File(url);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fl);
            ret = convertStreamToString(fin);
            //Make sure you close all streams.
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static ImageType getImageType(String contentType) {

        Log.d("getImageType", contentType);
        if (contentType.contains("svg")) {
            return ImageType.SVG;
        } else if (contentType.contains("png")) {
            return ImageType.PNG;
        } else if (contentType.contains("jpeg") || contentType.contains("jpg"))
            return ImageType.JPEG;
        else
            return null;

    }

    public static boolean isSVG(String url) {
        if (url.contains("pid") && url.contains("avatarId")) {
            return true;
        } else
            return false;
    }

    public static String fixImageUrl(String url) {
        int lastIndex = url.lastIndexOf('&');
        return url.substring(0, lastIndex);
    }

    public static Drawable getDrawableFromSVG(String url, Context context) {

        SVG svg = null;
        String svgData = getStringFromFile(context.getFilesDir() + "/" + url);

        try {
            svg = SVG.getFromString(svgData);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        Picture picture = svg.renderToPicture();
        return new PictureDrawable(picture);
    }

    //---------------------------------------------------------------------------------------------
    //Reused static Method

    public static GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> getGenericRequestBuilderForSVG(Context context) {
        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

        requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.ic_check)
                .error(R.drawable.zzz_controller_xbox)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        return requestBuilder;
    }

    public static String getEncoredCredentialString(Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        String credentials = sessionManager.getUsername() + ":" + sessionManager.getPassword();
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    // Image Type Icon ID
    public static int getIssueTypeIconId(String name) {
        switch (name) {
            case "Bug":
                return R.drawable.bug;

            case "Story":
                return R.drawable.story;

            case "Task":
                return R.drawable.task;

            case "Epic":
                return R.drawable.epic;

        }
        return 0;
    }
    //http://stackoverflow.com/questions/14853389/how-to-convert-utc-timestamp-to-device-local-time-in-android
    //http://stackoverflow.com/questions/18483314/unparseable-date-2013-07-11t134122-000z-at-offset-23
    public static String getDate(String OurDate)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);

            Log.d("OurDate", OurDate);
        }
        catch (Exception e)
        {
            Log.e("error",e.getMessage());
            OurDate = "00-00-0000 00:00";
        }
        return OurDate;
    }

}
