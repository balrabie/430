package com.example.gauge;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import de.nitri.gauge.Gauge;


// Some of what is missing...
//      -Reading the data from the database: database not complete yet
//      (will read from firebase online database)
//      -Search functionality


// Notice
// activity_mainpage.xml doesnt contain any GaugeBox and InfoBox initially
// Functions were defined to dynamically insert those views (flexibility purposes)
// The GaugeBox, InfoBox views are defined in their own xml files
// GaugeBox view contains 2 children Views: Gauge + textView
// InfoBox view contains 2 children Views: 2 textViews


// Assumption: Activity_mainpage.xml is a Relative Layout embedded inside a Scroll View
// Dont change the layout types else the code will fail

public class MainPage extends AppCompatActivity {
    private final int WIDTH = 300;
    private final int HEIGHT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        String title = "Sifour";

        // Illustration of how to use the functions
        // id0 is the id of the textView (which's defined in the xml)
        TextView textView_title = findViewById(R.id.textView_title);
        textView_title.setText(title);
        int id0 = textView_title.getId();

        // Insert a gauge box
        ArrayList<Integer> id1 = AppendGaugeBox(id0);

        // Insert an infobox about temperature (inferred by the unit which is Celsius)
        // note: id1.get(0) is the id of the previous view.
        ArrayList<Integer> id2 = AppendInfoBox(52.3, new String[]{"Title1","Location1","Date1"},
                id1.get(0), true);

        // Insert an infobox about humidity
        ArrayList<Integer> id3 = AppendInfoBox(32.13, new String[]{"Title2","Location2","Date2"},
                id2.get(0), false);

        // sample updates
        UpdateGaugeValue(80, id1);


        UpdateInfoBoxValue(42.1, id2);
        UpdateInfoBoxValue(43.2, id3);

        UpdateInfoBoxDate("..new date1", id2);
        UpdateInfoBoxDate("..new date2", id3);

    }


    // InfoBox contain information about humidity or temperature
    // InfoBox is a view that contain 2 children textViews
    // Inputs:
    // Value refers to the measured temperature or humidity
    // Details is a length-3 string array: MeasuredType-Location-DateUpdated
    // Example of Details would be {"Humidity","Bedroom","2:30pm,12 April, 2012"}
    // PreviousViewId is the id of the view that lies above our inserted view in our layout design.
    // ValueUnitIsDegreesCelcius refers to whether we want to display percentage or degrees celcius
    // The returned id list refers to the view, textView1, textView2 respectively
    public ArrayList<Integer>
    AppendInfoBox(Double Value, String[] Details,
                  int PreviousViewId, Boolean ValueUnitIsDegreesCelcius) {
        RelativeLayout parent = findViewById(R.id.RelativeLayout_multi);

        // Get the temperature box view from infotextbox.xml.xml View view = getLayoutInflater().inflate(R.layout.infotextbox, parent, false);
        View view = getLayoutInflater().inflate(R.layout.infotextbox, parent, false);

        //LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = vi.inflate(R.layout.infotextbox, null);

        view.setId(View.generateViewId());
        String value_str;
        if (ValueUnitIsDegreesCelcius) {
            view.setBackground(getDrawable(R.drawable.batchraindots));
            value_str = Double.toString(Value) + " \u2109";
        }
        else
        {
            view.setBackground(getDrawable(R.drawable.skybatch2));
            value_str = Double.toString(Value) + " %";
        }

        // create params for view (customizations)
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        if (PreviousViewId != -1) {
            params.addRule(RelativeLayout.BELOW, PreviousViewId);
            params.bottomMargin = 40;
        }


        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        textView1.setId(View.generateViewId());
        textView2.setId(View.generateViewId());



        textView1.setText(value_str);
        textView2.setText(SpecialFormatText(Details));

        textView1.setTypeface(null, Typeface.BOLD);
        textView1.setTextSize(50);
        textView1.setGravity(Gravity.CENTER); // text is in the middle

        RelativeLayout.LayoutParams params_textView1 =
                new RelativeLayout.LayoutParams(WIDTH, HEIGHT);
        RelativeLayout.LayoutParams params_textView2 =
                new RelativeLayout.LayoutParams(WIDTH, RelativeLayout.LayoutParams.WRAP_CONTENT);

        params_textView2.addRule(RelativeLayout.BELOW, textView1.getId());

        textView1.setLayoutParams(params_textView1);
        textView2.setLayoutParams(params_textView2);
        parent.addView(view, 0, params); // finally.. add the view to the parent

        //return view.getId();
        ArrayList<Integer> Ids = new ArrayList<>();
        Ids.add(view.getId());
        Ids.add(textView1.getId());
        Ids.add(textView2.getId());
        return Ids;
    }

    // GaugeBox shows information about the fuel level
    // GaugeBox is a view that contains 2 children views: gauge and textView
    // Input:
    // PreviousViewId is the id of the view that lies above our inserted view in our layout design.
    // The returned id list refers to the view, gauge, textView respectively
    public ArrayList<Integer> AppendGaugeBox(int PreviousViewId) {
        RelativeLayout parent = findViewById(R.id.RelativeLayout_multi);

        // Get the InfoBox view from activity_one_gauge.xml
        View view = getLayoutInflater().inflate(R.layout.activity_one_gauage, null, false);
        view.setId(View.generateViewId());

        // Gauge and TextView are the children views
        Gauge gauge = view.findViewById(R.id.gauge);
        TextView textView = view.findViewById(R.id.textView);

        gauge.setId(View.generateViewId());
        textView.setId(View.generateViewId());

        // create params for view (customizations)
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        if (PreviousViewId != -1) {
            params.addRule(RelativeLayout.BELOW, PreviousViewId);
            params.bottomMargin = 40;
        }

        textView.setText("0 %");
        textView.setGravity(Gravity.CENTER);


        RelativeLayout.LayoutParams params_textView =
                new RelativeLayout.LayoutParams(WIDTH, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params_gauge =
                new RelativeLayout.LayoutParams(WIDTH, HEIGHT);

        params_textView.addRule(RelativeLayout.BELOW, gauge.getId());

        gauge.setLayoutParams(params_gauge);
        textView.setLayoutParams(params_textView);

        textView.setLayoutParams(params_textView);

        //experiment
        //Object gauge_tag = Integer.toString(view.getId())+"gauge";
        //Object textView_tag = Integer.toString(view.getId())+"textView";
        //gauge.setTag(gauge_tag);
        //textView.setTag(textView_tag);

        parent.addView(view, 0, params); // finally.. add the view to the parent

        ArrayList<Integer> Ids = new ArrayList<>();
        Ids.add(view.getId());
        Ids.add(gauge.getId());
        Ids.add(textView.getId());

        return Ids;
    }

    // Sets the value of the needs of the gauge box
    // Inputs:
    // Value is the new value to set the gauge needle to
    // Ids refers to the id of the parent view, and the id's of 2 children: gauge and textView
    public void UpdateGaugeValue(double value, ArrayList<Integer> ids)
    {
        if (value>=0 && value<=100) {
            int id_gauge = ids.get(1);
            int id_textView = ids.get(2);

            Gauge gauge = findViewById(id_gauge);
            TextView textView = findViewById(id_textView);
            gauge.setValue((float)value);
            textView.setText(String.format("%.2f", value) + " %");
        }
    }

    // Update the value of InfoBox
    // Input:
    // Value refers to the measured temperature or humidity
    // Ids refers to the id of the parent view, and the id's of 2 children textViews
    public void UpdateInfoBoxValue(double value, ArrayList<Integer> ids) {
        TextView textView = findViewById(ids.get(1));

        String UNIT = " " + ((String) textView.getText()).split(" ")[1]; // **

        textView.setText(Double.toString(value) + UNIT);



    }

    // Update the last updated date of the InfoBox
    // Input:
    // Date refers to the last updated date of the measured value
    // Ids refers to the id of the parent view, and the id's of 2 children textViews
    public void UpdateInfoBoxDate(String date, ArrayList<Integer> ids) {
        TextView textView = findViewById(ids.get(2));

        String temp = textView.getText().toString();

        String[] details = temp.split("\n");

        String[] updated_details = new String[]{details[0],details[1],date};
        textView.setText(SpecialFormatText(updated_details));

    }


    // formats strings specifically for temperature and humidity text boxes
    // Details is a length-3 string array: MeasuredType-Location-DateUpdated
    // The output is a 3 lines text, each line with different formatting (color,style,size)
    // The formatted output is intended to be shown by a textView
    private Spanned SpecialFormatText(String[] details) {
        Spanned result;
        if (details.length == 3) {
            result = Html
                    .fromHtml("<font color=\"#000080\">\t<b>" + details[0] + "</b></font><br />"
                            + "\t<b>" + details[1] + "</b><br />"
                            + "\t<small><i>" + details[2] + "</i></small><br />");
        } else {
            result = Html.fromHtml("wrong format");
        }

        return result;
    }
}
