package job.com.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import job.com.news.article.City;
import job.com.news.article.City_Name;
import job.com.news.article.State;
import job.com.news.article.State_Name;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.LocaleHelper;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.WebService;
import job.com.news.sharedpref.SessionManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateArticle extends AppCompatActivity implements View.OnClickListener {
    private Spinner mStateSpinner, mCitySpinner, mArticleSpinner;
    private EditText mTitleEdit, mDescEdit;
    private TextView article_state_text, article_city_text, article_type_text, article_date_text, title_text, desc_text, total_charges_text;
    private String mProjectKey;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    private HashMap<String, Integer> mapState;//changes done

    private ArrayList<HashMap<String, String>> stateList;
    private TextView mTotalChargesView, mDateView;
    private RadioGroup radioGroupDays, radioGroupWords;
    private String mRsSymbol;
    private String mArticleCode;
    private ImageView mArticleImage1, mArticleImage2;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String mediaPath;
    private int currentImageView = 0, words, radioGroupDaysID, wordsCount = 0;
    private Button btn_submit;
    boolean valid = false;
    private int charges;
    String state_arr[], article_arr[], no_of_days_arr[], selectedState, selectedCity, selectedDays;
    ArrayAdapter<String> state_adapter, article_adapter, city_adapter, publish_days_adapter;
    RelativeLayout state_relative, city_relative, article_relative;
    BetterSpinner bsStateSpinner, bsCitySpinner, bsArticleSpinner, bsPublishDaysSpinner;
    RadioButton article_100d_radio, article_200d_radio, article_300d_radio, article_400d_radio;
    private SessionManager session, langSelection;

    //Indonesia


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        mProjectKey = getString(R.string.whiz_project_key);
        //  mProjectKey="0q4LozhupoQlXKFFiqtRHAZIJpdaHcObOO6gEKa6uuhQS4t1n33PutMsyyAm";
        mContext = this;
        langSelection = new SessionManager(getApplicationContext());


        // updateViews("hi_IN");

        setAppToolbar();
        initializeComponents();
        setListeners();

        setDateToText();
        //    setLang();

//        String languageToLoad  = "hi_IN"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());


        loadStateApi();

    }

    private void setLang() {
        String getLang = langSelection.getLanguage();
        String[] lang_arr = getResources().getStringArray(R.array.language_arr);
        if (getLang.equalsIgnoreCase(lang_arr[1])) {
            updateViews("hi");
        } else if (getLang.equalsIgnoreCase(lang_arr[2])) {
            updateViews("mr");
        }
    }

    private void setDateToText() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String strDate = sdf.format(date);
        mDateView.setText(strDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_article_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_keyboard) {
            openLangKb();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLangKb() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            printInputLanguages();

            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            inputMethodManager.showInputMethodPicker();
            inputMethodManager.showSoftInput(mTitleEdit, 0);


            InputMethodSubtype ims = inputMethodManager.getCurrentInputMethodSubtype();
            String localeString = ims.getLocale();
            Locale locale_str = new Locale(localeString);
            String currentLanguage = locale_str.getDisplayLanguage();
            Log.d("Core", "Language :" + currentLanguage);

            if (currentLanguage.equals("Indonesia")) {
                Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(Constant.Hindhi_Language_keyboard));
                startActivity(goToMarket);
                Log.d("Core", "Language :" + currentLanguage);
            } else {

                if (currentLanguage.equals("Indonesia")) {

                    Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse(Constant.Marathi_Language_keyboard));
                    startActivity(goToMarket);
                    Log.d("Core", "Language :" + currentLanguage);
                } else {

                }
            }


        }
    }

    private void setAppToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.create_article_menu));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeComponents() {
        mRsSymbol = getResources().getString(R.string.Rs);
        btn_submit = (Button) findViewById(R.id.article_btn_submit);

        state_arr = getResources().getStringArray(R.array.article_arr);
        article_arr = getResources().getStringArray(R.array.article_arr);
        no_of_days_arr = getResources().getStringArray(R.array.no_of_days_publishing);

        //  state_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, state_arr);
        state_relative = (RelativeLayout) findViewById(R.id.article_state_spinner);
        mStateSpinner = (Spinner) state_relative.findViewById(R.id.spinner);

        city_relative = (RelativeLayout) findViewById(R.id.article_city_spinner);
        mCitySpinner = (Spinner) city_relative.findViewById(R.id.spinner);

        article_relative = (RelativeLayout) findViewById(R.id.article_article_type_spinner);
        mArticleSpinner = (Spinner) article_relative.findViewById(R.id.spinner);

        mTitleEdit = (EditText) findViewById(R.id.article_title);
        mDescEdit = (EditText) findViewById(R.id.article_description);

        mDateView = (TextView) findViewById(R.id.article_date_value);

        bsStateSpinner = (BetterSpinner) findViewById(R.id.state_better_spinner);
        bsCitySpinner = (BetterSpinner) findViewById(R.id.city_better_spinner);
        bsArticleSpinner = (BetterSpinner) findViewById(R.id.article_better_spinner);
        bsPublishDaysSpinner = (BetterSpinner) findViewById(R.id.publish_better_spinner);
        bsCitySpinner.setClickable(false);
        bsCitySpinner.setFocusableInTouchMode(false);

        article_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, article_arr);
        bsArticleSpinner.setAdapter(article_adapter);// changed mArticleSpinner to bsArticleSpinner

        publish_days_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, no_of_days_arr);
        bsPublishDaysSpinner.setAdapter(publish_days_adapter);

        radioGroupDays = (RadioGroup) findViewById(R.id.article_radio_days);
        radioGroupDays.getCheckedRadioButtonId();

        radioGroupWords = (RadioGroup) findViewById(R.id.article_radio_words);
        radioGroupWords.getCheckedRadioButtonId();


        mArticleImage1 = (ImageView) findViewById(R.id.article_image1);
        mArticleImage2 = (ImageView) findViewById(R.id.article_image2);
        mTotalChargesView = (TextView) findViewById(R.id.article_total_charges_value);


        article_state_text = (TextView) findViewById(R.id.article_state_text);
        article_city_text = (TextView) findViewById(R.id.article_city_text);
        article_type_text = (TextView) findViewById(R.id.article_type_text);
        article_date_text = (TextView) findViewById(R.id.article_date_text);
        title_text = (TextView) findViewById(R.id.title_text);
        desc_text = (TextView) findViewById(R.id.desc_text);
        total_charges_text = (TextView) findViewById(R.id.total_charges_text);

    }

    private void setListeners() {
        mArticleImage1.setOnClickListener(this);
        mArticleImage2.setOnClickListener(this);
        btn_submit.setOnClickListener(this);


       /* bsArticleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mArticleCode = "m" + position;
                // Toast.makeText(CreateArticle.this, mArticleCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        bsArticleSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mArticleCode = "m" + position;
                // Toast.makeText(CreateArticle.this, mArticleCode, Toast.LENGTH_SHORT).show();
            }
        });

        bsPublishDaysSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDays = parent.getItemAtPosition(position).toString();
                Log.v("bsPublishDaysItemClick ", "selectedDays " + selectedDays);
            }
        });

        radioGroupWords.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                int wordsLength = setDescpWordsLength(radioId);
                Log.v("radioGroupWords ", "wordsLength " + wordsLength);
                //  mDescEdit.setText("");

                mDescEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(wordsLength)});
                int editTextLength = mDescEdit.getText().toString().length();
                Log.v("radioGroupWords ", "editTextLength " + editTextLength);
                if (!(editTextLength == 100)) {
                    if ((wordsLength <= editTextLength)) { //200<=300 & 200<=300
                    /*if (wordsLength < 200) {
                        Log.v("radioGroupWords ", "22 wordsLength " + wordsLength);
                    } else {

                    }*/

                        mDescEdit.setText(mDescEdit.getText().toString().substring(0, mDescEdit.getText().toString().length() - 100));
                    }
                }


            }
        });

        radioGroupDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioGroupDaysID = radioGroupDays.getCheckedRadioButtonId();
                calculateCharges(wordsCount, radioGroupDaysID);
            }
        });


        mDescEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordsCount = charSequence.toString().trim().length();
                Log.v("mDescEdit ", "wordsCount " + wordsCount);
                radioGroupDays.clearCheck();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private int setDescpWordsLength(int radioId) {
        // int radioId = radioGroupWords.getCheckedRadioButtonId();
        Log.v("", "radioId " + radioId);
        switch (radioId) {
            case R.id.article_100d_radio:
                words = 100;
                break;
            case R.id.article_200d_radio:
                words = 200;
                break;
            case R.id.article_300d_radio:
                words = 300;
                break;
            case R.id.article_400d_radio:
                words = 400;
                break;
            /*default:
                words = 0;
                break;*/
        }
        return words;
    }

    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        article_state_text.setText(resources.getString(R.string.state_lbl));
        article_city_text.setText(resources.getString(R.string.city_lbl));
        article_type_text.setText(resources.getString(R.string.article_lbl));
        article_date_text.setText(resources.getString(R.string.date_lbl));
        title_text.setText(resources.getString(R.string.title_lbl));
        desc_text.setText(resources.getString(R.string.desc_lbl));
        total_charges_text.setText(resources.getString(R.string.total_charges_str));

    }

    private void printInputLanguages() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showInputMethodPicker();
        List<InputMethodInfo> ims = inputMethodManager.getEnabledInputMethodList();

        for (InputMethodInfo method : ims) {
            List<InputMethodSubtype> submethods = inputMethodManager.getEnabledInputMethodSubtypeList(method, true);
            for (InputMethodSubtype submethod : submethods) {
                if (submethod.getMode().equals("keyboard")) {
                    String currentLocale = submethod.getLocale();
                    Log.i("Core", "Available input method locale : " + currentLocale);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTitleEdit.setFocusableInTouchMode(true);
        mTitleEdit.requestFocus();
    }

    private void calculateCharges(int count, int radioGroupDaysID) {
        Log.v("calculateCharges", " count " + count);
        try {
            int charLength = 0;
            mTotalChargesView.setText(count + "");
       /*     if (count > 0 && count <= 100) {
                charLength = 1;
            } else if (count > 101 && count <= 200) {
                charLength = 2;//free
            } else if (count > 201 && count <= 300) {
                charLength = 3;//1 rs perday/ 30 rs/30 day 30*1, 60*1,90*1,120*1
            } else if (count > 301 && count <= 400) {
                charLength = 4;// 2 rs /perday(300) ,30*2,60*2,90*2,120*2
            } *//*else {//& 3rs(400) 30*3,60*3,90*3,120*3
                charLength = 5;
            }*/

            if ((count > 0 && count <= 100)) {
                charLength = 0;

            } else if ((count > 100 && count <= 200)) {
                charLength = 1;

            } else if (count > 200 && count <= 300) {
                charLength = 2;

            } else if (count > 300 && count <= 400) {
                charLength = 3;

            }

            int days = getDays(radioGroupDaysID);
            Log.v("calculateCharges ", "days " + days);

            charges = charLength * days;

            Log.d("Core", "charges :" + charges);

            mTotalChargesView.setText(mRsSymbol + " " + charges);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDays(int radioId) {
        int days = 0;
        //  int radioId = radioGroupDays.getCheckedRadioButtonId();
        switch (radioId) {
            case R.id.article_30_radio:
                days = 30;
                break;
            case R.id.article_60_radio:
                days = 60;
                break;
            case R.id.article_90_radio:
                days = 90;
                break;
            case R.id.article_120_radio:
                days = 120;
                break;
            /*default:
                days = 0;
                break;*/
        }
        return days;
    }

    private void loadStateApi() {
        try {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(mContext))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            WebService webService = retrofit.create(WebService.class);
            Call<State> call = webService.getStateList();
            call.enqueue(new Callback<State>() {
                @Override
                public void onResponse(Call<State> call, Response<State> response) {

                    if (response.isSuccessful()) {
                        mProgressDialog.dismiss();
                        //Log.v("State ","response "+response.toString());
                        Log.e("Stateresponse ", new Gson().toJson(response.body()));


                        State state = response.body();
                        int responseCode = state.getStatus();
                        String responseMessage = state.getDescription();

                        List list = state.getStates();

                        displayStateData((ArrayList) state.getStates());
                    }
                }

                @Override
                public void onFailure(Call<State> call, Throwable t) {
                    mProgressDialog.dismiss();
                    t.printStackTrace();
                    if (t instanceof NoConnectivityException) {
                        // No internet connection
                        Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayStateData(ArrayList<State_Name> stateList) {
        try {
            ArrayList<String> list = new ArrayList();
            mapState = new HashMap<>();
            for (int i = 0; i < stateList.size(); i++) {
                State_Name stateData = stateList.get(i);
                list.add(stateData.getStateName());
                mapState.put(stateData.getStateName().trim(), stateData.getId());
            }


            state_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
            //mStateSpinner.setAdapter(adapter);
            bsStateSpinner.setAdapter(state_adapter); //changed from mStateSpinner to bsStateSpinner

           /* bsStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String selectedState = adapterView.getSelectedItem().toString().trim();

                    Integer stateId = mapState.get(selectedState);
                    System.out.println("State : " + selectedState + " ID :" + stateId);
                    // Toast.makeText(getApplicationContext(),selectedState,Toast.LENGTH_SHORT).show();
                    loadCity(stateId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/

            bsStateSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // selectedState = parent.getSelectedItem().toString();
                    selectedState = parent.getItemAtPosition(position).toString();

                    Integer stateId = mapState.get(selectedState);
                    System.out.println("State : " + selectedState + " ID :" + stateId);
                    Log.v("ItemClickListener ", "selectedState " + selectedState);
                    // Toast.makeText(getApplicationContext(),selectedState,Toast.LENGTH_SHORT).show();
                    bsCitySpinner.setHint("Select City");
                    bsCitySpinner.setText("");
                    loadCity(stateId);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCity(Integer stateId) {
        try {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(mContext))
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RequestBody bodyStateId = RequestBody.create(MediaType.parse("text/plain"), stateId + "");


            WebService webService = retrofit.create(WebService.class);
            Call<City> call = webService.getCityList(stateId);
            call.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                    if (response.isSuccessful()) {
                        mProgressDialog.dismiss();
                        Log.e("Cityresponse ", new Gson().toJson(response.body()));

                        City city = response.body();
                        int responseCode = city.getStatus();
                        String responseMessage = city.getDescription();
                        List list = city.getCities();

                        displayCityData((ArrayList) city.getCities());

                    }
                }

                @Override
                public void onFailure(Call<City> call, Throwable t) {
                    mProgressDialog.dismiss();
                    t.printStackTrace();
                    if (t instanceof NoConnectivityException) {
                        // No internet connection
                        Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayCityData(ArrayList<City_Name> cityList) {
        try {
            ArrayList<String> list = new ArrayList();
            for (int i = 0; i < cityList.size(); i++) {
                City_Name cityName = cityList.get(i);
                list.add(cityName.getCity());
            }
            bsCitySpinner.setFocusableInTouchMode(true);
            city_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
            // mCitySpinner.setAdapter(city_adapter);// change mCitySpinner to bsCitySpinner
            bsCitySpinner.setAdapter(city_adapter);// change mCitySpinner to bsCitySpinner

           /* bsCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedCity = adapterView.getSelectedItem().toString().trim();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/

            bsCitySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedCity = parent.getItemAtPosition(position).toString();

                    Log.v("bsCityItemClick ", "selectedCity " + selectedCity);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.article_btn_submit:
                if (validateFields()) {
                    /*Intent intent = new Intent(this, PayUActivity.class);
                    intent.putExtra("Price", charges);
                    startActivity(intent);
                    finish();*/
                }

                break;
            case R.id.article_image1:
                if (v instanceof ImageView)
                    currentImageView = 1;
                if (v instanceof ImageView) {
                    getImageFromGallery();
                }
                break;
            case R.id.article_image2:
                currentImageView = 2;
                if (v instanceof ImageView) {
                    getImageFromGallery();
                }
                break;


        }


    }

    private boolean validateFields() {

        if (bsStateSpinner.getText().toString().equals("")) {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.toast_msg_state_validations), Toast.LENGTH_SHORT).show();
            return valid;
        }
        if (bsCitySpinner.getText().toString().equals("")) {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.toast_msg_city_validations), Toast.LENGTH_SHORT).show();
            return valid;
        }
        if (bsArticleSpinner.getText().toString().equals("")) {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.toast_msg_article_validations), Toast.LENGTH_SHORT).show();
            return valid;
        }
        if (mDescEdit.getText().toString().trim().equals("")) {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.toast_msg_desc_validations), Toast.LENGTH_SHORT).show();
            return valid;

        }
        if (mTitleEdit.getText().toString().trim().equals("")) {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.toast_msg_title_validations), Toast.LENGTH_SHORT).show();
            return valid;
        } else {
            valid = true;
        }
        return valid;
    }

    private void getImageFromGallery() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                /*mUri = data.getData();
                ImageCropFunction();*/
            } /*else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == 2) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.getParcelable("data");
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    //imgfriend.setImageBitmap(thumbnail);
                    String profImage = getStringImage(bitmap);
                    sendProfileImage(profImage);
                }
            }*/

        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);
            String filename = mediaPath.substring(mediaPath.lastIndexOf("/") + 1);
            // Set the Image in ImageView for Previewing the Media
            if (currentImageView == 1) {
                mArticleImage1.setBackgroundResource(0);
                mArticleImage1.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            } else if (currentImageView == 2) {
                mArticleImage2.setBackgroundResource(0);
                mArticleImage2.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            }

            cursor.close();

            Bitmap bmp = BitmapFactory.decodeFile(mediaPath);

            System.out.println("Profile Bitmap Size width : " + bmp.getWidth() + " height : " + bmp.getHeight());

            String base64Image = getStringImage(bmp);
            //imgfriend.setImageBitmap(thumbnail);
            System.out.println("profile image : " + base64Image);
            //sendProfileImage(base64Image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStringImage(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 50, baos);
        //bmp.recycle();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }
}
