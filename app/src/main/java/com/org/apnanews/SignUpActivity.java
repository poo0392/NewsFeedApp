package com.org.apnanews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;

import com.org.apnanews.db.MemberTable;
import com.org.apnanews.db.PersonalDetails;
import com.org.apnanews.helper.ConnectivityInterceptor;
import com.org.apnanews.helper.NoConnectivityException;
import com.org.apnanews.interfaces.WebService;
import com.org.apnanews.register.LoginRegisterResponse;
import com.org.apnanews.register.RegisterMember;
import com.org.apnanews.sharedpref.MyPreferences;
import com.org.apnanews.sharedpref.SessionManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//chnages added on 3/9/2018.
public class SignUpActivity extends AppCompatActivity {

    private TextView mFNameView;
    private TextView mLNameView;
    private TextView mPasswordView;
    private TextView mConfirmPwd;
    private TextView mEmailView;
    private ImageView mProfileImage;
    private TextView mMobileView;
    private String mediaPath;

    private static final int SELECT_PHOTO = 100;
    private ProgressDialog progressDialog;
    private MyPreferences myPreferences;
    MemberTable memberTable;
    PersonalDetails pd;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        memberTable = new MemberTable(getApplicationContext());
        pd=new PersonalDetails(getApplicationContext());

        myPreferences = MyPreferences.getMyAppPref(this);
        session = new SessionManager(getApplicationContext());
        mFNameView = (EditText) findViewById(R.id.profile_first_name);
        mLNameView = (EditText) findViewById(R.id.profile_last_name);
        mPasswordView = (EditText) findViewById(R.id.profile_password);
        mEmailView = (EditText) findViewById(R.id.profile_email);
        mConfirmPwd = (EditText) findViewById(R.id.profile_confirm_password);
        mMobileView = (EditText) findViewById(R.id.profile_mobile);

        Button btnSignUp = (Button) findViewById(R.id.profile_sign_up_btn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
                //sendRegister("", "", "");
            }
        });

        TextView prof_login = (TextView) findViewById(R.id.profile_login);
        prof_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

    }

    private void attemptSignUp() {
        try {

            //Reset error
            mFNameView.setError(null);
            mLNameView.setError(null);
            mPasswordView.setError(null);
            mEmailView.setError(null);
            mConfirmPwd.setError(null);
            mMobileView.setError(null);

            // Store values at the time of the login attempt.
            String firstname = mFNameView.getText().toString().trim();
            String lastname = mLNameView.getText().toString().trim();
            String password = mPasswordView.getText().toString().trim();
            String email = mEmailView.getText().toString().trim();
            String confirmPwd = mConfirmPwd.getText().toString().trim();
            String mobile = mMobileView.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            //empty
            if (TextUtils.isEmpty(firstname)) {
                mFNameView.setError(getString(R.string.error_field_required));
                focusView = mFNameView;
                cancel = true;
            }

            if (TextUtils.isEmpty(lastname)) {
                mLNameView.setError(getString(R.string.error_field_required));
                focusView = mLNameView;
                cancel = true;
            }

            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.error_field_required));
                focusView = mPasswordView;
                cancel = true;
            }

            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }

            if (TextUtils.isEmpty(confirmPwd) || !password.equals(confirmPwd)) {
                mConfirmPwd.setError(getString(R.string.error_match_password));
                focusView = mConfirmPwd;
                cancel = true;
            }

            //Check for a valid mobile number.
            if (TextUtils.isEmpty(mobile) || isMobileNumberValid(mobile)) {
                mMobileView.setError(getString(R.string.error_invalid_mobile_no));
                focusView = mMobileView;
                cancel = true;
            }

            /*//Mobile
            if(mobile.length() < 10){
                mMobileView.setError(getString(R.string.error_invalid_mobile_no));
                focusView = mMobileView;
                cancel = true;
            }*/

            if (cancel) {
                focusView.requestFocus();
            } else {
               /* Toast.makeText(this, "Congrats !!! Signed up Successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();*/
                sendRegister(firstname, lastname, mobile, confirmPwd, email);
            }


        } catch (Exception e) {
            // e.printStackTrace();
            Log.d("Core", "e : " + e.toString());
        }
    }

    private void sendRegister(String first_name, String last_name, String mobile, String password, String email) {
        try {

            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("loading...");
            progressDialog.show();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(SignUpActivity.this))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebService webService = retrofit.create(WebService.class);

            //MultipartBody.Part body = null;
            /*if(mediaPath == null){
                body = null;
            } else {
                File file = new File(mediaPath);
                File compressedImageFile= Compressor.getDefault(SignUpActivity.this).compressToFile(file);
                // Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                body = MultipartBody.Part.createFormData("uploaded_file", compressedImageFile.getName(), requestFile);
            }*/

            RequestBody bodyFName = RequestBody.create(MediaType.parse("text/plain"), first_name);
            RequestBody bodyLName = RequestBody.create(MediaType.parse("text/plain"), last_name);
            RequestBody bodyMobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
            RequestBody bodyPwd = RequestBody.create(MediaType.parse("text/plain"), password);
            RequestBody bodyNoti = RequestBody.create(MediaType.parse("text/plain"), "cadscasd");
            RequestBody bodyPlat = RequestBody.create(MediaType.parse("text/plain"), 1 + "");
            RequestBody bodyEmail = RequestBody.create(MediaType.parse("text/plain"), email);

            Call<LoginRegisterResponse> responseCall = webService.registerRequest(bodyFName, bodyLName, bodyMobile, bodyPwd, bodyNoti,
                    bodyPlat, bodyEmail);
            responseCall.enqueue(new Callback<LoginRegisterResponse>() {
                @Override
                public void onResponse(Call<LoginRegisterResponse> call, Response<LoginRegisterResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        LoginRegisterResponse serverResponse = response.body();

                        if (serverResponse.getStatus() == 0) {
                            //register success
                            //Log.v("RegisterAPI ", "response " + new Gson().toJson(response.body()));
                            try {
                                myPreferences.setFirstName(serverResponse.getMember().getFirstName().trim());
                                myPreferences.setLastName(serverResponse.getMember().getLastName().trim());
                                myPreferences.setEmailId(serverResponse.getMember().getEmailId().trim());
                                myPreferences.setMobile(serverResponse.getMember().getMobile());
                                myPreferences.setMemberId(serverResponse.getMember().getMemberId());
                                myPreferences.setMemberToken(serverResponse.getMember().getMemberToken().trim());
                                if( serverResponse.getMember().getRole() == null ||serverResponse.getMember().getRole().isEmpty()){
                                    myPreferences.setRole("0");
                                }



                                //  Log.v("Response", "MemberId " + serverResponse.getMember().getMemberId());
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
                                Log.d("Core", "e :" + e.toString());
                            }
                            showSuccessAlertDialog(SignUpActivity.this, "Success", "You have Successfuly registered");
                        } else {
                            //register failed
                            String desc = serverResponse.getDescription().trim();
                            if (desc.equalsIgnoreCase("")) {

                                setFailedAlertDialog(SignUpActivity.this, "Failed", "Register failed. Please try again after some time.");
                                //Toast.makeText(SignUpActivity.this, "Register failed. Please try again after some time.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(SignUpActivity.this, "Register failed." + desc, Toast.LENGTH_SHORT).show();
                                setFailedAlertDialog(SignUpActivity.this, "Failed", desc);
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginRegisterResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    if (t instanceof NoConnectivityException) {
                        // No internet connection
                        // Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                        setFailedAlertDialog(SignUpActivity.this, "Failed", "No Internet! Please Check Your internet connection");
                    }
                }
            });

        } catch (Exception e) {

            Log.d("Core", "e : " + e.toString());
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
                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        session.setLogin(true);
                        dialog.dismiss();
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isMobileNumberValid(String mobile) {
        //TODO: Replace this with your own logic
        return mobile.length() < 10;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && null != data) {

            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);
            // Set the Image in ImageView for Previewing the Media
            //mProfileImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            mProfileImage.setImageURI(selectedImage);
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        session.setLogin(false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
