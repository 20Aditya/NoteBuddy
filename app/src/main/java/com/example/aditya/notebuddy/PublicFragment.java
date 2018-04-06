package com.example.aditya.notebuddy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView list;
    Firebase reference;
    ArrayList<Details> notes = new ArrayList<Details>();



    public PublicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicFragment newInstance(String param1, String param2) {
        PublicFragment fragment = new PublicFragment();
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
        View view =  inflater.inflate(R.layout.fragment_public, container, false);



        setHasOptionsMenu(true);

        list = (ListView)view.findViewById(R.id.list);
        reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/Public Notes");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()) {
                    String key = datas.child("Title").getValue().toString();
                    Log.d("List","datas=" + datas.child("Title").getValue().toString());
                    String filetype = datas.child("File Type").getValue().toString();
                    int id = imageid(filetype);
                    notes.add(new Details(key,id));
                }
                DetailsListAdapter adapter = new DetailsListAdapter(getActivity(),notes);
                list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("List", "Value=" + parent.getItemAtPosition(position));
                Intent viewevent = new Intent(getActivity().getApplicationContext(),ViewPublicNoteActivity.class);
                viewevent.putExtra(Utilities.Title,parent.getItemAtPosition(position).toString());
                startActivity(viewevent);
            }
        });

        return view;

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public int imageid(String filetype){

        if(filetype.equals(".pdf")){
            return R.drawable.pdf;
        }else if(filetype.equals(".docx")){
            return R.drawable.doc;
        }else if(filetype.equals(".xlsx")){
            return R.drawable.xlsx;
        }else if(filetype.equals(".txt")){
            return R.drawable.txt;
        }else{
            return R.drawable.upload;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menupublic,menu);

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
