package com.org.apnanews;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.apnanews.globals.Globals;

/**
 * Created by Pooja.Patil on 19/04/2018.
 */

public class AboutUsFragment extends Fragment {

    Context mContext;
    Toolbar toolbar;
    TextView txtAppVersion, txtAppDesc, txtAppFeatures, txtCopyright;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        mContext = getActivity();
        setHasOptionsMenu(true);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.about_toolbar_title));
        Globals.back_press_screen=1;
        attachViews(view);
        getPackageDetails();
        setData();
        return view;

    }

    private void setData() {
        txtAppDesc.setText(mContext.getResources().getString(R.string.app_desc));
        txtCopyright.setText("Copyright" + "\u00a9" + "2018" + "\n" + "Sabhi App Tech");

        txtAppFeatures.setText("\u2022" + "   " + "Multiple language news." + "\n" +
                "\u2022" + "   " + "Post your news in just one click." + "\n" +
                "\u2022" + "   " + "Latest News dashboard." + "\n" +
                "\u2022" + "   " + "Post news status.");
        txtAppFeatures.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        txtAppFeatures.setTextSize(14);
    }

    private void getPackageDetails() {
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        txtAppVersion.setText("App Version " + version);
    }

    private void attachViews(View view) {
        txtAppVersion = (TextView) view.findViewById(R.id.txt_app_version);
        txtAppDesc = (TextView) view.findViewById(R.id.txt_app_desc);
        txtAppFeatures = (TextView) view.findViewById(R.id.txt_features);
        txtCopyright = (TextView) view.findViewById(R.id.txt_copyright);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_create_article).setVisible(false);
        menu.findItem(R.id.action_change_language).setVisible(false);
        menu.findItem(R.id.action_change_pwd).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
