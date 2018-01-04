package creatio.com.entrayecto;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private String location, location_branch;
    private Bundle extras;
    private List<Polyline> polylines;
    private TextView txtTime,txtDistance;
    private static final int[] COLORS = new int[]{R.color.green, R.color.green, R.color.greenDark, R.color.colorAccent, R.color.primary_dark_material_light};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        polylines = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        extras = getIntent().getExtras();
        location = extras.getString("location", "0,0");
        location_branch = extras.getString("location_branch", "0,0");
        txtTime = findViewById(R.id.txtTime);
        txtDistance = findViewById(R.id.txtDistance);
        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView txtTitleBar = findViewById(R.id.txtTitleBar);
        txtTitleBar.setText("Trayecto orden " + extras.getString("order", ""));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        String[] l = location.split(",");
        String[] ll = location_branch.split(",");
        LatLng l1 = new LatLng(Double.parseDouble(l[0]), Double.parseDouble(l[1]));
        LatLng l2 = new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1]));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(l1);
        builder.include(l2);
        LatLngBounds bounds = builder.build();
        mMap.addMarker(new MarkerOptions().position(l1).title("Tu ubicación").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_user)));
        mMap.addMarker(new MarkerOptions().position(l2).title("Ubicación de la sucursal").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
        int padding = 64; // offset from edges of the map in pixels
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);

            }
        });
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(l1, l2)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Log.e("Rout", e.toString());
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int n) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.com_facebook_blue));
            polyOptions.width(25);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            n = i;

            txtDistance.setText("Distancia aproximada: " + route.get(i).getDistanceText());
            txtTime.setText("Duración aproximada: " + route.get(i).getDurationText());


            //Toast.makeText(Map.this, "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceText() + ": duration - " + route.get(i).getDurationText(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }
}
