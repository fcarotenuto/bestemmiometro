package appbrutte.com.bestemmiometro;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Locale;
/**
 * Main Activity
* */
public class MainActivity extends AppCompatActivity {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView txtBestemmia;
    private Button btnBestemmia;
    private Button btnShare;
    private MediaPlayer mediaplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        txtBestemmia=(TextView)this.findViewById(R.id.txtBestemmia);
        btnBestemmia=(Button)this.findViewById(R.id.btnBestemmia);
        btnBestemmia.setText(R.string.init_btn_bestemmia);
        btnShare=(Button)this.findViewById(R.id.btnShare);
        btnShare.setVisibility(View.GONE);
        btnBestemmia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();

            }
        });
        mediaplayer=new MediaPlayer();

    }
    @Override
    public void onStop(){
        super.onStop();
        //mediaplayer.release();
        //mediaplayer=null;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO", true);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                    txtBestemmia.setText(result.get(0));
                    btnBestemmia.setText(R.string.continue_btn_bestemmia);
                    btnShare.setVisibility(View.VISIBLE);
                    Uri audioUri = data.getData();
                    try {
                        System.out.println(audioUri);
                        mediaplayer.stop();
                        mediaplayer.reset();

                        mediaplayer.setDataSource(getApplicationContext(), audioUri);
                        mediaplayer.prepare();
                        mediaplayer.start();


                    }catch (Exception e){
                        e.printStackTrace();
                    };

                }
                break;
            }

        }
    }
}
