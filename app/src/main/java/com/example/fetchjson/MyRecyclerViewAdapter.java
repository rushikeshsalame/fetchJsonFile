package com.example.fetchjson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>
{
    private ArrayList<Employee> mEmployeeList = new ArrayList<>();

    public MyRecyclerViewAdapter(ArrayList<Employee> mEmployeeList)
    {
        this.mEmployeeList = mEmployeeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View employeeRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(employeeRow);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        String name = "Name: " + mEmployeeList.get(position).getName();
        holder.nameTextView.setText(name);

        String ageStr = "Age: " + mEmployeeList.get(position).getAge();
        holder.ageTextView.setText(ageStr);

        String salary = "Salary: " + mEmployeeList.get(position).getSalary();
        holder.salaryTextView.setText(salary);
    }

    @Override
    public int getItemCount()
    {
        return mEmployeeList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public void updateData(ArrayList<Employee> pEmployeesList)
    {
        if (pEmployeesList.size() > 0)
        {
            mEmployeeList.clear();
            mEmployeeList.addAll(pEmployeesList);
            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameTextView;

        private TextView ageTextView;

        private TextView salaryTextView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewName);
            ageTextView = itemView.findViewById(R.id.textViewAge);
            salaryTextView = itemView.findViewById(R.id.textViewSalary);
        }
    }
}
