package com.nova.hro.novamaterial;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class JobFinder extends AppCompatActivity {

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_finder);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayoutJob);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_job);
        tabLayout.addTab(tabLayout.newTab().setText("Search for a Job"));
        tabLayout.addTab(tabLayout.newTab().setText("Suggested Jobs"));
        Fragment initial = new JobFinder.JobSearcher();
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.jobfinderscroll, initial).commit();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    Fragment f1 = new JobFinder.JobSearcher();
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.jobfinderscroll, f1).commit();
                } else {
                    Fragment f = new JobFinder.JobSuggester();
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.jobfinderscroll, f).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public static class JobSuggester extends Fragment {
        ServerRequests serverRequests;
        ArrayList<Job> arrayJobs;
        ListView searchJobs;
        UserLocalStore userLocalStore;
        ArrayAdapter jobAdapter;
        TextView noJobText;
        User receivedUser;
        Context parentActivity;


        @Override
        public void onAttach(Context activity) {
            super.onAttach(activity);
            parentActivity = activity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_job_suggester, container, false);
            serverRequests = new ServerRequests(parentActivity);
            userLocalStore = new UserLocalStore(parentActivity);
            arrayJobs = new ArrayList<>();
            searchJobs = (ListView) v.findViewById(R.id.jobListView);
            noJobText = (TextView) v.findViewById(R.id.noJobText);
            receivedUser = userLocalStore.getAllDetails();
            jobAdapter = new MyJobAdapter(parentActivity, arrayJobs);
            searchJobs.setAdapter(jobAdapter);
            fetchJobListings();
            return v;
        }

        private void fetchJobListings() {
            arrayJobs.clear();
            noJobText.setVisibility(View.GONE);
            String userPosition = receivedUser.getPosition();
            String userExperience = receivedUser.getExperience();
            serverRequests.fetchJobListings(userPosition, userExperience, new GetJobObjectsCallBack() {
                @Override
                public void done(Job[] returnedJobs) {
                    if (returnedJobs == null || returnedJobs.length == 0)
                        noJobText.setVisibility(View.VISIBLE);
                    else {
                        System.out.println("adding results to arraylist");
                        for (int i = 0; i < returnedJobs.length; i++) {
                            arrayJobs.add(i, returnedJobs[i]);
                        }
                        System.out.println("setting adapter");
                        jobAdapter.notifyDataSetChanged();
                        System.out.println("done");
                    }
                }
            });

        }


        private class MyJobAdapter extends ArrayAdapter<Job> {

            ArrayList<Job> arrayJobs;
            Job temp;

            public MyJobAdapter(Context context, ArrayList<Job> arrayJobs) {
                super(context, R.layout.job_search_listview, arrayJobs);
                this.arrayJobs = arrayJobs;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    System.out.println("View is null, inflating");

                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi.inflate(R.layout.job_search_listview, null);
                    System.out.println("View has been inflated");
                }
                System.out.println("View not null");
                TextView returnedJobID = (TextView) convertView.findViewById(R.id.returnedJobID);
                TextView returnedJobDomain = (TextView) convertView.findViewById(R.id.returnedJobDomain);
                TextView returnedJobPosition = (TextView) convertView.findViewById(R.id.returnedJobPosition);
                TextView returnedJobDescription = (TextView) convertView.findViewById(R.id.returnedJobDescription);
                TextView returnedJobExperience = (TextView) convertView.findViewById(R.id.returnedJobExperience);
                TextView returnedJobLocation = (TextView) convertView.findViewById(R.id.returnedJobLocation);
                TextView returnedJobRemarks = (TextView) convertView.findViewById(R.id.returnedJobRemarks);
                Button applyForJob = (Button) convertView.findViewById(R.id.bApplyJob);

                temp = arrayJobs.get(position);
                System.out.println("Received id " + temp.getID());
                returnedJobID.setText(temp.getID());
                returnedJobDomain.setText(temp.getID());
                returnedJobID.setText(temp.getType());
                returnedJobPosition.setText(temp.getPosition());
                returnedJobDescription.setText(temp.getDescription());
                returnedJobExperience.setText(temp.getExperience());
                returnedJobLocation.setText(temp.getLocation());
                returnedJobRemarks.setText(temp.getRemarks());

                applyForJob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyForJob();
                    }
                });

                return convertView;
            }

            public void applyForJob() {
                serverRequests.applyForJob(receivedUser.getEmail(), receivedUser.getName(), temp.getID(), temp.getPosition());
            }
        }
    }

    public static class JobSearcher extends Fragment {

        EditText searchJob;
        ImageView searchImage;
        ListView searchJobListView;
        ArrayList<Job> arrayJob;
        ServerRequests serverRequests;
        Context parentActivity;
        UserLocalStore userLocalStore;
        TextView noJobSearch;
        ArrayAdapter<Job> jobArrayAdapter;
        User receivedUser;

        @Override
        public void onAttach(Context activity) {
            super.onAttach(activity);
            parentActivity = activity;
            serverRequests = new ServerRequests(activity);
            userLocalStore = new UserLocalStore(activity);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_search_job, container, false);
            searchJob = (EditText) v.findViewById(R.id.searchJobEditText);
            searchImage = (ImageView) v.findViewById(R.id.searchJobImage);
            receivedUser = userLocalStore.getAllDetails();
            noJobSearch = (TextView) v.findViewById(R.id.noJobSearch);
            searchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchJob();
                }
            });
            searchJobListView = (ListView) v.findViewById(R.id.searchJobListView);
            arrayJob = new ArrayList<>();
            jobArrayAdapter = new MyJobAdapter(parentActivity, arrayJob);
            searchJobListView.setAdapter(jobArrayAdapter);
            return v;
        }

        public void searchJob() {
            arrayJob.clear();
            serverRequests.searchJobListings(searchJob.getText().toString(), new GetJobObjectsCallBack() {
                @Override
                public void done(Job[] returnedJobs) {
                    if (returnedJobs == null || returnedJobs.length == 0) {
                        noJobSearch.setVisibility(View.VISIBLE);
                    } else {
                        noJobSearch.setVisibility(View.GONE);
                        for (int i = 0; i < returnedJobs.length; i++) {
                            arrayJob.add(i, returnedJobs[i]);
                        }
                        jobArrayAdapter = new MyJobAdapter(parentActivity, arrayJob);
                        searchJobListView.setAdapter(jobArrayAdapter);
                        jobArrayAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        private class MyJobAdapter extends ArrayAdapter<Job> {

            ArrayList<Job> arrayJobs;
            Job temp;

            public MyJobAdapter(Context context, ArrayList<Job> arrayJobs) {
                super(context, R.layout.job_search_listview, arrayJobs);
                this.arrayJobs = arrayJobs;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    System.out.println("View is null, inflating");

                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi.inflate(R.layout.job_search_listview, null);
                    System.out.println("View has been inflated");
                }
                System.out.println("View not null");
                TextView returnedJobID = (TextView) convertView.findViewById(R.id.returnedJobID);
                TextView returnedJobDomain = (TextView) convertView.findViewById(R.id.returnedJobDomain);
                TextView returnedJobPosition = (TextView) convertView.findViewById(R.id.returnedJobPosition);
                TextView returnedJobDescription = (TextView) convertView.findViewById(R.id.returnedJobDescription);
                TextView returnedJobExperience = (TextView) convertView.findViewById(R.id.returnedJobExperience);
                TextView returnedJobLocation = (TextView) convertView.findViewById(R.id.returnedJobLocation);
                TextView returnedJobRemarks = (TextView) convertView.findViewById(R.id.returnedJobRemarks);
                Button applyForJob = (Button) convertView.findViewById(R.id.bApplyJob);
                temp = arrayJobs.get(position);
                System.out.println("Received id " + temp.getID());
                returnedJobID.setText(temp.getID());
                returnedJobDomain.setText(temp.getID());
                returnedJobID.setText(temp.getType());
                returnedJobPosition.setText(temp.getPosition());
                returnedJobDescription.setText(temp.getDescription());
                returnedJobExperience.setText(temp.getExperience());
                returnedJobLocation.setText(temp.getLocation());
                returnedJobRemarks.setText(temp.getRemarks());

                applyForJob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyForJob();
                    }
                });
                return convertView;
            }

            public void applyForJob() {
                serverRequests.applyForJob(receivedUser.getEmail(), receivedUser.getName(), temp.getID(), temp.getPosition());
            }
        }
    }
}
