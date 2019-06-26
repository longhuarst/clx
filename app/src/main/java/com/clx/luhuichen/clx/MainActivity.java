package com.clx.luhuichen.clx;

import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.clx.luhuichen.clx.network.NettyClient;

public class MainActivity extends AppCompatActivity {

    MapView mMapView = null;//地图元素

    AMap aMap;//地图控制对象

    AMapLocationClient mLocationClient;


    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;



    NettyClient client_;




    //消息处理s
    public static Handler mhandler;

    public Handler getHandler(){
        return mhandler;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。



        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
//        //以下三种模式从5.1.0版本开始提供
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。


        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。


        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。




        startLocaion();


        LatLng latLng = new LatLng(39.906901,116.397972);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));




        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                Toast.makeText(MainActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();

            }
        };


        client_ = new NettyClient("172.20.10.7",19000);

        client_.start();//开启后台的连接线程






    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();

    }


    @Override
    protected void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();


    }


    @Override
    protected void onPause() {
        super.onPause();


        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();


    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);


        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);

    }









    public void startLocaion(){

        Log.i(TAG,"startLocaion-----");//获取当前定位结果来源，如网络定位结果，详见定位类型表


        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }



    String TAG = "clx";


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation !=null ) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    Log.i(TAG,"当前定位结果来源-----"+amapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i(TAG,"纬度 ----------------"+amapLocation.getLatitude());//获取纬度
                    Log.i(TAG,"经度-----------------"+amapLocation.getLongitude());//获取经度
                    Log.i(TAG,"精度信息-------------"+amapLocation.getAccuracy());//获取精度信息
                    Log.i(TAG,"地址-----------------"+amapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i(TAG,"国家信息-------------"+amapLocation.getCountry());//国家信息
                    Log.i(TAG,"省信息---------------"+amapLocation.getProvince());//省信息
                    Log.i(TAG,"城市信息-------------"+amapLocation.getCity());//城市信息
                    Log.i(TAG,"城区信息-------------"+amapLocation.getDistrict());//城区信息
                    Log.i(TAG,"街道信息-------------"+amapLocation.getStreet());//街道信息
                    Log.i(TAG,"街道门牌号信息-------"+amapLocation.getStreetNum());//街道门牌号信息
                    Log.i(TAG,"城市编码-------------"+amapLocation.getCityCode());//城市编码
                    Log.i(TAG,"地区编码-------------"+amapLocation.getAdCode());//地区编码
                    Log.i(TAG,"当前定位点的信息-----"+amapLocation.getAoiName());//获取当前定位点的AOI信息
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };








}
