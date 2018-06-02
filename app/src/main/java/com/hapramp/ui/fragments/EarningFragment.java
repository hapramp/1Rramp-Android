package com.hapramp.ui.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.steem.SteemPowerHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EarningFragment extends Fragment implements SteemPowerHelper.SteemPowerCallback {


    @BindView(R.id.steem_balance_label)
    TextView steemBalanceLabel;
    @BindView(R.id.steem_balance)
    TextView steemBalanceTv;
    @BindView(R.id.steemBalanceWrapper)
    RelativeLayout steemBalanceWrapper;
    Unbinder unbinder;
    @BindView(R.id.steem_power_label)
    TextView steemPowerLabel;
    @BindView(R.id.steem_power)
    TextView steemPowerTv;

    private Handler mHandler;

    public EarningFragment() {
        mHandler = new Handler();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earning, container, false);
        unbinder = ButterKnife.bind(this, view);
        requestInfo();
        return view;
    }

    private void requestInfo() {
        SteemPowerHelper.requestSteemUserEarningInfo(this);
    }

    //called on worker-thread. Any UI update should be performed via Handlers
    @Override
    public void onSteemPowerFetched(final float _steemPower) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (steemPowerTv != null) {
                    steemPowerTv.setText(String.format("%s STEEM", String.valueOf(_steemPower)));
                }
            }
        });
    }


    //called on worker-thread. Any UI update should be performed via Handlers
    @Override
    public void onSteemBalance(final float balance) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (steemBalanceTv != null) {
                    steemBalanceTv.setText(String.format("%s SBD", String.valueOf(balance)));
                }
            }
        });
    }


    //called on worker-thread. Any UI update should be performed via Handlers
    @Override
    public void onSteemPowerFetchFailed() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
