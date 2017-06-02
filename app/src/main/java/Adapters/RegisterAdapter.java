package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Objects.ActivityResponse;
import Objects.RegisterResponse;
import app.num.barcodescannerproject.R;

/**
 * Created by Administrador on 30/07/2016.
 */
public class RegisterAdapter extends ArrayAdapter<RegisterResponse> implements SectionIndexer{
        private Activity activity;
        private List<RegisterResponse> lActivities;
        HashMap<String, Integer> mapIndex;
        String[] sections;

    private static LayoutInflater inflater = null;

        public RegisterAdapter(Activity activity, int textViewResourceId, List<RegisterResponse> _lActivities) {
            super(activity, textViewResourceId, _lActivities);
            try {
                this.activity = activity;
                this.lActivities = _lActivities;

                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            } catch (Exception e) {

            }
            mapIndex = new LinkedHashMap<String, Integer>();

            for (int x = lActivities.size()-1; x >= 0; x--) {
                RegisterResponse fruit = lActivities.get(x);
                String ch = fruit.getFirst_name().substring(0, 1);
                ch = ch.toUpperCase(Locale.US);

                // HashMap will prevent duplicates
                mapIndex.put(ch, x);
            }

            Set<String> sectionLetters = mapIndex.keySet();

            // create a list from the set to sort
            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

            Collections.sort(sectionList);
            sections = new String[sectionList.size()];

            sectionList.toArray(sections);
        }

        public int getCount() {
            return lActivities.size();
        }

        public RegisterResponse getItem(RegisterResponse position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mapIndex.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public static class ViewHolder {
            public TextView display_name;
            public TextView display_number;
            public ImageView display_img;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;

            final ViewHolder holder;
            try {
                if (convertView == null) {
                    vi = inflater.inflate(R.layout.item_view_register, null);
                    holder = new ViewHolder();
                    holder.display_name = (TextView) vi.findViewById(R.id.nameRegister_tv);
                    holder.display_img = (ImageView) vi.findViewById(R.id.Register_ImgV);
                    vi.setTag(holder);

                } else {
                    holder = (ViewHolder) vi.getTag();
                }


                if(lActivities.get(position).getHave_attendance().equals("true")){
                    holder.display_img.setImageResource(R.drawable.ic_done_black);
                }else{
                    holder.display_img.setImageResource(R.drawable.ic_cruz_black);
                }

                holder.display_name.setText(lActivities.get(position).getFirst_name()+' '+lActivities.get(position).getLast_name());


            } catch (Exception e) {


            }
            return vi;
        }
}

