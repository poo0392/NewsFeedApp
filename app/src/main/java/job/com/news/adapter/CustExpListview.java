package job.com.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by POOJA on 3/4/2018.
 */

public class CustExpListview extends ExpandableListView {
    private Context context;

    int intGroupPosition, intChildPosition, intGroupid;


    public CustExpListview(Context context) {
        super(context);
        this.context = context;

    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,

                MeasureSpec.AT_MOST);

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

}


