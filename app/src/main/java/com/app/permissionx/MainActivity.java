package com.app.permissionx;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.zndroid.permission.PermissionX;
import com.zndroid.permission.callback.ExplainReasonCallbackWithBeforeParam;
import com.zndroid.permission.callback.ForwardToSettingsCallback;
import com.zndroid.permission.callback.RequestCallback;
import com.zndroid.permission.request.ExplainScope;
import com.zndroid.permission.request.ForwardScope;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button makeCallBtn = findViewById(R.id.makeCallBtn);
        makeCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionX.init(MainActivity.this)
                        .permissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.CALL_PHONE)
                        .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                            @Override
                            public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
//                                scope.showRequestReasonDialog(deniedList, "为了保证程序正常工作，请您同意以下权限申请" + deniedList, "我已明白");
                                CustomDialog customDialog = new CustomDialog(MainActivity.this, "您拒绝了以下权限", deniedList);
                                scope.showRequestReasonDialog(customDialog);
                            }
                        })
                        .onForwardToSettings(new ForwardToSettingsCallback() {
                            @Override
                            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                                CustomDialog customDialog = new CustomDialog(MainActivity.this, "您拒绝了以下权限", deniedList, true);
                                scope.showForwardToSettingsDialog(customDialog);
//                                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限" + deniedList, "我已明白");
                            }
                        })
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                                if (allGranted) {
                                    Toast.makeText(MainActivity.this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}