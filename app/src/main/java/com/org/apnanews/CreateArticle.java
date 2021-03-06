package com.org.apnanews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.org.apnanews.article.City;
import com.org.apnanews.article.City_Name;
import com.org.apnanews.article.State;
import com.org.apnanews.article.State_Name;
import com.org.apnanews.db.DBHelper;
import com.org.apnanews.db.SubCategoryTable;
import com.org.apnanews.db.SubCategoryTableHi;
import com.org.apnanews.db.SubCategoryTableMr;
import com.org.apnanews.helper.ConnectivityInterceptor;
import com.org.apnanews.helper.LocaleHelper;
import com.org.apnanews.helper.NoConnectivityException;
import com.org.apnanews.helper.TimeoutException;
import com.org.apnanews.interfaces.WebService;
import com.org.apnanews.models.NewsFeedModelResponse;
import com.org.apnanews.payU.PayUPnPActivity;
import com.org.apnanews.register.RegisterMember;
import com.org.apnanews.sharedpref.MyPreferences;
import com.org.apnanews.sharedpref.SessionManager;
import com.org.apnanews.utils.BetterSpinner;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//changes added on 26/03
public class CreateArticle extends AppCompatActivity implements View.OnClickListener {
    private int GET_PAYMENT_STATUS = 3;
    private Spinner mStateSpinner, mCitySpinner, mArticleSpinner;
    private EditText mTitleEdit, mDescEdit;
    private TextView article_state_text, article_city_text, article_type_text, article_date_text, title_text, desc_text, total_charges_text;
    private String mProjectKey,paymentStatus;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private LinkedHashMap<String, String> mapValuesFinal;
    private HashMap<String, Integer> mapState, cityMap;//changes done
    boolean validWords = false;
    int status = 0, moreWords;
    boolean mToggle = false;

    private ArrayList<HashMap<String, String>> stateList;
    private TextView mTotalChargesView, mDateView,img_sel_err;
    private RadioGroup radioGroupDays, radioGroupWords;
    private String mRsSymbol;
    private String mArticleCode, categoryId="", subCategoryId,stateId="", cityIdValue="";
    private ImageView mArticleImage1, mArticleImage2, iv_info_desc, iv_info_title;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, spaceCount, PICK_FROM_GALLERY = 2;
    private String mediaPath;
    private int currentImageView = 0, words, radioGroupDaysID, wordsCount = 0, charLength;
    private Button btn_submit;
    boolean valid = false;
    private int charges, sub_category_id;
    String state_arr[], article_arr[], no_of_days_arr[], lang_arr[];
    String getLangFromPref, language, selectedState, selectedCity, selectedDays;
    ArrayAdapter<String> state_adapter, article_adapter, sub_article_adapter, city_adapter, publish_days_adapter;
    RelativeLayout state_relative, city_relative, article_relative;
    BetterSpinner bsStateSpinner, bsCitySpinner, bsArticleSpinner, bsSubArticleSpinner;
    LinearLayout ll_sub_article;
    RadioButton article_100d_radio, article_200d_radio, article_300d_radio, article_400d_radio;
    private SessionManager session, langSelection;
    DBHelper db;
    List<RegisterMember> memberList;
    String memberId, memberToken;
    String base64Image1, base64Image2;
    String emailId, fullName, membertoken;
    int memberid;
    private MyPreferences myPreferences;
    int numOfWords = 0;
    List<String> newsPic;
    Uri realUri;
    SubCategoryTable subCategoryTable;
    SubCategoryTableHi subCategoryTableHi;
    SubCategoryTableMr subCategoryTableMr;
    ArrayList<String> filePaths = new ArrayList<>();
    String responseGson;
    List<MultipartBody.Part> photosToUploadList;
    String nWords = "";
    int imageSelected = 0;
    File file;
    long fileSizeInBytes;

    //Indonesia


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        mProjectKey = getString(R.string.whiz_project_key);
        //  mProjectKey="0q4LozhupoQlXKFFiqtRHAZIJpdaHcObOO6gEKa6uuhQS4t1n33PutMsyyAm";
        mContext = this;
        langSelection = new SessionManager(getApplicationContext());
        db = new DBHelper(mContext);
        subCategoryTable = new SubCategoryTable(mContext);
        subCategoryTableHi = new SubCategoryTableHi(mContext);
        subCategoryTableMr = new SubCategoryTableMr(mContext);

        // updateViews("hi_IN");

        setAppToolbar();
        getPrefData();
        getLang();
        initializeComponents();
        setListeners();

        setDateToText();


//        String languageToLoad  = "hi_IN"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());

        // bsStateSpinner.setFocusableInTouchMode(false);
        loadStateApi();

    }

    private void getLang() {
        getLangFromPref = langSelection.getLanguage();
        lang_arr = getResources().getStringArray(R.array.language_arr);
        if (getLangFromPref.equalsIgnoreCase(lang_arr[1])) {
            language = "Hindi";
        } else if (getLangFromPref.equalsIgnoreCase(lang_arr[2])) {
            language = "Marathi";
        }
    }

    private void setDateToText() {
        //Log.v("", "locale " + getResources().getConfiguration().locale);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", new Locale("hi", "IN"));


        // DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
        // String strDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date);
        //DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD,DateFormat.)
        // DateTimeFormatter.ofPattern("MM/dd/yyyy",Locale.ENGLISH);
        //DateFormat.getDa
        //
        // String s = dateFormat.format(date);
        String strDate = sdf.format(date);
        //String strDate =DateFormat.getDateInstance().format(date);
        // DateFormat.getInstance().format()

        //Date dateObj = sdf.parse(my_input_string);

        int datestyle = DateFormat.MEDIUM; // try also MEDIUM, and FULL
        int timestyle = DateFormat.SHORT;
        DateFormat df = DateFormat.getDateTimeInstance(datestyle, timestyle, new Locale("hi", "IN"));

        mDateView.setText(df.format(date));
        // mDateView.setText(strDate);
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
        //Log.v("CreateArticle ", "current Lang " + Locale.getDefault().getDisplayLanguage().toString());
        memberList = new ArrayList<>();
        newsPic = new ArrayList<>();
        photosToUploadList = new ArrayList<>();
        mRsSymbol = getResources().getString(R.string.Rs);
        btn_submit = (Button) findViewById(R.id.article_btn_submit);

        article_arr = getResources().getStringArray(R.array.article_arr);
        no_of_days_arr = getResources().getStringArray(R.array.no_of_days_publishing);

        state_relative = (RelativeLayout) findViewById(R.id.article_state_spinner);
        mStateSpinner = (Spinner) state_relative.findViewById(R.id.spinner);

        city_relative = (RelativeLayout) findViewById(R.id.article_city_spinner);
        mCitySpinner = (Spinner) city_relative.findViewById(R.id.spinner);

        article_relative = (RelativeLayout) findViewById(R.id.article_article_type_spinner);
        mArticleSpinner = (Spinner) article_relative.findViewById(R.id.spinner);

        mTitleEdit = (EditText) findViewById(R.id.article_title);
        mDescEdit = (EditText) findViewById(R.id.article_description);

        mDateView = (TextView) findViewById(R.id.article_date_value);
        img_sel_err = (TextView) findViewById(R.id.txt_image_sel_err);
        ll_sub_article = (LinearLayout) findViewById(R.id.ll_sub_article);
        bsStateSpinner = (BetterSpinner) findViewById(R.id.state_better_spinner);
        bsCitySpinner = (BetterSpinner) findViewById(R.id.city_better_spinner);
        bsArticleSpinner = (BetterSpinner) findViewById(R.id.article_better_spinner);
        bsSubArticleSpinner = (BetterSpinner) findViewById(R.id.sub_article_better_spinner);

        bsCitySpinner.setClickable(false);
        bsCitySpinner.setFocusableInTouchMode(false);

        article_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, article_arr);
        bsArticleSpinner.setAdapter(article_adapter);// changed mArticleSpinner to bsArticleSpinner




        radioGroupDays = (RadioGroup) findViewById(R.id.article_radio_days);
        radioGroupDays.getCheckedRadioButtonId();

        radioGroupWords = (RadioGroup) findViewById(R.id.article_radio_words);
        radioGroupWords.getCheckedRadioButtonId();


        mArticleImage1 = (ImageView) findViewById(R.id.article_image1);
        mArticleImage2 = (ImageView) findViewById(R.id.article_image2);
        mTotalChargesView = (TextView) findViewById(R.id.article_total_charges_value);

        iv_info_title = (ImageView) findViewById(R.id.iv_info_title);
        iv_info_desc = (ImageView) findViewById(R.id.iv_info_desc);


        article_state_text = (TextView) findViewById(R.id.article_state_text);
        article_city_text = (TextView) findViewById(R.id.article_city_text);
        article_type_text = (TextView) findViewById(R.id.article_type_text);
        article_date_text = (TextView) findViewById(R.id.article_date_text);
        title_text = (TextView) findViewById(R.id.title_text);
        desc_text = (TextView) findViewById(R.id.desc_text);
        total_charges_text = (TextView) findViewById(R.id.total_charges_text);

        setDefaultValues();
        if (mDescEdit.getText().toString().isEmpty()
                && mTitleEdit.getText().toString().isEmpty()) {
            mTotalChargesView.setText(mRsSymbol + "0");
        }
    }

    private void setDefaultValues() {
        radioGroupWords.check(R.id.article_100d_radio);

        //  words = 100;
        numOfWords = setDescpWordsNum(R.id.article_100d_radio);
        //Log.v("DefaultValues ", "numOfWords " + numOfWords);

    }

    private void setListeners() {
        mArticleImage1.setOnClickListener(this);
        mArticleImage2.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        iv_info_title.setOnClickListener(this);
        iv_info_desc.setOnClickListener(this);



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
                categoryId = String.valueOf(position + 1);
                //Log.v("bsArticleSpinner ", "categoryId " + categoryId);
                // Toast.makeText(CreateArticle.this, mArticleCode, Toast.LENGTH_SHORT).show();
                if (categoryId.equals("10")) {// position 9
                    ll_sub_article.setVisibility(View.VISIBLE);
                    bsSubArticleSpinner.setText("");
                    sub_article_adapter = new ArrayAdapter<String>(CreateArticle.this, R.layout.spinner_item, getResources().getStringArray(R.array.small_class_sub_category_items));
                    bsSubArticleSpinner.setAdapter(sub_article_adapter);// changed mArticleSpinner to bsArticleSpinner
                } else if (categoryId.equals("13")) {// position 12
                    bsSubArticleSpinner.setText("");

                    ll_sub_article.setVisibility(View.VISIBLE);

                    sub_article_adapter = new ArrayAdapter<String>(CreateArticle.this, R.layout.spinner_item, getResources().getStringArray(R.array.career_related_sub_category_items));
                    bsSubArticleSpinner.setAdapter(sub_article_adapter);// changed mArticleSpinner to bsArticleSpinner
                } else {
                    ll_sub_article.setVisibility(View.GONE);
                }

            }
        });

        bsSubArticleSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subCategoryId = String.valueOf(position + 1);
//                parent.getItemAtPosition(position);
                //Log.v("", "Value " + parent.getItemAtPosition(position).toString());

                String sub_cat_name = parent.getItemAtPosition(position).toString();
                //getStringByLocal(mContext,);

                if (getLangFromPref.equalsIgnoreCase(lang_arr[1])) {
                    sub_category_id = subCategoryTableHi.getSubCategoryIdByName(sub_cat_name);
                } else if (getLangFromPref.equalsIgnoreCase(lang_arr[2])) {
                    sub_category_id = subCategoryTableMr.getSubCategoryIdByName(sub_cat_name);
                } else {
                    sub_category_id = subCategoryTable.getSubCategoryIdByName(sub_cat_name);
                }
                //  String value = String.valueOf(item.getString(0));

                //Log.v("bsSubArticleSpinner ", "sub_category_id_db " + sub_category_id);
                //Log.v("bsSubArticleSpinner ", "subCategoryIdByPos " + subCategoryId);
            }
        });



        radioGroupWords.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                numOfWords = setDescpWordsNum(radioId);
                //Log.v("radioGroupWords ", "numOfWords " + numOfWords);
               /* if (numOfWords == 400) {
                    setDescTextLength(300000);
                } else {
                  //  setDescTextLength(numOfWords);
                    setLengthDescTextview();
                }*/

            }
        });

        radioGroupDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioGroupDaysID = radioGroupDays.getCheckedRadioButtonId();
                calculateCharges(numOfWords, radioGroupDaysID);
            }
        });

        mDescEdit.addTextChangedListener(new EditTextListener());
        /*if (moreWords == 1) {

            Toast.makeText(getApplicationContext(), "You cannot add more than " + numOfWords + " words", Toast.LENGTH_SHORT).show();
        }*/
        /*mDescEdit.addTextChangedListener(new TextWatcher() {
            boolean mToggle = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               *//* if(mToggle) {
                    setFailedAlertDialog(CreateArticle.this, "", "You cannot add more than " + numOfWords + " words");
                }*//*

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                wordsCount = countWords(s.toString());
                Log.v("mDescEdit ", "words " + wordsCount);
                charLength = s.toString().length();
                Log.v("mDescEdit ", "length " + s.toString().length());
                int count = getSpaces(mDescEdit.getText().toString());
                Log.v("getSpaces ", "count " + count);
                // if (mToggle) {

                if (numOfWords != 400) {
                    if (wordsCount <= numOfWords) {
                        validWords = true;
                    } else {

                        mDescEdit.setText(value(mDescEdit.getText().toString(), numOfWords));

                        // Toast.makeText(getApplicationContext(), "You cannot add more than " + numOfWords, Toast.LENGTH_SHORT).show();
                        //  mToggle=true;
                        //hideKeyboard();
                        //
                    }
                }
                //  }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            *//*String s=editable.toString();
               //

*//*          if (mToggle) {
                    setFailedAlertDialog(CreateArticle.this, "", "You cannot add more than " + numOfWords + " words");
                }
                mToggle = !mToggle;


            }
        });*/


    }

    private class EditTextListener implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mToggle = false;

        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            wordsCount = countWords(s.toString());
            //Log.v("mDescEdit ", "words " + wordsCount);
            charLength = s.toString().length();
            //Log.v("mDescEdit ", "length " + s.toString().length());
            int count = getSpaces(mDescEdit.getText().toString());
            //Log.v("getSpaces ", "count " + count);


            if (numOfWords != 400) {

                if (wordsCount <= numOfWords) {//99
                    validWords = true;
                    moreWords = 0;

                    // mDescEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(charLength)});
                } else {
                    mDescEdit.setText(value(mDescEdit.getText().toString(), numOfWords));
                    mDescEdit.setSelection(mDescEdit.getText().length());
                    if (!mToggle) {
                        mToggle = true;
                        Toast.makeText(getApplicationContext(), "You cannot add more than " + numOfWords + " words", Toast.LENGTH_SHORT).show();

                        // mDescEdit.setText(value(mDescEdit.getText().toString(), numOfWords));
                        //mDescEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(numOfWords)});
                        moreWords = 1;
                        //mDescEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter()});
                        //  mToggle=true;
                        //hideKeyboard();
                        //
                    }
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //    mToggle = false;
            /*String s=editable.toString();
               //

*/         /* if (mToggle) {
                setFailedAlertDialog(CreateArticle.this, "", "You cannot add more than " + numOfWords + " words");
            }
            mToggle = !mToggle;*/

        }
    }


    public String value(String s, int numOfWords) {
        String result = "";

        int wordsCount = countWords(s);
        if (wordsCount <= numOfWords) {
            result = s;
        } else {
            int length = wordsCount - numOfWords;
            result = s.substring(0, s.length() - length);
        }
        return result;
    }

    private int getSpaces(String s) {
        int spaceCount = 0;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }


    public int countWords(String str) {
        String words[] = str.split(" ");
        int count = words.length;
        return count;
    }

    private void setDescTextLength(int wordsCount) { // 100 words
        //Log.v("setDescTextLength ", "wordsCount " + wordsCount);

        //change 100 characters to 100 words

        //
        // words = countWords(mDescEdit.getText().toString());
        //  Log.v("setDescTextLength ", "words " + words);


        //   int spaceCount = getSpaceCount(mDescEdit.getText().toString());
        //  Log.v("setDescTextLength ", "spaceCount " + spaceCount);
        int editTextLength = mDescEdit.getText().toString().length();

        //   if (words > 0) {
        mDescEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editTextLength)});
        // }


        // Log.v("setDescTextLength ", "charLength " + editTextLength);

       /* if (!(words == 100)) {
            if ((words <= editTextLength)) { //200<=300 & 200<=300
                    *//*if (numOfWords < 200) {
                        Log.v("radioGroupWords ", "22 numOfWords " + numOfWords);
                    } else {

                    }*//*

                mDescEdit.setText(mDescEdit.getText().toString().substring(0, mDescEdit.getText().toString().length() - 100));
            }
        }*/

    }

    private int getSpaceCount(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                spaceCount++;
            } else {
                spaceCount = 0;
            }
        }
        return spaceCount;
    }

    public static String getStringByLocal(Activity context, int id, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(id);
    }

    private int setDescpWordsNum(int radioId) {
        // int radioId = radioGroupWords.getCheckedRadioButtonId();
        //Log.v("", "radioId " + radioId);
        switch (radioId) {
            case R.id.article_100d_radio:
                words = 100;
                mDescEdit.setText("");
                radioGroupDays.clearCheck();
                break;
            case R.id.article_200d_radio:
                words = 200;
                mDescEdit.setText("");
                radioGroupDays.clearCheck();
                break;
            case R.id.article_300d_radio:
                words = 300;
                radioGroupDays.clearCheck();
                mDescEdit.setText("");
                break;
            case R.id.article_400d_radio:
                words = 400;
                radioGroupDays.clearCheck();
                mDescEdit.setText("");
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
        //Log.v("calculateCharges", " count " + count);
        try {
            int charLength = 0;

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
                charLength = 1;

            } else if ((count > 100 && count <= 200)) {
                charLength = 1;

            } else if (count > 200 && count <= 300) {
                charLength = 2;

            } else if (count > 300 && count <= 400) {
                charLength = 3;

            } else if (count > 400) {
                charLength = 4;
            }

            int days = getDays(radioGroupDaysID);
            //Log.v("calculateCharges ", "days " + days + " charLength " + charLength);

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
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
           /* LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.custom_progress_view,null);
            final MaterialStyledDialog dialog=  new MaterialStyledDialog.Builder(this)
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setIcon(R.mipmap.ic_success)
                    .setTitle("Loading...")
                    .setDescription("Please Wait")
                    .setCustomView(customView) // Old standard padding: .setCustomView(your_custom_view, 20, 20, 20, 0)
                    //.setCustomView(your_custom_view, 10, 20, 10, 20) // int left, int top, int right, int bottom
                    .show();*/

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(50000, TimeUnit.MILLISECONDS)
                    .readTimeout(50000, TimeUnit.MILLISECONDS)
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
                        // Log.v("State ","response "+response.toString());
                        // Log.e("Stateresponse ", new Gson().toJson(response.body()));
                        State state = response.body();
                        if (state.getStatus() == 0) {

                            // int responseCode = state.getStatus();
                            // String responseMessage = state.getDescription();
                            // List<State_Name> states=new ArrayList<>();
                            // ArrayList<State_Name> stateList= new ArrayList<>(state.getStates());
                            // states.addAll(state.getStates());
                            displayStateData((ArrayList<State_Name>) state.getStates());
                        } else {
                            Toast.makeText(CreateArticle.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<State> call, Throwable t) {
                    mProgressDialog.dismiss();
                    t.printStackTrace();
                    if (t instanceof NoConnectivityException) {
                        // No internet connection
                        Toast.makeText(mContext, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                    } else {
                      //  bsStateSpinner.setFocusableInTouchMode(false);
                        Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayStateData( ArrayList<State_Name> stateList) {
       // List<State_Name> stateList=new ArrayList<>(states.getStates());

        try {
            ArrayList<String> list = new ArrayList<>();
            mapState = new HashMap<>();
            if (stateList.size()> 0|| stateList.size() != 0 || !stateList.isEmpty() ) {
                for (int i = 0; i < stateList.size(); i++) {
                    State_Name stateData = stateList.get(i);
                    list.add(stateData.getStateName());
                    mapState.put(stateData.getStateName().trim(), stateData.getId());
                }

                bsStateSpinner.setFocusableInTouchMode(true);
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
                        // stateId = String.valueOf(position + 1);

                        Integer state_id = mapState.get(selectedState);
                        stateId = String.valueOf(state_id);
                        System.out.println("State : " + selectedState + " stateId :" + state_id);
                        //Log.v("ItemClickListener ", "selectedState " + selectedState);
                        // Toast.makeText(getApplicationContext(),selectedState,Toast.LENGTH_SHORT).show();
                        bsCitySpinner.setHint("Select City");
                        bsCitySpinner.setText("");
                        loadCity(state_id);
                    }
                });
            } else {
                Toast.makeText(CreateArticle.this, "No data available", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCity(Integer stateId) {
        try {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(mContext))
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //RequestBody bodyStateId = RequestBody.create(MediaType.parse("text/plain"), stateId + "");


            WebService webService = retrofit.create(WebService.class);
            Call<City> call = webService.getCityList(stateId);
            call.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                    mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        //Log.e("Cityresponse ", new Gson().toJson(response.body()));

                        City city = response.body();
                        if (city.getStatus() == 0) {
                            //  int responseCode = city.getStatus();
                            //  String responseMessage = city.getDescription();
                            // List list = city.getCities();
                           // List<City_Name> cityList = new ArrayList<>();
                            // ArrayList<State_Name> stateList= new ArrayList<>(state.getStates());
                           // cityList.addAll(city.getCities());

                            displayCityData((ArrayList<City_Name>) city.getCities());

                        } else {
                            //  displayCityData();
                            Toast.makeText(CreateArticle.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<City> call, Throwable t) {
                    mProgressDialog.dismiss();
                    t.printStackTrace();
                    if (t instanceof NoConnectivityException) {
                        // No internet connection
                        //  Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                        setFailedAlertDialog(mContext, "Failed", "No Internet! Please Check Your internet connection", paymentStatus);
                    } else if (t instanceof TimeoutException) {
                        setFailedAlertDialog(mContext, "Failed", t.getMessage(), paymentStatus);
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
            cityMap = new HashMap<>();
            if (cityList.size() != 0 || !cityList.isEmpty()) {
                for (int i = 0; i < cityList.size(); i++) {
                    City_Name cityName = cityList.get(i);
                    list.add(cityName.getCity());
                    cityMap.put(cityName.getCity().trim(), cityName.getId());
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
                        cityIdValue = String.valueOf(cityMap.get(selectedCity));
                        //Log.v("bsCityItemClick ", "selectedCity " + selectedCity + " cityIdValue " + cityIdValue);
                    }
                });
            } else {
                cityList.clear();
                cityMap.clear();
                city_adapter.clear();
                Toast.makeText(CreateArticle.this, "No data available", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.article_btn_submit:
                if (validateFields()) {
                    if(paymentStatus!=null) {
                        if (paymentStatus.equals("Success")) {
                            postNewsAPI(paymentStatus);
                        } else {
                            proceedForPaymentDetails();
                        }
                    }else{
                        proceedForPaymentDetails();
                    }
                }
                break;
            case R.id.article_image1:
                // if (v instanceof ImageView)

                if (v instanceof ImageView) {
                    currentImageView = 1;
                    getImageFromGallery();
                }
                break;
            case R.id.article_image2:

                if (v instanceof ImageView) {
                    currentImageView = 2;
                    getImageFromGallery();
                }
                break;

            case R.id.iv_info_desc:
                openAlertInfoForKbSettingPopup();
                break;

            case R.id.iv_info_title:
                openAlertInfoForKbSettingPopup();
                break;
        }


    }

    private void proceedForPaymentDetails() {
      /*  LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_progress_view,null);
        final MaterialStyledDialog dialog=  new MaterialStyledDialog.Builder(this)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_success)
                .setTitle("Proceeding for payment..")
                .setDescription("")
                .setCustomView(customView) // Old standard padding: .setCustomView(your_custom_view, 20, 20, 20, 0)
                //.setCustomView(your_custom_view, 10, 20, 10, 20) // int left, int top, int right, int bottom
                .show();*/
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Proceeding for payment..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(CreateArticle.this, PayUPnPActivity.class);
               // intent.putExtra("Price", charges);
                intent.putExtra("Price", 1);
                startActivityForResult(intent, GET_PAYMENT_STATUS);

                mProgressDialog.dismiss();
               // dialog.cancel();
                //dialog.dismiss();
            }
        }, 3000);
      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
        new MaterialStyledDialog.Builder(mContext)
               // .setTitle()
                .setDescription("Proceeding for payment..")
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_success)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //register success
                        Intent intent = new Intent(CreateArticle.this, PayUPnPActivity.class);
                        intent.putExtra("Price", charges);
                        startActivityForResult(intent, GET_PAYMENT_STATUS);
                    }
                })
                .show();
         }
        }, 3000);*/
    }

    private void getPrefData() {
        myPreferences = MyPreferences.getMyAppPref(this);
        memberid = myPreferences.getMemberId();
        membertoken = myPreferences.getMemberToken().trim();

        //Log.v("getPrefData ", "memberid " + memberid);
        // emailId = myPreferences.getEmailId().trim();
        // fullName = myPreferences.getFirstName().trim() + " " + myPreferences.getLastName().trim();
    }

    private void postNewsAPI(final String paymentStatus) {
        //Log.v("Calling ", "postNewsAPI");
        mapValuesFinal = new LinkedHashMap<>();
        mProgressDialog = new ProgressDialog(CreateArticle.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(600000, TimeUnit.MILLISECONDS)
                .readTimeout(600000, TimeUnit.MILLISECONDS)
                .addInterceptor(new ConnectivityInterceptor(getApplicationContext()))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebService webService = retrofit.create(WebService.class);
        String newsTitle = mTitleEdit.getText().toString();
        String newsDesc = mDescEdit.getText().toString();
        //String newsList[] = {base64Image1, base64Image2};
       /* for (int i = 0; i < filePaths.size(); i++) {
            file = new File(filePaths.get(i));
        }*/

        realUri = Uri.parse(mediaPath);
        if (realUri != null) {
            file = new File(String.valueOf(realUri));
        }
        //request body is used to attach file.
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        //and request body and file name using multipart.
        MultipartBody.Part image = MultipartBody.Part.createFormData("news_images", file.getName(), requestBody); //"image" is parameter for photo in API.
        for (int i = 0; i < filePaths.size(); i++) {
            File files = new File(filePaths.get(i));
            RequestBody requestBodyy = RequestBody.create(MediaType.parse("image/*"), files);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("news_images[]", file.getName(), requestBodyy);
            photosToUploadList.add(filePart);
        }
        // }

        String countryId = "1";

        RequestBody paramMemberToken = RequestBody.create(MediaType.parse("text/plain"), membertoken);
        RequestBody paramMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberid);
        RequestBody paramCategoryId = RequestBody.create(MediaType.parse("text/plain"), "" + categoryId);
        RequestBody paramSubCategoryId = RequestBody.create(MediaType.parse("text/plain"), "" + sub_category_id);
        RequestBody paramCountryId = RequestBody.create(MediaType.parse("text/plain"), "" + countryId);
        RequestBody paramStateId = RequestBody.create(MediaType.parse("text/plain"), "" + stateId);
        RequestBody paramCityId = RequestBody.create(MediaType.parse("text/plain"), "" + cityIdValue);
        RequestBody paramNewsTitle = RequestBody.create(MediaType.parse("text/plain"), "" + newsTitle);
        RequestBody paramNewsDesc = RequestBody.create(MediaType.parse("text/plain"), "" + newsDesc);
        //  RequestBody paramNewsPic = RequestBody.create(MediaType.parse("text/plain"), "" + newsPic);

        mapValuesFinal.put("member_token", membertoken);
        mapValuesFinal.put("member_id", String.valueOf(memberid));
        mapValuesFinal.put("category_id", categoryId);
        mapValuesFinal.put("sub_category_id", String.valueOf(sub_category_id));
        mapValuesFinal.put("country_id", countryId);
        mapValuesFinal.put("state_id", stateId);
        mapValuesFinal.put("city_id", cityIdValue);
        mapValuesFinal.put("news_title", newsTitle);
        mapValuesFinal.put("news_desc", newsDesc);
        // mapValuesFinal.put("news_images", String.valueOf(newsPic));

    /*    Log.v("postNewsAPI ", "membertoken " + membertoken);
        Log.v("postNewsAPI ", "memberid " + memberid);
        Log.v("postNewsAPI ", "countryId " + countryId);
        Log.v("postNewsAPI ", "categoryId " + categoryId);
        Log.v("postNewsAPI ", "SubcategoryId " + sub_category_id);
        Log.v("postNewsAPI ", "stateId " + stateId);
        Log.v("postNewsAPI ", "cityIdValue " + cityIdValue);
        Log.v("postNewsAPI ", "newsTitle " + newsTitle);
        Log.v("postNewsAPI ", "newsDesc " + newsDesc);
        Log.v("postNewsAPI ", "image " + image);
        Log.v("postNewsAPI ", "imageName " + image.toString());
        Log.v("postNewsAPI ", "imageName11 " + new Gson().toJson(image));
        Log.v("postNewsAPI ", "photosToUploadList " + new Gson().toJson(photosToUploadList));
        Log.v("postNewsAPI ", "photosToUploadListSize " + photosToUploadList.size());
*/

        // String news_pic_str = Arrays.toString(newsList);
        //Log.v("postNewsAPI ", "reqParams " + mapValuesFinal.toString());

        /*Call<NewsFeedModelResponse> serverResponse = webService.post_news(paramMemberToken, paramMemberId,
                paramCategoryId, paramSubCategoryId, paramCountryId, paramStateId, paramCityId, paramNewsTitle, paramNewsDesc, image);
        */
        Call<NewsFeedModelResponse> serverResponse = webService.post_news(paramMemberToken, paramMemberId,
                paramCategoryId, paramSubCategoryId, paramCountryId, paramStateId, paramCityId, paramNewsTitle, paramNewsDesc, language, photosToUploadList);
        //    Call<NewsFeedModelResponse> serverResponse = webService.post_news(mapValuesFinal);

        String reqParam = bodyToString(serverResponse.request().body());
        /// Log.v("postNewsAPI ", "reqParam : " + reqParam);
        // Log.v("postNewsAPI ", "LoginParameters : " + serverResponse.request().body().toString());
        //Log.v("postNewsAPI ", "postNewsAPI req : " + serverResponse.request().toString());
        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();

                if (response.isSuccessful()) {

                  /*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*/

                    NewsFeedModelResponse serverResponse = response.body();
                    responseGson = new Gson().toJson(response.body());
                    //Log.v("PostNewsAPI ", "response " + responseGson);
                    //    newsList=serverResponse.toString();
                    if (serverResponse.getStatus() == 0) {
                        //Log.v("PostNewsAPI ", "response " + new Gson().toJson(response.body()));

                        showSuccessAlertDialog(CreateArticle.this, "Success", "Your article created succesfully\n Proceeding for payment");
                    } else {
                        //Log.v("PostNewsAPI ", "status " + serverResponse.getStatus() + " Desc " + serverResponse.getDescription());

                        if(paymentStatus.equals("Success")){
                            setFailedAlertDialog(CreateArticle.this, serverResponse.getStatus().toString(), "Failure",paymentStatus);
                        }

                        Toast.makeText(mContext, "status " + serverResponse.getStatus() + "\n Failure ", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFeedModelResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                t.printStackTrace();
                //Log.v("PostNewsAPI ", "Failure " + t.getMessage());
                Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                if (t instanceof NoConnectivityException) {
                    // No internet connection
                    Toast.makeText(mContext, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                    // setFailedAlertDialog(HomeActivity.this, "Failed", "No Internet! Please Check Your internet connection");
                }
            }
        });
    }

    private void showSuccessAlertDialog(Context context, String title, String desc) {
        new MaterialStyledDialog.Builder(context)
                .setTitle(title)
                .setDescription(desc)
                .setStyle(Style.HEADER_WITH_ICON)
                .setCancelable(false)
                .setIcon(R.mipmap.ic_success)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent(CreateArticle.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                       /* Intent intent = new Intent(CreateArticle.this, PayUPnPActivity.class);
                        intent.putExtra("Price", 1);
                        startActivity(intent);
                        finish();*/
                    }
                })
                .show();
    }

    private void setFailedAlertDialog(Context context, String title, String desc, final String paymentStatus) {
        new MaterialStyledDialog.Builder(context)
                .setTitle(title)
                .setDescription(desc)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_failed)
                .setCancelable(false)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //  if(paymentStatus.equals("Success")){

                        //  }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private void openAlertInfoForKbSettingPopup() {
        new MaterialStyledDialog.Builder(CreateArticle.this)
                .setTitle("Please select keyboard from settings")
                .setDescription("1. First Go to Google Play store and download Google Indic Keyboard (By Google). \n" +
                        "(skip 1. if you already have Google Indic Keyboard) " + "\n" +
                        "2. Click on settings button to go to Languages and input in your phone settings. " + "\n" +
                        "3. Tap on current keyboard under Keyboard & input methods. " + "\n" +
                        "4. Tap on choose keyboards. " + "\n" +
                        "5. Tap on the Google Indic Keyboard to select your language as Hindi or Marathi.")
                //"2. Go to your Phone Settings. " + "\n" +
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_info)
                .setPositiveText("Settings")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //register success
                        Intent intent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
               /* .setNeutralText("Settings")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent=new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                        startActivity(intent);
                    }
                })*/
                .show();
    }

    private boolean validateFields() {

        if (bsStateSpinner.getText().toString().equals("")) {
          Toast.makeText(mContext,mContext.getResources().getString(R.string.toast_msg_state_validations), Toast.LENGTH_SHORT).show();
        //    bsStateSpinner.setError(mContext.getResources().getString(R.string.toast_msg_state_validations));
            return valid;
        }
        if (bsCitySpinner.getText().toString().equals("")) {
            Toast.makeText(mContext,mContext.getResources().getString(R.string.toast_msg_city_validations), Toast.LENGTH_SHORT).show();
        //    bsCitySpinner.setError(mContext.getResources().getString(R.string.toast_msg_city_validations));

            return valid;
        }
        if (bsArticleSpinner.getText().toString().equals("")) {
            Toast.makeText(mContext,mContext.getResources().getString(R.string.toast_msg_article_validations), Toast.LENGTH_SHORT).show();
      //      bsArticleSpinner.setError(mContext.getResources().getString(R.string.toast_msg_article_validations));
            return valid;
        }
        if (mTitleEdit.getText().toString().trim().equals("")) {
            Toast.makeText(mContext,mContext.getResources().getString(R.string.toast_msg_title_validations), Toast.LENGTH_SHORT).show();
           // mTitleEdit.setError(mContext.getResources().getString(R.string.toast_msg_title_validations),mContext.getResources().getDrawable(R.mipmap.ic_info));
            return valid;
        }
        if (mDescEdit.getText().toString().trim().equals("")) {
            Toast.makeText(mContext,mContext.getResources().getString(R.string.toast_msg_desc_validations), Toast.LENGTH_SHORT).show();
         //   mDescEdit.setError(mContext.getResources().getString(R.string.toast_msg_desc_validations),mContext.getResources().getDrawable(R.mipmap.ic_info));
            return valid;

        }

        if (imageSelected == 0) {
          //  Toast.makeText(mContext,"Please select atleast one image", Toast.LENGTH_SHORT).show();
            img_sel_err.setText("Please select atleast one image");  //changed on 12/05/17
            return valid;
        }
        if (charges == 0) {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.toast_msg_charges_validations), Toast.LENGTH_SHORT).show();
            return valid;
        } else {
            valid = true;
        }
        return valid;
    }

    private void getImageFromGallery() {


        //if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, PICK_FROM_GALLERY);
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            //  photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_FILE);
               /* Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri photoUri = FileProvider.getUriForFile(CreateArticle.this, BuildConfig.APPLICATION_ID + ".provider", );
                intent.setData(photoUri);
                startActivity(intent);*/
        }
        /*} else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image*//*");
            startActivityForResult(photoPickerIntent, SELECT_FILE);
        }*/
        /*try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);//Intent.ACTION_GET_CONTENT//ACTION_PICK
            photoPickerIntent.setType("image*//*");
            startActivityForResult(photoPickerIntent, SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.v("onActivityResult ", "requestCode " + requestCode);
        //Log.v("onActivityResult ", "resultCode " + resultCode);

        if (requestCode == SELECT_FILE || requestCode == PICK_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data);
            }
        } else if (requestCode == GET_PAYMENT_STATUS) {
            if (resultCode == Activity.RESULT_OK) {
                // fetch the message String
                paymentStatus = data.getStringExtra("message");
                //Log.v("onActivityResult ", "status " + status);
                // Set the message string in textView
                // textViewMessage.setText(message);
                if (paymentStatus.equals("Success")) {
                    //postNewsAPI();
                    // postPaymentStatus(1);
                    postNewsAPI(paymentStatus);
                }/*else{
                   // postPaymentStatus(0);
                   // postNewsAPI("Success");
                }*/

            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // CharSequence userText = textBox.getText();
        if (!categoryId.equals("") &&
                sub_category_id != 0 &&
                !stateId.equals("") &&
                !cityIdValue.equals("") &&
                !mTitleEdit.getText().toString().equals("") &&
                !mDescEdit.getText().toString().equals("")
                ) {
            outState.putInt("articleSpinner", Integer.parseInt(categoryId) - 1);
            outState.putInt("subArticleSpinner", sub_category_id);
            outState.putInt("stateId", Integer.parseInt(stateId));
            outState.putInt("cityIdValue", Integer.parseInt(cityIdValue));
            outState.putCharSequence("title", mTitleEdit.getText().toString());
            outState.putCharSequence("desc", mDescEdit.getText().toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        try {
            int arSpinnerPos = savedInstanceState.getInt("articleSpinner");
            int subArSpinnerPos = savedInstanceState.getInt("subArticleSpinner");
            int stateId = savedInstanceState.getInt("stateId");
            int cityId = savedInstanceState.getInt("cityIdValue");
            CharSequence title = savedInstanceState.getCharSequence("title");
            CharSequence desc = savedInstanceState.getCharSequence("desc");
            bsArticleSpinner.setSelection(arSpinnerPos);
            bsSubArticleSpinner.setSelection(subArSpinnerPos);
            bsCitySpinner.setSelection(cityId);
            bsStateSpinner.setSelection(stateId);
            mTitleEdit.setText(title);
            mDescEdit.setText(desc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //textBox.setText(userText);
    }


    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            File imageFIle = null;
            Uri selectedImage = null;
            imageSelected = 1;
            img_sel_err.setText("");
            try {
                selectedImage = data.getData();
                mediaPath = getPathFromURI(selectedImage);
                imageFIle = new File(mediaPath);
                //    Log.v("onSelectFromGallery ", " mediaPath " + mediaPath);
                // Toast.makeText(getApplicationContext(), "Image Path: " + mediaPath + "\nUri Path: " + selectedImage.toString(), Toast.LENGTH_SHORT).show();
                //  Log.v("onSelectFromGallery ", " selectedImage " + selectedImage.toString());

               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageFIle = new File();
                    selectedImage = FileProvider.getUriForFile(CreateArticle.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            imageFIle);
                }else{

                }*/

                fileSizeInBytes = imageFIle.length();
                //Log.v("onSelectFromGallery ", "get size " + fileSizeInBytes);

                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                float fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                float fileSizeInMB = fileSizeInKB / 1024;

                if (fileSizeInMB < 5) {
                    // doCrop();
                    img_sel_err.setText("");
                    resizeImage(selectedImage);
                } else {
                    imageSelected = 0;
                    img_sel_err.setText("Image size too large, please selct image less than 5mb.");
                    //Toast.makeText(this, "Image size too large, please selct image less than 5mb.", Toast.LENGTH_LONG).show();
                }

         /*   String filename = mediaPath.substring(mediaPath.lastIndexOf("/") + 1);
            try {
             *//*   Bitmap a = (BitmapFactory.decodeFile(mediaPath));
                Bitmap photo = compressImageToMax(a,5000000);*//*
               // imageView.setImageBitmap(photo);

                // bimatp factory
                File imageFIle = new File(mediaPath);
                Log.v("onSelectFromGallery ", " imageFIle.length() " + imageFIle.length());
                try {
                    if (imageFIle.length() > 5000000) {
                        Toast.makeText(CreateArticle.this, "Too Large Image, Please Select another.", Toast.LENGTH_SHORT).show();
                    } else {*//*

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        // downsizing image as it throws OutOfMemory Exception for larger
                        // images
                        options.inSampleSize = 2;

                        Bitmap compressedBitmap = BitmapFactory.decodeFile(mediaPath, options);
                        Bitmap originalSize = BitmapFactory.decodeFile(mediaPath);
                        Log.v("onSelectFromGallery ", " originalSize " + originalSize.getByteCount());

                        Log.v("onSelectFromGallery ", " compressedSize " + compressedBitmap.getByteCount());
                        //create file which we want to send to server.

                        // Set the Image in ImageView for Previewing the Media
                        if (currentImageView == 1) {
                            mArticleImage1.setBackgroundResource(0);
                            mArticleImage1.setImageBitmap(compressedBitmap);
                            // mArticleImage1.setImageBitmap(decoded);

                            //   base64Image1 = getStringImage(bmp);
                            //  newsPic.add(base64Image1);
                            filePaths.add(mediaPath);

                        } else if (currentImageView == 2) {
                            mArticleImage2.setBackgroundResource(0);
                            mArticleImage2.setImageBitmap(compressedBitmap);
                            // mArticleImage2.setImageBitmap(decoded);
                            // base64Image2 = getStringImage(bmp);
                            // newsPic.add(base64Image2);
                            filePaths.add(mediaPath);
                        }*//*
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            img_sel_err.setText("");
            imageSelected = 0;
        }
    }

    private void resizeImage(Uri selectedImage) {
        InputStream imageStream = null;
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Bitmap compressedBitmap = BitmapFactory.decodeFile(mediaPath, options);
            //imageView.setImageBitmap(bm);
            imageStream = getContentResolver().openInputStream(selectedImage);
            // Bitmap selected = BitmapFactory.decodeStream(imageStream);
            compressedBitmap = getResizedBitmap(compressedBitmap, 300);// 400 is for example, replace with desired size

            // imageView.setImageBitmap(selected);

            // Set the Image in ImageView for Previewing the Media
            if (currentImageView == 1) {
                mArticleImage1.setBackgroundResource(0);
                mArticleImage1.setImageBitmap(compressedBitmap);
                // mArticleImage1.setImageBitmap(decoded);

                //   base64Image1 = getStringImage(bmp);
                //  newsPic.add(base64Image1);
                filePaths.add(mediaPath);

            } else if (currentImageView == 2) {
                mArticleImage2.setBackgroundResource(0);
                mArticleImage2.setImageBitmap(compressedBitmap);
                // mArticleImage2.setImageBitmap(decoded);
                // base64Image2 = getStringImage(bmp);
                // newsPic.add(base64Image2);
                filePaths.add(mediaPath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean MaxSizeImage(String imagePath) {
        boolean temp = false;
        File file = new File(imagePath);
        long length = file.length();
        //Log.v("MaxSizeImage ", "length " + length);

        if (length < 5000000) // 5 mb
            temp = true;

        return temp;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private float getFileSize() {
        //  realUri = Uri.parse(mediaPath);
        //create file which we want to send to server.
        File imageFIle = new File(mediaPath);
        //Log.v("", "length " + imageFIle.length());

        float fileSizeInBytes = imageFIle.length();
        float fileSizeInKB = fileSizeInBytes / 1024;
        //Log.v("", "fileSizeInKB " + fileSizeInKB);
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        //String calString=Float.toString(fileSizeInMB);
        // String calString=Float.toString(fileSizeInMB);
        // int size = (int) fileSizeInMB;
        // Log.v("", "size " + size);
        return fileSizeInMB;
    }

    private String getPathFromURI(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        // fileSizeInBytes = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }

    private String getRealPathFromURI(Uri data) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap compressImageToMax(Bitmap image, int maxBytes) {
        int oldSize = image.getByteCount();

        // attempt to resize the image as much as possible while valid
        while (image != null && image.getByteCount() > maxBytes) {

            // Prevent image from becoming too small
            if (image.getWidth() <= 20 || image.getHeight() <= 20)
                return null;

            // scale down the image by a factor of 2
            image = Bitmap.createScaledBitmap(image, image.getWidth() / 2, image.getHeight() / 2, false);

            // the byte count did not change for some reason, can not be made any smaller
            if (image.getByteCount() == oldSize)
                return null;

            oldSize = image.getByteCount();
            //Log.v("compressImageToMax ", " oldSize " + oldSize);
        }

        return image;
    }

    public String getStringImage(Bitmap bmp) {
        Bitmap bp = Bitmap.createScaledBitmap(bmp, 500, 500, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bmp.compress(Bitmap.CompressFormat.PNG, 50, baos);
        bp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        //bmp.recycle();

        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        //Log.v("getStringImage ", " compressedSize " + decoded.getByteCount());


        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
/*mUri = data.getData();
                ImageCropFunction();*/
             /*else if (requestCode == REQUEST_CAMERA) {
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