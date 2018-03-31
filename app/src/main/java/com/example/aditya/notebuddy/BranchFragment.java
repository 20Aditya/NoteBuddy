package com.example.aditya.notebuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BranchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BranchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BranchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FloatingActionButton fab;
    ListView list;
    Firebase reference;
    ArrayList<String> notes = new ArrayList<>();
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;

    public BranchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BranchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BranchFragment newInstance(String param1, String param2) {
        BranchFragment fragment = new BranchFragment();
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

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_branch, container, false);


        fab = view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     startActivity(new Intent(getActivity(), AddNoteActivity.class));
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }
            });
        }

        list = (ListView)view.findViewById(R.id.list);
        reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/Information Technology");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()) {
                    String key = datas.child("Title").getValue().toString();
                    Log.d("List","datas=" + datas.child("Title").getValue().toString());
                    notes.add(key);
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, notes);
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
                Intent viewevent = new Intent(getActivity(),ViewNoteActivity.class);
                viewevent.putExtra(Utilities.Title,parent.getItemAtPosition(position).toString());
                startActivity(viewevent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
            }
        });


        return view;

    }


    /*public void onclickfunction(View view, String title){
        Intent viewevent = new Intent(getActivity(),ViewNoteActivity.class);
        viewevent.putExtra(Utilities.Title,title);
        viewevent.putExtra(Utilities.)
        startActivity(viewevent);
    }*/


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void update(final String branch){

        notes.clear();
        reference = new Firebase("https://notebuddy-9b5d4.firebaseio.com/" + branch);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()) {
                    String key = datas.child("Title").getValue().toString();
                    Log.d("List","datas=" + datas.child("Title").getValue().toString());
                    notes.add(key);
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, notes);
                list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewevent = new Intent(getActivity(), ViewNoteActivity.class);
                viewevent.putExtra(Utilities.Title,parent.getItemAtPosition(position).toString());
                viewevent.putExtra(Utilities.Branch, branch);
                startActivity(viewevent);

            }
        });


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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.IT:
                update("Information Technology");
                break;
            case R.id.CSE:
                update("Computer Science");
                break;
            case R.id.Mech:
                update("Mechanical");
                break;
            case R.id.Elec:
                update("Electrical");
                break;
            case R.id.Elex:
                update("Electronics");
                break;
            case R.id.Civil:
                update("Civil");
                break;
            case R.id.Chem:
                update("Chemical");
                break;

        }

        return true;
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
