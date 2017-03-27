package pers.victor.androiddemos.activity;

import android.view.View;

import pers.victor.androiddemos.R;

import pers.victor.androiddemos.view.DownloadProgressView;

public class FlymeDownloadActivity extends ToolbarActivity {
    private boolean isDownloading = false;
    private boolean isOnDestroy;

    @Override
    public int bindLayout() {
        return R.layout.activity_flyme_download;
    }

    @Override
    public void initView() {
        final DownloadProgressView downloadProgressView = $(R.id.download_progress);
        downloadProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDownloading) {
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isDownloading = true;
                        float p = 0f;
                        while (p <= 101f) {
                            if (isOnDestroy) {
                                return;
                            }
                            downloadProgressView.setProgress(p);
                            p += 0.1f;
                            try {
                                Thread.sleep(8);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        isDownloading = false;
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        isOnDestroy = true;
        super.onDestroy();
    }
}
