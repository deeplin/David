package com.david.incubator.ui.menu.chart.table;

/**
 * filename: com.eternal.davidconsole.ui.table.PageTurnTable.java
 * author: Ling Lin
 * created on: 2017/7/31 16:56
 * email: 10525677@qq.com
 * description: * PageTurnHScrollTable
 * 带分页功能的可滚动列数据表格
 * <p>
 * ProgressDialog 正在显示时若改变设备方向，会导致出错退出...目前只能在 AndroidManifest.xml 中设置
 * screen 保持特定方向(portrait/landscape)
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.david.databinding.LayoutPageTurnTableBinding;
import com.david.R;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class PageTurnTable extends FrameLayout implements Consumer<List<String>> {

    LayoutPageTurnTableBinding binding;
    BaseDataRetriever baseDataRetriever;
    LayoutInflater layoutInflater;

    private int rowSize;

    public PageTurnTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 从 Layout XML文件中加载控件
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = LayoutPageTurnTableBinding.inflate(layoutInflater, this, true);
        binding.setViewModel(this);
    }

    public void initialize(BaseDataRetriever baseDataRetriever) {
        this.baseDataRetriever = baseDataRetriever;
        rowSize = baseDataRetriever.getRowSize();

        binding.tableLayout.removeAllViews();
        setTableStructure();
    }

    public void start() {
        baseDataRetriever.setConsumer(this);
        baseDataRetriever.goNext();
    }

    public void stop() {
        baseDataRetriever.setConsumer(null);
    }

    public void goNext() {
        baseDataRetriever.goNext();
    }

    public void goPrevious() {
        baseDataRetriever.goPrevious();
    }

    private void setTableStructure() {
        for (int rowIndex = 0; rowIndex < rowSize + 1; rowIndex++) {
            TableLayout.LayoutParams rowLayoutParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            TableRow tableRow = (TableRow) layoutInflater.inflate(R.layout.tablerow_table_row, null);
            tableRow.setLayoutParams(rowLayoutParams);
            binding.tableLayout.addView(tableRow);

            if(rowIndex == 0){
                List<String> headList = baseDataRetriever.getHeadList();
                for(int index = 0; index < baseDataRetriever.getHeadList().size(); index ++){
                    String headString = headList.get(index);

                    // 填充固定列表头
                    TextView head = (TextView) layoutInflater.inflate(R.layout.textview_table_head, null);
                    head.setText(headString);

                    int weight = index == 0 && baseDataRetriever.getHeadList().size() > 2 ? 2 : 1;
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                            0, TableRow.LayoutParams.WRAP_CONTENT, weight);

                    head.setLayoutParams(layoutParams);
                    tableRow.setBackgroundResource(R.color.background);
                    tableRow.addView(head);
                }
            }else {
                if (rowIndex % 2 == 0) {
                    tableRow.setBackgroundResource(R.color.border);
                }
                for (int columnIndex = 0; columnIndex < baseDataRetriever.getHeadList().size(); columnIndex++) {
                    int weight = columnIndex == 0 && baseDataRetriever.getHeadList().size() > 2 ? 2 : 1;
                    TableRow.LayoutParams cellLayoutParams = new TableRow.LayoutParams(
                            0, TableLayout.LayoutParams.WRAP_CONTENT, weight);
                    TextView cell = (TextView) layoutInflater.inflate(R.layout.textview_table_head, null);
                    cell.setLayoutParams(cellLayoutParams);
                    if (columnIndex >= baseDataRetriever.getColoredValue()) {
                        cell.setTextColor(ContextCompat.getColor(getContext(), R.color.info));
                    }
                    tableRow.addView(cell);
                }
            }
        }
    }

    /*
    * 异步写写表格
    * rowList: 0 行号, rowId: 1-6
    * */
    @Override
    public void accept(@NonNull List<String> rowList){
        int rowId = Integer.parseInt(rowList.get(0));
        TableRow tableRow = (TableRow) binding.tableLayout.getChildAt(rowId);

        for (int index = 1; index < rowList.size(); index++) {
            TextView cell = (TextView) tableRow.getChildAt(index - 1);
            cell.setText(rowList.get(index));
        }
    }
}