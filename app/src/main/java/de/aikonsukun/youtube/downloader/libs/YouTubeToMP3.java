package de.aikonsukun.youtube.downloader.libs;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YouTubeToMP3 extends Activity {

    private String youtubeLink;
    private String musicTitle = ""; // example piano - Mozart - 1
    private String fileName = ""; // example pianoMoz1.mp3
    private Uri downloadLink;
    private Context context;

    public YouTubeToMP3(Context context) {
        this.context = context;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
        getYoutubeDownloadLink();
    }

    private void getYoutubeDownloadLink() {
        Log.d("Run", "0 init");
        new YouTubeExtractor(context) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles == null) {
                    Log.d("Run", "1 how it shouldn't be");
                } else {
                    Log.d("Run", "1 how it should be");
                    // Iterate over itags
                    for (int i = 0, itag; i < ytFiles.size(); i++) {
                        Log.d("Run", "2");
                        itag = ytFiles.keyAt(i);
                        YtFile ytFile = ytFiles.get(itag);
                        if (ytFile.getFormat().getHeight() == -1) {
                            musicTitle = videoMeta.getTitle();
                            downloadLink = Uri.parse(ytFile.getUrl());
                            Log.d("Run", "3");
                            if (musicTitle.length() > 55) {
                                fileName = musicTitle.substring(0, 55) + "." + ytFile.getFormat().getExt();
                            } else {
                                fileName = musicTitle + "." + ytFile.getFormat().getExt();
                            }
                            fileName = fileName.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
                        }
                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }

    public void download(Uri downloadLink) {

        DownloadManager.Request request = new DownloadManager.Request(downloadLink);
        request.setTitle(musicTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

    public Uri getDownloadLink() {
        return downloadLink;
    }
}
