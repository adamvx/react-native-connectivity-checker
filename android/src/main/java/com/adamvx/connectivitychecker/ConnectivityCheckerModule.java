package com.adamvx.connectivitychecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

@ReactModule(name = ConnectivityCheckerModule.NAME)
public class ConnectivityCheckerModule extends ReactContextBaseJavaModule {
  public static final String NAME = "ConnectivityChecker";
  private final static String TOPIC = "RNConnectivityChecker";
  private final static String EVENT_TYPE = "eventType";
  private final static String EVENT_STATUS = "status";

  private int listenerCount = 0;

  private final IntentFilter locationFilter = new IntentFilter();

  private final BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      final boolean locationEnabled = intent.getAction() != null
        && intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)
        && isLocationEnabledSync();

      final WritableMap eventMap = new WritableNativeMap();
      eventMap.putString(EVENT_TYPE, EventType.LOCATION.getName());
      eventMap.putBoolean(EVENT_STATUS, locationEnabled);
      getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TOPIC, eventMap);
    }
  };

  ConnectivityCheckerModule(ReactApplicationContext context) {
    super(context);
    locationFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  public boolean isLocationEnabledSync() {
    boolean locationEnabled;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      LocationManager mLocationManager = (LocationManager) getReactApplicationContext().getSystemService(Context.LOCATION_SERVICE);
      try {
        boolean gpsProvider = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkProvider = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        locationEnabled = mLocationManager.isLocationEnabled() || gpsProvider || networkProvider;
      } catch (Exception e) {
        System.err.println("Unable to determine if location enabled. LocationManager was null");
        return false;
      }
    } else {
      int locationMode = Settings.Secure.getInt(getReactApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
      locationEnabled = locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    return locationEnabled;
  }

  @ReactMethod
  public void addListener(String eventName) {
    if (listenerCount == 0) {
      getReactApplicationContext().registerReceiver(mLocationReceiver, locationFilter);
    }
    listenerCount += 1;
  }

  @ReactMethod
  public void removeListeners(Integer count) {
    listenerCount -= count;
    if (listenerCount == 0) {
      getReactApplicationContext().unregisterReceiver(mLocationReceiver);
    }
  }

  @ReactMethod
  public void isLocationEnabled(Promise promise) {
    promise.resolve(isLocationEnabledSync());
  }

  private enum EventType {
    LOCATION("location");

    private final String name;

    EventType(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
