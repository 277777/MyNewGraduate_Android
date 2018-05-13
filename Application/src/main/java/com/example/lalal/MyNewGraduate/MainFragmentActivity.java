package com.example.lalal.MyNewGraduate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lalal.Fragment.Dongtai.DongtaiMainFragment;
import com.example.lalal.Fragment.Manage.ManageMainFragment;
import com.example.lalal.Fragment.Personal.PermissionsActivity;
import com.example.lalal.Fragment.Personal.PersonalMainFragment;
import com.example.lalal.Tools.LoginPhoto.FileStorage;

import java.io.File;

public class MainFragmentActivity extends Activity {

    private RadioGroup radioGroup;

    private ManageMainFragment mManageMainFragment;
    private PersonalMainFragment mPersonalMainFragment;
    private DongtaiMainFragment mDongtaiMainFragment;

    private Fragment manage,personal,dongtai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        mManageMainFragment = new ManageMainFragment();
        mPersonalMainFragment = new PersonalMainFragment();
        mDongtaiMainFragment = new DongtaiMainFragment();
        getFragmentManager().beginTransaction().add(R.id.framelayout,mManageMainFragment,"manage").commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
        manage = getFragmentManager().findFragmentByTag("manage");
        System.out.println(manage);
        initView();
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_manager:
                        if(personal!=null){
                            Log.v("manage","personal----------hide----------");
                            getFragmentManager().beginTransaction().hide(personal).commitAllowingStateLoss();
                        }
                        if(dongtai!=null){
                            Log.v("manage","dongtai----------hide----------");
                            getFragmentManager().beginTransaction().hide(dongtai).commitAllowingStateLoss();
                        }
                        Log.v("manage","manage----------show----------");
                        getFragmentManager().beginTransaction().show(manage).commitAllowingStateLoss();
                        break;
                    case R.id.rb_personal:
                        if(manage!=null){
                            Log.v("personal","manage----------hide----------");
                            getFragmentManager().beginTransaction().hide(manage).commitAllowingStateLoss();
                        }
                        if(dongtai!=null){
                            Log.v("personal","dongtai----------hide----------");
                            getFragmentManager().beginTransaction().hide(dongtai).commitAllowingStateLoss();
                        }
                        if(personal!=null){
                            Log.v("personal","personal----------show----------");
                            getFragmentManager().beginTransaction().show(personal).commitAllowingStateLoss();
                        }
                        else{
                            Log.v("personal","personal----------replace----------");
                            getFragmentManager().beginTransaction().add(R.id.framelayout,mPersonalMainFragment,"personal").commitAllowingStateLoss();
                            getFragmentManager().executePendingTransactions();
                            personal = getFragmentManager().findFragmentByTag("personal");
                        }
                        break;
                    case R.id.rb_dongtai:
                        if(manage!=null){
                            Log.v("dongtai","manage----------hide----------");
                            getFragmentManager().beginTransaction().hide(manage).commitAllowingStateLoss();
                        }
                        if(personal!=null){
                            Log.v("dongtai","personal----------hide----------");
                            getFragmentManager().beginTransaction().hide(personal).commitAllowingStateLoss();
                        }
                        if(dongtai!=null){
                            Log.v("dongtai","dongtai----------show----------");
                            getFragmentManager().beginTransaction().show(dongtai).commitAllowingStateLoss();
                        }
                        else{
                            Log.v("dongtai","dongtai----------replace----------");
                            getFragmentManager().beginTransaction().add(R.id.framelayout,mDongtaiMainFragment,"dongtai").commitAllowingStateLoss();
                            getFragmentManager().executePendingTransactions();
                            dongtai = getFragmentManager().findFragmentByTag("dongtai");
                        }
                        break;
                }
            }
        });
    }



}
