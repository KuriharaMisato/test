package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {

    TextView txt;
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        txt = findViewById(R.id.tv_rs);
        codeScannerView = (CodeScannerView) findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, codeScannerView);


        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt.setText(result.getText());
                        Intent intent = new Intent(ScannerActivity.this, MainActivity.class);
                        intent.putExtra("rs", result.getText());
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCamera();
    }

    private void requestCamera() {
        codeScanner.startPreview();
    }
}