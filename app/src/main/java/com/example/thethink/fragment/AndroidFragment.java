package com.example.thethink.fragment;

/*
 *  描述：    美女社区
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.thethink.R;
import com.example.thethink.adapter.GridAdapter;
import com.example.thethink.entity.AndroidData;
import com.example.thethink.ui.WebViewActivity;
import com.example.thethink.utils.L;
import com.example.thethink.view.CustomDialog;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AndroidFragment extends Fragment {

    //列表
    private ListView mListView;
    //数据
    private List<AndroidData> mList = new ArrayList<>();
    //适配器
    private GridAdapter mAdapter;
    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView iv_img;
    //PhotoView
    private PhotoViewAttacher mAttacher;
    //标题
    private List<String> mListTitle = new ArrayList<>();
    //地址
    private List<String> mListUrl = new ArrayList<>();

    /**
     * 1.监听点击事件
     * 2.提示框
     * 3.加载图片
     * 4.PhotoView
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_android, null);
        findView(view);
        return view;
    }

    //初始化View
    private void findView(View view) {

        mListView = view.findViewById(R.id.mListView);

        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_girl,
                R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        iv_img = (ImageView) dialog.findViewById(R.id.iv_img);

        String welfare = null;
        //解析
        RxVolley.get("http://gank.io/api/search/query/listview/category/"+"Android"+"/count/50/page/1", new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("Android Json:" + t);
                parsingJson(t);
            }
        });

        //监听点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("position:" + position);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", mListTitle.get(position));
                intent.putExtra("url", mListUrl.get(position));
                startActivity(intent);
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String desc = json.getString("desc");
                String who = json.getString("who");
                String url = json.getString("url");
                mListUrl.add(url);
                mListTitle.add(desc);
                AndroidData data = new AndroidData();
                data.setDesc(desc);
                data.setWho(who);
                data.setUrl(url);
                mList.add(data);
            }
            mAdapter = new GridAdapter(getActivity(), mList);
            //设置适配器
            mListView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
