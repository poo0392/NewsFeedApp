package com.org.apnanews.globals;

import com.org.apnanews.models.PayUTransactionDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by POOJA on 1/15/2018.
 */

public class Globals {
    public static String selectedLanguage = null;
    public static int back_press_screen=0;
    public static boolean checkSync=false;
    public static List<PayUTransactionDetailsModel.ResultList> paymentDetails = new ArrayList<>();
}
