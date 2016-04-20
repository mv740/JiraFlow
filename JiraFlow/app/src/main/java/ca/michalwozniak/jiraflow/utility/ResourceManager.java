package ca.michalwozniak.jiraflow.utility;

import android.content.Context;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ca.michalwozniak.jiraflow.model.ImageType;

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
        int indexStart = contentType.indexOf("/");
        int end = contentType.indexOf(";");

        String result = contentType.substring(indexStart + 1, end);

        switch (result) {
            case "svg+xml":
                return ImageType.SVG;
            default:
                return null;
        }
    }

    public static Drawable getDrawableFromSVG(String url, Context context)
    {

        SVG svg = null;
        String svgData = getStringFromFile(context.getFilesDir() + "/" + url);

        try {
            svg = SVG.getFromString(svgData);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        Picture picture = svg.renderToPicture();
        return  new PictureDrawable(picture);
    }

}
