package com.example.chungmin.helpu.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.serverrequest.AppStatsManager;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.AppStats;


public class AppStatsSummary extends HelpUBaseActivity {

    private TextView tvTtlCustReqtDay, tvTtlCustReqtWeek, tvTtlCustReqtMonth,
            tvTtlServPdrDay, tvTtlServPdrWeek, tvTtlServPdrMonth,
            tvTtlCustReqDoneDay, tvTtlCustReqDoneWeek, tvTtlCustReqDoneMonth,
            tvTtlQuotationDay, tvTtlQuotationWeek, tvTtlQuotationMonth,
            tvTtlUserDay, tvTtlUserWeek, tvTtlUserMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_stats_summary);

        tvTtlCustReqtDay = (TextView) findViewById(R.id.tvTtlCustReqtDay);
        tvTtlCustReqtWeek = (TextView) findViewById(R.id.tvTtlCustReqtWeek);
        tvTtlCustReqtMonth = (TextView) findViewById(R.id.tvTtlCustReqtMonth);
        tvTtlServPdrDay = (TextView) findViewById(R.id.tvTtlServPdrDay);
        tvTtlServPdrWeek = (TextView) findViewById(R.id.tvTtlServPdrWeek);
        tvTtlServPdrMonth = (TextView) findViewById(R.id.tvTtlServPdrMonth);
        tvTtlCustReqDoneDay = (TextView) findViewById(R.id.tvTtlCustReqDoneDay);
        tvTtlCustReqDoneWeek = (TextView) findViewById(R.id.tvTtlCustReqDoneWeek);
        tvTtlCustReqDoneMonth = (TextView) findViewById(R.id.tvTtlCustReqDoneMonth);
        tvTtlQuotationDay = (TextView) findViewById(R.id.tvTtlQuotationDay);
        tvTtlQuotationWeek = (TextView) findViewById(R.id.tvTtlQuotationWeek);
        tvTtlQuotationMonth = (TextView) findViewById(R.id.tvTtlQuotationMonth);
        tvTtlUserDay = (TextView) findViewById(R.id.tvTtlUserDay);
        tvTtlUserWeek = (TextView) findViewById(R.id.tvTtlUserWeek);
        tvTtlUserMonth = (TextView) findViewById(R.id.tvTtlUserMonth);

        AppStatsManager.getAppStats(new Callback.GetAppStatsCallback() {
            @Override
            public void complete(AppStats returnedAppStats) {
                tvTtlCustReqtDay.setText(Integer.toString(returnedAppStats.getTotalCustomerRequestInOneDay()));
                tvTtlCustReqtWeek.setText(Integer.toString(returnedAppStats.getTotalCustomerRequestInOneWeek()));
                tvTtlCustReqtMonth.setText(Integer.toString(returnedAppStats.getTotalCustomerRequestInOneMonth()));
                tvTtlServPdrDay.setText(Integer.toString(returnedAppStats.getTotalServiceProviderInOneDay()));
                tvTtlServPdrWeek.setText(Integer.toString(returnedAppStats.getTotalServiceProviderInOneWeek()));
                tvTtlServPdrMonth.setText(Integer.toString(returnedAppStats.getTotalServiceProviderInOneMonth()));
                tvTtlCustReqDoneDay.setText(Integer.toString(returnedAppStats.getTotalCustomerRequestDoneInOneDay()));
                tvTtlCustReqDoneWeek.setText(Integer.toString(returnedAppStats.getTotalCustomerRequestDoneInOneWeek()));
                tvTtlCustReqDoneMonth.setText(Integer.toString(returnedAppStats.getTotalCustomerRequestDoneInOneMonth()));
                tvTtlQuotationDay.setText(Double.toString(returnedAppStats.getTotalQuotationInOneDay()));
                tvTtlQuotationWeek.setText(Double.toString(returnedAppStats.getTotalQuotationInOneWeek()));
                tvTtlQuotationMonth.setText(Double.toString(returnedAppStats.getTotalQuotationInOneMonth()));
                tvTtlUserDay.setText(Integer.toString(returnedAppStats.getTotalUserInOneDay()));
                tvTtlUserWeek.setText(Integer.toString(returnedAppStats.getTotalUserInOneWeek()));
                tvTtlUserMonth.setText(Integer.toString(returnedAppStats.getTotalUserInOneMonth()));
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showAlert(msg);
            }
        });

    }


}
