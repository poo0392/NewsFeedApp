package com.org.apnanews.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja.Patil on 24/04/2018.
 */

public class PayUTransactionDetailsModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    /*@SerializedName("result")
    @Expose
    private List<ResultList> result;*/
    @SerializedName("result")
    @Expose
    private ResultList result;

    public ResultList getResult() {
        return result;
    }

    public void setResult(ResultList result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

   /* public List<ResultList> getResult() {
        return result;
    }

    public void setResult(List<ResultList> result) {
        this.result = result;



    }*/

    public static class ResultList {

        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("paymentId")
        @Expose
        private String paymentId;
        @SerializedName("productinfo")
        @Expose
        private String productinfo;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("txnid")
        @Expose
        private String txnid;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("addedon")
        @Expose
        private String payment_date;
        @SerializedName("bankcode")
        @Expose
        private String bankcode;
        @SerializedName("error")
        @Expose
        private String error;
        @SerializedName("error_Message")
        @Expose
        private String error_Message;
        @SerializedName("field9")
        @Expose
        private String transaction_message;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getProductinfo() {
            return productinfo;
        }

        public void setProductinfo(String productinfo) {
            this.productinfo = productinfo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getTxnid() {
            return txnid;
        }

        public void setTxnid(String txnid) {
            this.txnid = txnid;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPayment_date() {
            return payment_date;
        }

        public void setPayment_date(String payment_date) {
            this.payment_date = payment_date;
        }

        public String getBankcode() {
            return bankcode;
        }

        public void setBankcode(String bankcode) {
            this.bankcode = bankcode;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError_Message() {
            return error_Message;
        }

        public void setError_Message(String error_Message) {
            this.error_Message = error_Message;
        }

        public String getTransaction_message() {
            return transaction_message;
        }

        public void setTransaction_message(String transaction_message) {
            this.transaction_message = transaction_message;
        }

        @Override
        public String toString() {
            return "ResultList{" +
                    "firstname='" + firstname + '\'' +
                    ", paymentId='" + paymentId + '\'' +
                    ", productinfo='" + productinfo + '\'' +
                    ", status='" + status + '\'' +
                    ", key='" + key + '\'' +
                    ", txnid='" + txnid + '\'' +
                    ", amount='" + amount + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    ", payment_date='" + payment_date + '\'' +
                    ", bankcode='" + bankcode + '\'' +
                    ", error='" + error + '\'' +
                    ", error_Message='" + error_Message + '\'' +
                    ", transaction_message='" + transaction_message + '\'' +
                    '}';
        }
    }




}
