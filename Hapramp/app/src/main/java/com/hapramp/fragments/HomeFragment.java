package com.hapramp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.PostsRecyclerAdapter;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.logger.L;
import com.hapramp.models.response.PostResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PostFetchCallback {

    @BindView(R.id.homeRv)
    RecyclerView homeRv;
    Unbinder unbinder;
    private PostsRecyclerAdapter recyclerAdapter;
    private Context mContext;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new PostsRecyclerAdapter(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeRv.setLayoutManager(new LinearLayoutManager(mContext));
        homeRv.setAdapter(recyclerAdapter);
        fetchPosts();
    }

    private void fetchPosts(){
        DataServer.getPosts(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPostFetched(List<PostResponse> postResponses) {
        recyclerAdapter.setPostResponses(postResponses);
    }

    @Override
    public void onPostFetchError() {
        L.D.m("HomeFragment","Fetch Error: Post");
    }

}
