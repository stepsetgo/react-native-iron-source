package co.squaretwo.ironsource;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;


public class RNIronSourceModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String TAG = "RNIronSource";

    private ReactApplicationContext reactContext;

    public RNIronSourceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void initializeIronSource(final String appId, final String userId, final ReadableMap options, final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final Activity activity = reactContext.getCurrentActivity();
                final boolean validateIntegration = options.getBoolean("validateIntegration");
                if(activity != null) {
                    IronSource.setUserId(userId);
                    IronSource.init(activity, appId);
                    if (validateIntegration) {
                        IntegrationHelper.validateIntegration(activity);
                    }
                }

                promise.resolve(null);
            }
        });
    }

    @ReactMethod
    public void setConsent(boolean consent) {
        IronSource.setConsent(consent);
    }

    @ReactMethod
    public void getAdvertiserId(Promise promise) {
      try {
        promise.resolve(IronSource.getAdvertiserId(this.reactContext));
      }
      catch (Exception e) {
        promise.resolve(null);
      }
    }

    @Override
    public void onHostResume() {
      final Activity activity = reactContext.getCurrentActivity();
      IronSource.onResume(activity);
    }

    @Override
    public void onHostPause() {
      final Activity activity = reactContext.getCurrentActivity();
      IronSource.onPause(activity);
    }

    @Override
    public void onHostDestroy() {
    }
}
