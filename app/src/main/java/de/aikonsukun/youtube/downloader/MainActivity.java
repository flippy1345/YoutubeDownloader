package de.aikonsukun.youtube.downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

 // https://github.com/HaarigerHarald/android-youtubeExtractor/blob/master/sampleApp/src/main/java/at/huber/sampleDownload/SampleDownloadActivity.java //
public class MainActivity extends AppCompatActivity {

    Button nextButton;
    EditText urlTextEdit;
    String youtubeLink = "https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextButton = (Button) findViewById(R.id.firstStepButton);
        urlTextEdit = (EditText) findViewById(R.id.urlTexteEdit);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeLink += urlTextEdit.getText().toString();
                Log.d("urlTextEdit", "URL : " + youtubeLink);
                getYoutubeDownloadUrl(youtubeLink);
            }
        });
    }

    private void getYoutubeDownloadUrl(String youtubeLink) {
        new YouTubeExtractor(this) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    finish();
                    return;
                }
                // Iterate over itags
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    // ytFile represents one file with its url and meta data
                    YtFile ytFile = ytFiles.get(itag);

                    // Just add videos in a decent format => height -1 = audio
                    if (ytFile.getFormat().getHeight() == -1) {
                        showResults(vMeta.getTitle(), ytFile);
                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }


    private void showResults(final String videoTitle, final YtFile ytfile) {
        String audioData = "Audio " + ytfile.getFormat().getAudioBitrate() + " kbit/s";
        audioData += (ytfile.getFormat().isDashContainer()) ? " dash" : "";
        Log.d("showResults", "showResults : " + audioData);

        String filename;
        if (videoTitle.length() > 55) {
            filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
        } else {
            filename = videoTitle + "." + ytfile.getFormat().getExt();
        }
        filename = filename.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
        downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
        finish();
    }


    private void downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName) {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

}


