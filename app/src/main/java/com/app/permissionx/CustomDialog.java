package com.app.permissionx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zndroid.permission.request.RationaleDialog;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @name:CustomDialog
 * @author:lazy
 * @email:luzhenyuxfcy@sina.com
 * @date : 2020/8/2 23:02
 * @version:
 * @description:
 */
public class CustomDialog extends RationaleDialog {

    private LinkedHashMap<String, String> permissionMap = new LinkedHashMap<>();
    private List<String> permissions;
    private HashSet<String> groupSet;
    private String message;
    private Button negativeBtn;
    private Button positiveBtn;
    private LinearLayout permissionsLayout;

    private boolean showDescription;

    private WeakReference<Context> context;

    public CustomDialog(@NonNull Context context, String message, List<String> permissions) {
        this(context, message, permissions, false);
    }

    public CustomDialog(@NonNull Context context, String message, List<String> permissions, boolean showDescription) {
        super(context, R.style.CustomDialog);
        this.message = message;
        this.permissions = permissions;
        this.context = new WeakReference<>(context);
        this.groupSet = new HashSet<>();
        this.showDescription = showDescription;

        initPermissionPools();
    }

    private void initPermissionPools(){
        permissionMap.put(Manifest.permission.READ_CALENDAR, Manifest.permission_group.CALENDAR);
        permissionMap.put(Manifest.permission.WRITE_CALENDAR, Manifest.permission_group.CALENDAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissionMap.put(Manifest.permission.READ_CALL_LOG, Manifest.permission_group.CALL_LOG);
            permissionMap.put(Manifest.permission.WRITE_CALL_LOG, Manifest.permission_group.CALL_LOG);
            permissionMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission_group.CALL_LOG);
        }
        permissionMap.put(Manifest.permission.CAMERA, Manifest.permission_group.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionMap.put(Manifest.permission.READ_CONTACTS, Manifest.permission_group.CONTACTS);
            permissionMap.put(Manifest.permission.WRITE_CONTACTS, Manifest.permission_group.CONTACTS);
            permissionMap.put(Manifest.permission.GET_ACCOUNTS, Manifest.permission_group.CONTACTS);
        }
        permissionMap.put(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission_group.LOCATION);
        permissionMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission_group.LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionMap.put(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission_group.LOCATION);
        }
        permissionMap.put(Manifest.permission.RECORD_AUDIO, Manifest.permission_group.MICROPHONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionMap.put(Manifest.permission.READ_PHONE_STATE, Manifest.permission_group.PHONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                permissionMap.put(Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission_group.PHONE);
                permissionMap.put(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission_group.PHONE);
            }
            permissionMap.put(Manifest.permission.CALL_PHONE, Manifest.permission_group.PHONE);
            permissionMap.put(Manifest.permission.ADD_VOICEMAIL, Manifest.permission_group.PHONE);
            permissionMap.put(Manifest.permission.USE_SIP, Manifest.permission_group.PHONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                permissionMap.put(Manifest.permission.ACCEPT_HANDOVER, Manifest.permission_group.PHONE);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionMap.put(Manifest.permission.BODY_SENSORS, Manifest.permission_group.SENSORS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionMap.put(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission_group.ACTIVITY_RECOGNITION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionMap.put(Manifest.permission.SEND_SMS, Manifest.permission_group.SMS);
            permissionMap.put(Manifest.permission.RECEIVE_SMS, Manifest.permission_group.SMS);
            permissionMap.put(Manifest.permission.READ_SMS, Manifest.permission_group.SMS);
            permissionMap.put(Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission_group.SMS);
            permissionMap.put(Manifest.permission.RECEIVE_MMS, Manifest.permission_group.SMS);
        }
        permissionMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
        permissionMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionMap.put(Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission_group.STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);

        TextView textView = findViewById(R.id.messageText);
        textView.setText(message);
        positiveBtn = findViewById(R.id.positiveBtn);
        negativeBtn = findViewById(R.id.negativeBtn);
        permissionsLayout = findViewById(R.id.permissionsLayout);

        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (context.get().getResources().getDisplayMetrics().widthPixels * 0.8);

        buildPermissionsLayout();
    }

    @NonNull
    @Override
    public View getPositiveButton() {
        return positiveBtn;
    }

    @Nullable
    @Override
    public View getNegativeButton() {
        return negativeBtn;
    }

    @NonNull
    @Override
    public List<String> getPermissionsToRequest() {
        return permissions;
    }

    private void buildPermissionsLayout() {
        for (String p : permissions) {
            Set<Map.Entry<String,String>> entries = permissionMap.entrySet();
            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                if (entry.getKey().equals(p)) {
                    String permissionGroup = entry.getValue();
                    if (permissionGroup != null && !groupSet.contains(permissionGroup)) {
                        TextView textView = (TextView) LayoutInflater.from(context.get()).inflate(R.layout.permissions_item, permissionsLayout, false);
                        try {
                            PermissionGroupInfo groupInfo = context.get().getPackageManager().getPermissionGroupInfo(permissionGroup, 0);
                            textView.setText(showDescription ? groupInfo.loadDescription(context.get().getPackageManager()) : groupInfo.loadLabel(context.get().getPackageManager()));
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        permissionsLayout.addView(textView);
                        groupSet.add(permissionGroup);
                    }
                }
            }

        }
    }
}
