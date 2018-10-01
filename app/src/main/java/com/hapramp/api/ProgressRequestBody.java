package com.hapramp.api;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
  private static final int DEFAULT_BUFFER_SIZE = 2048;
  boolean alreadRead;
  private File mFile;
  private String mPath;
  private UploadCallbacks mListener;

  public ProgressRequestBody(final File file, final UploadCallbacks listener) {
    mFile = file;
    mListener = listener;
  }

  @Override
  public MediaType contentType() {
    return MediaType.parse("image/*");
  }

  @Override
  public long contentLength() throws IOException {
    return mFile.length();
  }

  @Override
  public void writeTo(BufferedSink sink) throws IOException {
    long fileLength = mFile.length();
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    FileInputStream in = new FileInputStream(mFile);
    long uploaded = 0;
    try {
      int read;
      Handler handler = new Handler(Looper.getMainLooper());
      while ((read = in.read(buffer)) != -1) {
        if (alreadRead) {
          handler.post(new ProgressUpdater(uploaded, fileLength));
          uploaded += read;
        } else {
          handler.post(new Runnable() {
            @Override
            public void run() {
              if (mListener != null) {
                mListener.onProcessing();
              }
            }
          });
        }
        sink.write(buffer, 0, read);
      }
      alreadRead = true;
    }
    finally {
      in.close();
    }
  }

  public interface UploadCallbacks {
    void onProgressUpdate(int percentage);

    void onError();

    void onProcessing();

    void onFinish();
  }

  private class ProgressUpdater implements Runnable {
    private long mUploaded;
    private long mTotal;

    public ProgressUpdater(long uploaded, long total) {
      mUploaded = uploaded;
      mTotal = total;
    }

    @Override
    public void run() {
      mListener.onProgressUpdate((int) (100 * mUploaded / mTotal));
    }
  }
}
