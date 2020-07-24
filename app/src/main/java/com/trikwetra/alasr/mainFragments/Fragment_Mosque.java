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
import com.trikwetra.alasr.adapter.MosqueAdapter;
import com.trikwetra.alasr.bean.MosqueBean;
import java.util.ArrayList;

public class Fragment_Mosque extends Fragment {
    private RecyclerView rvMosqueList;
    private ArrayList<MosqueBean> mosqueList;
    private MosqueAdapter mosqueAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mosque, container, false);

        mosqueList = new ArrayList<>();
        mosqueList.add(new MosqueBean("Jama Masjid","Hyderabad"));
        mosqueList.add(new MosqueBean("Mecca Masjid","Bhopal"));
        mosqueList.add(new MosqueBean("Houghton Masjid West Street","Lucknow"));
        mosqueList.add(new MosqueBean("Jamali Kamali","Agra"));
        mosqueList.add(new MosqueBean("Adhai Din Ka Jhonpra","Delhi"));
        mosqueList.add(new MosqueBean("Jama Mosque","Ajmer"));

        rvMosqueList = view.findViewById(R.id.rv_mosque_list);
        rvMosqueList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvMosqueList.setLayoutManager(layoutManager);
        rvMosqueList.setNestedScrollingEnabled(false);

        mosqueAdapter = new MosqueAdapter(mosqueList, getContext());
        rvMosqueList.setAdapter(mosqueAdapter);
        
        return view;
    }
}
