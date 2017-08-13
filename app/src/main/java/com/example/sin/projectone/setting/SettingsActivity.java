package com.example.sin.projectone.setting;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.HttpUtilsAsync;
import com.example.sin.projectone.ImgManager;
import com.example.sin.projectone.PaymentMethodManager;
import com.example.sin.projectone.R;
import com.example.sin.projectone.UserManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.width;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    private static final String KEY_PREF_SYNC_CONN = "sync_frequency" ;
    private ImgManager imgManager;
    private PaymentMethodManager paymentMM;
    private UserManager userManager;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Setting");
        setupActionBar();
        userManager = new UserManager(this);
        paymentMM = new PaymentMethodManager(this);
        imgManager = ImgManager.getInstance();
        uploadSetting(userManager);
        setPrefFromExistFile("kbank_qrcode.png","kbank_qrcode_status");
        setPrefFromExistFile("ktb_qrcode.png","ktb_qrcode_status");

    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //DO WHAT YOU WANT WHEN YOU HIT UP BUTTON
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        uploadSetting(userManager);
        // put your code here...

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);

    }
    public void setPrefFromExistFile(String fname, String preference){
        System.out.println(preference+"--------------------------");
//            Preference pref_status = findPreference(preference);
        if(imgManager.checkImageName(fname)){
            Log.d("setpref", "setPrefFromExistFile: true");
            System.out.println(preference+"--------------------------");
//                preference.setTitle("NOOOOOOOO");
            paymentMM.setValue(preference,"true","boolean");

        }
        else{

        }
    }

    public void uploadSetting(UserManager userManager){
        String shopID = userManager.getShopId();
        Map<String, ?> allEntries = paymentMM.getAllValue();
        System.out.println(allEntries);
        RequestParams params = new RequestParams();

        for (final Map.Entry<String, ?> entry : allEntries.entrySet()) {
            params.put("shopID",shopID);
            params.put("name", entry.getKey());
            params.put("value",entry.getValue().toString());
            HttpUtilsAsync.post(Constant.URL_SEND_SETTING, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    System.out.println("map values "+ entry.getKey() + ": " + entry.getValue().toString());
                    System.out.println(entry.getKey() + " Success");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    System.out.println(entry.getKey() + " Failed");

                }
            });
            System.out.println("map values "+ entry.getKey() + ": " + entry.getValue().toString());
//            if(entry.getValue().toString().equals("true")||entry.getValue().toString().equals("false")){
//                paymentMM.setValue(entry.getKey(),entry.getValue().toString(),"boolean");
//            }
//            paymentMM.setValue(entry.getKey(),entry.getValue().toString(),"string");
        }
    }
    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || PaymentMethodPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PaymentMethodPreferenceFragment extends PreferenceFragment {
        private PaymentMethodManager paymentMM;
        private ImgManager imgManager;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            paymentMM = new PaymentMethodManager(getActivity());
            imgManager = ImgManager.getInstance();
            addPreferencesFromResource(R.xml.pref_payment_method);
            setHasOptionsMenu(true);
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            String syncConnPref = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_CONN, "");
            Preference ckboxPref = findPreference("paypal_link");
            System.out.println("==========");
            System.out.println(ckboxPref.getKey());
            ckboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference,
                                                  Object newValue) {
                    try {
                        if(imgManager.checkImageName("paypal_qrcode.png")){
                            if(imgManager.deleteImage("paypal_qrcode.png")){
                                System.out.println("paypal_qrcode.png deteled");

                            }

                        }
                        System.out.println(PreferenceManager
                                .getDefaultSharedPreferences(preference.getContext())
                                .getString(preference.getKey(), "")+"****/*/*/*/**");
                        imgManager.saveImgToInternalStorage(encodeAsBitmap(PreferenceManager
                                .getDefaultSharedPreferences(preference.getContext())
                                .getString(preference.getKey(), "")),"paypal_qrcode.png");


                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    //Do stuff
                    return true;
                }
            });


//            bindPreferenceSummaryToValue(findPreference("paypal_link"));
//            bindPreferenceSummaryToValue(findPreference("paypal_link"));
            setEnablePaymentMethod("ktb_qrcode_status", this.getActivity());
            setEnablePaymentMethod("kbank_qrcode_status", this.getActivity());
//            bindOnClickToPreference("kbank_qrcode_help", this.getActivity());
//            bindOnClickToPreference("ktb_qrcode_help", this.getActivity());

            Log.d("sync_frequency", "onCreate: "+ syncConnPref);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


        Bitmap encodeAsBitmap(String str) throws WriterException {
            int WIDTH=500;
            BitMatrix result;
            WindowManager manager = (WindowManager) this.getActivity().getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            try {
                result = new MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, width, width, null);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
            return bitmap;
        }
        public void setEnablePaymentMethod(final String prefName, final Context context){
            System.out.println("++++-------------------------++++");
            String tempPref = prefName.substring(0,prefName.indexOf('_'))+"_status";
            Preference pref = findPreference(tempPref);
            System.out.println(tempPref);
//            Preference pref = findPreference(prefName);
            System.out.print(prefName);
            System.out.println(findPreference(prefName));
            System.out.println(pref+"-------------------------++++");
            Log.d("check Preferance", "bindOnClickToPreference: "+ pref.toString());
            if(Boolean.valueOf(paymentMM.getValue(prefName,"boolean"))){
                System.out.println(tempPref+"trueeee");
                pref.setEnabled(true);
            }
            else{
                Toast.makeText(context, "Need to set QR code first."+tempPref, Toast.LENGTH_SHORT).show();
            }
            System.out.println("++++-------------------------++++");
        }
        public void bindOnClickToPreference(final String prefName, final Context context){
            Preference pref = findPreference(prefName);
            System.out.print(prefName);
            System.out.println(findPreference(prefName));
            System.out.println(pref+"-------------------------++++");
            Log.d("check Preferance", "bindOnClickToPreference: "+ pref.toString());
            if(Boolean.valueOf(paymentMM.getValue("prefName","boolean"))){
                pref.setEnabled(true);
            }
            else{
                Toast.makeText(context, "Need to set QR code first."+prefName, Toast.LENGTH_SHORT).show();
            }

            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {





                    return false;
                }
            });
        }


    }
    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
