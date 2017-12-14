package com.quick.core.util.gis;

/**
 *
 * GPS坐标转换基础函数集合
 * SHANG01
 * http://blog.csdn.net/xiliangxiaoke/article/details/48436117
 * GPS坐标转换基础函数集合
 * 地球坐标系（WGS84）
 * 火星坐标系（GCJ02）
 * 百度地图的坐标系是在火星坐标系的基础上又自行加密的BD09坐标系
 * 高德地图的坐标系是常见的火星坐标系GCJ02
 * 使用android.location获取的经纬度,或者是通过手持或车载的GPS终端获取的经纬度，属于WGS84的真实的地理坐标
 */
public class CoordMath {

    private final static double PI = 3.14159265358979324;
    private final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * WGS84转GCJ02
     *
     * @param point WGS84
     * @return
     */
    public static MyLatLngPoint wgs2gcj(MyLatLngPoint point) {
        MyLatLngPoint newpt;
        if (isOutOfChina(point)) {
            return point;
        }
        MyLatLngPoint d = delta(point);
        newpt = new MyLatLngPoint(point.getLat() + d.getLat(), point.getLng() + d.getLng());


        return newpt;
    }


    /**
     * 国测局转WGS84
     *
     * @param point
     * @return
     */
    public static MyLatLngPoint gcj2wgs(MyLatLngPoint point) {
        double gcjLat = point.getLat();
        double gcjLon = point.getLng();
        double initDelta = 0.01;
        double threshold = 0.000000001;
        double dLat = initDelta;
        double dLon = initDelta;
        double mLat = gcjLat - dLat;
        double mLon = gcjLon - dLon;
        double pLat = gcjLat + dLat;
        double pLon = gcjLon + dLon;
        double wgsLat;
        double wgsLon;
        int i = 0;
        while (true) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            MyLatLngPoint tmp = gcj2wgs(new MyLatLngPoint(wgsLat, wgsLon));//gcj_encrypt
            dLat = tmp.getLat() - gcjLat;
            dLon = tmp.getLng() - gcjLon;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
                break;

            if (dLat > 0) pLat = wgsLat;
            else mLat = wgsLat;
            if (dLon > 0) pLon = wgsLon;
            else mLon = wgsLon;

            if (++i > 10000) break;
        }
        //console.log(i);
        return new MyLatLngPoint(wgsLat, wgsLon);
    }

    /**
     * 国测局转百度
     *
     * @param point
     * @return
     */
    public static MyLatLngPoint gcj2bd(MyLatLngPoint point) {
        double gcjLon = point.getLng();
        double gcjLat = point.getLat();
        double x = gcjLon;
        double y = gcjLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new MyLatLngPoint(bdLat, bdLon);
    }

    /**
     * 百度转国测局
     *
     * @param point
     * @return
     */
    public static MyLatLngPoint bd2gcj(MyLatLngPoint point) {
        double bdLon = point.getLng();
        double bdLat = point.getLat();
        double x = bdLon - 0.0065;
        double y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gcjLon = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);
        return new MyLatLngPoint(gcjLat, gcjLon);//{'lat' : gcjLat, 'lon' : gcjLon};
    }


    /**
     * wgs转百度
     *
     * @param point
     * @return
     */
    public static MyLatLngPoint wgs2bd(MyLatLngPoint point) {
//wgs--gcj
        MyLatLngPoint gcjpt = wgs2gcj(point);
//gcj--bd
        return gcj2bd(gcjpt);
    }


    /**
     * 百度转wgs
     *
     * @param point
     * @return
     */
    public static MyLatLngPoint bd2wgs(MyLatLngPoint point) {
//bd---gcj
        MyLatLngPoint gcjpt = bd2gcj(point);
//gcj--wgs
        return gcj2wgs(gcjpt);
    }

    private static boolean isOutOfChina(MyLatLngPoint point) {
        if (point.getLng() < 72.004 || point.getLng() > 137.8347)
            return true;
        if (point.getLat() < 0.8293 || point.getLat() > 55.8271)
            return true;
        return false;
    }

    private static MyLatLngPoint delta(MyLatLngPoint point) {
        MyLatLngPoint d;
        double a = 6378245.0;//  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        double dlat;
        double dlng;
        double radlat;
        double magic;
        double sqrtmagic;

        dlat = transformLat(point.getLng() - 105.0, point.getLat() - 35.0);
        dlng = transformLon(point.getLng() - 105.0, point.getLat() - 35.0);
        radlat = point.getLat() / 180.0 * PI;
        magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);

        d = new MyLatLngPoint(dlat, dlng);

        return d;
    }


    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

}