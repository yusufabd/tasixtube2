package uz.androidclub.tas_ixtube;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import uz.androidclub.tas_ixtube.utils.FontsOverride;

/**
 * Created by yusufabd on 3/14/2017.
 */

public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/comfortaa.ttf");
    }
}
