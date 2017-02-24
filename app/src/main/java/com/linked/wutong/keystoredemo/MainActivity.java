package com.linked.wutong.keystoredemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    private TextView textShow;
    private Button encrypt;
    private Button decrypt;
    private EditText dataText;


    private Encryptor encryptor;
    private Decryptor decryptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataText = (EditText) findViewById(R.id.data);
        encrypt = (Button) findViewById(R.id.encrypt);
        decrypt = (Button) findViewById(R.id.decrypt);
        textShow = (TextView) findViewById(R.id.text);

        encryptor = new Encryptor();

        try {
            decryptor = new Decryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }

        encrypt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                encryptText();
            }
        });

        decrypt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                decryptText();
            }
        });
    }

    private void decryptText() {
        try {
            textShow.setText(decryptor
                    .decryptData(TAG, encryptor.getEncryption(), encryptor.getIv()));
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void encryptText() {

        try {
            final byte[] encryptedText = encryptor
                    .encryptText(TAG, dataText.getText().toString());
            textShow.setText(Base64.encodeToString(encryptedText, Base64.DEFAULT));
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "onClick() called with: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}
