package job.com.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import job.com.news.globals.Globals;
import job.com.news.sharedpref.MyPreferences;

/**
 * Created by Zafar.Hussain on 28/03/2018.
 */

public class ProfileFragment extends Fragment {
    Toolbar toolbar;
    CircleImageView profile_image;
    CardView cardView_details;
    EditText edt_first_name,edt_last_name,edt_phone,edt_email;
    ImageView iv_email_verified;
    MyPreferences myPreferences;
    String emailId,firstName,lastName,phoneNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.profile_toolbar_title));
        Globals.back_press_screen=1;
        getPreferenceData();
        attachViews(view);
        return view;

    }

    private void getPreferenceData() {
        myPreferences = MyPreferences.getMyAppPref(getActivity());
        emailId = myPreferences.getEmailId();
        firstName = myPreferences.getFirstName();
        lastName=myPreferences.getLastName();
        phoneNum=myPreferences.getMobile();
    }

    private void attachViews(View view) {
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        cardView_details=(CardView)view.findViewById(R.id.cardView_details);
        edt_first_name=(EditText)view.findViewById(R.id.edt_first_name);
        edt_last_name=(EditText)view.findViewById(R.id.edt_last_name);
        edt_phone=(EditText)view.findViewById(R.id.edt_phone);
        edt_email=(EditText)view.findViewById(R.id.edt_email);
        iv_email_verified=(ImageView)view.findViewById(R.id.iv_email_verified);


        edt_first_name.setText(firstName);
        edt_last_name.setText(lastName);
        edt_phone.setText(phoneNum);
        edt_email.setText(emailId);
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
