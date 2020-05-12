package com.didichuxing.doraemonkit.kit.loginfo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.config.LogInfoConfig;
import com.didichuxing.doraemonkit.kit.core.BaseFragment;
import com.didichuxing.doraemonkit.kit.core.DokitIntent;
import com.didichuxing.doraemonkit.kit.core.DokitViewManager;
import com.didichuxing.doraemonkit.kit.core.SettingItem;
import com.didichuxing.doraemonkit.kit.core.SettingItemAdapter;
import com.didichuxing.doraemonkit.widget.titlebar.HomeTitleBar;

/**
 * Created by wanglikun on 2018/10/9.
 */

public class LogInfoSettingFragment extends BaseFragment {
    private static final String TAG = "LogInfoSettingFragment";
    private RecyclerView mSettingList;
    private SettingItemAdapter mSettingItemAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        HomeTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setListener(new HomeTitleBar.OnTitleBarClickListener() {
            @Override
            public void onRightClick() {
                finish();
            }
        });
        mSettingList = findViewById(R.id.setting_list);
        mSettingList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSettingItemAdapter = new SettingItemAdapter(getContext());
        mSettingItemAdapter.append(new SettingItem(R.string.dk_kit_log_info, LogInfoConfig.isLogInfoOpen()));
        mSettingItemAdapter.setOnSettingItemSwitchListener(new SettingItemAdapter.OnSettingItemSwitchListener() {
            @Override
            public void onSettingItemSwitch(View view, SettingItem data, boolean on) {
                if (data.desc == R.string.dk_kit_log_info) {
                    if (on) {
                        DokitIntent intent = new DokitIntent(LogInfoDokitView.class);
                        intent.mode = DokitIntent.MODE_SINGLE_INSTANCE;
                        DokitViewManager.getInstance().attach(intent);
                        //开启日志服务
                        LogInfoManager.getInstance().start();
                    } else {
                        DokitViewManager.getInstance().detach(LogInfoDokitView.class);
                        //关闭日志服务
                        LogInfoManager.getInstance().stop();
                        //清空回调
                        LogInfoManager.getInstance().removeListener();
                    }
                    LogInfoConfig.setLogInfoOpen(on);
                }
            }
        });
        mSettingList.setAdapter(mSettingItemAdapter);
    }

    @Override
    protected int onRequestLayout() {
        return R.layout.dk_fragment_log_info_setting;
    }
}