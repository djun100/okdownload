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

package com.liulishuo.okdownload.sample.util;
import com.liulishuo.okdownload.sample.util.UtilDownload.BeanUrl;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class DemoUtil {

    public static final String URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk";

    private static List<BeanUrl> sMBeanUrls =new ArrayList<>();

    public static void calcProgressToView(ProgressBar progressBar, long offset, long total) {
        final float percent = (float) offset / total;
        progressBar.setProgress((int) (percent * progressBar.getMax()));
    }


    public static File getParentFile(@NonNull Context context) {
        final File externalSaveDir = context.getExternalCacheDir();
        if (externalSaveDir == null) {
            return context.getCacheDir();
        } else {
            return externalSaveDir;
        }
    }

    private static void initUrls(){
        sMBeanUrls.add(new BeanUrl()
                .setUrl("http://dldir1.qq.com/weixin/android/weixin6516android1120.apk").setName("1. WeChat"));
        sMBeanUrls.add(new BeanUrl().setUrl("https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk").setName("2. LiuLiShuo"));
        sMBeanUrls.add(new BeanUrl().setUrl("https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk").setName("3. Alipay"));
        sMBeanUrls.add(new BeanUrl().setUrl("https://dldir1.qq.com/qqfile/QQforMac/QQ_V6.2.0.dmg").setName("4. QQ for Mac"));
        sMBeanUrls.add(new BeanUrl().setUrl("https://zhstatic.zhihu.com/pkg/store/zhihu").setName("5. ZhiHu"));
        sMBeanUrls.add(new BeanUrl().setUrl("http://d1.music.126.net/dmusic/CloudMusic_official_4.3.2.468990.apk").setName("6. NetEaseMusic"));
        sMBeanUrls.add(new BeanUrl().setUrl("http://d1.music.126.net/dmusic/NeteaseMusic_1.5.9_622_officialsite.dmg").setName("7. NetEaseMusic for Mac"));
        sMBeanUrls.add(new BeanUrl().setUrl("http://dldir1.qq.com/weixin/Windows/WeChatSetup.exe").setName("8. WeChat for Windows"));
        sMBeanUrls.add(new BeanUrl().setUrl("https://dldir1.qq.com/foxmail/work_weixin/wxwork_android_2.4.5.5571_100001.apk").setName("9. WeChat Work"));
//        sMBeanUrls.add(new BeanUrl().setUrl("https://dldir1.qq.com/foxmail/work_weixin/WXWork_2.4.5.213.dmg").setName("10. WeChat Work for Mac"));
    }



    static {
        initUrls();
    }

    public static List<BeanUrl> getBeanUrls() {
        return sMBeanUrls;
    }
}
