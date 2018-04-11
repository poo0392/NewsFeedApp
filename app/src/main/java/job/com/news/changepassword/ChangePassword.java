package job.com.news.changepassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import job.com.news.Constant;
import job.com.news.R;
import job.com.news.forgotpassword.ForgotPasswordResp;
import job.com.news.interfaces.WebService;
import job.com.news.sharedpref.MyPreferences;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePassword extends AppCompatActivity {

    private EditText mPwdView;
    private ProgressDialog progressDialog;
    private MyPreferences myPreferences;
    private int memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        myPreferences = MyPreferences.getMyAppPref(this);
        setAppToolbar();
        mPwdView = (EditText) findViewById(R.id.pwd_change_pwd);
        getPrefrenceData();
        Button mSubmitButton = (Button) findViewById(R.id.change_pwd_submit_btn);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSubmit();
            }
        });

    }

    private void setAppToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.change_pwd_menu));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void attemptSubmit() {
        try {

            // Reset errors.
            mPwdView.setError(null);

            String pwd = mPwdView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(pwd)) {
                mPwdView.setError(getString(R.string.error_field_required));
                focusView = mPwdView;
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

                sendForgotPwdReq(pwd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPrefrenceData() {
        //Load Data From SharedPrefernce in UserName & Passowrd
        try {
            memberId = myPreferences.getMemberId();
        } catch (Exception e) {
            Log.d("Core", "e :" + e.toString());
        }
    }

    private void sendForgotPwdReq(String pwd) {
        try {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebService webService = retrofit.create(WebService.class);

            System.out.println(" Change Id : " + myPreferences.getMemberId() + " Token" + myPreferences.getMemberToken());

            RequestBody bodyMemberId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(memberId));
            RequestBody bodyMemberToken = RequestBody.create(MediaType.parse("text/plain"), myPreferences.getMemberToken());
            RequestBody bodyPwd = RequestBody.create(MediaType.parse("text/plain"), pwd);

            Log.v("ChangePassword ", "bodyMemberId  : " + String.valueOf(memberId));
            Log.v("ChangePassword ", "bodyMemberToken  : " + myPreferences.getMemberToken());
            Log.v("ChangePassword ", "bodyPwd : " + pwd);

            Call<ForgotPasswordResp> serverResponse = webService.changePasswordRequest(bodyMemberId, bodyMemberToken, bodyPwd);
            Log.v("ChangePassword ", "LoginParameters url : " + serverResponse.request().url());
            serverResponse.enqueue(new Callback<ForgotPasswordResp>() {
                @Override
                public void onResponse(Call<ForgotPasswordResp> call, Response<ForgotPasswordResp> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        ForgotPasswordResp serverResponse = response.body();
                        if (serverResponse.getStatus() == 0) {
                            //register success
                            String desc = serverResponse.getDescription();
                            Toast.makeText(ChangePassword.this, "0", Toast.LENGTH_SHORT).show();
                            showSuccessAlertDialog(ChangePassword.this, "Success", "Your new password is updated succesfully");
                            //   finish();
                        } else {
                            //login failed
                            String desc = serverResponse.getDescription();
                            Log.v("ForgotPasswordResp ", "failed_desc" + desc);
                            Toast.makeText(ChangePassword.this, "1", Toast.LENGTH_SHORT).show();
                            // setFailedAlertDialog(ChangePassword.this, "Failed", "Invalid Credentials.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ForgotPasswordResp> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessAlertDialog(Context context, String title, String desc) {
        new MaterialStyledDialog.Builder(context)
                .setTitle(title)
                .setDescription(desc)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_success)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //password change success
                        finish();
                    }
                })
                .show();
    }

    private void setFailedAlertDialog(Context context, String title, String desc) {
        new MaterialStyledDialog.Builder(context)
                .setTitle(title)
                .setDescription(desc)
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
