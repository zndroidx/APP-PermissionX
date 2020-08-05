package com.app.permissionx;

import android.Manifest;
import android.content.Context;
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

    private WeakReference<Context> context;

    public CustomDialog(@NonNull Context context, String message, List<String> permissions) {
        super(context);
        this.message = message;
        this.permissions = permissions;
        this.context = new WeakReference<>(context);
        this.groupSet = new HashSet<>();
    }

    private void initPermissionPools(){
        permissionMap.put(Manifest.permission_group.CALENDAR, Manifest.permission.READ_CALENDAR);
        permissionMap.put(Manifest.permission_group.CALENDAR, Manifest.permission.WRITE_CALENDAR);
        permissionMap.put(Manifest.permission_group.CALL_LOG, Manifest.permission.READ_CALL_LOG);
        permissionMap.put(Manifest.permission_group.CALL_LOG, Manifest.permission.WRITE_CALL_LOG);
        permissionMap.put(Manifest.permission_group.CALL_LOG, Manifest.permission.PROCESS_OUTGOING_CALLS);
        permissionMap.put(Manifest.permission_group.CAMERA, Manifest.permission.CAMERA);
        permissionMap.put(Manifest.permission_group.CONTACTS, Manifest.permission.READ_CONTACTS);
        permissionMap.put(Manifest.permission_group.CONTACTS, Manifest.permission.WRITE_CONTACTS);
        permissionMap.put(Manifest.permission_group.CONTACTS, Manifest.permission.GET_ACCOUNTS);
        permissionMap.put(Manifest.permission_group.LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        permissionMap.put(Manifest.permission_group.LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionMap.put(Manifest.permission_group.LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        permissionMap.put(Manifest.permission_group.MICROPHONE, Manifest.permission.RECORD_AUDIO);
        permissionMap.put(Manifest.permission_group.PHONE, Manifest.permission.READ_PHONE_STATE);
        permissionMap.put(Manifest.permission_group.PHONE, Manifest.permission.READ_PHONE_NUMBERS);
        permissionMap.put(Manifest.permission_group.PHONE, Manifest.permission.CALL_PHONE);
        permissionMap.put(Manifest.permission_group.PHONE, Manifest.permission.ANSWER_PHONE_CALLS);
        permissionMap.put(Manifest.permission_group.PHONE, Manifest.permission.ADD_VOICEMAIL);
        permissionMap.put(Manifest.permission_group.PHONE, Manifest.permission.USE_SIP);
        permissionMap.put(Manifest.permission_group.PHONE, Manifest.permission.ACCEPT_HANDOVER);
        permissionMap.put(Manifest.permission_group.SENSORS, Manifest.permission.BODY_SENSORS);
        permissionMap.put(Manifest.permission_group.ACTIVITY_RECOGNITION, Manifest.permission.ACTIVITY_RECOGNITION);
        permissionMap.put(Manifest.permission_group.SMS, Manifest.permission.SEND_SMS);
        permissionMap.put(Manifest.permission_group.SMS, Manifest.permission.RECEIVE_SMS);
        permissionMap.put(Manifest.permission_group.SMS, Manifest.permission.READ_SMS);
        permissionMap.put(Manifest.permission_group.SMS, Manifest.permission.RECEIVE_WAP_PUSH);
        permissionMap.put(Manifest.permission_group.SMS, Manifest.permission.RECEIVE_MMS);
        permissionMap.put(Manifest.permission_group.STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionMap.put(Manifest.permission_group.STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionMap.put(Manifest.permission_group.STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION);
    }

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
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
                if (entry.getValue().equals(p)) {
                    String permissionGroup = entry.getKey();
                    if (permissionGroup != null && !groupSet.contains(permissionGroup)) {
                        TextView textView = (TextView) LayoutInflater.from(context.get()).inflate(R.layout.permissions_item, permissionsLayout, false);
                        permissionsLayout.addView(textView);
                        groupSet.add(permissionGroup);
                    }
                }
            }

        }
    }
}
