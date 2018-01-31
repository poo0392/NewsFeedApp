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
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import job.com.news.payU.PayUActivity;
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
    private String mProjectKey;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    private HashMap<String, Integer> mapState;

    private ArrayList<HashMap<String, String>> stateList;
    private TextView mTotalChargesView, mDateView;
    private RadioGroup radioGroupDays;
    private String mRsSymbol;
    private String mArticleCode;
    private ImageView mArticleImage1, mArticleImage2;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String mediaPath;
    private int currentImageView = 0;
    private Button btn_submit;
    private int charges;
    String state_arr[], article_arr[];
    ArrayAdapter<String> state_adapter, article_adapter;
    RelativeLayout state_relative, city_relative, article_relative;
    BetterSpinner bsStateSpinner,bsCitySpinner,bsArticleSpinner;

    //Indonesia


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        mProjectKey = getString(R.string.whiz_project_key);
        //  mProjectKey="0q4LozhupoQlXKFFiqtRHAZIJpdaHcObOO6gEKa6uuhQS4t1n33PutMsyyAm";
        mContext = this;

        setAppToolbar();

        updateViews("hi_IN");
        initializeComponents();
        setListeners();
        openLangKb();
        setDateToText();


//        String languageToLoad  = "hi_IN"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());


        loadStateApi();

        radioGroupDays = (RadioGroup) findViewById(R.id.article_radio_days);
        radioGroupDays.getCheckedRadioButtonId();

        mArticleImage1 = (ImageView) findViewById(R.id.article_image1);
        mArticleImage1.setOnClickListener(this);

        mArticleImage2 = (ImageView) findViewById(R.id.article_image2);
        mArticleImage2.setOnClickListener(this);

        mTotalChargesView = (TextView) findViewById(R.id.article_total_charges_value);

    }

    private void setDateToText() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String strDate = sdf.format(date);
        mDateView.setText(strDate);
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
        getSupportActionBar().setTitle("Create Article");
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

        state_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, state_arr);
        state_relative = (RelativeLayout) findViewById(R.id.article_state_spinner);
        mStateSpinner = (Spinner) state_relative.findViewById(R.id.spinner);

        city_relative = (RelativeLayout) findViewById(R.id.article_city_spinner);
        mCitySpinner = (Spinner) city_relative.findViewById(R.id.spinner);

        article_relative = (RelativeLayout) findViewById(R.id.article_article_type_spinner);
        mArticleSpinner = (Spinner) article_relative.findViewById(R.id.spinner);

        mTitleEdit = (EditText) findViewById(R.id.article_title);
        mDescEdit = (EditText) findViewById(R.id.article_description);

        mDateView = (TextView) findViewById(R.id.article_date_value);

        bsStateSpinner =(BetterSpinner)findViewById(R.id.state_better_spinner);
        bsCitySpinner =(BetterSpinner)findViewById(R.id.city_better_spinner);
        bsArticleSpinner =(BetterSpinner)findViewById(R.id.article_better_spinner);
    }

    private void setListeners() {
        btn_submit.setOnClickListener(this);
        article_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, article_arr);
        bsArticleSpinner.setAdapter(article_adapter);// changed mArticleSpinner to bsArticleSpinner
        bsArticleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mArticleCode = "m" + position;
                Toast.makeText(CreateArticle.this, mArticleCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDescEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int count = charSequence.toString().trim().length();
                calculateCharges(count);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();
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

    private void calculateCharges(int count) {
        try {
            int charLength = 0;
            mTotalChargesView.setText(count + "");
            if (count > 0 && count <= 100) {
                charLength = 1;
            } else if (count > 101 && count <= 200) {
                charLength = 2;
            } else if (count > 201 && count <= 300) {
                charLength = 3;
            } else if (count > 301 && count <= 400) {
                charLength = 4;
            } else {
                charLength = 5;
            }

            int days = getDays();

            charges = charLength * days;

            Log.d("Core", "charges :" + charges);

            mTotalChargesView.setText(mRsSymbol + " " + charges);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDays() {
        int days = 0;
        int radioId = radioGroupDays.getCheckedRadioButtonId();
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
            default:
                days = 0;
                break;
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
                    mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
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
                        Toast.makeText(mContext,"No Internet",Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }catch (Exception e) {
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


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
            //mStateSpinner.setAdapter(adapter);
            bsStateSpinner.setAdapter(adapter); //changed from mStateSpinner to bsStateSpinner

            bsStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RequestBody bodyStateId = RequestBody.create(MediaType.parse("text/plain"), stateId + "");


            WebService webService = retrofit.create(WebService.class);
            Call<City> call = webService.getCityList(stateId);
            call.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                    mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
            bsCitySpinner.setAdapter(adapter);// change mCitySpinner to bsCitySpinner
            bsCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedCity = adapterView.getSelectedItem().toString().trim();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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

                Intent intent = new Intent(this, PayUActivity.class);
                intent.putExtra("Price", charges);
                startActivity(intent);
                finish();
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
