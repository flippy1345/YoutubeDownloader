package de.aikonsukun.youtube.downloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.aikonsukun.youtube.downloader.libs.YouTubeToMP3;

// https://github.com/HaarigerHarald/android-youtubeExtractor/blob/master/sampleApp/src/main/java/at/huber/sampleDownload/SampleDownloadActivity.java //
public class MainActivity extends AppCompatActivity {

    private Button nextButton;
    private EditText urlTextEdit;
    private YouTubeToMP3 youTubeToMP3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextButton = (Button) findViewById(R.id.firstStepButton);
        urlTextEdit = (EditText) findViewById(R.id.urlTexteEdit);
        youTubeToMP3 = new YouTubeToMP3(this.getApplicationContext());

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeLink = urlTextEdit.getText().toString();
                Log.d("urlTextEdit", "URL : " + youtubeLink);
                youTubeToMP3.setYoutubeLink(youtubeLink);
                Log.d("getDownloadLink", "youTubeToMP3.getDownloadLink(): " + youTubeToMP3.getDownloadLink());
                //youTubeToMP3.download(youTubeToMP3.getDownloadLink());
            }
        });
    }
}


