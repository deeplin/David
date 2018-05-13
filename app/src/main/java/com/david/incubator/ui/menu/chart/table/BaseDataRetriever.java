package com.david.incubator.ui.menu.chart.table;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * filename: com.eternal.davidconsole.ui.table.ScaleDataRetriever.java
 * author: Ling Lin
 * created on: 2017/7/31 16:55
 * email: 10525677@qq.com
 * description:
 */

public abstract class BaseDataRetriever<T> implements Consumer {

    private int rowSize;
    protected List<String> headList;

    protected long currentId;
    private boolean lastRecord;

    protected int rowId;

    private volatile Consumer<List<String>> consumer;

    public BaseDataRetriever() {
        rowSize = 10;
        headList = new ArrayList<>();

        currentId = Long.MAX_VALUE / 2; //防止溢出;
        lastRecord = false;

        buildHead();
    }

    protected void buildHead() {
        headList.add(ResourceUtil.getString(R.string.time));
        headList.add(getSensorTitle());
    }

    protected abstract String getSensorTitle();

    protected abstract String getValue(T command);

    public int getRowSize() {
        return rowSize;
    }

    public int getColoredValue() {
        return 1;
    }

    public void goPrevious() {
        currentId += rowSize;
        Observable.just(this).observeOn(Schedulers.io()).subscribe(this);
    }

    public void goNext() {
        if (!lastRecord) {
            currentId -= rowSize;
            Observable.just(this).observeOn(Schedulers.io()).subscribe(this);
        }
    }

    public List<String> getHeadList() {
        return headList;
    }

    public void setConsumer(Consumer<List<String>> consumer) {
        this.consumer = consumer;
        if (consumer != null) {
            currentId = Long.MAX_VALUE / 2;
        }
    }

    protected abstract List<T> getList(int rowSize, long currentId);

    protected abstract void addRecord(List<String> record, T analogCommand);

    @Override
    public void accept(final Object o) throws Exception {
        if (consumer == null)
            return;

        final List<T> list = getList(rowSize, currentId);
        if (list.size() <= 0) {
            /*没有数据*/
            currentId += rowSize;
            return;
        }

        lastRecord = false;
        rowId = 1;

        Observable.create((ObservableOnSubscribe<List<T>>) e -> {
            e.onNext(list);
            e.onComplete();
        })
                .flatMap(Observable::fromIterable)
                .map(command -> {
                    List<String> record = new ArrayList<>();
                    record.add(String.valueOf(rowId));
                    addRecord(record, command);
                    rowId++;
                    return record;
                }).subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> recordList) {
                Observable.just(recordList)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e);
            }

            @Override
            public void onComplete() {
                /*填写空白行*/
                if (rowId < rowSize + 1) {
                    lastRecord = true;
                    for (; rowId <= rowSize; rowId++) {
                        List<String> record = new ArrayList<>();
                        record.add(String.valueOf(rowId));
                        for (int columnIndex = 0; columnIndex < headList.size(); columnIndex++) {
                            record.add("--");
                        }
                        Observable.just(record)
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
                    }
                }
            }
        });
    }
}
