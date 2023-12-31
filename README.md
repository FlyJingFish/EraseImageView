# EraseImageView

[![](https://jitpack.io/v/FlyJingFish/EraseImageView.svg)](https://jitpack.io/#FlyJingFish/EraseImageView)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/EraseImageView.svg)](https://github.com/FlyJingFish/EraseImageView/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/EraseImageView.svg)](https://github.com/FlyJingFish/EraseImageView/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/EraseImageView.svg)](https://github.com/FlyJingFish/EraseImageView/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/EraseImageView.svg)](https://github.com/FlyJingFish/EraseImageView/blob/master/LICENSE)


可擦除带动画的ImageView

## [点此下载apk,也可扫下边二维码下载](https://github.com/FlyJingFish/EraseImageView/blob/master/apk/release/app-release.apk?raw=true)

<img src="/screenshot/download_qrcode.png" alt="show" width="200px" />

<img src="/screenshot/SVID_20230617_223820_3.gif" alt="show" width="308px" height=""/>

## 第一步，根目录build.gradle

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
## 第二步，需要引用的build.gradle （最新版本[![](https://jitpack.io/v/FlyJingFish/EraseImageView.svg)](https://jitpack.io/#FlyJingFish/EraseImageView)）

```gradle
    dependencies {
        implementation 'com.github.FlyJingFish:EraseImageView:1.0.8'
    }
```
## 第三步，使用说明

```xml
<com.flyjingfish.searchanimviewlib.EraseImageView
        android:id="@+id/eraseView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="centerCrop"
        android:src="@drawable/ic_above"
        android:layout_above="@+id/ll_bottom"
        app:FlyJFish_erase_DrawPathType="Serpentine"
        app:FlyJFish_erase_paddingTop="10dp"
        app:FlyJFish_erase_paddingBottom="16dp"
        app:FlyJFish_erase_paddingLeft="10dp"
        app:FlyJFish_erase_paddingRight="16dp"
        app:FlyJFish_erase_paddingStart="10dp"
        app:FlyJFish_erase_paddingEnd="16dp"
        app:FlyJFish_erase_radius="50dp"
        app:FlyJFish_erase_resource="@drawable/ic_search"
        app:FlyJFish_erase_duration="6000"
        app:FlyJFish_erase_repeatCount="-1"
        app:FlyJFish_erase_repeatMode="reverse"
        app:FlyJFish_erase_autoStart="true"
        app:FlyJFish_erase_eraseMode="true"
        app:FlyJFish_erase_handMode="false"
        app:FlyJFish_erase_resource_percentBottom="0.85"
        app:FlyJFish_erase_resource_percentLeft="0.0625"
        app:FlyJFish_erase_resource_percentRight="0.85"
        app:FlyJFish_erase_resource_percentTop="0.0625"/>
```

### 属性一览

| attr                                  |  format   |   description    |
|---------------------------------------|:---------:|:----------------:|
| FlyJFish_erase_resource               | reference |      擦除的图标       |
| FlyJFish_erase_DrawPathType           |   enum    |     动画擦除路径类型     |
| FlyJFish_erase_paddingLeft            | dimension |     动画擦除时左边距     |
| FlyJFish_erase_paddingTop             | dimension |     动画擦除时上边距     |
| FlyJFish_erase_paddingRight           | dimension |     动画擦除时右边距     |
| FlyJFish_erase_paddingBottom          | dimension |     动画擦除时下边距     |
| FlyJFish_erase_paddingStart           | dimension | 动画擦除时左（rtl： 右）边距 |
| FlyJFish_erase_paddingEnd             | dimension | 动画擦除时右（rtl： 左）边距 |
| FlyJFish_erase_radius                 | dimension |     擦除块的圆半径      |
| FlyJFish_erase_resource_percentLeft   |   float   | 擦除块距离擦除图标左边的百分比  |
| FlyJFish_erase_resource_percentTop    |   float   | 擦除块距离擦除图标上边的百分比  |
| FlyJFish_erase_resource_percentRight  |   float   | 擦除块距离擦除图标右边的百分比  |
| FlyJFish_erase_resource_percentBottom |   float   | 擦除块距离擦除图标下边的百分比  |
| FlyJFish_erase_duration               |  integer  |       动画时长       |
| FlyJFish_erase_repeatCount            |  integer  |      动画重复次数      |
| FlyJFish_erase_repeatMode             |   enum    |      动画重复模式      |
| FlyJFish_erase_autoStart              |  boolean  |     动画是否自动开始     |
| FlyJFish_erase_eraseMode              |  boolean  |     是否擦除记录模式     |
| FlyJFish_erase_handMode               |  boolean  |     是否手动触摸模式     |

### 部分动画相关方法介绍，其他方法自行尝试

| attr            |   description   |
|-----------------|:---------------:|
| startEraseAnim  |      开始动画       |
| stopEraseAnim   |      结束动画       |
| resumeEraseAnim |      继续动画       |
| pauseEraseAnim  |      暂停动画       |
| isPaused        |     是否暂停动画      |
| setInterpolator |      设置插值器      |

## 最后推荐我写的另外一些库

- [OpenImage 轻松实现在应用内点击小图查看大图的动画放大效果](https://github.com/FlyJingFish/OpenImage)

- [AndroidAOP 一个注解就可请求权限，禁止多点，切换线程等等，更可定制出属于你的 Aop 代码](https://github.com/FlyJingFish/AndroidAOP)

- [主页查看更多开源库](https://github.com/FlyJingFish)


