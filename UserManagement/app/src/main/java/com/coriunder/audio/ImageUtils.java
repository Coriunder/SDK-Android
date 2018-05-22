package com.coriunder.audio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {
    private static final String TAG = "MahalaPlayer";

    private final static Object syncRoot = new Object();

    private static LruCache<String, Bitmap> mImageCache;

    private static void initCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = maxMemory / 8;

        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public static void addBitmapToCache(String keyUrl, Bitmap bitmap) {
        if (mImageCache == null) {
            initCache();
        }

        if (keyUrl != null && mImageCache.get(keyUrl) == null && bitmap != null) {
            mImageCache.put(keyUrl, bitmap);
        }
    }

    public static Bitmap getCachedImage(String url, boolean downloadAllowed) {
        if (mImageCache == null) {
            initCache();
        }

        if (downloadAllowed) {
            if (url == null) {
                return null;
            }

            Bitmap bitmap = mImageCache.get(url);

            if (bitmap == null) {
                synchronized (syncRoot) {
                    bitmap = downloadImage(url);
                    addBitmapToCache(url, bitmap);
                }
            }

            return bitmap;
        } else {
            return mImageCache.get(url);
        }
    }

    private static Bitmap downloadImage(String url) {
        /*WindowManager windowManager = (WindowManager) App.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();*/
        int newWidth = 200;//display.getWidth();

        return downloadImage(url, newWidth);
    }

    private static Bitmap downloadImage(String url, int newWidth) {
        int inSampleSize = 1;
        HttpURLConnection urlConnection = null;
        try {
            if (url != null && url.trim().length() > 0) {
                urlConnection = (HttpURLConnection) new URL(url).openConnection();
                InputStream in = urlConnection.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(in, null, options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;

                int newHeight = (int) (((float) newWidth / imageWidth) * imageHeight);
                int widthSampleSize = imageWidth / newWidth;
                int heightSampleSize = imageHeight / newHeight;
                inSampleSize = (widthSampleSize > heightSampleSize) ? widthSampleSize : heightSampleSize;
            }
        } catch (Exception e) {
            Log.d(TAG, "Can't download image: " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return downloadImageWithSampleSize(url, inSampleSize);
    }

    private static Bitmap downloadImageWithSampleSize(String url, int samapleSize) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        try {
            if (url != null && url.trim().length() > 0) {
                urlConnection = (HttpURLConnection) new URL(url).openConnection();
                InputStream in = urlConnection.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = samapleSize;
                bitmap = BitmapFactory.decodeStream(in, null, options);
            }
        } catch (Exception e) {
            Log.d(TAG, "Can't download imageWithSampleSize: " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return bitmap;
    }
/*
    private static ImageLoader initImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(App.getContext())
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        imageLoader.init(config);
        return imageLoader;
    }

    public static void getCachedImageAsync(final String url, final ImageView imageView) {
        imageLoader.displayImage(url, imageView);
    }

    public static void getCachedImageAsync(final String url, final ImageView imageView, int imageForLoadingResId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(imageForLoadingResId)
                .build();
        imageLoader.displayImage(url, imageView, options);
    }
*/

}
