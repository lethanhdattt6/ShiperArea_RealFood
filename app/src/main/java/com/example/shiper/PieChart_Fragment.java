package com.example.shiper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import java.util.ArrayList;
import java.util.List;

public class PieChart_Fragment extends Fragment {
    int danggiaoShipper = 0, daNhanDonHang = 0, dangGiaoHang = 0, giaoHangThanhCong = 0, giaoHangThatBai = 0;
    AnyChartView anyChartView;

    public PieChart_Fragment(int danggiaoShipper, int daNhanDonHang, int dangGiaoHang, int giaoHangThanhCong, int giaoHangThatBai) {
        this.danggiaoShipper = danggiaoShipper;
        this.daNhanDonHang = daNhanDonHang;
        this.dangGiaoHang = dangGiaoHang;
        this.giaoHangThanhCong = giaoHangThanhCong;
        this.giaoHangThatBai = giaoHangThatBai;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        anyChartView = view.findViewById(R.id.pieChart);
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Đang giao hàng ", dangGiaoHang));
        data.add(new ValueDataEntry("Giao hàng thành công", giaoHangThanhCong));
        data.add(new ValueDataEntry("Giao hàng thất bại", giaoHangThatBai));

        pie.data(data);
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Trạng thái đơn hàng")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView.setChart(pie);
        return view;
    }
}
