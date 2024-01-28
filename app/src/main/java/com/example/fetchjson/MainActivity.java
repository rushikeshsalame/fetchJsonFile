package com.example.fetchjson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
{
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;
    private ViewGroup mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUIRef();

        new MyJSONTask().execute();
    }

    private void setUIRef()
    {
        mLoading = findViewById(R.id.myLoadingLayout);

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);

        ArrayList<Employee> employees = new ArrayList<>();

        mMyRecyclerViewAdapter = new MyRecyclerViewAdapter(employees);

        recyclerView.setAdapter(mMyRecyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private class MyJSONTask extends AsyncTask<Void, Void, String>
    {
        //Web URL of the JSON file
        String jsonURL = "https://aamras.com/dummy/EmployeeDetails.json";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if (mLoading != null)
            {
                mLoading.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try
            {
                //---Loading JSON from the Web URL---//
                URL url = new URL(jsonURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line).append("\n");
                }
                if (stringBuffer.length() == 0)
                {
                    return null;
                } else
                {
                    return stringBuffer.toString();
                }
            } catch (IOException e)
            {
                return null;
            } finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String jsonStr)
        {
            super.onPostExecute(jsonStr);

            if (jsonStr != null)
            {
                //---Parsing JSON---//
                ArrayList<Employee> employeesList = new ArrayList<>();
                try
                {
                    JSONObject rootJsonObject = new JSONObject(jsonStr);

                    JSONArray employeeJsonArray = rootJsonObject.getJSONArray("employee");

                    for (int i = 0; i < employeeJsonArray.length(); i++)
                    {
                        //Create a temp employee object
                        Employee aEmployee = new Employee();

                        JSONObject jsonObject = employeeJsonArray.getJSONObject(i);

                        //Get employee details
                        aEmployee.setName(jsonObject.getString("name"));
                        aEmployee.setAge(jsonObject.getInt("age"));
                        aEmployee.setSalary(jsonObject.getInt("salary"));

                        //add to list
                        employeesList.add(aEmployee);
                    }

                    if (employeesList.size() > 0)
                    {
                        //Replace RecyclerView Adapter Data
                        mMyRecyclerViewAdapter.updateData(employeesList);
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            if (mLoading != null)
            {
                mLoading.setVisibility(View.GONE);
            }
        }
    }
}