package su.ict.business59.partnersforpurchasing.utills;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kaowneaw on 11/18/2016.
 */

public class ImageUtils {

    public Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                            boolean filter) {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    public File saveBitmapToFile(Bitmap bitmap) {

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopShare";
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        File file = new File(dir, ts+".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}