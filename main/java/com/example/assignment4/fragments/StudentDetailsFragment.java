package com.example.assignment4.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.assignment4.helpers.Constants;
import com.example.assignment4.R;
import com.example.assignment4.helpers.util;


public class StudentDetailsFragment extends Fragment {
        private String mName,mRoll,mClassName;
        private EditText etName,etRoll,etClassName;
        private Button btnAdd;
        private OnAddFragmentListener fragmentCaller;
        util utl = new util();

        public StudentDetailsFragment() {
        }

        public static StudentDetailsFragment newInstance(Bundle bundle){
            StudentDetailsFragment fragment = new StudentDetailsFragment();
            if(bundle != null)
                fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflates the layout for this fragment
            final View view = inflater.inflate(R.layout.fragment_student_details, container, false);

            init(view);
            final Context context =getActivity();
            /**
             * send student details to other fragment on clicking add button using bundle
             */
            btnAdd = view.findViewById(R.id.fragment_studentDetails_btn_add);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etName.requestFocus();
                    mName = etName.getText().toString();
                    mRoll = etRoll.getText().toString();
                    mClassName = etClassName.getText().toString();
                    //for validating whether the fields entered are correct or not
                    if(utl.validName(context,mName,mRoll,mClassName)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.STUDENT_NAME, mName);
                        bundle.putString(Constants.STUDENT_ROLLNO, mRoll);
                        bundle.putString(Constants.STUDENT_CLASS, mClassName);
                        fragmentCaller.addFragmentListener(bundle);
                    }
                }
            });
            Bundle viewBundle = getArguments();
            if(viewBundle != null) {
                String result = viewBundle.getString(Constants.KEY);
                if (result.equals(getString(R.string.value_view))) {
                    etName.setEnabled(false);
                    etRoll.setEnabled(false);
                    etClassName.setEnabled(false);
                    mName = viewBundle.getString(Constants.STUDENT_NAME);
                    mRoll = viewBundle.getString(Constants.STUDENT_ROLLNO);
                    mClassName = viewBundle.getString(Constants.STUDENT_CLASS);
                    etName.setText(mName);
                    etRoll.setText(mRoll);
                    etClassName.setText(mClassName);
                    btnAdd.setVisibility(View.INVISIBLE);
                }
            }
            return view;
        }

        //this is self defined method for getting the edit texts from the layouts
        public void init(View view){
            util utl = new util();
            etName = view.findViewById(R.id.fragment_studentDetails_et_name);
            etName.requestFocus();
            etRoll = view.findViewById(R.id.fragment_studentDetails_et_rollno);
            etClassName = view.findViewById(R.id.fragment_studentDetails_et_class);
        }

        public void instantiateAddListener(OnAddFragmentListener fragmentCaller){
            this.fragmentCaller=fragmentCaller;
        }

        //for clearing the edit text when new add student button is clicked
        public void clearEditText(){
            etName.getText().clear();
            etRoll.getText().clear();
            etClassName.getText().clear();
        }

        //for setting the text to textviews
        public void setEditText(Bundle bundle) {
            if (bundle != null) {
                etName.setText(bundle.getString(Constants.STUDENT_NAME));
                etRoll.setText(bundle.getString(Constants.STUDENT_ROLLNO));
                etClassName.setText(bundle.getString(Constants.STUDENT_CLASS));

            }
        }

        public interface OnAddFragmentListener {
            void addFragmentListener(Bundle bundle);
        }

    }
