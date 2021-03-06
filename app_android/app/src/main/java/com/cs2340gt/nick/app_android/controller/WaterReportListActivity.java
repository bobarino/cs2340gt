package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Location;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.WaterReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * main activity for handling the production of the list of water reports submitted
 * produced by SEAN on 3/1/17.
 */

public class WaterReportListActivity extends AppCompatActivity implements View.OnClickListener {

    private Button returnButton;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_report_list);

        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);


        //Step 1.  Setup the recycler view by getting it from our layout in the main window
        View recyclerView = findViewById(R.id.water_report_list);
        assert recyclerView != null;
        //Step 2.  Hook up the adapter to the view
        setupRecyclerView((RecyclerView) recyclerView);

    }

    /**
     * Set up an adapter and hook it to the provided view
     * @param recyclerView  the view that needs this adapter
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Model model = Model.getInstance();
        //grabReports(model);

        recyclerView.setAdapter(new SimpleReportRecyclerViewAdapter(model.getReportList()));
    }

    /**
     * This inner class is our custom adapter.  It takes our basic model information and
     * converts it to the correct layout for this view.
     *
     * In this case, we are just mapping the toString of the Course object to a text field.
     */
    @SuppressWarnings("UnusedAssignment")
    public class SimpleReportRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleReportRecyclerViewAdapter.ViewHolder> {

        /**
         * Collection of the items to be shown in this list.
         */
        private final List<WaterReport> mReports;

        /**
         * set the items to be used by the adapter
         * @param items the list of items to be displayed in the recycler view
         */
        public SimpleReportRecyclerViewAdapter(List<WaterReport> items) {
            mReports = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /*

              This sets up the view for each individual item in the recycler display
              To edit the actual layout, we would look at: res/layout/course_list_content.xml
              If you look at the example file, you will see it currently just 2 TextView elements
             */
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.water_report_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final Model model = Model.getInstance();

            FirebaseDatabase.getInstance().getReference().child("reports")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //WaterReport waterReport = snapshot.getValue(WaterReport.class);
                                //System.out.println(waterReport.getCondition());
                                String condition = snapshot.child("condition").getValue(String.class);
                                String dateTime = snapshot.child("dateTime").getValue(String.class);
                                Long longitude = snapshot.child("location").child("longitude").getValue(Long.class);
                                Long latitude = snapshot.child("location").child("latitude").getValue(Long.class);
                                String cred = snapshot.child("reporter").child("credential").getValue(String.class);
                                String emailAddress = snapshot.child("reporter").child("emailAddress").getValue(String.class);
                                String password = snapshot.child("reporter").child("password").getValue(String.class);
                                String source = snapshot.child("source").getValue(String.class);


                                Account fakeAcc = new Account(emailAddress, password, Credential.USER);
                                Location fakeLoc = new Location((double)(latitude), (double)(longitude));
                                WaterReport wr = new WaterReport(fakeAcc, source, condition,
                                        dateTime, fakeLoc);
                                model.addReport(wr);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

            /*
            This is where we have to bind each data element in the list (given by position parameter)
            to an element in the view (which is one of our two TextView widgets
             */
            //start by getting the element at the correct position
            holder.mReport = mReports.get(position);
            /*
              Now we bind the data to the widgets.  In this case, pretty simple, put the id in one
              textview and the string rep of a course in the other.
             */
            //holder.mIdView.setText("" + mReports.get(position).getId());
            holder.mContentView.setText(mReports.get(position).toString());


            /*
             * set up a listener to handle if the user clicks on this list item, what should happen?
             */
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mTwoPane) {
//                        //if a two pane window, we change the contents on the main screen
//                        Bundle arguments = new Bundle();
//                        arguments.putInt(CourseDetailFragment.ARG_COURSE_ID, holder.mCourse.getId());
//
//                        CourseDetailFragment fragment = new CourseDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.course_detail_container, fragment)
//                                .commit();
//                    } else {
//                        //on a phone, we need to change windows to the detail view
//                        Context context = v.getContext();
//                        //create our new intent with the new screen (activity)
//                        Intent intent = new Intent(context, Water.class);
//                        /*
//                            pass along the id of the course so we can retrieve the correct data in
//                            the next window
//                         */
//                        intent.putExtra(CourseDetailFragment.ARG_COURSE_ID, holder.mCourse.getId());
//
//                        model.setCurrentCourse(holder.mCourse);
//
//                        //now just display the new window
//                        context.startActivity(intent);
//                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mReports.size();
        }

        /**
         * This inner class represents a ViewHolder which provides us a way to cache information
         * about the binding between the model element (in this case a Course) and the widgets in
         * the list view (in this case the two TextView)
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public WaterReport mReport;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view == returnButton) {
            finish();
            startActivity(new Intent(this, LoggedInActivity.class));
        }
    }


}
