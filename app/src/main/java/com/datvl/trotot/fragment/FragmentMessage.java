package com.datvl.trotot.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.datvl.trotot.OnEventListener;
import com.datvl.trotot.R;
import com.datvl.trotot.adapter.ListMessageUserAdapter;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.model.MessageUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class FragmentMessage extends Fragment {

    RecyclerView recyclerView;
    List<MessageUser> listMessage;
    Common cm = new Common();
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    public String url;
    public String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_message,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_message_user_view);
        progressBar = view.findViewById(R.id.progressBarListMessage);

        sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        final String username = cm.getUsername(this.getActivity());
        url = cm.getListMessageUser() + username;

        listMessage = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        GetApi getApi = new GetApi(url, getActivity(), new OnEventListener() {
            @Override
            public void onSuccess(JSONArray object) {
                for (int i=0 ; i< object.length() ; i++){
                    try {
                        JSONObject jsonObject = object.getJSONObject(i);
                        listMessage.add(new MessageUser(
                                Integer.parseInt(jsonObject.getString("id")),
                                username.equals(jsonObject.getString("username_second")) ? jsonObject.getString("username_first") : jsonObject.getString("username_second"),
                                jsonObject.getString("created_at_string")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fillter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                progressBar.setVisibility(View.GONE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ListMessageUserAdapter viewAdapter = new ListMessageUserAdapter(listMessage);
                recyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        return view;
    }
}
