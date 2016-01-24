package xyz.sangcomz.open_sns.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.TypedValue;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import xyz.sangcomz.open_sns.R;

/**
 * Created by sangc on 2015-12-18.
 */
public class Utils {
    public static int convertDP(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public static File BitmapToFileCache(Bitmap bitmap) {
        final long unixTime = System.currentTimeMillis() / 1000L;
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/food_note_tmp/";
        String fileName = unixTime + "tmp_sns.jpg";
        File file = new File(filePath);
        OutputStream out = null;
        // If no folders
        if (!file.exists()) {
            file.mkdirs();
//             Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
        File fileCacheItem = new File(file, fileName);
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileCacheItem;
    }

    public static Drawable drawableFromUrl(Context context, String url) throws IOException {
        if (url == null || url.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return context.getResources().getDrawable(R.drawable.bg_drawer, null);
            } else {
                return context.getResources().getDrawable(R.drawable.bg_drawer);
            }
        } else {
            Bitmap myBitmap = null;
            try {
                myBitmap = Glide
                        .with(context)
                        .load(url)
                        .asBitmap()
                        .into(800, 400)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return new BitmapDrawable(null, myBitmap);
        }

    }


    public static String getDateString(Context con, String form, int time) {

        if (con != null) {
            int curTime = (int) (System.currentTimeMillis() / 1000L);
            int diffTime = (curTime - time);


            if (diffTime > 0 & diffTime / 60 < 60) {
                return diffTime / 60 + con.getResources().getString(R.string.date_minutes_ago);
            } else if (diffTime > 0 & diffTime / 60 / 60 < 24) {
                return diffTime / 60 / 60 + con.getResources().getString(R.string.date_hours_ago);
            }
//        else if(diffTime>0 & diffTime/60/60/24 < 7){
//            return diffTime/60/60/24+r.getString(R.string.date_days_ago);
//        }
            else {
                Date timestampDate = new Date(time * 1000L);
                SimpleDateFormat format = new SimpleDateFormat(form);
                format.setTimeZone(TimeZone.getDefault());
                return format.format(timestampDate);
            }
        }
        return "-";
    }
}
