package uz.androidclub.tas_ixtube.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;

import uz.androidclub.tas_ixtube.R;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PathView pathView = (PathView)findViewById(R.id.pathView);
        pathView.getPathAnimator().
                delay(100).
                duration(1500).
                interpolator(new AccelerateDecelerateInterpolator()).
                start();
        Handler handler = new Handler();
        pathView.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkInternet();
            }
        }, 3000);
    }

    private void checkInternet() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
