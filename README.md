# SDR-LIBRARY
用于sdr开发APP集成的library，封装了一些基础的UI组件以供APP使用
## gradle

project

```
    allprojects {
        repositories {
            ...
            ...
            ...
            maven {
                url "https://jitpack.io"
            }
        }
    }
```

module


>implementation 'com.github.sz-sdr:SDR-LIBRARY:`last-release`


### 已依赖
```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    // base
    implementation 'com.android.support:design:28.0.0'
    // 必须依赖的库
    // rx java     rx android
    api 'io.reactivex.rxjava2:rxandroid:2.1.0'
    api 'io.reactivex.rxjava2:rxjava:2.2.2'
    // retrofit2  已依赖OKHttp3
    api 'com.squareup.retrofit2:retrofit:2.4.0'
    api 'com.squareup.retrofit2:converter-gson:2.4.0'
    api 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
    api 'com.squareup.okhttp3:logging-interceptor:3.9.1'  // 打印log的interceptor

    // other
    // glide 变换
    api 'jp.wasabeef:glide-transformations:4.0.0'
    api ("com.github.bumptech.glide:glide:4.8.0"){
        exclude group: 'com.android.support'
    }
    annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
    api 'com.github.bumptech.glide:okhttp3-integration:4.8.0'

    // material dialog
    api 'com.afollestad.material-dialogs:core:0.9.6.0'
    // okdownload  下载文件时候使用
    // core
    api 'com.liulishuo.okdownload:okdownload:1.0.4'
    // provide sqlite to store breakpoints
    api 'com.liulishuo.okdownload:sqlite:1.0.4'
    // provide okhttp to connect to backend
    // and then please import okhttp dependencies by yourself
    api 'com.liulishuo.okdownload:okhttp:1.0.4'
    // logger
    api 'com.orhanobut:logger:2.2.0'
    // 图片查看 缩放
    api 'com.davemorrissey.labs:subsampling-scale-image-view:3.10.0'
    // Android 6.0授权
    api 'com.github.HyFun:RxPermissions:0.10.4'
    //BaseRecyclerViewAdapterHelper
    api 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.22'
    // RxActivityResult
    api 'com.github.VictorAlbertos:RxActivityResult:0.4.5-2.x'
}
```
以上api依赖的库将会一直在app中


## 使用

### 初始化
在application中

```java
// 初始化
SDR_LIBRARY.register(application, new ActivityConfig(application));
// 传入统一的glide app
SDR_LIBRARY.getInstance().setGlide(GlideApp.get(application));
```

### Base
- BaseActivity
    沉浸式状态栏的activity
- BaseFragment
    沉浸式状态栏+懒加载fragment，

    ```java
    protected void onFragmentFirstVisible() {

    }
    ```
    第一次加载的时候可以在该方法中获取数据。

    fragment数量比较少的时候推荐在PagerAdapter中的
    ``` java
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
    ```
    方法中什么都不用操作

- 配置方法

    | 方法名 | 作用 | 备注 |
    | ------ | ------ | ------ |
    | int onHeaderBarToolbarRes() | 设置toolbar的res，不设置返回0 |
    | int onHeaderBarStatusViewAlpha() | 设置状态栏的透明度（0~255），一般0/90 |
    | Drawable onHeaderBarDrawable() | 设置HeaderBar的drawable |
    | Object onHeaderBarImage() | 设置HeaderBar的图片，如果不设置，默认加载onHeaderBarDrawable()的drawable |
    | boolean isImageHeader() | 设置HeaderBar顶部是否是图片模式。true则HeaderBar在content顶部，覆盖着content，false则是在content顶部，没有覆盖 |
    | int onHeaderBarTitleGravity() | 设置标题位置 |

- 设置方法

    | 方法名 | 作用 | 备注 |
    | ------ | ------ | ------ |
    | setHeaderImage(Object image) | 设置HeaderBar的image |
    | setHeaderBarAlpha(@IntRange(from = 0, to = 255) int alpha) | 设置状态栏的透明度（0~255），一般0/90 |
    | setHeaderBarScrollChange(OnScrollListenerView view, int viewHeight) | 设置HeaderBar的滑动监听，boolean isImageHeader()返回true时才生效 |
    | setDisplayHomeAsUpEnabled() | 设置点击toolbar返回时finish()该activity |
    | setNavigationOnClickListener() | 点击navigation的监听事件 |
    | setToolbarPadding(Toolbar toolBar) | 设置toolbar的padding |
    | setTitle(int titleId) | 设置toolbar标题 |
    | setHeaderBarTitleViewAlpha(float alpha) | 设置HeaderBar title的透明度 |

- 获取方法

    | 方法名 | 作用 | 备注 |
    | ------ | ------ | ------ |
    | int getHeaderBarHeight() | 获取HeaderBar的高度 |
    | int getStatusBarHeight() | 获取状态栏的高度 |
    | FrameLayout getHeaderBarView() | 获取HeaderBarView |
    | ToolbarMarqueeTextView getHeaderBarTitleView(Toolbar toolbar) | 获取toolbar 的titleview |

### MVP

- AbstractPresenter

- AbstractView

- BasePresenter

### Support

- update app 检测、更新APP。不需要存储权限

    在mainfest.xml文件的application中加入

    ```xml
    <meta-data
            android:name="AppName"
            android:value="bpm" />
    ```

    检测并更新代码

    ```java
    UpdateAppManager.checkUpdate(getContext(), true, new AppNeedUpdateListener() {
        @Override
        public void isNeedUpdate(boolean need) {
            Logger.d("APP是否需要更新>>>>>>>>>" + need);
        }
    });
    ```

- weather 获取天气数据
    ```java
    new WeatherObservable().getWeatherData()
    ```

### UI

- dialog

    关键类：SDRLoadingDialog、SDRSimpleDialog

- popupwindow

    关键类：PopWindowMenuHelper

- toast

   关键类：ToastSimple、ToastTop

- tree

    关键类：TreeNode、TreeNodeRecyclerAdapter

- 下载图片

    关键类：RxSaveImage

### Util

- CommonUtil

    | 方法名 | 作用 |
    | ------ | ------ |
    | getPackageInfo(Context context) | 获取packageinfo信息，常用于获取版本号、版本名称 |
    | dip2px(Context context, float dpValue) | dp转px |
    | isNetworkConnected(Context context) | 检测网络是否可用 |
    | installApk(Context context, File file) | 安装应用，适配Android 7.0+ |
    | getScreenHeight(Activity activity) | 获取屏幕高度 |
    | getScreenWidth(Activity activity) | 获取屏幕宽度 |
    | setTransformBg(float bgAlpha, Activity activity) | 设置activity阴影，适配华为。黑色透明一般取0.6f，正常设置1.0f |
    | viewImage(Context context, boolean isLocal, Object image) | 查看单张图片 |
    | viewImageList(Context context, boolean isLocal, int position, List imageList) | 查看多张图片 |

- HttpUtil

    | 方法名 | 作用 |
    | ------ | ------ |
    | clearDomainAddress(String domainAddress) | 去掉请求协议头和端口，即纯IP。如 http://192.168.0.1:8888/ 转化为 192.168.0.1|

- ToastTopUtil
- ToastUtil
- AlertUtil


### 仿网易云音乐Activity动画

#### 启动动画。

Splash页面主题
```xml
<!--Splash界面主题-->
<style name="AppTheme.SplashTheme">
    <item name="android:windowFullscreen">true</item>
    <item name="windowActionBar">false</item>
    <item name="windowNoTitle">true</item>
</style>
```
跳转下一个界面代码

```java
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.sdr_activity_zoom_in, R.anim.sdr_activity_zoom_out);
        finish();
    }
}, 1000);
```


#### 通用页面动画

在主题中添加以下即可

```xml
<item name="android:windowAnimationStyle">@style/default_animation</item>
```

