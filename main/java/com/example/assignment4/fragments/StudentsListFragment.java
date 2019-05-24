package com.example.assignment4.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.assignment4.helpers.Constants;
import com.example.assignment4.R;
import com.example.assignment4.models.StudentModel;
import com.example.assignment4.activities.StudentDetailsActivity;
import com.example.assignment4.adapters.RecyclerViewAdapter;

import java.util.ArrayList;


public class StudentsListFragment extends Fragment implements RecyclerViewAdapter.RecyclerViewClickListener {


    private StudentListFragmentListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<StudentModel> mAl_StudentsDetails=new ArrayList<>();
    private TextView noDataFound;
    private Button mAddStudent;
    private String mName,mRoll,mClassName;
    private int mViewPosition;
    private String result=Constants.ADD;

    //default contructor
    public StudentsListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_students_list, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(mAl_StudentsDetails,this);
        mRecyclerView.setAdapter(mAdapter);

        noDataFound = view.findViewById(R.id.fragment_students_list_tv_nodatafound);
        final Bundle bundle = new Bundle();

        mAddStudent=view.findViewById(R.id.fragment_studentsList_btn_add);
        mAddStudent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getResult(getString(R.string.value_add));
                bundle.putString(Constants.KEY,getString(R.string.value_add));
                mListener.studentFragment(bundle);
            }
        });

        return view;
    }

    /**
     *     method of interface RecyclerViewClickListener declared in recyclerAdapter performs the operation selected through dialog
     *     box
     * @param view        view of selected view
     * @param position    position of selected view
     */
    public void onItemClicked(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final Bundle bundle = new Bundle();
        mViewPosition=position;
        builder.setTitle(getString(R.string.builder_title));
        String[] options = {getString(R.string.dialog_btn_view),getString(R.string.dialog_btn_edit),getString(R.string.dialog_btn_delete)};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialogSelectedFunction(Constants.VIEW,bundle);
                        break;
                    case 1:
                        dialogSelectedFunction(Constants.EDIT,bundle);
                        break;
                    case 2:
                        mViewPosition=position;
                        mAl_StudentsDetails.remove(mViewPosition);
                        mAdapter.notifyItemRemoved(mViewPosition);
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // set result key value ie edit or add
    public void getResult(String result){
        this.result = result;
    }


    //adds data to Arraylist and notify changes to adapter
    public void sendData(Bundle bundle){
        mName = bundle.getString(Constants.STUDENT_NAME);
        mRoll = bundle.getString(Constants.STUDENT_ROLLNO);
        mClassName = bundle.getString(Constants.STUDENT_CLASS);
        if(result.equals(getString(R.string.value_edit))) {
            mAl_StudentsDetails.remove(mViewPosition);
            mAl_StudentsDetails.add(mViewPosition,new StudentModel(mName,mRoll,mClassName));
            mAdapter = new RecyclerViewAdapter(mAl_StudentsDetails,this);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAl_StudentsDetails.add(new StudentModel(mName, mRoll, mClassName));
            mAdapter.notifyDataSetChanged();
        }
        if(!mAl_StudentsDetails.isEmpty()) {
            noDataFound.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            noDataFound.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * add student details in bundle
     * @param bundle bundle in which info will be stored
     */
    public void putDataInBundle(Bundle bundle)
    {
        mName=mAl_StudentsDetails.get(mViewPosition).getName();
        mRoll=mAl_StudentsDetails.get(mViewPosition).getRoll();
        mClassName=mAl_StudentsDetails.get(mViewPosition).getClassName();

        bundle.putString(Constants.STUDENT_NAME,mName);
        bundle.putString(Constants.STUDENT_ROLLNO,mRoll);
        bundle.putString(Constants.STUDENT_CLASS,mClassName);
    }

    /**
     * performs edit or view operation as per selected
     * @param choice     which operation is selected
     * @param bundle        bundle to send data to other fragment
     */
    public void dialogSelectedFunction(String choice,Bundle bundle)
    {
        putDataInBundle(bundle);
        if(choice.equals(Constants.VIEW))
        {
            bundle.putString(Constants.KEY,getString(R.string.value_view));
            Intent intent = new Intent(getActivity(), StudentDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            bundle.putString(Constants.KEY,getString(R.string.value_edit));
            getResult(getString(R.string.value_edit));
            mListener.studentFragment(bundle);
        }
    }
    public void instantiateListener(StudentListFragmentListener mListener){
        this.mListener=mListener;
    }
    //interface to send data between fragments
    public interface StudentListFragmentListener {
        void studentFragment(Bundle bundle);
    }
}


