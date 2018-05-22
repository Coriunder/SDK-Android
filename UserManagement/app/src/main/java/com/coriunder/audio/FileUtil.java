package com.coriunder.audio;

import android.content.Context;
import android.os.Environment;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public interface OnFileReadyListener {
        public void onFileReady(File file);
    }

    /**
     * Retrieves the file from response OR returns the file if it's already existed. If the error occurred while
     * retrieving or writing file @null will be returned.
     *
     * @param fileName - the name of file without path and extension example:{@value "My media file"}
     * @param response - HttpResponse to get the file from
     * @param listener - Listener to call when file is retrieved/found.
     */
    public static void writeFile(final Context context, final String fileName, final HttpResponse response, final OnFileReadyListener listener) {
        if (response != null && listener != null) {
/*
            final ProgressSpinner spinner = new ProgressSpinner(context);
            spinner.setCancelable(false);
            spinner.show();
*/
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // Getting the extension of the file
                    Header contentHeader = response.getFirstHeader("Content-Disposition");
                    if (contentHeader == null || contentHeader.getValue() == null) {
                        listener.onFileReady(null);
                        return;
                    }
                    String contentHeaderValue = contentHeader.getValue();
                    int startPoint = contentHeaderValue.indexOf("\"");
                    int endPoint = contentHeaderValue.lastIndexOf("\"");
                    String fullFileName = contentHeaderValue.substring(startPoint + 1, endPoint);
                    String extension = fullFileName.substring(fullFileName.lastIndexOf("."));

                    final String fileNameWithExtension = fileName + extension;

                    File mediaDir = getMediaDir();
                    mediaDir.mkdirs();
                    mediaDir = new File(mediaDir, fileNameWithExtension);
                    if (mediaDir.exists()) {
                        listener.onFileReady(mediaDir);
/*
                        spinner.dismiss();
*/
                    } else {
                        try {
                            mediaDir.getParentFile().mkdirs();
                            mediaDir.createNewFile();
                            HttpEntity entity = response.getEntity();
                            FileOutputStream fileOS = new FileOutputStream(mediaDir);
                            entity.writeTo(fileOS);
                            entity.consumeContent();
                            fileOS.flush();
                            fileOS.close();

                            listener.onFileReady(mediaDir);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            listener.onFileReady(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onFileReady(null);
                        } finally {
/*
                            spinner.dismiss();
*/
                        }
                    }
                }
            }).start();
        }
    }

    public static File getMediaDir() {
        return new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Coriunder");
    }
}
