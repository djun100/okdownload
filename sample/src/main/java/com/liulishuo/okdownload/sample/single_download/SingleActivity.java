/*
 * Copyright (c) 2017 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liulishuo.okdownload.sample.single_download;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;
import com.liulishuo.okdownload.sample.R;
import com.liulishuo.okdownload.sample.base.BaseSampleActivity;
import com.liulishuo.okdownload.sample.util.DemoUtil;
import com.liulishuo.okdownload.sample.util.UtilDownload;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;


/**
 * On this demo you can see the simplest way to download a task.
 */
public class SingleActivity extends BaseSampleActivity {

    private static final String TAG = "SingleActivity";
    private DownloadTask task;
    TextView statusTv;
    ProgressBar progressBar;
    View actionView;
    TextView actionTv;
    SingleDownloadListener mSingleDownloadListener;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        statusTv=findViewById(R.id.statusTv);
        progressBar=findViewById(R.id.progressBar);
        actionView=findViewById(R.id.actionView);
        actionTv=findViewById(R.id.actionTv);


        task = UtilDownload.createTask(new UtilDownload.BeanUrl()
                .setUrl("http://dldir1.qq.com/weixin/android/weixin6516android1120.apk")
                .setName("single-test"));
        mSingleDownloadListener=new SingleDownloadListener();


        final StatusUtil.Status status = UtilDownload.getInitStatus(task);
        final BreakpointInfo info = UtilDownload.getInitBreakpointInfo(task);

        if (status == StatusUtil.Status.COMPLETED) {
            progressBar.setProgress(progressBar.getMax());
        }
        statusTv.setText(status.toString());
        if (info != null) {
            Log.d(TAG, "init status with: " + info.toString());
            DemoUtil.calcProgressToView(progressBar, info.getTotalOffset(), info.getTotalLength());
        }
        actionTv.setText(R.string.start);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                final boolean started = task.getTag() != null;
                if (started) {
                    task.cancel();
                } else {
                    actionTv.setText(R.string.cancel);
                    task.enqueue(mSingleDownloadListener);
                    task.setTag("mark-task-started");
                }
            }
        });
    }

    @Override public int titleRes() {
        return R.string.single_download_title;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel();
    }

    class SingleDownloadListener extends DownloadListener4WithSpeed{
        private long totalLength;
        private String readableTotalLength;

        @Override public void taskStart(@NonNull DownloadTask task) {
            statusTv.setText(R.string.task_start);
        }

        @Override
        public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info,
                              boolean fromBreakpoint,
                              @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
            statusTv.setText(R.string.info_ready);

            totalLength = info.getTotalLength();
            readableTotalLength = Util.humanReadableBytes(totalLength, true);
            DemoUtil.calcProgressToView(progressBar, info.getTotalOffset(), totalLength);
        }

        @Override public void connectStart(@NonNull DownloadTask task, int blockIndex,
                                           @NonNull Map<String, List<String>> requestHeaders) {
            final String status = "Connect Start " + blockIndex;
            statusTv.setText(status);
        }

        @Override
        public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode,
                               @NonNull Map<String, List<String>> responseHeaders) {
            final String status = "Connect End " + blockIndex;
            statusTv.setText(status);
        }

        @Override
        public void progressBlock(@NonNull DownloadTask task, int blockIndex,
                                  long currentBlockOffset,
                                  @NonNull SpeedCalculator blockSpeed) {
        }

        @Override public void progress(@NonNull DownloadTask task, long currentOffset,
                                       @NonNull SpeedCalculator taskSpeed) {
            final String readableOffset = Util.humanReadableBytes(currentOffset, true);
            final String progressStatus = readableOffset + "/" + readableTotalLength;
            final String speed = taskSpeed.speed();
            final String progressStatusWithSpeed = progressStatus + "(" + speed + ")";

            statusTv.setText(progressStatusWithSpeed);
            DemoUtil.calcProgressToView(progressBar, currentOffset, totalLength);
        }

        @Override
        public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info,
                             @NonNull SpeedCalculator blockSpeed) {
        }

        @Override public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause,
                                      @Nullable Exception realCause,
                                      @NonNull SpeedCalculator taskSpeed) {
            final String statusWithSpeed = cause.toString() + " " + taskSpeed.averageSpeed();
            statusTv.setText(statusWithSpeed);

            actionTv.setText(R.string.start);
            // mark
            task.setTag(null);
            if (cause == EndCause.COMPLETED) {
            }
        }
    }
}
