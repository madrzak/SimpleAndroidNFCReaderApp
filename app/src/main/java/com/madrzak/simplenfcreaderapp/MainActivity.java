package com.madrzak.simplenfcreaderapp;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int FLAGS = NfcAdapter.FLAG_READER_NFC_A
            | NfcAdapter.FLAG_READER_NFC_V
            | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    private NfcAdapter adapter;

    private TextView tvSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSerial = (TextView) findViewById(R.id.tvSerial);

        adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) {
            Log.e(TAG, "This device doesn't support NFC");
            tvSerial.setText("This device doesn't support NFC");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.enableReaderMode(this, new NfcAdapter.ReaderCallback() {
            @Override
            public void onTagDiscovered(Tag tag) {
                String tagId = bytesToHexString(tag.getId());
                Log.i(TAG, "Scanned tag id: " + tagId);
                updateTv(tagId);
            }
        }, FLAGS, new Bundle());
    }

    @Override
    protected void onPause() {
        super.onPause();

        adapter.disableReaderMode(this);
    }


    private void updateTv(final String text) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              tvSerial.setText(text);
                          }
                      }
        );
    }


    private String bytesToHexString(byte[] src) {

        return String.format("%0" + (src.length * 2) + "X", new BigInteger(1, src));
    }
}
