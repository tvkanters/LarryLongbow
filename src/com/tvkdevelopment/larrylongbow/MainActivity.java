package com.tvkdevelopment.larrylongbow;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/** The activity that lets you ask Larry for advice and shows it */
public class MainActivity extends Activity {

    /** The random number generator */
    private final static Random RNG = new Random();
    /** The amount of ms between each period showing up */
    private final static int TYPE_SPEED = 500;
    /** The TTS pitch to use for advice */
    private final static float TTS_PITCH = 1.4f;
    /** The TTS speed to use for advice */
    private final static float TTS_SPEED = 1.2f;

    /** The possible requests to show in the button */
    private String[] mRequests;
    /** The possible advice Larry can give */
    private String[] mAdvice;

    /** The button to request advice */
    private Button mRequestButton;
    /** The field that contains the advice */
    private TextView mAdviceField;
    /** The button to thank Larry and close the app */
    private Button mThanksButton;
    /** The button toggle TTS */
    private CheckBox mTtsCheckbox;

    /** The timer used while Larry is typing */
    private Timer mTypingTimer;
    /** The runnable to add periods or finish typing */
    private final TypingRunnable mTypingRunnable = new TypingRunnable();
    /** The amount of dots shown while Larry is typing */
    private int mDotCount;

    /** The TTS engine for pronouncing advice */
    private TextToSpeech mTts;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Get requests and advice
        mRequests = getResources().getStringArray(R.array.requests);
        mAdvice = getResources().getStringArray(R.array.advice);

        // Find the UI elements
        mRequestButton = (Button) findViewById(R.id.requestButton);
        mAdviceField = (TextView) findViewById(R.id.advice);
        mThanksButton = (Button) findViewById(R.id.thanksButton);
        mTtsCheckbox = (CheckBox) findViewById(R.id.ttsCheckbox);

        // Prepare the request button
        generateRequestText();
        mRequestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Update the request button
                mRequestButton.setEnabled(false);
                mRequestButton.setTextColor(Color.GRAY);

                // Update the advice field
                mAdviceField.setText(getString(R.string.typing));

                // Make Larry start typing
                mDotCount = 0;
                mTypingTimer = new Timer();
                mTypingTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(mTypingRunnable);
                    }
                }, TYPE_SPEED, TYPE_SPEED);
            }
        });

        // Prepare the thanks button
        mThanksButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });

        // Prepare the TTS checkbox
        mTtsCheckbox.setChecked(Pref.TTS.getBoolean());
        mTtsCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                Pref.TTS.put(isChecked);
            }
        });

        // Prepare the TTS engine
        mTts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(final int status) {
                if (status == TextToSpeech.SUCCESS) {
                    mTts.setLanguage(Locale.UK);
                    mTts.setPitch(TTS_PITCH);
                    mTts.setSpeechRate(TTS_SPEED);
                }
            }
        });
    }

    /**
     * Sets the text of the request button.
     */
    private void generateRequestText() {
        mRequestButton.setText(mRequests[RNG.nextInt(mRequests.length)]);
    }

    /**
     * The runnable that lets Larry type or finish typing.
     */
    private class TypingRunnable implements Runnable {

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            if (mDotCount++ == 3) {
                final String advice = mAdvice[RNG.nextInt(mAdvice.length)].toLowerCase(Locale.getDefault());

                // When there are enough dots, show the advice
                mAdviceField.setText("<LarryLongbow> " + advice);
                mTypingTimer.cancel();

                // Update the buttons
                mRequestButton.setEnabled(true);
                mRequestButton.setTextColor(Color.BLACK);
                generateRequestText();
                mThanksButton.setVisibility(View.VISIBLE);

                // Pronounce the advice if needed
                if (Pref.TTS.getBoolean()) {
                    mTts.speak(advice, TextToSpeech.QUEUE_FLUSH, null);
                }

            } else {
                // Add periods while Larry is typing
                mAdviceField.setText(mAdviceField.getText() + ".");
            }
        }
    }
}
