package job.com.news.payU;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import job.com.news.Constant;
import job.com.news.HomeActivity;
import job.com.news.NewsFeedApplication;
import job.com.news.R;
import job.com.news.db.MemberTable;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.WebService;
import job.com.news.models.PayUTransactionDetailsModel;
import job.com.news.register.RegisterMember;
import job.com.news.sharedpref.MyPreferences;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zafar.Hussain on 10/04/2018.
 */

public class PayUPnPActivity extends AppCompatActivity {
    private static final String TAG = "PayUPnPActivity";
    private int GET_PAYMENT_STATUS = 2;
    MyPreferences getPref;
    Context context;
    String emailID, mobileNo, productInfo, first_name, mRsSymbol, membertoken;
    int amountPref, memberid;
    MemberTable memberTable;
    Button payNowButton;
    TextView amountTextView;
    private RadioGroup radioGroup_select_env;
    private SharedPreferences.Editor editor;
    private SharedPreferences settings;
    AppCompatRadioButton radio_btn_sandbox, radio_btn_production;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    List<RegisterMember> membersList;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payu_home);
        context = this;
        setAppToolbar();
        memberTable = new MemberTable(context);

        getPrefData();
        getDbData();
        attachViews();
        initListeners();
        setUpUserDetails();

    }

    private void getDbData() {
        membersList = new ArrayList<>();
        membersList.addAll(memberTable.getMemberListByMemberId(memberid));
    }

    private void getPrefData() {
        getPref = MyPreferences.getMyAppPref(context);
        settings = getSharedPreferences("settings", MODE_PRIVATE);
        getPref.setOverrideResultScreen(true);
        MyPreferences.selectedTheme = -1;


        getPref = MyPreferences.getMyAppPref(this);
        memberid = getPref.getMemberId();
        membertoken = getPref.getMemberToken().trim();

        Log.v("getPrefData ", "memberid " + memberid);


    }

    private void setUpUserDetails() {


        amountPref = getIntent().getIntExtra("Price", 0);
        //  Log.v("PayUAct", "amountPref " + amountPref);
        mRsSymbol = getResources().getString(R.string.Rs);
        //emailID = "pooja130192@gmail.com";
        emailID = membersList.get(0).getEmailId();
        //mobileNo = "8600700392";
        mobileNo = membersList.get(0).getMobile();
        productInfo = "product_info";
        //first_name = "pooja";
        first_name = membersList.get(0).getFirstName();
        amountTextView.setText(mRsSymbol + " " + String.valueOf(amountPref));
    }

    private void initListeners() {
        /*radioGroup_select_env.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_btn_sandbox:
                        selectSandBoxEnv();
                        break;
                    case R.id.radio_btn_production:
                        selectProdEnv();
                        break;
                    default:
                        selectSandBoxEnv();
                        break;
                }
            }
        });*/
        selectProdEnv();
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payNowButton.setEnabled(false);
                launchPayUMoneyFlow();
            }
        });
    }

    private void launchPayUMoneyFlow() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        double amount = 0;
        try {
            amount = amountPref;

        } catch (Exception e) {
            e.printStackTrace();
        }

        String txnId = System.currentTimeMillis() + "";
        String phone = mobileNo;
        String productName = productInfo;
        String firstName = first_name;
        String email = emailID;
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((NewsFeedApplication) getApplication()).getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
            * Hash should always be generated from your server side.
            * */
            generateHashFromServer(mPaymentParams);

/*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
           /* mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            payNowButton.setEnabled(true);
        }
    }

    private void selectProdEnv() {

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ((NewsFeedApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
                editor = settings.edit();
                editor.putBoolean("is_prod_env", true);
                editor.apply();

               /* if (PayUmoneyFlowManager.isUserLoggedIn(getApplicationContext())) {
                    logoutBtn.setVisibility(View.VISIBLE);
                } else {
                    logoutBtn.setVisibility(View.GONE);
                }*/

                setupCitrusConfigs();
            }
        }, MyPreferences.MENU_DELAY);
    }

    private void attachViews() {


        payNowButton = (Button) findViewById(R.id.pay_now_button);
        amountTextView = (TextView) findViewById(R.id.txt_amount);
        radio_btn_sandbox = (AppCompatRadioButton) findViewById(R.id.radio_btn_sandbox);
        radio_btn_production = (AppCompatRadioButton) findViewById(R.id.radio_btn_production);
        radioGroup_select_env = (RadioGroup) findViewById(R.id.radio_grp_env);
        // payNowButton.setOnClickListener(this);

        if (settings.getBoolean("is_prod_env", false)) {
            ((NewsFeedApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
            radio_btn_production.setChecked(true);
        } else {
            ((NewsFeedApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
            radio_btn_sandbox.setChecked(true);
        }
    }

    private void setAppToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("PayU Home");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void selectSandBoxEnv() {
        ((NewsFeedApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
        editor = settings.edit();
        editor.putBoolean("is_prod_env", false);
        editor.apply();

      /*  if (PayUmoneyFlowManager.isUserLoggedIn(getApplicationContext())) {
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            logoutBtn.setVisibility(View.GONE);

        }*/
        setupCitrusConfigs();
    }

    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((NewsFeedApplication) getApplication()).getAppEnvironment();
        if (appEnvironment == AppEnvironment.PRODUCTION) {
            // Toast.makeText(PayUPnPActivity.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(PayUPnPActivity.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productInfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstName", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        Log.v("", "postParams " + postParams);
        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PayUPnPActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                //  URL url = new URL("https://payu.herokuapp.com/get_hash");
                URL url = new URL("http://thanehousingfederation.com/newsapp/moneyhash.php");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);
            Log.v("", "merchantHash " + merchantHash);
            progressDialog.dismiss();
            payNowButton.setEnabled(true);

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(PayUPnPActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);

                if (MyPreferences.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUPnPActivity.this, MyPreferences.selectedTheme, getPref.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUPnPActivity.this, R.style.AppTheme_default, getPref.isOverrideResultScreen());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                String payuResponse = transactionResponse.getPayuResponse();
                List<PayUTransactionDetailsModel.ResultList> paymentDetails = new ArrayList<>();
                PayUTransactionDetailsModel.ResultList results = new PayUTransactionDetailsModel.ResultList();
                try {
                    JSONObject result = (new JSONObject(payuResponse)).getJSONObject("result");
                    // JSONObject jsonObj = new JSONObject(payuResponse);
                    // obj=jsonObj;
                    Log.v("", "paymentDetails " + paymentDetails);
                    // obj = (Object)payuResponse;
                    // JSONArray jsonArr = new JSONArray("[" + payuResponse + "]");
                    // List<Data> dataList = new ArrayList<>();
                    //  for (int i = 0; i < jsonArr.length(); i++) {

                    //  JSONObject jsonObj = jsonArr.getJSONObject(i);
                    //Data data = new Data();

                    //model.setResult(result.getString(""));
                    results.setPaymentId(result.getString("paymentId"));
                    results.setStatus(result.getString("status"));
                    results.setKey(result.getString("key"));
                    results.setTxnid(result.getString("txnid"));
                    results.setAmount(result.getString("amount"));
                    results.setPayment_date(result.getString("addedon"));
                    results.setProductinfo(result.getString("productinfo"));
                    results.setTransaction_message(result.getString("field9"));
                    results.setBankcode(result.getString("bankcode"));
                    results.setError(result.getString("error"));
                    results.setError_Message(result.getString("error_Message"));

                    paymentDetails.add(results);
                    // result.
                    // paymentDetails.add(new Res);
                    Log.v("checkPayuResp ", "paymentDetails " + paymentDetails.toString());
                    //  }

          /*  JSONObject jsonObj = new JSONObject(payuResponse);
            //paymentDetails.addAll(jsonObj.get("result"));
            model.setResult(jsonObj.get("result"));*/
                    // obj=jsonObj;

                    //obj = (Object) payuResponse;
                    // paymentD.add(obj);
                    // model.setResult(obj);
                    //   Log.v("", "paymentDetails " + paymentDetails);
                    // Log.v("checkPayuResp ", "obj " + obj);


                    //  paymentDetails=transactionResponse.getPayuResponse();


                    //  paymentDetails=transactionResponse.getPayuResponse();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Log.v("", "Transaction Status" + " Success");
                    showStatusDialog("Success", "Transaction Completed");
                    postPaymentStatus(1);
                } else {
                    //Failure Transaction
                    Log.v("", "Transaction Status" + " Failed");
                    showStatusDialog("Failed", "Transaction Failed");
                    postPaymentStatus(0);
                }

              /*  // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();
                Log.v("Final ", "payuResponse " + payuResponse);
                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.v("Final ", "merchantResponse " + merchantResponse);
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //dialog.dismiss();
                               // Toast.makeText(getApplicationContext(),"Payment ")
                                Intent i = new Intent(PayUPnPActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).show();*/

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }

    private void postPaymentStatus(final int status) {
       /* mProgressDialog = new ProgressDialog(PayUPnPActivity.this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();*/
        OkHttpClient client = new OkHttpClient.Builder()
               /* .connectTimeout(0000, TimeUnit.MILLISECONDS)
                .readTimeout(600000, TimeUnit.MILLISECONDS)*/
                .addInterceptor(new ConnectivityInterceptor(getApplicationContext()))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebService webService = retrofit.create(WebService.class);
        PayUTransactionDetailsModel.ResultList results = new PayUTransactionDetailsModel.ResultList();
        String paymentId=results.getPaymentId();
        String Status=results.getStatus();
        String Key=results.getKey();
        String Txnid=results.getTxnid();
        String Amount=results.getAmount();
        String Payment_date=results.getPayment_date();
        String Productinfo=results.getProductinfo();
        String Transaction_message=results.getTransaction_message();
        String Bankcode=results.getBankcode();
        String Error=results.getError();
        String Error_Message=results.getError_Message();

        RequestBody pMemberToken = RequestBody.create(MediaType.parse("text/plain"), membertoken);
        RequestBody pMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberid);
        RequestBody pPaymentId = RequestBody.create(MediaType.parse("text/plain"), paymentId);
        RequestBody pStatus = RequestBody.create(MediaType.parse("text/plain"), Status);
        RequestBody pKey = RequestBody.create(MediaType.parse("text/plain"), Key);
        RequestBody pTxnid = RequestBody.create(MediaType.parse("text/plain"), Txnid);
        RequestBody pAmount = RequestBody.create(MediaType.parse("text/plain"),Amount);
        RequestBody pPayment_date = RequestBody.create(MediaType.parse("text/plain"), Payment_date);
        RequestBody pProductinfo = RequestBody.create(MediaType.parse("text/plain"),Productinfo);
        RequestBody pTransaction_message = RequestBody.create(MediaType.parse("text/plain"),Transaction_message);
        RequestBody pBankcode = RequestBody.create(MediaType.parse("text/plain"),Bankcode);
        RequestBody pError = RequestBody.create(MediaType.parse("text/plain"),Error);
        RequestBody pError_Message = RequestBody.create(MediaType.parse("text/plain"),Error_Message);


        Call<PayUTransactionDetailsModel> serverResponse = webService.postPaymentDetails(pMemberToken, pMemberId,
                pPaymentId, pTransaction_message, pStatus, pKey, pTxnid, pAmount, pProductinfo, pPayment_date, pBankcode,
                pError,pError_Message);


        serverResponse.enqueue(new Callback<PayUTransactionDetailsModel>() {
            @Override
            public void onResponse(Call<PayUTransactionDetailsModel> call, Response<PayUTransactionDetailsModel> response) {
              //  mProgressDialog.dismiss();

                if (response.isSuccessful()) {

                  /*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*/

                    PayUTransactionDetailsModel serverResponse = response.body();

                    if (serverResponse.getStatus().equals("0")) {

                        Log.v("PostPaymentdetails ", "status " + serverResponse.getStatus());
                        if(status==1){

                        }else{

                        }
                     //   showSuccessAlertDialog(CreateArticle.this, "Success", "Your article created succesfully\n Proceeding for payment");
                    }
                }
            }

            @Override
            public void onFailure(Call<PayUTransactionDetailsModel> call, Throwable t) {
              // mProgressDialog.dismiss();
                t.printStackTrace();
                Log.v("PostPaymentdetails ", "Failure " + t.getMessage());
                //Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                if (t instanceof NoConnectivityException) {
                    // No internet connection
                    Toast.makeText(PayUPnPActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                    // setFailedAlertDialog(HomeActivity.this, "Failed", "No Internet! Please Check Your internet connection");
                }
            }
        });

    }

    private void showStatusDialog(String title, String desc) {
        final String status = title;
        MaterialStyledDialog.Builder dialog = new MaterialStyledDialog.Builder(context);
        dialog.setTitle(title)
                .setDescription(desc)
                .setStyle(Style.HEADER_WITH_ICON);

        if (title.equals("Success")) {
            dialog.setIcon(R.mipmap.ic_success);

        } else {
            dialog.setIcon(R.mipmap.ic_failed);
            dialog.setPositiveText(R.string.button_ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent i = new Intent(PayUPnPActivity.this, HomeActivity.class);
                            startActivity(i);
                            i.putExtra("status", status);
                            setResult(GET_PAYMENT_STATUS, i);
                            finish();
                        }
                    });
        }

        dialog.show();
    }
}
