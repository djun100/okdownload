package com.liulishuo.okdownload.sample.util;

import com.cy.app.UtilContext;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;

import java.io.File;

public class UtilDownload {

    public static DownloadTask createTask(BeanUrl beanUrl) {
        final File parentFile = DemoUtil.getParentFile(UtilContext.getContext());
        DownloadTask task = new DownloadTask.Builder(beanUrl.getUrl(), parentFile)
                .setFilename(beanUrl.getName())
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(16)
                // ignore the same task has already completed in the past.
                .setPassIfAlreadyCompleted(false)
                .build();
        return task;
    }

    public static boolean isTaskRunning(DownloadTask task) {
        final StatusUtil.Status status = StatusUtil.getStatus(task);
        return status == StatusUtil.Status.PENDING || status == StatusUtil.Status.RUNNING;
    }

    /**
     * eg:status == StatusUtil.Status.COMPLETED
     * status.toString()
     *
     * @param task
     * @return
     */
    public static StatusUtil.Status getInitStatus(DownloadTask task) {
        return StatusUtil.getStatus(task);
    }

    /**
     * eg:info.toString()
     * info.getTotalOffset()    已下载byte
     * info.getTotalLength()    文件大小byte
     *
     * @param task
     * @return
     */
    public static BreakpointInfo getInitBreakpointInfo(DownloadTask task) {
        return StatusUtil.getCurrentInfo(task);
    }

    public static void download(BeanUrl beanUrl, DownloadListener downloadListener) {
        startDownloadTask(createTask(beanUrl), downloadListener);
    }

    public static void startDownloadTask(DownloadTask task, DownloadListener downloadListener) {
        if (task != null) {
            task.enqueue(downloadListener);
        }
    }

    public static void stopDownloadTask(DownloadTask task) {
        if (task != null) {
            task.cancel();
        }
    }

    public static class BeanUrl {
        public String url;
        public String name;

        public String getUrl() {
            return url;
        }

        public BeanUrl setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getName() {
            return name;
        }

        public BeanUrl setName(String name) {
            this.name = name;
            return this;
        }
    }

}
