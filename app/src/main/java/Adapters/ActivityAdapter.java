package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Objects.ActivityResponse;
import Objects.EventResponse;
import app.num.barcodescannerproject.R;

/**
 * Created by Administrador on 30/07/2016.
 */
public class ActivityAdapter extends ArrayAdapter<ActivityResponse> {
        private Activity activity;
        private ArrayList<ActivityResponse> lActivities;
        private static LayoutInflater inflater = null;

        public ActivityAdapter(Activity activity, int textViewResourceId, ArrayList<ActivityResponse> _lActivities) {
            super(activity, textViewResourceId, _lActivities);
            try {
                this.activity = activity;
                this.lActivities = _lActivities;

                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            } catch (Exception e) {

            }
        }

        public int getCount() {
            return lActivities.size();
        }

        public ActivityResponse getItem(ActivityResponse position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public static class ViewHolder {
            public TextView display_name;
            public TextView display_number;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    vi = inflater.inflate(R.layout.item_view_activity, null);
                    holder = new ViewHolder();

                    holder.display_name = (TextView) vi.findViewById(R.id.activity_name_tv);
                    holder.display_number = (TextView) vi.findViewById(R.id.activity_place_tv);


                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }



                holder.display_name.setText(lActivities.get(position).getName());
                holder.display_number.setText(lActivities.get(position).getStarts_at());


            } catch (Exception e) {


            }
            return vi;
        }
}

