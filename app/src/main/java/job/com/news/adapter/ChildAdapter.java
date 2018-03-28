package job.com.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import job.com.news.R;

/**
 * Created by Zafar.Hussain on 28/03/2018.
 */

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    Context context;
    private HashMap<String, List<String>> listDataChild;
    String key;


    public ChildAdapter(Context context, HashMap<String, List<String>> listThirdLevelChild, String key) {
        this.context = context;
        this.listDataChild = listThirdLevelChild;
        this.key = key;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_child_item, viewGroup, false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.child_list_item.setText(listDataChild.get(key).get(position));
    }

    @Override
    public int getItemCount() {
        return listDataChild.get(key).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView child_list_item;
        public ViewHolder(View itemView) {
            super(itemView);
            child_list_item=(TextView)itemView.findViewById(R.id.child_list_item);
        }
    }
}
