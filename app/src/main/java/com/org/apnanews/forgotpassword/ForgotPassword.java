package com.org.apnanews.forgotpassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.org.apnanews.Constant;
import com.org.apnanews.R;
import com.org.apnanews.interfaces.WebService;
import com.org.apnanews.sharedpref.MyPreferences;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassword extends AppCompatActivity {

    private EditText mEmailView;
    private ProgressDialog progressDialog;
    private MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmailView = (EditText) findViewById(R.id.email_forgot_pwd);
        Button mSubmitButton = (Button) findViewById(R.id.forgot_submit_btn);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSubmit();
            }
        });

    }

    private void attemptSubmit() {
        try {

            // Reset errors.
            mEmailView.setError(null);

            String email = mEmailView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));

                focusView = mEmailView;
                cancel = true;
            } else if (!TextUtils.isEmpty(email) && !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                mEmailView.setError(getString(R.string.email_pattern_error));

                focusView = mEmailView;
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

                sendForgotPwdReq(email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendForgotPwdReq(String email) {
        try {

            progressDialog = new ProgressDialog(ForgotPassword.this);
            progressDialog.setMessage("loading...");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebService webService = retrofit.create(WebService.class);

            RequestBody bodyEmail = RequestBody.create(MediaType.parse("text/plain"), email);

            Call<ForgotPasswordResp> serverResponse = webService.forgotPasswordRequest(bodyEmail);

            serverResponse.enqueue(new Callback<ForgotPasswordResp>() {
                @Override
                public void onResponse(Call<ForgotPasswordResp> call, Response<ForgotPasswordResp> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        ForgotPasswordResp serverResponse = response.body();
                        if (serverResponse.getStatus() == 0) {
                            //register success
                            String desc = serverResponse.getDescription();
                           // Toast.makeText(ForgotPassword.this, desc, Toast.LENGTH_SHORT).show();
                            showSuccessAlertDialog(ForgotPassword.this, "Success", "Your new password is sent to your email id");

                        } else {
                            //login failed
                            String desc = serverResponse.getDescription();
                            //Log.v("", "desc : " + desc);
                          //  Toast.makeText(ForgotPassword.this, "failed", Toast.LENGTH_SHORT).show();
                            setFailedAlertDialog(ForgotPassword.this, "Failed", "Invalid Credentials.");
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
}
