package demos.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.victor.androiddemos.R;

import demos.util.bind.Bind;
import demos.util.bind.BindView;


public class GetLocationActivity extends BaseActivity {
    @BindView(R.id.tv_get_location)
    TextView tvGetLocation;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            updateToNewLocation(null);
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateToNewLocation(null);
        }

        @Override
        public void onLocationChanged(Location location) {
            updateToNewLocation(location);
        }
    };
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        Bind.bind(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("定位");

        String provider = LocationManager.GPS_PROVIDER;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

//        location = locationManager.getLastKnownLocation(provider);


        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度
        criteria.setAltitudeRequired(false);//不要求海拔信息
        criteria.setBearingRequired(false);//不要求方位信息
        criteria.setCostAllowed(true);//是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));


        locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);
        updateToNewLocation(location);
    }

    private void updateToNewLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            tvGetLocation.setText("纬度:" + latitude + "\n经度:" + longitude);
        } else {
            tvGetLocation.setText("未开启定位服务");
        }
    }
}
