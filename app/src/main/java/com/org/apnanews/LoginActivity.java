package com.org.apnanews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.org.apnanews.db.MemberTable;
import com.org.apnanews.db.PersonalDetails;
import com.org.apnanews.forgotpassword.ForgotPassword;
import com.org.apnanews.helper.ConnectivityInterceptor;
import com.org.apnanews.helper.NoConnectivityException;
import com.org.apnanews.interfaces.WebService;
import com.org.apnanews.models.NewsFeedList;
import com.org.apnanews.register.LoginRegisterResponse;
import com.org.apnanews.register.RegisterMember;
import com.org.apnanews.sharedpref.MyPreferences;
import com.org.apnanews.sharedpref.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    //chnages added on 3/9/2018.
    // UI references.
    private EditText mUsernameView, mPasswordView;
    private Button mSubmitBtn;
    private ProgressDialog progressDialog;
    private MyPreferences myPreferences;
    private String TAG = LoginActivity.class.getName();
    private Context context;
    private AlertDialog mdailog;
    private SessionManager session, langSelection;
    private Button mLoginInButton;
    private TextView sign_up, forgot_password;
    private List<NewsFeedList> newsFeedList = new ArrayList<>();
    MemberTable memberTable;
    PersonalDetails pd;
    MaterialDialog dialogMaterial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        myPreferences = MyPreferences.getMyAppPref(this);
        session = new SessionManager(getApplicationContext());
        langSelection = new SessionManager(getApplicationContext());
        memberTable = new MemberTable(LoginActivity.this);
        pd = new PersonalDetails(LoginActivity.this);

        checkForLoginSession();
        //setLocaleLang();
        initializeComponents();
        getPrefrenceData();
        setListeners();
    }

    private void getPrefrenceData() {
        //Load Data From SharedPrefernce in UserName & Passowrd
        try {
            mUsernameView.setText(myPreferences.getEmailId());
        } catch (Exception e) {
            Log.d("Core", "e :" + e.toString());
        }
    }

    private void setListeners() {
        mLoginInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void initializeComponents() {

        //survetej94@gmail.com
        //dearsheru@94


        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        mLoginInButton = (Button) findViewById(R.id.login_button);


        sign_up = (TextView) findViewById(R.id.sign_up);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
    }

    private void checkForLoginSession() {

        if (session.isLoggedIn()) {
            setLocaleLang();
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            //Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setLocaleLang() {

        String[] lang_arr = getResources().getStringArray(R.array.language_arr);
        String getLang = langSelection.getLanguage();
        //Log.v("LoginActivity ", "getLang " + getLang);
        Configuration config = new Configuration();
        Locale locale;
        if (getLang.equalsIgnoreCase(lang_arr[1])) {
            locale = new Locale("hi");
            Locale.setDefault(locale);
            config.setLocale(locale);
        } else if (getLang.equalsIgnoreCase(lang_arr[2])) {
            locale = new Locale("mr");
            Locale.setDefault(locale);
            config.setLocale(locale);
        }
        //getResources().getConfiguration().setTo(config);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        //mUsernameView.setText("tejas.surve@gmail.com");
        //mPasswordView.setText("admin123");
        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //Toast.makeText(this, "Congrats !!!", Toast.LENGTH_SHORT).show();

            /*Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();*/

            sendlogin(username, password);
          //  showSuccessAlertDialog(LoginActivity.this, "Success", "Logged In Successfully");
        }
    }

    private void sendlogin(String username, String password) {
        try {

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("loading...");
            progressDialog.show();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30000, TimeUnit.MILLISECONDS)
                    .readTimeout(30000, TimeUnit.MILLISECONDS)
                    .addInterceptor(new ConnectivityInterceptor(LoginActivity.this))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebService webService = retrofit.create(WebService.class);

            RequestBody bodyUsername = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody bodyPwd = RequestBody.create(MediaType.parse("text/plain"), password);
            RequestBody bodyNoti = RequestBody.create(MediaType.parse("text/plain"), "cadscasd");
            RequestBody bodyPlat = RequestBody.create(MediaType.parse("text/plain"), 1 + "");


            Call<LoginRegisterResponse> serverResponse = webService.loginRequest(bodyUsername, bodyPwd, bodyNoti, bodyPlat);
//            Log.v("sendlogin ","LoginParameters url : "+serverResponse.request().url());
//            Log.v("sendlogin ","LoginParameters : "+serverResponse.request().body().toString());
//            Log.v("sendlogin ","LoginParameters req : "+serverResponse.request().toString());
            String reqParam = bodyToString(serverResponse.request().body());
            //  Log.v("sendlogin ","reqParam : "+reqParam);
            serverResponse.enqueue(new Callback<LoginRegisterResponse>() {
                @Override
                public void onResponse(Call<LoginRegisterResponse> call, Response<LoginRegisterResponse> response) {
                    progressDialog.dismiss();
                    //  Log.v("LoginAPI ", "response " + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        LoginRegisterResponse serverResponse = response.body();
                        String serverrespose2 = new Gson().toJson(response.body());
                        //Log.v("LoginAPI ", "response " + serverrespose2);
                        if (serverResponse.getStatus() == 0) {

                            myPreferences.setFirstName(serverResponse.getMember().getFirstName().trim());
                            myPreferences.setLastName(serverResponse.getMember().getLastName().trim());
                            myPreferences.setEmailId(serverResponse.getMember().getEmailId().trim());
                            myPreferences.setMobile(serverResponse.getMember().getMobile());
                            myPreferences.setMemberId(serverResponse.getMember().getMemberId());
                            myPreferences.setMemberToken(serverResponse.getMember().getMemberToken().trim());
                            myPreferences.setRole(serverResponse.getMember().getRole());

                            System.out.println(
                                    " email : " + myPreferences.getEmailId().trim() + " mob : " + myPreferences.getMobile() + " memberId : "
                                            + "firstName: " + myPreferences.getFirstName().trim() +
                                            "lastName: " + myPreferences.getLastName().trim() +
                                            +myPreferences.getMemberId() + " Member : " + myPreferences.getMemberToken() +
                                            "Role: " + myPreferences.getRole());


                            //addMemmberToDb();


                            //  Log.v("Response", "MemberId " + serverResponse.getMember().getMemberId());
                            try {
                                if (!pd.checkUser(serverResponse.getMember().getMemberId())) {
                                    RegisterMember model = new RegisterMember();
                                    model.setMemberId(serverResponse.getMember().getMemberId());
                                    model.setMemberToken(serverResponse.getMember().getMemberToken().trim());
                                    model.setFirstName(serverResponse.getMember().getFirstName().trim());
                                    model.setLastName(serverResponse.getMember().getLastName().trim());
                                    model.setEmailId(serverResponse.getMember().getEmailId().trim());
                                    model.setMobile(serverResponse.getMember().getMobile());
                                    model.setRole(serverResponse.getMember().getRole());

                                    pd.insertMember(model);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           // Toast.makeText(LoginActivity.this,"Logged In Successfully", Toast.LENGTH_SHORT).show();
                            showSuccessAlertDialog(LoginActivity.this, "Success", "Logged In Successfully");

                        } else {
                            //login failed
                            //  String desc = serverResponse.getDescription().trim();
                            // Toast.makeText(LoginActivity.this, "Login failed." + desc, Toast.LENGTH_SHORT).show();
                            // Log.v(" sendlogin "," desc "+serverResponse.getDescription());
                            setFailedAlertDialog(LoginActivity.this, "Failed", "Invalid Credentials.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginRegisterResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.v(" onFailure ", " getMessage " + t.getMessage());
                    if (t instanceof NoConnectivityException) {
                        // No internet connection
                        // Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                        setFailedAlertDialog(LoginActivity.this, "Failed", "No Internet! Please Check Your internet connection");
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e : " + e.toString());
        }
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

    private void setFailedAlertDialog(Context context, String title, String desc) {
        new MaterialStyledDialog.Builder(context)
                .setTitle(title)
                .setDescription(desc)
                .setCancelable(false)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_failed)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showSuccessAlertDialog(final Context context, String title, String desc) {
       MaterialStyledDialog.Builder dialog= new MaterialStyledDialog.Builder(context);

        dialog.setTitle(title)
                .setDescription(desc)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_success)
                .setCancelable(false)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogMaterial=dialog;
                        //register success
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                       // Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        finish();

                        session.setLogin(true);

                    }
                })
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialogMaterial!=null){
            dialogMaterial.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        showBackPressAlertDialog(LoginActivity.this, "Confirm Please...", "Do you want to close the app ?");
    }

    private void showBackPressAlertDialog(Context context, String title, String desc) {
        new MaterialStyledDialog.Builder(context)
                .setTitle(title)
                .setDescription(desc)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_success)
                .setCancelable(false)
                .setPositiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        ActivityCompat.finishAffinity(LoginActivity.this);
                    }
                })
                .setNegativeText("No")
                .show();
    }


}
