package job.com.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import job.com.news.forgotpassword.ForgotPassword;
import job.com.news.interfaces.WebService;
import job.com.news.register.LoginRegisterResponse;
import job.com.news.sharedpref.MyPreferences;
import job.com.news.sharedpref.SessionManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        myPreferences = MyPreferences.getMyAppPref(this);
        session = new SessionManager(getApplicationContext());
        langSelection = new SessionManager(getApplicationContext());


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
            startActivity(intent);
            finish();
        }
    }
    private void setLocaleLang() {

        String[] lang_arr = getResources().getStringArray(R.array.language_arr);
        String getLang = langSelection.getLanguage();
        Log.v("LoginActivity ","getLang "+getLang);
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
        }
    }

    private void sendlogin(String username, String password) {
        try {

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("loading...");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebService webService = retrofit.create(WebService.class);

            RequestBody bodyUsername = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody bodyPwd = RequestBody.create(MediaType.parse("text/plain"), password);
            RequestBody bodyNoti = RequestBody.create(MediaType.parse("text/plain"), "cadscasd");
            RequestBody bodyPlat = RequestBody.create(MediaType.parse("text/plain"), 1 + "");

            Call<LoginRegisterResponse> serverResponse = webService.loginRequest(bodyUsername, bodyPwd, bodyNoti, bodyPlat);

            serverResponse.enqueue(new Callback<LoginRegisterResponse>() {
                @Override
                public void onResponse(Call<LoginRegisterResponse> call, Response<LoginRegisterResponse> response) {
                    progressDialog.dismiss();

                    if (response.isSuccessful()) {
                        LoginRegisterResponse serverResponse = response.body();
                        if (serverResponse.getStatus() == 0) {
                            myPreferences.setFirstName(serverResponse.getMember().getFirstName().trim());
                            myPreferences.setLastName(serverResponse.getMember().getLastName().trim());
                            myPreferences.setEmailId(serverResponse.getMember().getEmailId().trim());
                            myPreferences.setMobile(serverResponse.getMember().getMobile());
                            myPreferences.setMemberId(serverResponse.getMember().getMemberId());
                            myPreferences.setMemberToken(serverResponse.getMember().getMemberToken().trim());

                            System.out.println(
                                    " email : " + myPreferences.getEmailId().trim() + " mob : " + myPreferences.getMobile() + " memberId : "
                                            + myPreferences.getMemberId() + " Member : " + myPreferences.getMemberToken());

                            //register success
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            session.setLogin(true);
                        } else {
                            //login failed
                            String desc = serverResponse.getDescription().trim();
                            Toast.makeText(LoginActivity.this, "Login failed." + desc, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginRegisterResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "e : " + e.toString());
        }
    }


}
