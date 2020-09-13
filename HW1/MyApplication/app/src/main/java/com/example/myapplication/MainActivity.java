package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.expression.Expression;
import com.example.myapplication.parser.ExpressionParser;

public class MainActivity extends AppCompatActivity {

    private static TextView inputTV;
    private static TextView outputTV;

    private static TableLayout buttonTable;
    private static TableRow[] rows;
    private static Button[][] buttons;

    private static StringBuilder mainExpression = new StringBuilder();
    private static Double result = 0.0D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inputTV = findViewById(R.id.inputET);
        outputTV = findViewById(R.id.outputET);

        inputTV.setMovementMethod(new ScrollingMovementMethod());
        outputTV.setMovementMethod(new ScrollingMovementMethod());

        outputTV.setText("0.0");

        buttonTable = findViewById(R.id.buttonTable);

        rows = new TableRow[5];
        buttons = new Button[5][4];

        TableLayout.LayoutParams buttonRowParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        buttonParams.setMargins(20, 20, 20, 20);

        for (int i = 0; i < 5; i++) {
            rows[i] = new TableRow(MainActivity.this);
            rows[i].setLayoutParams(buttonRowParams);
            buttonTable.addView(rows[i]);
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new Button(MainActivity.this);
                buttons[i][j].setLayoutParams(buttonParams);
                buttons[i][j].setBackgroundColor(Color.WHITE);
                buttons[i][j].setTextSize(20);
                buttons[i][j].setOnClickListener(baseClickListener);
                rows[i].addView(buttons[i][j]);
            }
        }

        buttons[0][0].setText("C");
        buttons[0][0].setOnClickListener(clearAllClickListener);
        buttons[0][0].setOnLongClickListener(clearAllLongClickListener);
        buttons[0][0].setBackgroundColor(Color.RED);

        buttons[0][1].setText("CE");
        buttons[0][1].setOnClickListener(clearClickListener);
        buttons[0][1].setBackgroundColor(Color.RED);

        buttons[0][2].setText("(");

        buttons[0][3].setText(")");

        for (int i = 1; i < 10; i++)
            buttons[1 + (i - 1) / 3][(i - 1) % 3].setText(String.valueOf(i));

        buttons[4][1].setText(String.valueOf(0));

        buttons[4][0].setText("=");
        buttons[4][0].setOnClickListener(evaluateClickListener);
        buttons[4][0].setBackgroundColor(Color.rgb(0xff, 0xa5, 0x00));

        buttons[4][2].setText(".");

        buttons[1][3].setText("+");

        buttons[2][3].setText("-");

        buttons[3][3].setText("*");

        buttons[4][3].setText("/");

        outputTV.setOnLongClickListener(outputClickListener);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateInput();
        updateOutput();
    }

    private static void updateOutput() {
        outputTV.setText(String.valueOf(result));
    }

    private static void updateInput() {
        inputTV.setText(mainExpression.toString());
    }

    private final Button.OnClickListener baseClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainExpression.append(((Button) v).getText());
            updateInput();
        }
    };

    private final Button.OnClickListener evaluateClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            ExpressionParser parser = new ExpressionParser();
            Pair<Expression, Boolean> exp = parser.parse(mainExpression.toString());
            if (exp.first == null || !exp.second) {
                Toast.makeText(MainActivity.this, R.string.incorrect_input_en, Toast.LENGTH_LONG).show();
            } else {
                result = exp.first.evaluate();
                updateOutput();
            }
        }
    };

    private final Button.OnLongClickListener outputClickListener = new Button.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", ((TextView) v).getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(MainActivity.this, R.string.text_copied_en, Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    private final Button.OnClickListener clearClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mainExpression.length() > 0)
                mainExpression.deleteCharAt(mainExpression.length() - 1);
            updateInput();
        }
    };

    private final Button.OnClickListener clearAllClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainExpression = new StringBuilder();
            updateInput();
        }
    };

    private final Button.OnLongClickListener clearAllLongClickListener = new Button.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            clearAllClickListener.onClick(v);
            result = 0.0D;
            updateOutput();
            return false;
        }
    };

}
