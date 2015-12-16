package ua.sinoptik.sinoptik;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Created by Админ on 14.12.2015.
 */
public class TitlesFragment extends Fragment {
    final String LOG_TAG = "myLogs";
    public interface onItemClickListener {
        public void itemClick(int position, int groupPosition);
    }

    ExpandableListView listView;
    onItemClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listener = (onItemClickListener) getActivity();
        View v = inflater.inflate(R.layout.titles, container, false);

        listView = (ExpandableListView) v.findViewById(R.id.listView);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                listener.itemClick(childPosition,groupPosition);
                return false;
            }
        });
        return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "1 " + getActivity());


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Context context = activity;


    }


}
