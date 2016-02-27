package com.nova.hro.novamaterial;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class JobFinder extends AppCompatActivity {

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    ViewPager mPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_finder);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayoutJob);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager = (ViewPager) findViewById(R.id.jobViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_job);
        mPager.setAdapter(new JobPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(mPager);

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
        RecyclerView searchJobs;
        UserLocalStore userLocalStore;
        MyJobRecyclerAdapter jobAdapter;
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
            searchJobs = (RecyclerView) v.findViewById(R.id.suggestedJobsRecycler);
            noJobText = (TextView) v.findViewById(R.id.noJobText);
            receivedUser = userLocalStore.getAllDetails();
            searchJobs.setLayoutManager(new LinearLayoutManager(getContext()));
            jobAdapter = new MyJobRecyclerAdapter(arrayJobs);
            searchJobs.setAdapter(jobAdapter);
            searchJobs.setHasFixedSize(true);
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

        private class MyJobRecyclerAdapter extends RecyclerView.Adapter<MyJobRecyclerAdapter.ViewHolder> {

            ArrayList<Job> arrayJob = new ArrayList<>();

            public MyJobRecyclerAdapter(ArrayList<Job> jl) {
                arrayJob = jl;
            }

            @Override
            public MyJobRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater li = LayoutInflater.from(parent.getContext());
                View inflatedView = li.inflate(R.layout.job_item_list, parent, false);
                ViewHolder VH = new ViewHolder(inflatedView);
                return VH;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                Job currentJob = arrayJob.get(position);
                holder.jobDesc.setText("Job Description: " + currentJob.getDescription());
                holder.jobID.setText("ID: " + currentJob.getID());
                holder.jobRemarks.setText("Remarks: " + currentJob.getRemarks());
                holder.jobExp.setText("Experience Reqd: " + currentJob.getExperience() + " years");
                holder.jobDomain.setText("Domain: " + currentJob.getDomain());
                holder.jobLoc.setText("Location: " + currentJob.getLocation());
                holder.jobType.setText("Type: " + currentJob.getType());
                holder.jobPos.setText("Position: " + currentJob.getPosition());
            }

            @Override
            public int getItemCount() {
                return arrayJob.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView jobID, jobType, jobDesc, jobLoc, jobPos, jobRemarks, jobExp, jobDomain;
                Button applyForJob;

                public ViewHolder(View itemView) {
                    super(itemView);
                    jobID = (TextView) itemView.findViewById(R.id.returnedJobID);
                    jobDesc = (TextView) itemView.findViewById(R.id.returnedJobDescription);
                    jobLoc = (TextView) itemView.findViewById(R.id.returnedJobLocation);
                    jobPos = (TextView) itemView.findViewById(R.id.returnedJobPosition);
                    jobRemarks = (TextView) itemView.findViewById(R.id.returnedJobRemarks);
                    jobExp = (TextView) itemView.findViewById(R.id.returnedJobExperience);
                    jobType = (TextView) itemView.findViewById(R.id.returnedJobType);
                    jobDomain = (TextView) itemView.findViewById(R.id.returnedJobDomain);
                    applyForJob = (Button) itemView.findViewById(R.id.bApplyJob);
                    applyForJob.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.bApplyJob:
                            serverRequests.applyForJob(receivedUser.getEmail(), receivedUser.getName(), jobID.getText().toString(), jobPos.getText().toString());
                    }
                }
            }
        }
    }

    public static class JobSearcher extends Fragment {

        EditText searchJob;
        ImageView searchImage;
        RecyclerView searchJobListView;
        ArrayList<Job> arrayJob;
        ServerRequests serverRequests;
        Context parentActivity;
        UserLocalStore userLocalStore;
        TextView noJobSearch;
        MyJobRecyclerAdapter jobArrayAdapter;
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
            searchJobListView = (RecyclerView) v.findViewById(R.id.jobSearchRecycler);
            arrayJob = new ArrayList<>();
            jobArrayAdapter = new MyJobRecyclerAdapter(arrayJob);
            searchJobListView.setLayoutManager(new LinearLayoutManager(getContext()));
            searchJobListView.setAdapter(jobArrayAdapter);
            //searchJobListView.setHasFixedSize(true);
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
                        jobArrayAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        public class MyJobRecyclerAdapter extends RecyclerView.Adapter<MyJobRecyclerAdapter.ViewHolder> {

            ArrayList<Job> arrayJob = new ArrayList<>();

            public MyJobRecyclerAdapter(ArrayList<Job> jl) {
                arrayJob = jl;
            }

            @Override
            public MyJobRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater li = LayoutInflater.from(parent.getContext());
                View inflatedView = li.inflate(R.layout.job_item_list, parent, false);
                ViewHolder VH = new ViewHolder(inflatedView);
                return VH;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                Job currentJob = arrayJob.get(position);
                holder.jobDesc.setText("Job Description: " + currentJob.getDescription());
                holder.jobID.setText("ID: " + currentJob.getID());
                holder.jobRemarks.setText("Remarks: " + currentJob.getRemarks());
                holder.jobExperience.setText("Experience Required: " + currentJob.getExperience() + " years");
                holder.jobDomain.setText("Domain: " + currentJob.getDomain());
                holder.jobLoc.setText("Location: " + currentJob.getLocation());
                holder.jobType.setText("Type: " + currentJob.getType());
                holder.jobPos.setText("Position: " + currentJob.getPosition());
            }

            @Override
            public int getItemCount() {
                return arrayJob.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView jobID, jobDesc, jobType, jobLoc, jobPos, jobRemarks, jobExperience, jobDomain;
                Button applyForJob;

                public ViewHolder(View itemView) {
                    super(itemView);
                    jobID = (TextView) itemView.findViewById(R.id.returnedJobID);
                    jobDesc = (TextView) itemView.findViewById(R.id.returnedJobDescription);
                    jobLoc = (TextView) itemView.findViewById(R.id.returnedJobLocation);
                    jobType = (TextView) itemView.findViewById(R.id.returnedJobType);
                    jobPos = (TextView) itemView.findViewById(R.id.returnedJobPosition);
                    jobRemarks = (TextView) itemView.findViewById(R.id.returnedJobRemarks);
                    jobExperience = (TextView) itemView.findViewById(R.id.returnedJobExperience);
                    jobDomain = (TextView) itemView.findViewById(R.id.returnedJobDomain);
                    applyForJob = (Button) itemView.findViewById(R.id.bApplyJob);
                    applyForJob.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.bApplyJob:
                            serverRequests.applyForJob(receivedUser.getEmail(), receivedUser.getName(), jobID.getText().toString(), jobPos.getText().toString());
                    }
                }
            }
        }
    }

    private class JobPagerAdapter extends FragmentPagerAdapter {

        public JobPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 0) return new JobFinder.JobSearcher();
            else return new JobFinder.JobSuggester();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Search for a Job";
            else return "Suggested Jobs";
        }
    }
}
