package job.com.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.WebService;
import job.com.news.models.PayUTransactionDetailsModel;
import job.com.news.payU.PayUPnPActivity;
import job.com.news.sharedpref.MyPreferences;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static job.com.news.globals.Globals.paymentDetails;

public class MainActivity extends AppCompatActivity {
    MyPreferences getPref;
    String membertoken;
    int memberid, status;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //  StrictMode.setThreadPolicy(policy);

        getPref = MyPreferences.getMyAppPref(this);
        status=getIntent().getExtras().getInt("status");
        Log.v("MainActivity ","status "+status);
        memberid = getPref.getMemberId();
        membertoken = getPref.getMemberToken().trim();
        callPostPayuRecordAPI();

    }

    private void callPostPayuRecordAPI() {
        LinkedHashMap mapValuesFinal = new LinkedHashMap<>();
        Log.v("Calling ", "postPaymentStatusAPI");
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)//MILLISECONDS
                .readTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(new ConnectivityInterceptor(getApplicationContext()))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebService webService = retrofit.create(WebService.class);
        Log.v(" ", "paymentDetails " + paymentDetails.toString());
        PayUTransactionDetailsModel.ResultList results = new PayUTransactionDetailsModel.ResultList();

        String paymentId = paymentDetails.get(0).getPaymentId();
        String Status = paymentDetails.get(0).getStatus();
        String Key = paymentDetails.get(0).getKey();
        String Txnid = paymentDetails.get(0).getTxnid();
        String Amount = paymentDetails.get(0).getAmount();
        String Payment_date = paymentDetails.get(0).getPayment_date();
        String Productinfo = paymentDetails.get(0).getProductinfo();
        String Transaction_message = paymentDetails.get(0).getTransaction_message();
        String Bankcode = paymentDetails.get(0).getBankcode();
        String Error = paymentDetails.get(0).getError();
        String Error_Message = paymentDetails.get(0).getError_Message();


        RequestBody pMemberToken = RequestBody.create(MediaType.parse("text/plain"), membertoken);
        RequestBody pMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberid);
        RequestBody pPaymentId = RequestBody.create(MediaType.parse("text/plain"), paymentId);
        RequestBody pStatus = RequestBody.create(MediaType.parse("text/plain"), Status);
        RequestBody pKey = RequestBody.create(MediaType.parse("text/plain"), Key);
        RequestBody pTxnid = RequestBody.create(MediaType.parse("text/plain"), Txnid);
        RequestBody pAmount = RequestBody.create(MediaType.parse("text/plain"), Amount);
        RequestBody pPayment_date = RequestBody.create(MediaType.parse("text/plain"), Payment_date);
        RequestBody pProductinfo = RequestBody.create(MediaType.parse("text/plain"), Productinfo);
        RequestBody pTransaction_message = RequestBody.create(MediaType.parse("text/plain"), Transaction_message);
        RequestBody pBankcode = RequestBody.create(MediaType.parse("text/plain"), Bankcode);
        RequestBody pError = RequestBody.create(MediaType.parse("text/plain"), Error);
        RequestBody pError_Message = RequestBody.create(MediaType.parse("text/plain"), Error_Message);


        mapValuesFinal.put("member_token", membertoken);
        mapValuesFinal.put("member_id", String.valueOf(memberid));
        mapValuesFinal.put("payment_id", paymentId);
        mapValuesFinal.put("status", Status);
        mapValuesFinal.put("txtkey", Key);
        mapValuesFinal.put("txnid", Txnid);
        mapValuesFinal.put("amount", Amount);
        mapValuesFinal.put("payment_date", Payment_date);
        mapValuesFinal.put("product_info", Productinfo);
        mapValuesFinal.put("transaction_message", Transaction_message);
        mapValuesFinal.put("bank_code", Bankcode);
        mapValuesFinal.put("error", Error);
        mapValuesFinal.put("error_msg", Error_Message);

        Log.v("postPaymentStatus ", "reqParams " + mapValuesFinal.toString());

      /*  Call<PayUTransactionDetailsModel> serverResponse = webService.postPaymentDetails(pMemberToken, pMemberId,
                pPaymentId, pTransaction_message, pStatus, pKey, pTxnid, pAmount, pProductinfo, pPayment_date, pBankcode,
                pError, pError_Message);*/

        try {
            Call<PayUTransactionDetailsModel> serverResponse = webService.postPaymentDetails(
                    pMemberToken, pMemberId, pPaymentId, pTransaction_message, pStatus, pKey, pTxnid,
                    pAmount, pProductinfo, pPayment_date, pBankcode, pError, pError_Message);


           /* Response<PayUTransactionDetailsModel> response = serverResponse.execute();
            if (response.isSuccessful()) {

                  *//*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*//*

                PayUTransactionDetailsModel result = response.body();

                if (result.getStatus().equals("0")) {

                    Log.v("PostPaymentdetails ", "status " + result.getStatus());

                 *//*   Intent i = new Intent(MainActivity.this, PayUPnPActivity.class);
                    i.putExtra("post_status", result.getStatus());
                    //  i.putExtras(results);
                    setResult(Activity.RESULT_OK, i);
                    finish();*//*
                        *//*if (status == 1) {

                        } else {

                        }*//*
                    //   showSuccessAlertDialog(CreateArticle.this, "Success", "Your article created succesfully\n Proceeding for payment");
                }
            }*/


            serverResponse.enqueue(new Callback<PayUTransactionDetailsModel>() {
                @Override
                public void onResponse(Call<PayUTransactionDetailsModel> call, Response<PayUTransactionDetailsModel> response) {
                    mProgressDialog.dismiss();

                    if (response.isSuccessful()) {

                  /*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*/

                        PayUTransactionDetailsModel serverResponse = response.body();

                        if (serverResponse.getStatus().equals("0")) {

                            Log.v("PostPaymentdetails ", "status " + serverResponse.getStatus());

                      /*
                        */
                            if (status == 1) {
                                Intent i = new Intent(MainActivity.this, PayUPnPActivity.class);
                                i.putExtra("post_status", "1");
                                //  i.putExtras(results);
                                setResult(Activity.RESULT_OK, i);
                                finish();
                            } else {
                                Intent i = new Intent(MainActivity.this, PayUPnPActivity.class);
                                i.putExtra("post_status", "0");
                                //  i.putExtras(results);
                                setResult(Activity.RESULT_OK, i);
                                finish();
                            }
                            //   showSuccessAlertDialog(CreateArticle.this, "Success", "Your article created succesfully\n Proceeding for payment");
                            // }
                        }
                    }
                }


                @Override
                public void onFailure(Call<PayUTransactionDetailsModel> call, Throwable t) {
                    mProgressDialog.dismiss();
                    t.printStackTrace();
                    Log.v("PostPaymentdetails ", "Failure " + t.getMessage());
                    //Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                    if (t instanceof NoConnectivityException) {
                        // No internet connection
                        //    Toast.makeText(Cr.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                        // setFailedAlertDialog(HomeActivity.this, "Failed", "No Internet! Please Check Your internet connection");
                    }
                }
            });

        }/* catch (IOException e) {
            // handle error
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
        } */ catch (Exception e) {
            e.printStackTrace();
        }
    }
}

      /*  serverResponse.enqueue(new Callback<PayUTransactionDetailsModel>() {
            @Override
            public void onResponse(Call<PayUTransactionDetailsModel> call, Response<PayUTransactionDetailsModel> response) {
                //  mProgressDialog.dismiss();

                if (response.isSuccessful()) {

                  *//*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*//*

                    PayUTransactionDetailsModel serverResponse = response.body();

                    if (serverResponse.getStatus().equals("0")) {

                        Log.v("PostPaymentdetails ", "status " + serverResponse.getStatus());

                        Intent i = new Intent(MainActivity.this, PayUPnPActivity.class);
                        i.putExtra("post_status", serverResponse.getStatus());
                        //  i.putExtras(results);
                        setResult(Activity.RESULT_OK, i);
                        finish();
                        *//*if (status == 1) {

                        } else {

                        }*//*
                        //   showSuccessAlertDialog(CreateArticle.this, "Success", "Your article created succesfully\n Proceeding for payment");
                    }
                }
            }

            @Override
            public void onFailure(Call<PayUTransactionDetailsModel> call, Throwable t) {
                //  mProgressDialog.dismiss();
                t.printStackTrace();
                Log.v("PostPaymentdetails ", "Failure " + t.getMessage());
                //Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                if (t instanceof NoConnectivityException) {
                    // No internet connection
                    //    Toast.makeText(Cr.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                    // setFailedAlertDialog(HomeActivity.this, "Failed", "No Internet! Please Check Your internet connection");
                }
            }
        });*/



