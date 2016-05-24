
package com.lordcard.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.lordcard.common.anim.PlayCardAnima;
import com.lordcard.common.exception.CrashApplication;
import com.lordcard.common.net.DownloadUtils;
import com.lordcard.constant.Database;

public class ImageUtil {

    // /**PhotoDialog图片缓存*/
    // public static ConcurrentHashMap<Integer, WeakReference<Bitmap>>
    // pdBitmapCache;
    // /**EnvalueDialog图片缓存*/
    // public static ConcurrentHashMap<Integer, WeakReference<Bitmap>>
    // edBitmapCache;
    // /**BagDialog图片缓存*/
    // public static Map<Integer, WeakReference<Bitmap>> bdBitmapCache;
    private static ConcurrentHashMap<String, WeakReference<Drawable>> drawableWeakMap = new ConcurrentHashMap<String, WeakReference<Drawable>>();
    // private static ConcurrentHashMap<String, WeakReference<Drawable>>
    // pokerMap = new ConcurrentHashMap<String, WeakReference<Drawable>>();
    // private static ConcurrentHashMap<String, SoftReference<Drawable>>
    // drawableWeakMap = new ConcurrentHashMap<String,
    // SoftReference<Drawable>>();
    public static ConcurrentHashMap<String, WeakReference<Bitmap>> bitMapCacheMap = new ConcurrentHashMap<String, WeakReference<Bitmap>>();
    // private static ConcurrentHashMap<String, SoftReference<Bitmap>>
    // bitMapCacheMap = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
    // private static ConcurrentHashMap<String, SoftReference<GifView>>
    // gigViewMap = new ConcurrentHashMap<String, SoftReference<GifView>>();
    public static ConcurrentHashMap<String, WeakReference<GifView>> gigViewMap = new ConcurrentHashMap<String, WeakReference<GifView>>();
    public static ConcurrentHashMap<String, WeakReference<Bitmap>> girlbitMapCacheMap = new ConcurrentHashMap<String, WeakReference<Bitmap>>();
    public static ConcurrentHashMap<String, WeakReference<Bitmap>> headMapCacheMap = new ConcurrentHashMap<String, WeakReference<Bitmap>>();
    public final static String cachePath = Environment.getExternalStorageDirectory().getPath()
            + "/com.qianqian360.game/cache/";
    public final static String chatPath = Environment.getExternalStorageDirectory().getPath()
            + "/com.qianqian360.game/cache/chat/";

    // public final static String cachePath = "/mnt/sdcard/game/cache/";
    // public final static String chatPath = "/mnt/sdcard/game/cache/chat/";
    public interface ImageCallback {

        public void imageLoaded(Bitmap bitmap, ImageView view);
    }

    public interface ImageGroupCallback {

        public void imageLoaded(Bitmap bitmap, ViewGroup viewGoup);
    }

    /**
     * @param drawableId
     * @param isCache 是否做缓存
     * @param isDistortion 是否失真
     * @return
     */
    public static Drawable getDrawFromProject(int drawableId, boolean isCache, boolean isDistortion) {
        Context ctx = CrashApplication.getInstance();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // opt.inSampleSize = 2;
        if (isDistortion) { // 失真
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
        } else {
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = getBitmapByKey(String.valueOf(drawableId));
        if (bitmap == null) {
            try
            {
                if (ctx == null || ctx.getResources() == null
                        || ctx.getResources().openRawResource(drawableId) == null)
                {
                    Log.w("", "");
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            bitmap = BitmapFactory.decodeStream(ctx.getResources().openRawResource(drawableId),
                    null, opt);
            bitmap = addBitMap2Cache(String.valueOf(drawableId), bitmap);
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(ctx.getResources(), bitmap);
        if (isCache) {
            WeakReference<Drawable> softDrawable = new WeakReference<Drawable>(bitmapDrawable);
            bitmapDrawable = null;
            bitmap = null;
            drawableWeakMap.put(String.valueOf(drawableId), softDrawable);
            return softDrawable.get();
        } else {
            bitmap = null;
            return bitmapDrawable;
        }
    }

    /**
     * 获取Drawable图片资源
     * 
     * @param drawableId
     * @param isCache 是否做缓存
     * @param isDistortion 是否失真
     * @return
     */
    public static Drawable getDrawableResId(int drawableId, boolean isCache, boolean isDistortion) {
        String key = String.valueOf(drawableId);
        Drawable drawable = getDrawableByKey(key);
        if (drawable != null)
            return drawable;
        return getDrawFromProject(drawableId, isCache, isDistortion);
    }

    /**
     * 获取缓存的tDrawable值
     * 
     * @Title: getDrawableByKey
     * @param @param key
     * @param @return
     * @return Drawable
     * @throws
     */
    public static Drawable getDrawableByKey(String key) {
        // 在内存缓存中，则返回Bitmap对象
        if (drawableWeakMap.containsKey(key)) {
            WeakReference<Drawable> drawableRef = drawableWeakMap.get(key);
            if (drawableRef != null && drawableRef.get() != null) {
                Drawable drawable = drawableRef.get();
                if (drawable != null) {
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    Bitmap bm = bd.getBitmap();
                    if (bm != null && !bm.isRecycled()) {
                        return drawableRef.get();
                    }
                }
            }
            drawableWeakMap.remove(key);
        }
        return null;
    }

    /**
     * 增加图片到缓存
     * 
     * @Title: addDrawable2Cache
     * @param @param drawableId
     * @param @param drawable
     * @return void
     * @throws
     */
    public static Drawable addDrawable2Cache(String key, Drawable drawable) {
        WeakReference<Drawable> softDrawable = new WeakReference<Drawable>(drawable);
        drawable = null;
        drawableWeakMap.put(key, softDrawable);
        return softDrawable.get();
    }

    public static void clearBitMapCache() {
        if (null != bitMapCacheMap && bitMapCacheMap.size() > 0) {
            for (String bitMapKey : bitMapCacheMap.keySet()) {
                WeakReference<Bitmap> bitmapRef = bitMapCacheMap.get(bitMapKey);
                Bitmap bitmap = bitmapRef.get();
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                bitmap = null;
                bitmapRef = null;
                bitMapCacheMap.remove(bitMapKey);
            }
            bitMapCacheMap.clear();
        }
    }

    /**
     * 回收指定的bitMap
     */
    public static void clearsingleCache(String path) {
        if (null != girlbitMapCacheMap && girlbitMapCacheMap.size() > 0
                && girlbitMapCacheMap.contains(path)) {
            WeakReference<Bitmap> bitmapRef = girlbitMapCacheMap.remove(path);
            ;
            if (null != bitmapRef) {
                Bitmap bitmap = bitmapRef.get();
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                bitmap = null;
                bitmapRef = null;
            }
        }
    }

    /**
     * 清空美女图鉴图片缓存
     */
    public static void clearGirlBitMapCache() {
        if (null != girlbitMapCacheMap && girlbitMapCacheMap.size() > 0) {
            for (String bitMapKey : girlbitMapCacheMap.keySet()) {
                WeakReference<Bitmap> bitmapRef = girlbitMapCacheMap.get(bitMapKey);
                Bitmap bitmap = bitmapRef.get();
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                bitmap = null;
                bitmapRef = null;
                girlbitMapCacheMap.remove(bitMapKey);
            }
            girlbitMapCacheMap.clear();
        }
    }

    /**
     * 获取本地图片（返回null 则表明不存在）
     * 
     * @param path
     * @return
     */
    public static Drawable ImageHasLocal(String path) {
        // 判断缓存是否有图片
        Bitmap bitmap = getBitmapByKey(path);
        if (null != bitmap) {
            return new BitmapDrawable(bitmap);
        }
        try {
            String picName = convertUrlToFileName(path);
            if (!TextUtils.isEmpty(picName)) {
                // res目录是否有图片
                bitmap = getBitmapFromResources(picName.substring(0, picName.lastIndexOf(".")));
            }
        } catch (Exception e) {
            bitmap = null;
        }
        if (bitmap != null) {
            return new BitmapDrawable(bitmap);
        }
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            // SD卡是否有图片
            bitmap = getImageFromSdCard(path);
        } else {
            // 内存卡是否有图片
            bitmap = getImageFromData(path);
        }
        if (bitmap != null) {
            return new BitmapDrawable(bitmap);
        }
        return null;
    }

    /**
     * 清楚游戏头像图片缓存
     */
    public static void clearHeadMapCache() {
        if (null != headMapCacheMap && headMapCacheMap.size() > 0) {
            for (String bitMapKey : headMapCacheMap.keySet()) {
                WeakReference<Bitmap> bitmapRef = headMapCacheMap.get(bitMapKey);
                Bitmap bitmap = bitmapRef.get();
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                bitmap = null;
                bitmapRef = null;
                headMapCacheMap.remove(bitMapKey);
            }
            girlbitMapCacheMap.clear();
        }
    }

    /**
     * 从缓存获取图片
     * 
     * @param key
     * @return
     */
    public static Bitmap getBitmapByKey(String key) {
        if (bitMapCacheMap.containsKey(key)) {
            WeakReference<Bitmap> WeakReference = bitMapCacheMap.get(key);
            Bitmap tempBitmap = WeakReference.get();
            if (null != tempBitmap && !tempBitmap.isRecycled()) {
                return tempBitmap;
            }
            bitMapCacheMap.remove(key);
        }
        return null;
    }

    public static Bitmap getGirlBitmapByKey(String key) {
        if (girlbitMapCacheMap.containsKey(key)) {
            WeakReference<Bitmap> WeakReference = girlbitMapCacheMap.get(key);
            Bitmap tempBitmap = WeakReference.get();
            if (tempBitmap != null && !tempBitmap.isRecycled()) {
                return tempBitmap;
            }
            girlbitMapCacheMap.remove(key);
        }
        return null;
    }

    /**
     * 获取控件Select图片
     * 
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path, boolean fromNetWork) {
        Bitmap bitmap = getBitmapByKey(path);
        if (null != bitmap) {
            return bitmap;
        }
        try {
            String picName = convertUrlToFileName(path);
            if (!TextUtils.isEmpty(picName)) {
                bitmap = getBitmapFromResources(picName.substring(0, picName.lastIndexOf(".")));
            }
        } catch (Exception e) {
            bitmap = null;
        }
        if (null != bitmap) {
            return bitmap;
        }
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        Bitmap bitmapCache = null;
        if (sdCardExist) {
            bitmapCache = getImageFromSdCard(path);
        } else {
            bitmapCache = getImageFromData(path);
        }
        if (bitmapCache != null) {
            bitmap = bitmapCache;
        }
        if (null != bitmap) {
            WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(bitmap);
            bitMapCacheMap.put(path, weakReference);
            bitmap = null;
            return weakReference.get();
        }
        if (fromNetWork) {
            return getBitMapFromNetWork(path);
        }
        return null;
    }

    /**
     * 增加BitMap 到缓存
     * 
     * @Title: addBitMap2Cache
     * @param @param bitMapId
     * @param @param bitmap
     * @return void
     * @throws
     */
    public static Bitmap addBitMap2Cache(String bitMapKey, Bitmap bitmap) {
        WeakReference<Bitmap> softBitmap = new WeakReference<Bitmap>(bitmap);
        bitmap = null;
        bitMapCacheMap.put(bitMapKey, softBitmap);
        return softBitmap.get();
    }

    /**
     * 从网络上获取图片
     * 
     * @Title: getBitMapFromNetWork
     * @param @param path
     * @param @return
     * @return Bitmap
     * @throws
     */
    public static Bitmap getBitMapFromNetWork(String path) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        byte[] data = null;
        try {
            data = DownloadUtils.getImage(path);
            if (data != null && data.length != 0) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                // 判断sd卡是否存在
                if (sdCardExist) {
                    saveImageToSdCard(path, tempBitmap);
                } else {
                    saveImageToData(path, tempBitmap);
                }
                WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(tempBitmap);
                bitMapCacheMap.put(path, weakReference);
                tempBitmap = null;
                return weakReference.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据图片ID获取缓存的图片资源
     * 
     * @param drawableId
     * @param isDistortion 图片是否设置失真true:是 false:否
     * @return
     */
    public static Drawable getResDrawable(int drawableId, boolean isDistortion) {
        return getDrawableResId(drawableId, true, isDistortion);
    }

    // public static Drawable getPokerResDrawable(int drawableId, boolean
    // isDistortion) {
    // String key = String.valueOf(drawableId);
    // Drawable drawable = getDrawableByKey(key);
    // if (drawable != null)
    // return drawable;
    //
    // return getDrawFromProject(drawableId, true, isDistortion,true);
    // }
    /**
     * 根据图片名称获取缓存的图片资源
     * 
     * @param name
     * @param isCache
     * @param isDistortion 图片是否设置失真
     * @return
     */
    public static Drawable getResDrawableByName(String name, boolean isCache, boolean isDistortion) {
        Context context = CrashApplication.getInstance();
        int drawableId = context.getResources().getIdentifier(name, "drawable",
                context.getPackageName());
        if (drawableId == 0) {
            return null;
        }
        return getDrawableResId(drawableId, isCache, isDistortion);
    }

    /**
     * 根据图片名称从资源中获取Bitmap
     * 
     * @param name
     * @return
     */
    public static Bitmap getBitmapFromResources(String name) {
        Context context = CrashApplication.getInstance();
        int drawableId = context.getResources().getIdentifier(name, "drawable",
                context.getPackageName());
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, drawableId);
    }

    /**
     * 设置大奖赛奖品图片
     * 
     * @param filename
     * @param imageview
     * @param imageCallback
     */
    public static void setImg(final String path, final ImageView imageview,
            final ImageCallback imageCallback) {
        Bitmap bitmap = getBitmap(path, false);
        final Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageCallback.imageLoaded((Bitmap) msg.obj, imageview);
            }
        };
        if (null != bitmap && !bitmap.isRecycled()) {
            Message message = handler.obtainMessage(0, bitmap);
            handler.sendMessage(message);
        } else {
            new Thread() {

                public void run() {
                    Bitmap bp = getBitMapFromNetWork(path);
                    Message message = handler.obtainMessage(0, bp);
                    handler.sendMessage(message);
                }
            }.start();
        }
    }

    /**
     * 设置图片
     * 
     * @param path
     * @param imageview
     * @param imageCallback
     */
    public static void setImgs(final String path, final ViewGroup imageview,
            final ImageGroupCallback ImageGroupCallback) {
        Bitmap bitmap = getBitmap(path, false);
        final Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ImageGroupCallback.imageLoaded((Bitmap) msg.obj, imageview);
            }
        };
        if (null != bitmap && !bitmap.isRecycled()) {
            Message message = handler.obtainMessage(0, bitmap);
            handler.sendMessage(message);
        } else {
            new Thread() {

                public void run() {
                    Bitmap bp = getBitMapFromNetWork(path);
                    Message message = handler.obtainMessage(0, bp);
                    handler.sendMessage(message);
                }
            }.start();
        }
    }

    public static void replaceImg(final String path, final ImageView imageview,
            final ImageCallback imageCallback) {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageCallback.imageLoaded((Bitmap) msg.obj, imageview);
            }
        };
        new Thread() {

            public void run() {
                byte[] data = null;
                try {
                    data = DownloadUtils.getImage(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (data != null && data.length != 0) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    Bitmap tempBitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length, options);
                    boolean sdCardExist = Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
                    if (sdCardExist) {
                        saveImageToSdCard(path, tempBitmap);
                    } else {
                        saveImageToData(path, tempBitmap);
                    }
                    WeakReference<Bitmap> reference = new WeakReference<Bitmap>(tempBitmap);
                    bitMapCacheMap.put(path, reference);
                    tempBitmap = null;
                    Message message = handler.obtainMessage(0, reference.get());
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 获取Gif动画图片
     * 
     * @param name
     * @return
     */
    public static GifView getGifDrawable(String name) {
        Context context = CrashApplication.getInstance();
        int drawableId = context.getResources().getIdentifier(name, "drawable",
                context.getPackageName());
        if (drawableId == 0) {
            return null;
        }
        return getGifResDrawable(drawableId);
    }

    public static void clearGifCache() {
        if (null != gigViewMap && gigViewMap.size() > 2) {
            for (String key : gigViewMap.keySet()) {
                WeakReference<GifView> gifviewRef = gigViewMap.get(key);
                if (null != gifviewRef) {
                    GifView gifView = gifviewRef.get();
                    gifView.destroyDrawingCache();
                    gifView.destroy();
                    gifviewRef.clear();
                    gifviewRef = null;
                    gigViewMap.remove(key);
                }
            }
            gigViewMap.clear();
        }
    }

    /**
     * 获取Gif动画图片
     * 
     * @param context
     * @param drawableId
     * @return
     */
    public static GifView getGifResDrawable(int drawableId) {
        String key = String.valueOf(drawableId);
        // 在内存缓存中，则返回Bitmap对象
        if (gigViewMap.containsKey(key)) {
            WeakReference<GifView> drawableCache = gigViewMap.get(key);
            if (drawableCache != null && drawableCache.get() != null) {
                return drawableCache.get();
            }
            gigViewMap.remove(key);
        }
        Context ctx = CrashApplication.getInstance();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        GifView gifView = new GifView(ctx);
        InputStream is = ctx.getResources().openRawResource(drawableId);
        gifView.setGifImage(is);
        // gifView.setGifImage(drawableId);
        gifView.setGifImageType(GifImageType.SYNC_DECODER);
        WeakReference<GifView> weakGif = new WeakReference<GifView>(gifView);
        // gifView.destroy();
        gifView = null;
        is = null;
        gigViewMap.put(key, weakGif);
        return weakGif.get();
    }

    public static AnimationDrawable createStart() {
        AnimationDrawable animationDrawable = null;
        animationDrawable = new AnimationDrawable();
        for (int i = 0; i < 4; i++) {
            Drawable drawable = ImageUtil.getResDrawableByName("star" + i,
                    true, true);
            animationDrawable.addFrame(drawable, 300);

        }
        for (int i = 2; i >= 0; i--) {
            Drawable drawable = ImageUtil.getResDrawableByName("star" + i,
                    true, true);
            animationDrawable.addFrame(drawable, 300);
        }
        animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
        return animationDrawable;
    }

    /**
     * 获取Animation动画
     * 
     * @param name
     * @return
     */
    public static AnimationDrawable getResAnimaSoft(String name) {
        AnimationDrawable anima = null;
        if (name.equals("wait")) {
            anima = PlayCardAnima.createAnimDrawAble();
        } else if (name.equals("bomb")) {
            anima = PlayCardAnima.createBomb();
        } else if (name.equals("wanBomb")) {
            anima = PlayCardAnima.createWangBomb();
        } else if (name.equals("baoxiang_start_")) {
            anima = PlayCardAnima.createBaoXiang();
        } else if (name.equals("lot")) {
            anima = PlayCardAnima.createLotMain();
        } else if (name.equals("shunz")) {
            anima = PlayCardAnima.createShunz();
        } else if (name.equals("feiji")) {
            anima = PlayCardAnima.createFeiji();
        } else if (name.equals("point")) {
            anima = PlayCardAnima.createPoint();
        } else if (name.equals("new")) {
            anima = PlayCardAnima.createImageNew();
        } else if (name.equals("news")) {
            anima = PlayCardAnima.createImageNews();
        } else if (name.equals("home_receive_been_button")) {
            anima = PlayCardAnima.createImageLingZhiDou();
        }
        return anima;
    }

    /**
     * 释放Bitmap的内存
     */
    public static void releaseDrawable(Drawable drawable) {
        if (drawable == null)
            return;
        if (drawable.getClass() == BitmapDrawable.class) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmapDrawable.setCallback(null);
            bitmapDrawable = null;
        }
        drawable.setCallback(null);
        drawable = null;
    }

    /**
     * 释放动画的图片
     * 
     * @Title: releaseAnimDrawable
     * @param @param animationDrawable
     * @return void
     * @throws
     */
    public static void releaseAnimDrawable(AnimationDrawable animationDrawable) {
        if (animationDrawable != null) {
            animationDrawable.stop();
            int frameNum = animationDrawable.getNumberOfFrames();
            for (int i = 0; i < frameNum; i++) {
                try {
                    releaseDrawable(animationDrawable.getFrame(i));
                } catch (Exception e) {
                }
            }
            animationDrawable = null;
        }
    }

    /**
     * 图片保存到内存中
     * 
     * @param cachePath
     * @param imageURL
     * @param bitmap
     */
    @SuppressLint("WorldReadableFiles")
    public static void saveImageToData(String imageURL, Bitmap bitmap) {
        try {
            FileOutputStream outStream = Database.currentActivity.openFileOutput(
                    convertUrlToFileName(imageURL), Context.MODE_WORLD_READABLE);
            Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.PNG;
            bitmap.compress(localCompressFormat, 100, outStream);
            outStream.close();
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 获取美女图鉴或头像Select图片
     * 
     * @param path
     * @param fromNetWork
     * @param isgrils true:美女图鉴，false:头像
     * @return
     */
    public static Bitmap getGirlBitmap(String path, boolean fromNetWork, boolean isgrils) {
        Bitmap bitmap = getGirlBitmapByKey(path);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            String picName = convertUrlToFileName(path);
            if (!TextUtils.isEmpty(picName)) {
                bitmap = getBitmapFromResources(picName.substring(0, picName.lastIndexOf(".")));
            }
        } catch (Exception e) {
            bitmap = null;
        }
        Bitmap bitmapCache = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            bitmapCache = getImageFromSdCard(path);
        } else {
            bitmapCache = getImageFromData(path);
        }
        if (bitmapCache != null) {
            bitmap = bitmapCache;
        }
        if (null != bitmap) {
            WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(bitmap);
            if (isgrils) {
                girlbitMapCacheMap.put(path, weakReference);
            } else {
                headMapCacheMap.put(path, weakReference);
            }
            bitmap = null;
            return weakReference.get();
        }
        if (fromNetWork) {
            return getBitMapFromNetWork(path);
        }
        return null;
    }

    /**
     * 内存中取图片
     * 
     * @param cachePath
     * @param imageURL
     * @param bitmap
     */
    public static Bitmap getImageFromData(String imageURL) {
        try {
            String localIconNormal = convertUrlToFileName(imageURL);
            FileInputStream localStream = Database.currentActivity.openFileInput(localIconNormal);
            Bitmap bitmap = BitmapFactory.decodeStream(localStream);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 图片保存到SdCard中
     * 
     * @param cachePath
     * @param imageURL
     * @param bitmap
     */
    public static void saveImageToSdCard(String imageURL, Bitmap bitmap) {
        File dir = new File(cachePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File bitmapFile = new File(cachePath + convertUrlToFileName(imageURL));
        if (!bitmapFile.exists()) {
            try {
                bitmapFile.createNewFile();
            } catch (IOException e) {
            }
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
        }
    }

    /**
     * 从SdCard中取图片
     * 
     * @param cachePath
     * @param imageURL
     * @return
     */
    public static Bitmap getImageFromSdCard(String imageURL) {
        String bitmapName = cachePath + convertUrlToFileName(imageURL);
        Bitmap bitmap = null;
        if (new File(bitmapName).exists()) {
            bitmap = BitmapFactory.decodeFile(bitmapName);
        }
        return bitmap;
    }

    /**
     * 从SdCard中判断有无图片
     * 
     * @param cachePath
     * @param imageURL
     * @return
     */
    public static boolean ImageFromSdCardExist(String imageURL) {
        String bitmapName = cachePath + convertUrlToFileName(imageURL);
        if (new File(bitmapName).exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取图片名包括后缀
     * 
     * @param url
     * @return
     */
    public static String convertUrlToFileName(String url) {
        if (!url.endsWith("/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return "";
    }

    /**
     * 下载图片到SD卡
     * 
     * @param path
     * @return
     */
    public static void downImg(final String path) {
        new Thread() {

            @Override
            public void run() {
                byte[] data = null;
                try {
                    data = DownloadUtils.getImage(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (data != null && data.length != 0) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    Bitmap tempBitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length, options);
                    boolean sdCardExist = Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
                    if (sdCardExist) {
                        saveImageToSdCard(path, tempBitmap);
                    }
                    bitMapCacheMap.put(path, new WeakReference<Bitmap>(tempBitmap));
                }
            }
        }.start();
    }

    /**
     * 剪切图片，根据屏幕分辨率
     * 
     * @param newWidth 宽度
     * @param newHeight 高度
     * @return
     */
    public static BitmapDrawable getcutBitmap(String path, boolean fromNetWork) {
        Bitmap beforeimg = getGirlBitmap(path, fromNetWork, true);
        if (null != beforeimg) {
            int width = beforeimg.getWidth();
            int height = beforeimg.getHeight();
            int cutwidth = 0;
            int cutheight = 0;
            if ((Database.SCREEN_WIDTH * height) < (width * Database.SCREEN_HEIGHT)) {
                cutwidth = (int) ((Database.SCREEN_WIDTH * height) / Database.SCREEN_HEIGHT);
                cutheight = height;
            } else {
                cutwidth = width;
                cutheight = (int) ((Database.SCREEN_HEIGHT * width) / Database.SCREEN_WIDTH);
            }
            Bitmap newbmp = Bitmap.createBitmap(beforeimg, 0, 0, cutwidth - 1, cutheight - 1);
            if (newbmp != beforeimg) {
                beforeimg.recycle();
                beforeimg = null;
            }
            // newbmp = resizeBitmap(newbmp, newbmp.getWidth(),
            // newbmp.getHeight());
            BitmapDrawable bd = new BitmapDrawable(newbmp);
            return bd;
        }
        return null;
    }

    /**
     * 缩放图片
     * 
     * @param newWidth 宽度
     * @param newHeight 高度
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, float newWidth, float newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = (newWidth) / width;
        float scaleHeight = (newHeight) / height;
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    /**
     * 清空imageWeakMap
     */
    public static void clearImageWeakMap() {
        if (null != drawableWeakMap && drawableWeakMap.size() > 0) {
            for (String key : drawableWeakMap.keySet()) {
                WeakReference<Drawable> reference = drawableWeakMap.get(key);
                if (reference != null && reference.get() != null) {
                    releaseDrawable(reference.get());
                }
                reference.clear();
                reference = null;
                drawableWeakMap.remove(key);
            }
            drawableWeakMap.clear();
        }
    }

    // /**
    // * 清空扑克缓存
    // */
    // public static void clearPokerMap() {
    // if (null != pokerMap && pokerMap.size() > 0) {
    // for (String key : pokerMap.keySet()) {
    // WeakReference<Drawable> reference = pokerMap.get(key);
    // if (reference != null && reference.get() != null) {
    // releaseDrawable(reference.get());
    // }
    // reference.clear();
    // reference = null;
    // pokerMap.remove(key);
    // }
    // pokerMap.clear();
    // }
    // }
    /**
     * 获取美女说话的文字
     * 
     * @param grilNmae
     * @return
     */
    public static String getGirlSayText(String grilNmae) {
        String say = "";
        if ("girl1_left".equals(grilNmae)) {
            say = "这牌打得\n真衰啊";
        } else if ("girl2_left".equals(grilNmae)) {
            say = "知道厉害了吧！";
        } else if ("girl3_left".equals(grilNmae)) {
            say = "看你能怎么办";
        } else if ("girl4_left".equals(grilNmae)) {
            say = "让人无语啊";
        } else if ("girl5_left".equals(grilNmae)) {
            say = "嗯..快出牌嘛..";
        } else if ("girl6_left".equals(grilNmae)) {
            say = "真是讨厌啊！";
        } else if ("girl7_left".equals(grilNmae)) {
            say = "欺负人啊！";
        } else if ("girl8_left".equals(grilNmae)) {
            say = "打不起了吧？";
        }
        return say;
    }

    /**
     * 比赛类型
     * 
     * @param num
     * @return
     */
    public static String getGameType(int num) {
        String result = "";
        switch (num) {
            case 1:
                result = "预赛";
                break;
            case 2:
                result = "半决赛";
                break;
            case 3:
                result = "决赛";
                break;
        }
        return result;
    }

    /**
     * Drawable转Bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap Drawable2Bitmap(Drawable drawable) {
        try {
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
