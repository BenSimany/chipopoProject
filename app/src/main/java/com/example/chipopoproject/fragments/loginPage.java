package com.example.chipopoproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.chipopoproject.R;
import com.example.chipopoproject.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loginPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //private FirebaseAuth mAuth;


    public loginPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainPage.
     */
    // TODO: Rename and change types and number of parameters
    public static loginPage newInstance(String param1, String param2) {
        loginPage fragment = new loginPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_page, container, false);
        Button button1 = (Button) view.findViewById(R.id.buttonRegister);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mainPage_to_registerPage);
            }
        });
        Button button2 = (Button) view.findViewById(R.id.buttonLogin);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(view).navigate(R.id.action_mainPage_to_calenderPage);
                //the line up is directing to calender page, not sure why effi commented it out
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.login();
                //mainActivity.getStudent("0544746355");

            }
        });


        return view;
    }
}