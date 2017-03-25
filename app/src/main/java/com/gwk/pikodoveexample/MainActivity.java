package com.gwk.pikodoveexample;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gwk.pikodove.generator.PikoGenerator;
import com.gwk.pikodove.generator.PikoGeneratorBlueprint;
import com.gwk.pikodove.parser.PikoParser;
import com.gwk.pikodove.parser.PikoParserBlueprint;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnShowObjectRepresentation;
    private Button btnShowJsonRepresentation;
    private Button btnShowPikoRepresentation;
    private Button btnTest;
    private TextView tvResult;

    private User originalUser;
    private byte[] pikoRepresentation;
    private User parsedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        originalUser = new User();
        originalUser.setByDefaultValue();

        try {
            pikoRepresentation = PikoGenerator.fromClass(originalUser, new PikoGeneratorBlueprint(User.class));
            PikoParserBlueprint pikoParserBlueprint = new PikoParserBlueprint(User.class);
            parsedUser = (User) PikoParser.fromPiko(pikoRepresentation, pikoParserBlueprint);
        } catch (IllegalAccessException | InstantiationException | IOException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        btnShowObjectRepresentation = (Button) findViewById(R.id.btnShowObjectRepresentation);
        btnShowJsonRepresentation = (Button) findViewById(R.id.btnShowJsonRepresentation);
        btnShowPikoRepresentation = (Button) findViewById(R.id.btnShowPikoRepresentation);
        btnTest = (Button) findViewById(R.id.btnTest);
        tvResult = (TextView) findViewById(R.id.textViewResult);
        initListeners();
    }

    private void initListeners() {
        btnShowObjectRepresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setMessage(originalUser.toString()).show();
            }
        });
        btnShowJsonRepresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setMessage(originalUser.toJsonObject()).show();
            }
        });
        btnShowPikoRepresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setMessage(new String(pikoRepresentation)).show();
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // json
                int lengthJson = originalUser.toJsonObject().length();
                String result = "Json Data Length : " + lengthJson;
                long startTime = System.nanoTime();
                try {
                    new User().fromJsonObject(originalUser.toJsonObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                long endTime = System.nanoTime();
                long durationJson = (endTime - startTime) / 1000;
                result += "\nJson Data Parse Time : " + durationJson + " ms";

                // piko
                int lengthPiko = pikoRepresentation.length;
                result += "\nPiko Data Length : " + lengthPiko;
                PikoParserBlueprint pikoParserBlueprint = new PikoParserBlueprint(User.class);
                startTime = System.nanoTime();
                try {
                    parsedUser = (User) PikoParser.fromPiko(pikoRepresentation, pikoParserBlueprint);
                } catch (IllegalAccessException | IOException | NoSuchFieldException | InstantiationException e) {
                    e.printStackTrace();
                }
                endTime = System.nanoTime();
                long durationPiko = (endTime - startTime) / 1000;
                result += "\nPiko Data Parse Time : " + durationPiko + " ms";
                result += "\nLength percentage : " + (((float)(lengthJson - lengthPiko) / (float)lengthJson) * 100f) + "%";
                result += "\nDuration percentage : " + (((float)(durationJson - durationPiko) / (float)durationJson) * 100f) + "%";
                tvResult.setText(result);
            }
        });

    }


    String toBinary(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for( int i = 0; i < Byte.SIZE * bytes.length; i++ ) {
            if(i % 8 == 0 && i != 0) {
                sb.append(" ");
            }
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }
}
