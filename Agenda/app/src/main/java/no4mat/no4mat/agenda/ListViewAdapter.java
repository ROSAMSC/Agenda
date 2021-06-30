package no4mat.no4mat.agenda;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ListViewAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] name;
    private final String[] lastName;
    private final String[] date;
    private final String[] time;

    public ListViewAdapter (Activity context, String[] name, String[] lastName, String[] date, String[] time) {
        super(context, R.layout.custom_list, name);
        this.context = context;
        this.name = name;
        this.lastName = lastName;
        this.date = date;
        this.time = time;
    }

    public View getView (int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.itemName);
        TextView lastName = (TextView) rowView.findViewById(R.id.itemLastName);
        TextView date = (TextView) rowView.findViewById(R.id.itemDate);
        TextView time = (TextView) rowView.findViewById(R.id.itemTime);


        name.setText(this.name[position]);
        lastName.setText(this.lastName[position]);
        date.setText(this.date[position]);
        time.setText(this.time[position]);

        return rowView;
    }
}
