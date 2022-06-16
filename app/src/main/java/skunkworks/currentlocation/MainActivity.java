/**
 * Copyright 2018 Ricoh Company, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package skunkworks.currentlocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;

import com.theta360.pluginlibrary.activity.PluginActivity;
import com.theta360.pluginlibrary.callback.KeyCallback;
import com.theta360.pluginlibrary.receiver.KeyReceiver;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MainActivity extends PluginActivity implements OfflineWarningDaialog.OfflineWarningDaialogListener {
    private static final String TAG = "MainActivity";

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialogEnable = false;
        Log.d(TAG, "onDialogNegativeClick(): Dialog Disabled.");
    }

    MapView mMapView;
    MyLocationNewOverlay mLocationOverlay;
    IMapController mapController;

    //Coordinates at the first startup (Tokyo Sky Tree)
    private static final double INITIAL_LAT = 35.710058;
    private static final double INITIAL_LNG = 139.810718;
    //Zoom magnification at first startup
    private static final double INITIAL_ZOOM = 15.0;

    private double savedLat;
    private double savedLng;
    private double savedZoom;
    private boolean dialogEnable;

    private boolean gnssEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意　setContentView　より前に置く
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        Log.d(TAG, "MaxZoom=" + mMapView.getMaxZoomLevel() + ", MinZoom" + mMapView.getMinZoomLevel() );
        //地図の著作権表示
        mMapView.getOverlays().add(new CopyrightOverlay(this));

        mapController =  mMapView.getController();

        gnssEnable = checkGnss();

        // Set enable to close by pluginlibrary, If you set false, please call close() after finishing your end processing.
        setAutoClose(true);
        // Set a callback when a button operation event is acquired.
        setKeyCallback(new KeyCallback() {
            @Override
            public void onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyReceiver.KEYCODE_CAMERA) {

                    //実験用ログ：この時点で表示していた地図の中心座標とズームレベル
                    IGeoPoint curGeoPoint = mMapView.getMapCenter();
                    double curLat = curGeoPoint.getLatitude();
                    double curLng = curGeoPoint.getLongitude();
                    Log.d(TAG, "Cur Geo Point : Lat=" + curLat + ", Lng=" + curLng );
                    double curZoom = mMapView.getZoomLevelDouble();
                    Log.d(TAG, "Cur Zoom Level = " + curZoom);

                    //測位の有効/無効により振る舞いを変える
                    if (gnssEnable) {
                        //現在地に戻して再フォロー
                        mapController.animateTo(mLocationOverlay.getMyLocation());
                        mLocationOverlay.enableFollowLocation();
                    } else {
                        //測位を有効にするよう促すダイアログを表示
                        skunkworks.currentlocation.PositionInfoAddOffDaialog dialog = new skunkworks.currentlocation.PositionInfoAddOffDaialog();
                        dialog.show( getSupportFragmentManager(), "position_info_add_off_dialog" );
                    }

                }
            }

            @Override
            public void onKeyUp(int keyCode, KeyEvent event) {
                //NOP
            }

            @Override
            public void onKeyLongPress(int keyCode, KeyEvent event) {
                //NOP
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView!=null) {
            mMapView.onResume();
        }

        //THETA X needs to open WebAPI camera before camera.takePicture
        notificationWebApiCameraOpen();

        //前回起動時の座標とズームレベルを取得し、表示位置とズームレベルを初期化する。
        restorePluginInfo();
        InitialLocationAndZoom(savedLat, savedLng, savedZoom);

        //オフライン、かつ、Daialg表示有効 であった場合、注意を促す。
        if ( (!checkOnline()) && (dialogEnable) ) {
            OfflineWarningDaialog dialog = new OfflineWarningDaialog();
            dialog.show( getSupportFragmentManager(), "offline_warning_dialog" );
        }

        if (isApConnected()) {

        }
    }

    @Override
    protected void onPause() {
        // Do end processing
        //close();

        //終了時点の座標とズームレベルを保存する
        IGeoPoint curGeoPoint = mMapView.getMapCenter();
        savedLat = curGeoPoint.getLatitude();
        savedLng = curGeoPoint.getLongitude();
        savedZoom = mMapView.getZoomLevelDouble();
        savePluginInfo();

        //THETA X needs to close WebAPI camera before finishing plugin
        notificationWebApiCameraClose();

        super.onPause();
        if (mMapView!=null) {
            mMapView.onPause();
        }
    }



    void InitialLocationAndZoom(double inLat, double inLng, double inZoom) {
        //測位前の初期位置を設定する
        mapController.setZoom( inZoom );
        GeoPoint centerPoint = new GeoPoint( inLat, inLng );
        mapController.setCenter(centerPoint);

        //測位できたら、マークをオーバレイしてフォローする動作を有効にする
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),mMapView);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        mMapView.getOverlays().add(mLocationOverlay);
    }

    boolean checkOnline(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d(TAG, "activeNetwork : isConnected=" + String.valueOf(isConnected));

        return isConnected;
    }

    boolean checkGnss(){
        boolean result;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "location manager Enabled");
            result = true;
        } else {
            Log.d(TAG, "not gnssEnable");
            result = false;
        }
        return result;
    }

    //==============================================================
    // 設定保存・復帰
    //==============================================================
    private static final String SAVE_KEY_LAST_LAT  = "lastLat";
    private static final String SAVE_KEY_LAST_LNG = "lastLng";
    private static final String SAVE_KEY_LAST_ZOOM = "lastZoom";
    private static final String SAVE_KEY_DIALOG_ENABLE = "dialogEnable";
    SharedPreferences sharedPreferences;

    void restorePluginInfo() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        savedLat = sharedPreferences.getFloat(SAVE_KEY_LAST_LAT, (float)INITIAL_LAT);
        savedLng = sharedPreferences.getFloat(SAVE_KEY_LAST_LNG, (float)INITIAL_LNG);
        savedZoom = sharedPreferences.getFloat(SAVE_KEY_LAST_ZOOM, (float)INITIAL_ZOOM);
        dialogEnable = sharedPreferences.getBoolean(SAVE_KEY_DIALOG_ENABLE, true);
    }

    void savePluginInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(SAVE_KEY_LAST_LAT, (float) savedLat);
        editor.putFloat(SAVE_KEY_LAST_LNG, (float) savedLng);
        editor.putFloat(SAVE_KEY_LAST_ZOOM, (float) savedZoom);
        editor.putBoolean(SAVE_KEY_DIALOG_ENABLE, dialogEnable);
        editor.commit();
    }
}
