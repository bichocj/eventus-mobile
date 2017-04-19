package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Objects.EventResponse;
import app.num.barcodescannerproject.R;

/**
 * Created by Administrador on 30/07/2016.
 */
public class EventAdapter extends ArrayAdapter<EventResponse> {
        private Activity activity;
        private ArrayList<EventResponse> lEvents;
        private static LayoutInflater inflater = null;

        public EventAdapter (Activity activity, int textViewResourceId,ArrayList<EventResponse> _lEvents) {
            super(activity, textViewResourceId, _lEvents);
            try {
                this.activity = activity;
                this.lEvents = _lEvents;

                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            } catch (Exception e) {

            }
        }

        public int getCount() {
            return lEvents.size();
        }

        public EventResponse getItem(EventResponse position) {
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
                    vi = inflater.inflate(R.layout.item_view_event, null);
                    holder = new ViewHolder();

                    holder.display_name = (TextView) vi.findViewById(R.id.event_name_tv);
                    holder.display_number = (TextView) vi.findViewById(R.id.event_date_tv);


                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }



                holder.display_name.setText(lEvents.get(position).getName());
                holder.display_number.setText(lEvents.get(position).getSlug());


            } catch (Exception e) {


            }
            return vi;
        }
}

