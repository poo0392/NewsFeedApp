package job.com.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;

import job.com.news.adapter.ImageAdapter;

/**
 * Created by POOJA on 1/28/2018.
 */

public class NewsFeedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    private NewsFeedApplication newsFeedApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newsFeedApplication = NewsFeedApplication.getApp();
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        setData();


        /*addNewsFeedItems();
        mAdapter = new HomeDashboardAdapter(HomeActivity.this, mNewsFeedList);
        mRecyclerView.setAdapter(mAdapter);*/
        adapter = new ImageAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
    }

    private void setData() {

        ArrayList list = new ArrayList();
        list.add("White man shouts 'go back to Lebanon' to Sikh-American girl");
        list.add("NEW YORK: A Sikh-American girl was harassed on a subway train here when a white man, mistaking her to be from the Middle East, allegedly shouted \"go back to Lebanon\" and \"you don't belong in this country,\" the latest in a series of hate crimes against people of South-Asian origin.\n" +
                "Rajpreet Heir was taking the subway train to a friend's birthday party in Manhattan this month when the white man began shouting at her, according to a report in the New York Times.\n" +
                "Heir recounted the ordeal in a video for a Times section called 'This Week in Hate', which highlights hate crimes and harassment around the country since the election of President Donald Trump.\n" +
                "Heir said she was looking at her phone when the white man shouted at her saying, \"Do you even know what a Marine looks like? Do you know what they have to see? What they do for this country? Because of people like you.\"");
        list.add("20 March 2017");


        newsFeedApplication.hashMap.put(0 + "", list);

        list = new ArrayList();
        list.add("'We are here to stay', say Indian-Americans amid growing hate crime incidents in the US");
        list.add("WASHINGTON: \"We are here to stay\", Indian-Americans have vowed while holding a series of meetings to express their concern over growing hate crime incidents against ethnic and religious minorities in the US.\n" +
                "\"No matter what gunmen or the President (Donald Trump) say, this is our country, we are here to stay, and we will keep demanding our rightful and equal place in this quintessential nation of immigrants,\" said Suman Raghunathan from the South Asian Americans leading Together (SAALT) at a town hall discussion here on Friday.\n" +
                "Initiated by SAALT, South Asian groups are planning to organise a number of similar town halls across the country.\n" +
                "Prominent community leaders who addressed the town hall were Arjun Sethi of the Georgetown University Law Center, Dr Revathi Vikram of ASHA for Women, Shabab Ahmed Mirza of KhushDC, Darakshan Raja of Washington Peace Center and Kathy Doan of the Capital Area Immigrants' Rights Coalition.");
        list.add("21 March 2017");

        newsFeedApplication.hashMap.put(1 + "", list);

        list = new ArrayList();
        list.add("Development of all, appeasement of none: Yogi's mantra for UP");
        list.add("NEW DELHI: In his first public speech as the Uttar Pradesh chief minister, Yogi Adityanath said on Saturday that his government will work for the development of all+ and there will be no discrimination on the grounds of religion, caste or gender in the state.\n" +
                "\"We will follow \"sabka saath, sabka vikas\" mantra of PM Modi,\" Yogi Adityanath said, adding that there will be no attempt to \"appease\" any section+ of the population.\n" +
                "\"We will show UP how a government should be run. How it should treat a common man,\" the UP CM said while addressing a huge gathering at Gorakhpur's Maharana Pratap Inter College.\n" +
                "Yogi Adityanath is on his maiden two-day tour to Gorakhpur after becoming the chief minister.\n" +
                "He also claimed that the newly formed BJP government will fulfill all promises made in the election manifesto.");
        list.add("22 March 2017");

        newsFeedApplication.hashMap.put(2 + "", list);

        list = new ArrayList();
        list.add("India to seal border with Pakistan by 2018: Rajnath Singh");
        list.add("NEW DELHI: Union home minister Rajnath Singh on Saturday said India plans to seal international boundaries+ with neighbouring countries Pakistan and Bangladesh soon.\n" +
                "\"India is planning to seal the international boundaries with Pakistan and Bangladesh+ as soon as possible. This could be India's major step against terrorism and the problem of refugees,\" Singh said while addressing the passing out parade of the Border Security Force Assistant Commandants at the BSF Academy in Madhya Pradesh's Tekanpur area.");
        list.add("23 March 2017");

        newsFeedApplication.hashMap.put(3 + "", list);

        list = new ArrayList();
        list.add("India v Australia: Shane Warne tips come in handy for Kuldeep Yadav");
        list.add("DHARAMSALA: Newcomer Kuldeep Yadav on Saturday revealed he had learnt some tricks of the trade from none other than Aussie spin legend Shane Warne, after scripting a memorable debut against Australia in Dharamsala.\n" +
                "The chinaman bowler had his rivals in a spin, taking four wickets for 68 runs to help India bowl out the Aussies for 300 on the opening day of the decisive fourth Test in Dharamsala.");
        list.add("24 March 2017");

        newsFeedApplication.hashMap.put(4 + "", list);

    }
}
