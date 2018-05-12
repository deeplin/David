package com.david.incubator.ui.setting;

import android.content.Context;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.david.R;
import com.david.common.ui.TabConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.LayoutSettingLoginBinding;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:26
 * email: 10525677@qq.com
 * description:
 */

public class SettingLoginLayout extends TabConstraintLayout<LayoutSettingLoginBinding> implements View.OnClickListener {

    ObservableInt navigationView;

    private StringBuilder password;

    public SettingLoginLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;

        password = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            TableRow tableRow = getTableRow(row);
            binding.loginKeyboard.addView(tableRow);
        }
    }

    private TableRow getTableRow(int row) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        TableRow tableRow = (TableRow) layoutInflater.inflate(R.layout.tablerow_table_row, null);

        for (int column = 0; column < 3; column++) {
            Button button = (Button) layoutInflater.inflate(R.layout.button_login, tableRow, false);
            int value = (row * 3 + column + 1) % 10;
            button.setText(String.valueOf(value));
            button.setTag(value);
            button.setOnClickListener(this);
            tableRow.addView(button);
        }

        switch (row) {
            case (0):
                ImageButton deleteButton = (ImageButton) layoutInflater.inflate(R.layout.image_button_login, tableRow, false);
                deleteButton.setImageDrawable(getResources().getDrawable(R.mipmap.login_delete));
                deleteButton.setTag(10);
                deleteButton.setOnClickListener(this);
                tableRow.addView(deleteButton);
                break;
            case (1):
                ImageButton enterButton = (ImageButton) layoutInflater.inflate(R.layout.image_button_login, tableRow, false);
                enterButton.setImageDrawable(getResources().getDrawable(R.mipmap.login_enter));
                enterButton.setTag(11);
                enterButton.setOnClickListener(this);
                tableRow.addView(enterButton);
                break;
            case (2):
                Button zeroButton = (Button) layoutInflater.inflate(R.layout.button_login, tableRow, false);
                zeroButton.setText(String.valueOf(0));
                zeroButton.setTag(0);
                zeroButton.setOnClickListener(this);
                tableRow.addView(zeroButton);
                break;
        }

        TableLayout.LayoutParams rowLayoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 0, 1);
        tableRow.setLayoutParams(rowLayoutParams);
        return tableRow;
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        if (tag < 10) {
            if (password.length() < Constant.SYSTEM_PASSWORD.length()) {
                password.append(String.valueOf(tag));
                displayMask();
            }
        } else if (tag == 10) {
            if (password.length() > 0) {
                password.deleteCharAt(password.length() - 1);
                displayMask();
            }
        } else {
            if (password.toString().equals(Constant.SYSTEM_PASSWORD)) {
                navigationView.set(FragmentPage.SYSTEM_HOME);
            } else if (password.toString().equals(Constant.USER_PASSWORD)) {
                navigationView.set(FragmentPage.USER_HOME);
            } else {
                password.delete(0, password.length());
                displayMask();
            }
        }
    }

    private void displayMask() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < password.length(); index++) {
            stringBuilder.append("* ");
        }
        binding.loginMask.setText(stringBuilder.toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setting_login;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }
}
