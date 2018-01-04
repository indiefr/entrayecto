package creatio.com.entrayecto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.*;

public class FRInit extends Fragment {
    private FragmentPagerItemAdapter adapter;
    private SharedPreferences prefs;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_init, container, false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        FragmentPagerItems viewPagers = FragmentPagerItems.with(getActivity()).add("Menú", FRMenu.class).create();
        adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), viewPagers);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        java.util.Map<String, ?> allEntries = prefs.getAll();
        for (java.util.Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            if (entry.getKey().contains("name")) {
                if (!entry.getValue().toString().contains("Notificaciones")) {
                    viewPagers.add(FragmentPagerItem.of(entry.getValue().toString(), FRLists.class));
                    adapter.notifyDataSetChanged();
                }
            }
        }
        viewPagers.add(FragmentPagerItem.of("Sucursales", FRContact.class));
        adapter.notifyDataSetChanged();
        SmartTabLayout viewPagerTab = view.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        return view;
    }
}
