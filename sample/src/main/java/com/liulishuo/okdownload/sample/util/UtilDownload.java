package com.liulishuo.okdownload.sample.util;

import com.cy.app.UtilContext;
import com.liulishuo.okdownload.DownloadTask;

import java.io.File;

public class UtilDownload {

    public static DownloadTask createTask(BeanUrl beanUrl){
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




//    public static DownloadTask getDownloadTask(String url){
//
//    }

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
