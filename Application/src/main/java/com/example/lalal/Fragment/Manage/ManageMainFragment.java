package com.example.lalal.Fragment.Manage;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lalal.CustomView.MagnetView;
import com.example.lalal.MyNewGraduate.R;

public class ManageMainFragment extends Fragment {

    private MagnetView btn_xinlv;
    private MagnetView btn_bluetooth;
    private MagnetView btn_xiaoxitongzhi;
    private MagnetView btn_chuanshu;
    private MagnetView btn_qita;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_managemain, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_xinlv = (MagnetView) view.findViewById(R.id.btn_xinlv);
        btn_bluetooth = (MagnetView) view.findViewById(R.id.btn_bluetooth);
        btn_xiaoxitongzhi = (MagnetView) view.findViewById(R.id.btn_xiaoxitongzhi);
        btn_chuanshu = (MagnetView) view.findViewById(R.id.btn_chuanshu);
        btn_qita = view.findViewById(R.id.btn_qita);

        //心率
        btn_xinlv.setOnClickIntent(new MagnetView.OnViewClick() {
            @Override
            public void onClick() {
                Intent intent = new Intent(getActivity().getApplicationContext(), HeartActivity.class);
                startActivity(intent);
            }
        });
        //蓝牙
        btn_bluetooth.setOnClickIntent(new MagnetView.OnViewClick() {
            @Override
            public void onClick() {
//                Intent intent = new Intent(ManageMainFragment.this,BluetoothActivity.class);
                Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                startActivity(intent);
            }
        });
        //消息通知
        btn_xiaoxitongzhi.setOnClickIntent(new MagnetView.OnViewClick() {
            @Override
            public void onClick() {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });
        //传输
        btn_chuanshu.setOnClickIntent(new MagnetView.OnViewClick() {
            @Override
            public void onClick() {
//                Intent intent = new Intent(getActivity(), MusicCSActivity.class);
//                startActivity(intent);
            }
        });
        btn_qita.setOnClickIntent(new MagnetView.OnViewClick() {
            @Override
            public void onClick() {
//                PackageManager packageManager = getActivity().getPackageManager();
//                    Intent intent = packageManager.getLaunchIntentForPackage("com.netease.cloudmusic");
//                    startActivity(intent);
            }
        });
    }
}
