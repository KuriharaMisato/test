package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_scan;
    String[] permission = {
            Manifest.permission.CAMERA
    };
    int PMS_CODE = 11;
    EditText ed;
    DAO dao;
    Date now;
    SimpleDateFormat format = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
    HistoryAdapter adapter;
    RecyclerView rcv;
    List<History> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = findViewById(R.id.ed_result);
        btn_scan = findViewById(R.id.btn_scan);
        dao = new DAO(MainActivity.this);
        btn_scan.setOnClickListener(view -> {
            if (CheckPermission() == true){
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Result();
        setRCV();
    }
    public Date TXTtoDate(String txt){
        try {
            return format.parse(txt);
        }catch (Exception e){
            return new Date();
        }
    }

    public void setRCV(){
        rcv = findViewById(R.id.rcv_history);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        rcv.setLayoutManager(mLayoutManager);
        rcv.setItemAnimator(new DefaultItemAnimator());
        list = dao.getAll();
        Collections.sort(list, new Comparator<History>() {
            @Override
            public int compare(History t0, History t1) {
                return TXTtoDate(t1.strDate).compareTo(TXTtoDate(t0.strDate));
            }
        });
        for (History h: list){
            if (h.content.contains("http") || h.content.contains("https")){
                h.color = "#246bce";
            }else {
                h.color = "#000000";
            }
        }
        adapter = new HistoryAdapter(list, MainActivity.this, new ITFOnClick() {
            @Override
            public void onClick(History history) {
                if (history.content.contains("http") || history.content.contains("https")){
                    openLink(history.content);
                }
            }
        });
        rcv.setAdapter(adapter);
    }

    public void Result(){
        Intent intent = getIntent();
        String rs = intent.getStringExtra("rs");

        if (rs != null){
            ed.setVisibility(View.VISIBLE);
            ed.setText(rs);
            History history = new History();
            history.content = rs;
            now = new Date();
            history.strDate = format.format(now);
            if (dao.insert(history) > 0){
                setRCV();
            }

            if (rs.contains("http") || rs.contains("https")){
                openLink(rs);
            }
        }
    }

    public boolean openLink(String txt){
        try {
            Uri uriUrl = Uri.parse(txt);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
            return true;
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "liên kết bị lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean CheckPermission(){
//        List<String> listP = new ArrayList<>();
//        for (String p: permission){
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), p) != PackageManager.PERMISSION_GRANTED){
//                listP.add(p);
//            }
//        }
//        if (listP.size()<1){
//            ActivityCompat.requestPermissions(this, listP.toArray(new String[listP.size()]), PMS_CODE);
//            return false;
//        }
        return true;
    }
}