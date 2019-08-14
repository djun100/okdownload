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

package com.liulishuo.okdownload.sample.util.queue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.SeekBar;

import com.cy.okdownload.BeanUrl;
import com.cy.okdownload.UtilDownload;
import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadContextListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.sample.R;
import com.liulishuo.okdownload.sample.util.DemoUtil;
import com.liulishuo.okdownload.sample.util.queue.QueueRecyclerAdapter.QueueViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueueController {
    private static final String TAG = "QueueController";

    private List<DownloadTask> taskList = new ArrayList<>();
    private DownloadContext context;

    private final QueueListener listener = new QueueListener();

    private File queueDir;

    public DownloadContext.Builder getBuilder() {
        return builder;
    }

    DownloadContext.Builder builder;
    public void initTasks(@NonNull Context context, @NonNull DownloadContextListener listener) {
        final DownloadContext.QueueSet set = new DownloadContext.QueueSet();
        final File parentFile = new File(UtilDownload.getUsableCacheDir(context), "queue");
        this.queueDir = parentFile;

        set.setParentPathFile(parentFile);
        set.setMinIntervalMillisCallbackProcess(200);

        builder = set.commit();

        for (BeanUrl beanUrl:DemoUtil.getBeanUrls()){
            DownloadTask boundTask = builder.bind(beanUrl.getUrl());
            TagUtil.saveTaskName(boundTask, beanUrl.getName());
        }
        builder.setListener(listener);

        this.context = builder.build();
        this.taskList = Arrays.asList(this.context.getTasks());
    }

    public void deleteFiles() {
        if (queueDir != null) {
            String[] children = queueDir.list();
            if (children != null) {
                for (String child : children) {
                    if (!new File(queueDir, child).delete()) {
                        Log.w("QueueController", "delete " + child + " failed!");
                    }
                }
            }

            if (!queueDir.delete()) {
                Log.w("QueueController", "delete " + queueDir + " failed!");
            }
        }

        for (DownloadTask task : taskList) {
            TagUtil.clearProceedTask(task);
        }
    }

    public void setPriority(DownloadTask task, int priority) {
        final DownloadTask newTask = task.toBuilder().setPriority(priority).build();
        this.context = context.toBuilder()
                .bindSetTask(newTask)
                .build();
        newTask.setTags(task);
        TagUtil.savePriority(newTask, priority);
        this.taskList = Arrays.asList(this.context.getTasks());
    }

    public void start(boolean isSerial) {
        this.context.start(listener, isSerial);
    }

    public void stop() {
        if (this.context.isStarted()) {
            this.context.stop();
        }
    }

    void bind(final QueueViewHolder holder, int position) {
        final DownloadTask task = taskList.get(position);
        Log.d(TAG, "bind " + position + " for " + task.getUrl());

        listener.bind(task, holder);
        listener.resetInfo(task, holder);

        // priority
        final int priority = TagUtil.getPriority(task);
        holder.priorityTv
                .setText(holder.priorityTv.getContext().getString(R.string.priority, priority));
        holder.prioritySb.setProgress(priority);
        if (this.context.isStarted()) {
            holder.prioritySb.setEnabled(false);
        } else {
            holder.prioritySb.setEnabled(true);
            holder.prioritySb.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        boolean isFromUser;

                        @Override public void onProgressChanged(SeekBar seekBar, int progress,
                                                                boolean fromUser) {
                            isFromUser = fromUser;
                        }

                        @Override public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override public void onStopTrackingTouch(SeekBar seekBar) {
                            if (isFromUser) {
                                final int priority = seekBar.getProgress();
                                setPriority(task, priority);
                                holder.priorityTv
                                        .setText(seekBar.getContext()
                                                .getString(R.string.priority, priority));
                            }
                        }
                    });
        }
    }

    int size() {
        return taskList.size();
    }
}