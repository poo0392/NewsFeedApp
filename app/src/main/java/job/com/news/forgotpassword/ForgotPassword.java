package job.com.news.forgotpassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import job.com.news.Constant;
import job.com.news.HomeActivity;
import job.com.news.LoginActivity;
import job.com.news.R;
import job.com.news.interfaces.WebService;
import job.com.news.register.LoginRegisterResponse;
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

        } catch (Exception e){
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
                    if(response.isSuccessful()){
                        ForgotPasswordResp serverResponse = response.body();
                        if(serverResponse.getStatus() == 0){
                            //register success
                            String desc = serverResponse.getDescription().trim();
                            Toast.makeText(ForgotPassword.this, desc , Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            //login failed
                            String desc = serverResponse.getDescription().trim();
                            Toast.makeText(ForgotPassword.this, desc , Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ForgotPasswordResp> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
