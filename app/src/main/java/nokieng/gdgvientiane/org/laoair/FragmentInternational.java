package nokieng.gdgvientiane.org.laoair;

/**
 * Created by kieng on 3/31/2015. on project K Flight
 * package name ${PACKAGE_NAME}
 */

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nokieng.gdgvientiane.org.laoair.Helper.Utilities;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentInternational extends Fragment {

    private static final String TAG = FragmentInternational.class.getSimpleName();
//    private static final String TAG_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36";

    public static String[] LEAVE_FROM = new String[]{};

    public static String[] ADULTS = new String[]{};
    public static String[] CHILDREN = new String[]{};
    public static String[] INFANT = new String[]{};
    public static String[] GOING_TO = new String[]{};


    public static final String KEY_SUCCESS = "Success";
    public static final String KEY_LEAVE_FROM = "LeaveFrom";
    public static final String KEY_GO_TO = "GoTo";
    public static final String KEY_ROUND_TYPE = "RoundType";
    public static final String KEY_LEAVE_DATE = "LeaveDate";
    public static final String KEY_RETURN_DATE = "ReturnDate";
    public static final String KEY_CLASS_TYPE = "ClassType";
    public static final String KEY_FULL_NAME_LEAVE = "FullNameLeave";
    public static final String KEY_FULL_NAME_GO_TO = "FullNameGoTo";

//    private int expireDate = 3 * 24 * 60 * 60 * 1000;


    public static final String VALUE_GET_DESTINATION_INTER = "GetDestInter";

    private static Utilities utilities;

    public static FragmentInternational newInstance() {
        FragmentInternational fragmentInternational = new FragmentInternational();
        Bundle args = new Bundle();
//        args.putString("key", "value");
        fragmentInternational.setArguments(args);
        return fragmentInternational;
    }


    private RadioGroup rgRoundType, rgClassType;
    private RadioButton rbRoundTrip, rbOneWay, rbTypeAll, rbTypeEconomy, rbTypeBusiness;
    private Spinner spnLeaveFrom, spnGoTo, spnAdults, spnChild, spnInfant;
    private TextView txtDepartureDate, txtReturnDate;
    private Button btnSearch;
    private LinearLayout layoutDateTo, layoutDateFrom;

    private String strDesCode = "";
    //    private String strSuccess = "";
    private String strRoundType = "";
    private String strLeaveFrom = "";
    private String strGoTo = "";
    private String strDepartureDate = "";
    private String strReturnDate = "";
    private String strClassType = "";
//    private String strAdults = "";
//    private String strChild = "";
//    private String strInfants = "";


    private ArrayAdapter<String> adapterLeaveFrom;
    private ArrayAdapter<String> adapterGoTo;
    private ArrayAdapter<String> adapterAdults;
    private ArrayAdapter<String> adapterChildren;
    private ArrayAdapter<String> adapterInfant;

    private HashMap<String, String> mMapSpnGoTo = new HashMap<>();
    private ArrayList<String> mListSpnGoTo = new ArrayList<>();

    //    private Context dialogContext = null;
    private boolean isRestore = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_international, container, false);

        utilities = new Utilities(getActivity());

        //initial resource VALUES
        LEAVE_FROM = getResources().getStringArray(R.array.INTER_LEAVE_FROM);
        ADULTS = getResources().getStringArray(R.array.ADULTS);
        CHILDREN = getResources().getStringArray(R.array.CHILDREN);
        INFANT = getResources().getStringArray(R.array.INFANT);
        GOING_TO = getResources().getStringArray(R.array.GOING_TO);

        rgRoundType = (RadioGroup) rootView.findViewById(R.id.rg_inter_way);
        rgClassType = (RadioGroup) rootView.findViewById(R.id.rg_inter_trip_type);

        rbRoundTrip = (RadioButton) rootView.findViewById(R.id.rb_inter_round_trip);
        rbOneWay = (RadioButton) rootView.findViewById(R.id.rb_inter_one_way);
        rbTypeAll = (RadioButton) rootView.findViewById(R.id.rb_inter_type_all);
        rbTypeEconomy = (RadioButton) rootView.findViewById(R.id.rb_inter_type_economy);
        rbTypeBusiness = (RadioButton) rootView.findViewById(R.id.rb_inter_type_business);

        spnLeaveFrom = (Spinner) rootView.findViewById(R.id.spn_inter_leave_from);
        spnGoTo = (Spinner) rootView.findViewById(R.id.spn_inter_go_to);
        spnAdults = (Spinner) rootView.findViewById(R.id.spn_inter_adult);
        spnChild = (Spinner) rootView.findViewById(R.id.spn_inter_children);
        spnInfant = (Spinner) rootView.findViewById(R.id.spn_inter_infants);

        txtDepartureDate = (TextView) rootView.findViewById(R.id.txt_inter_date_from);
        txtReturnDate = (TextView) rootView.findViewById(R.id.txt_inter_date_to);

        layoutDateFrom = (LinearLayout) rootView.findViewById(R.id.layout_inter_date_from);
        layoutDateTo = (LinearLayout) rootView.findViewById(R.id.layout_inter_date_to);

        btnSearch = (Button) rootView.findViewById(R.id.btn_inter_search);

        //set all adpater
        adapterLeaveFrom = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.text_view1, LEAVE_FROM);
        spnLeaveFrom.setAdapter(adapterLeaveFrom);

        adapterAdults = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.text_view1, ADULTS);
        spnAdults.setAdapter(adapterAdults);

        adapterChildren = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.text_view1, CHILDREN);
        spnChild.setAdapter(adapterChildren);

        adapterInfant = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.text_view1, INFANT);
        spnInfant.setAdapter(adapterInfant);

        if (savedInstanceState == null) {
            spnLeaveFrom.setSelection(0);
            spnGoTo.setSelection(0);
        }

        restoreStateView(savedInstanceState);

        spnLeaveFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //reset goTo spn
                mMapSpnGoTo.clear();
                mListSpnGoTo.clear();

                adapterGoTo = new ArrayAdapter<>(getActivity().getApplicationContext()
                        , R.layout.text_view1, GOING_TO);
                spnGoTo.setAdapter(adapterGoTo);
                adapterGoTo.notifyDataSetChanged();
                if (savedInstanceState != null)
                    isRestore = true;
                Log.d(TAG, "isRestore: " + isRestore);

                strDesCode = utilities.getInterLeaveFrom(spnLeaveFrom);
                //INVALID . on switch . case INVALID_OPTION, i let it return INVALID
//                if (!isRestore) {
                if (spnLeaveFrom.getSelectedItemPosition() > 0) {
                    aqGetDestination(VALUE_GET_DESTINATION_INTER, strDesCode);
                }
//                }
                Log.d(TAG, "strDestinationCode :" + strDesCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        isRestore = false;


        layoutDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                txtDepartureDate.setError(null);
                showDatePickerDialog(intYears, intMonth, intDay, mSelectDateFrom);*/
                final Calendar c = Calendar.getInstance();
                int intDay = c.get(Calendar.DAY_OF_MONTH);
                int intMonth = c.get(Calendar.MONTH);
                int intYears = c.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtDepartureDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        txtReturnDate.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth + 7));
                    }
                }, intYears, intMonth, intDay);
                //get date picker instance from datePickerDialog
                DatePicker dp = dpd.getDatePicker();
                //set the DatePicker minimum date selection to current date
                dp.setMinDate(c.getTimeInMillis());
                //dp.setMinDate(System.currentTimeMillis() - 1000); alternate way to get current day
                //add One year
                c.add(Calendar.YEAR, 1);
                //set Maximum day with range one year
                dp.setMaxDate(c.getTimeInMillis());
                dpd.show();
            }
        });


        layoutDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtReturnDate.setError(null);
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtReturnDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                DatePicker dp = dpd.getDatePicker();
                dp.setMinDate(utilities.convert_yyyy_MM_dd_ToMillis(txtDepartureDate.getText().toString()));
                /// 3.156e+10 == 1 year in milliSecond
                dp.setMaxDate((long) (utilities.convert_yyyy_MM_dd_ToMillis(txtDepartureDate.getText().toString()) + 3.156e+10));
                dpd.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSearch();
            }
        });

        if (rbOneWay.isChecked()) {
            layoutDateTo.setEnabled(false);
            layoutDateTo.setVisibility(View.INVISIBLE);
        } else {
            layoutDateTo.setEnabled(true);
            layoutDateTo.setVisibility(View.VISIBLE);
        }
        rgRoundType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_inter_round_trip:
                        layoutDateTo.setVisibility(View.VISIBLE);
                        layoutDateTo.setEnabled(true);
                        break;
                    case R.id.rb_inter_one_way:
                        layoutDateTo.setVisibility(View.INVISIBLE);
                        layoutDateTo.setEnabled(false);
                        break;
                }
            }
        });

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String strDepartDate = "";
        String strReturnDate = "";
        //check those two view must not be null
        if (txtDepartureDate != null && txtReturnDate != null) {
            strDepartDate = txtDepartureDate.getText().toString();
            strReturnDate = txtReturnDate.getText().toString();
        }
//        Log.d(TAG, "SAVE : " + strDepartDate + strReturnDate);
        outState.putCharSequence("txtInterDepart", strDepartDate);
        outState.putCharSequence("txtInterReturn", strReturnDate);

        FragmentInternational fragmentInternational = newInstance();
        fragmentInternational.setArguments(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void restoreStateView(Bundle bundle) {
        String strDepart = "";
        String strReturn = "";
        if (bundle != null) {
            strDepart = String.valueOf(bundle.getString("txtInterDepart", ""));
            strReturn = String.valueOf(bundle.getString("txtInterReturn", ""));
        } /*else {
            Log.d(TAG, "restoreView Bundle null");
        }*/

//        Log.d(TAG, "txtInterDepart : " + strDepart + " txtInterReturn :" + strReturn);
        Calendar mCalendar = Calendar.getInstance();
        if (!strDepart.equals("")) {
            txtDepartureDate.setText(strDepart);
        } else {
            txtDepartureDate.setText(String.valueOf(mCalendar.get(Calendar.YEAR) + "-" + (mCalendar.get(Calendar.MONTH) + 1) + "-" + mCalendar.get(Calendar.DAY_OF_MONTH)));
        }
        if (!strReturn.equals("")) {
            txtReturnDate.setText(strReturn);
        } else {
            txtReturnDate.setText(String.valueOf(mCalendar.get(Calendar.YEAR) + "-" + (mCalendar.get(Calendar.MONTH) + 1) + "-" + (mCalendar.get(Calendar.DAY_OF_MONTH) + 7)));
        }
    }

    private void attemptSearch() {

        boolean isCancel = false;
        if (spnLeaveFrom.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "Where you leave?", Toast.LENGTH_SHORT).show();
            isCancel = true;
        } else if (spnGoTo.getSelectedItem().equals("-- Loading --")) {
            Toast.makeText(getActivity().getApplicationContext(), "Getting destination...", Toast.LENGTH_SHORT).show();
            isCancel = true;
        }/*
         do not have to check if the current date
        else if (!utilities.isCurrentDate(txtDepartureDate.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), "Select Current date", Toast.LENGTH_SHORT).show();
            isCancel = true;
        }*/ /*else if (rbRoundTrip.isChecked()) {
            if (!Utilities.isDateAfter(txtDepartureDate.getText().toString(), txtReturnDate.getText().toString())) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid return date", Toast.LENGTH_SHORT).show();
                isCancel = true;
            }
        }*/

        Log.d(TAG, "CURRENT DATE : " + utilities.isCurrentDate(txtDepartureDate.getText().toString()));
        Log.d(TAG, "CURRENT DATE : " + txtDepartureDate.getText().toString());

        Log.d(TAG, "DATE : " + txtDepartureDate.getText().toString());
        Log.d(TAG, "IS AFTER DATE " + !Utilities.isDateAfter(txtDepartureDate.getText().toString(), txtReturnDate.getText().toString()));

        if (!isCancel) {
            int position = spnGoTo.getSelectedItemPosition();
            String strItemAtPosition = mListSpnGoTo.get(position);

            Pattern p = Pattern.compile("\\(.*\\)");
            Matcher m = p.matcher(strItemAtPosition);
            if (m.find()) {
                strGoTo = String.valueOf(m.group().subSequence(1, m.group().length() - 1));
            }

            Log.d(TAG, "String GoTo :" + strGoTo + " position :" + position);

            if (txtReturnDate.getVisibility() == View.GONE) {
                strReturnDate = "";
            } else {
                strReturnDate = txtReturnDate.getText().toString();
            }
            strLeaveFrom = utilities.getInterLeaveFrom(spnLeaveFrom);
            strRoundType = getRoundType();
            strDepartureDate = txtDepartureDate.getText().toString();
            strClassType = getClassType();
/*            strAdults = String.valueOf(spnAdults.getSelectedItemPosition() + 1);
            strChild = String.valueOf(spnChild.getSelectedItemPosition());
            strInfants = String.valueOf(spnInfant.getSelectedItemPosition());*/

            HashMap<String, String> mMap = new HashMap<>();
            mMap.put(KEY_ROUND_TYPE, strRoundType);
            mMap.put(KEY_LEAVE_FROM, strLeaveFrom);
            mMap.put(KEY_GO_TO, strGoTo);
            mMap.put(KEY_LEAVE_DATE, strDepartureDate);
            mMap.put(KEY_RETURN_DATE, strReturnDate);
            mMap.put(KEY_CLASS_TYPE, strClassType);
            mMap.put(KEY_FULL_NAME_LEAVE, spnLeaveFrom.getSelectedItem().toString());
            mMap.put(KEY_FULL_NAME_GO_TO, spnGoTo.getSelectedItem().toString());

            Log.d(TAG, "Intent Values : " + mMap.toString());
            Intent intent = new Intent(getActivity(), AllFlightActivity.class);
            intent.putExtra(FragmentAllFlight.KEY_HASHMAP_SEARCH, mMap);
            startActivity(intent);
        }
    }

    private String getRoundType() {
        if (rbRoundTrip.isChecked()) {
            return "0";
        } else if (rbOneWay.isChecked()) {
            return "1";
        } else {
            return "0";
        }
    }


    private String getClassType() {
        if (rbTypeEconomy.isChecked()) {
            return "Economy (E)";
        } else if (rbTypeBusiness.isChecked()) {
            return "Business (B)";
        } else {
            return "Business/Economy (E/B)";
        }
    }


    private void aqGetDestination(String KEY_GET_DES_INTER, String strDesCode) {
        String url = "http://tk.aseanlinc.com/airline/tkairservices.php?Task=" + KEY_GET_DES_INTER + "&Code=" + strDesCode;
        AQuery aq = new AQuery(getActivity().getApplicationContext());
        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "get destination  : " + json);
                if (json != null) {
                    try {
                        if (json.get(KEY_SUCCESS).toString().trim().equals("1")) {
                            //clear before get data more
                            mMapSpnGoTo.clear();
                            mListSpnGoTo.clear();

                            JSONObject o = json.getJSONObject("0");
                            JSONObject goTo = o.getJSONObject(KEY_GO_TO);
                            for (int i = 0; i < goTo.length(); i++) {
                                try {
                                    String data = goTo.getString("k" + i);
                                    String[] params = data.split(",");
                                    mMapSpnGoTo.put(params[1], params[0] + "(" + params[1] + ")");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.d(TAG, "mMapListGoTo : " + mMapSpnGoTo.toString());
                            setSpnGoTo(mMapSpnGoTo);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Error Success 0", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        status.invalidate();
                    }
                } else {
                    status.invalidate();
                }
            }
        };
/*        cb.fileCache(true);
        cb.expire(expireDate);*/
        aq.ajax(url, JSONObject.class, cb);
    }

    private void setSpnGoTo(HashMap<String, String> items) {
        for (String item : items.values()) {
            mListSpnGoTo.add(item);
        }
        adapterGoTo = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.text_view1, mListSpnGoTo);
        spnGoTo.setAdapter(adapterGoTo);
        adapterGoTo.notifyDataSetChanged();
    }
}