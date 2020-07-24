package com.trikwetra.alasr.mainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.trikwetra.alasr.R;
import com.trikwetra.alasr.adapter.PrayerAdapter;
import com.trikwetra.alasr.bean.PrayerBean;
import java.util.ArrayList;

public class Fragment_Schedule extends Fragment {
    private RecyclerView rvPrayerList;
    private ArrayList<PrayerBean> prayerList;
    private PrayerAdapter prayerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        prayerList = new ArrayList<>();
        prayerList.add(new PrayerBean("Fazar","03.15","-04.15","03.15 - 03.40"));
        prayerList.add(new PrayerBean("Zahaar","13.09","-08.15","13.09 - 13.35"));
        prayerList.add(new PrayerBean("Asr","17.05","-12.15","17.05 - 17.50"));
        prayerList.add(new PrayerBean("Magrib","19.15","-14.35","19.15 - 19.40"));
        prayerList.add(new PrayerBean("Esha","21.15","-19.35","21.15 - 21.45"));
        prayerList.add(new PrayerBean("Qiyam","03.15","-24.40","03.15 - 03.40"));

        rvPrayerList = view.findViewById(R.id.rv_prayer_list);
        rvPrayerList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvPrayerList.setLayoutManager(layoutManager);
        rvPrayerList.setNestedScrollingEnabled(false);

        prayerAdapter = new PrayerAdapter(prayerList, getContext());
        rvPrayerList.setAdapter(prayerAdapter);
        return view;
    }
}
