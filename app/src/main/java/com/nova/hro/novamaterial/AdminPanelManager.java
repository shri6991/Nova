package com.nova.hro.novamaterial;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shrikant on 6/22/2015.
 */
public class AdminPanelManager {

    public static class FragmentQueryDB extends Fragment {
        ListView searchListView;
        ImageView bSearch;
        EditText etSearch;
        ArrayList<User> arrayUser;
        ServerRequests serverRequests;
        Context parentActivity;
        ArrayAdapter<User> userArrayAdapter;

        public FragmentQueryDB() {
        }

        @Override
        public void onAttach(Context activity) {
            super.onAttach(activity);
            serverRequests = new ServerRequests(activity);
            parentActivity = activity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            arrayUser = new ArrayList<>();
            View view = inflater.inflate(R.layout.fragment_query_db, container, false);
            userArrayAdapter = new SearchAdapter(parentActivity, arrayUser);
            searchListView = (ListView) view.findViewById(R.id.searchListView);
            searchListView.setAdapter(userArrayAdapter);
            bSearch = (ImageView) view.findViewById(R.id.bSearch);
            etSearch = (EditText) view.findViewById(R.id.searchBox);
            bSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchUserQuery();
                }
            });
            return view;
        }

        public void fetchUserQuery() {
            serverRequests.fetchUsersQuery(removeSpaces(etSearch.getText().toString()), new GetUserObjectsCallBack() {
                @Override
                public void done(User[] returnedUsers) {
                    arrayUser.clear();
                    for (int i = 0; i < returnedUsers.length; i++) {
                        if (returnedUsers[i] == null) {
                            System.out.println("returneduser at position is null : " + i);
                            break;
                        }
                        arrayUser.add(i, returnedUsers[i]);
                    }
                    userArrayAdapter.notifyDataSetChanged();
                }
            });
        }

        String removeSpaces(String string) {
            string = string.trim();
            string = string.replace(' ', '+');
            return string;

        }

        private class SearchAdapter extends ArrayAdapter<User> {
            ArrayList<User> arrayUser;
            LayoutInflater li;
            Context context;

            public SearchAdapter(Context context, ArrayList<User> arrayUser) {
                super(context, R.layout.user_search_listview, arrayUser);
                this.arrayUser = arrayUser;
                this.context = context;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    System.out.println("View is null, inflating");
                    li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = li.inflate(R.layout.user_search_listview, null);
                    System.out.println("View has been inflated");
                }

                if (arrayUser != null) {
                    User receivedUser = arrayUser.get(position);
                    System.out.println("Received name " + receivedUser.getName());
                    TextView returnedName = (TextView) convertView.findViewById(R.id.returnedName);
                    TextView returnedEmail = (TextView) convertView.findViewById(R.id.returnedEmail);
                    TextView returnedPosition1 = (TextView) convertView.findViewById(R.id.returnedPosition1);
                    TextView returnedExperience = (TextView) convertView.findViewById(R.id.returnedExperience);
                    TextView returnedPhone = (TextView) convertView.findViewById(R.id.returnedPhone);
                    TextView returnedDesloc = (TextView) convertView.findViewById(R.id.returnedDesloc);
                    Button bSaveUser = (Button) convertView.findViewById(R.id.bAdminSaveUser);
                    returnedName.setText(receivedUser.getName());
                    returnedEmail.setText(receivedUser.getEmail());
                    returnedPhone.setText("+91 " + receivedUser.getPhone());
                    returnedPosition1.setText("Domain of interest: " + receivedUser.getPosition());
                    returnedExperience.setText("Work Experience: " + receivedUser.getExperience());
                    returnedDesloc.setText("Desired Location: " + receivedUser.getDesloc());

                    bSaveUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
                return convertView;
            }
        }
    }

    public static class FragmentNewJob extends Fragment implements View.OnClickListener {
        final String[] loclist = {"Mumbai", "Chennai", "New Delhi", "Kolkata", "Bangalore", "Hyderabad", "Allahabad", "Patna", "Jaipur", "Pune", "Noida", "Bhubhaneshwar", "Ahmedabad", "Surat", "Lucknow", "Kanpur", "Nagpur", "Bhopal", "Trichy"};
        EditText jobId, jobPosition, jobDescription, jobRemarks;
        AutoCompleteTextView jobDomain, jobLocation, jobExperience;
        Button bAddJob, bDiscard;
        ServerRequests serverRequests;
        Spinner jobType;
        String jtype = "Contract";
        Context parentActivity;
        String[] openings = {"Sales", "Marketing", "Advertising", "Business Development", "Accounts", "Finance", "Treasury", "Audit", "Compliance", "Taxation", "Procurement", "Manufacturing", "Quality", "IT Networking", "Software Development", "Java", "Database Administration", "SQL server", "Oracle", "mySQL", "Progress", "DB2", "Mainframe Technologies", "HR operations", "Learning", "Talent Management", "OD", "HR Compliances", "Compensation & Benefits", "Storage Management", "IT Infrastructure Mangement", "Big Data", "Web Development", "Analytics", "General Management", "Microsoft Technologies", "SAP", "Oracle ERP", "Peoplesoft", "ERP - Other", "Cloud Technologies", "Administrator - UNIX", "Administrator - AIX", "Administrator - Windows", "Weblogic", "Websphere", "Tuxedo", "Business Intelligence Tools", "Hyperion", "MSBI", "CRM", "Android", "iOS", "AWS", "Microsoft Azure", "Investment Banking", "Corporate Banking", "Retail Banking", "Fund Management", "Wealth Management", "Stock Broking", "Commodity Broking", "Backoffice Jobs", "General Administration", "Teaching", "Others- specify"};
        String[] experience = {"Fresher", "1 year", "2 years", "3 years", "4 years", "5 years", "6 years", "7 years", "8 years", "9 years", "10 years", "11 years", "12 years", "13 years", "14 years", "15 years", "16 years", "17 years", "18 years", "19 years", "20 years", "21 years", "22 years", "23 years", "24 years", "25 years", "More than 25 years"};
        String[] type = {"Contract Job", "Permanent Job"};

        public FragmentNewJob() {
        }

        @Override
        public void onAttach(Context activity) {
            super.onAttach(activity);
            parentActivity = activity;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bSaveNewJob:
                    System.out.println(jobId.getText().toString() + jobDomain.getText().toString() + jobPosition.getText().toString() + jtype + jobExperience.getText().toString() + jobDescription.getText().toString() + jobLocation.getText().toString() + jobRemarks.getText().toString());
                    addJob(jobId.getText().toString(), jobDomain.getText().toString(), jobPosition.getText().toString(), jtype, jobExperience.getText().toString(), jobDescription.getText().toString(), jobLocation.getText().toString(), jobRemarks.getText().toString());
                case R.id.DiscardNewJob:
                    clearFields();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_newjob, container, false);
            jobDescription = (EditText) view.findViewById(R.id.addDescription);
            jobId = (EditText) view.findViewById(R.id.addID);
            jobType = (Spinner) view.findViewById(R.id.addType);
            jobExperience = (AutoCompleteTextView) view.findViewById(R.id.addExperience);
            jobLocation = (AutoCompleteTextView) view.findViewById(R.id.addLocation);
            jobRemarks = (EditText) view.findViewById(R.id.addRemarks);
            jobDomain = (AutoCompleteTextView) view.findViewById(R.id.addDomain);
            jobPosition = (EditText) view.findViewById(R.id.addPosition);
            bAddJob = (Button) view.findViewById(R.id.bSaveNewJob);
            bDiscard = (Button) view.findViewById(R.id.DiscardNewJob);
            bAddJob.setOnClickListener(this);
            bDiscard.setOnClickListener(this);

            serverRequests = new ServerRequests(parentActivity);
            ArrayAdapter<String> jobOpeningsAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, openings);
            ArrayAdapter<String> jobLocationAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, loclist);
            ArrayAdapter<String> jobExperienceAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, experience);
            ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(parentActivity, R.layout.support_simple_spinner_dropdown_item, type);

            jobDomain.setAdapter(jobOpeningsAdapter);
            jobLocation.setAdapter(jobLocationAdapter);
            jobExperience.setAdapter(jobExperienceAdapter);
            jobType.setAdapter(jobTypeAdapter);
            jobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) jtype = "Contract";
                    else jtype = "Permanent";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return view;
        }

        public void addJob(String ID, String domain, String position, String type, String experience, String description, String location, String remarks) {
            clearFields();
            serverRequests.addJob(new Job(ID, domain, position, type, experience, description, location, remarks), new GetJobCallBack() {
                @Override
                public void done(Job returnedJob) {
                }
            });
        }

        public void clearFields() {
            jobId.setText("");
            jobRemarks.setText("");
            jobDescription.setText("");
            jobLocation.setText("");
            jobPosition.setText("");
            jobDomain.setText("");
            jobExperience.setText("");
            jobLocation.setText("");
        }
    }

    public static class EditJob extends Fragment {

        ServerRequests serverRequests;
        UserLocalStore userLocalStore;
        User receivedUser;
        EditText searchJob;
        ImageView searchBtn;
        RecyclerView editJobRecycler;
        MyRecyclerAdapter editJobAdapter;
        ArrayList<Job> arrayJob;
        TextView noJobSearch;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            serverRequests = new ServerRequests(context);
            userLocalStore = new UserLocalStore(context);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_edit_job, container, false);
            receivedUser = userLocalStore.getAllDetails();
            arrayJob = new ArrayList<>();
            noJobSearch = (TextView) view.findViewById(R.id.noJobEdit);
            searchJob = (EditText) view.findViewById(R.id.etEditJob);
            searchBtn = (ImageView) view.findViewById(R.id.editJobImage);
            editJobRecycler = (RecyclerView) view.findViewById(R.id.jobEditRecycler);
            editJobAdapter = new MyRecyclerAdapter(arrayJob);
            editJobRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            editJobRecycler.setAdapter(editJobAdapter);
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchJob();
                }
            });
            return view;
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
                        editJobAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.VH> {

            ArrayList<Job> jobArray;

            public MyRecyclerAdapter(ArrayList<Job> jl) {
                jobArray = jl;
            }

            @Override
            public VH onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater li = LayoutInflater.from(parent.getContext());
                View v = li.inflate(R.layout.job_edit_listview, parent);
                return new VH(v);
            }

            @Override
            public void onBindViewHolder(VH holder, int position) {
                Job current = jobArray.get(position);
                holder.jobID.setText(current.getID());
                holder.jobDescription.setText(current.getDescription());
                holder.jobDomain.setText(current.getDomain());
                holder.jobExperience.setText(current.getExperience());
                holder.jobRemarks.setText(current.getRemarks());
                holder.jobLocation.setText(current.getLocation());
                holder.jobPosition.setText(current.getID());
            }

            @Override
            public int getItemCount() {
                return jobArray.size();
            }

            public class VH extends RecyclerView.ViewHolder {
                TextView jobID, jobPosition, jobExperience, jobRemarks, jobLocation, jobDescription, jobDomain;
                EditText etjobID, etjobPosition, etjobExperience, etjobRemarks, etjobLocation, etjobDescription, etjobDomain;
                Button bEditJob;

                public VH(View v) {
                    super(v);
                    jobID = (TextView) v.findViewById(R.id.returnedJobID);
                    jobExperience = (TextView) v.findViewById(R.id.returnedJobExperience);
                    jobRemarks = (TextView) v.findViewById(R.id.returnedJobRemarks);
                    jobLocation = (TextView) v.findViewById(R.id.returnedJobLocation);
                    jobDescription = (TextView) v.findViewById(R.id.returnedJobDescription);
                    jobDomain = (TextView) v.findViewById(R.id.returnedJobDomain);
                    jobPosition = (TextView) v.findViewById(R.id.returnedJobPosition);
                    etjobID = (EditText) v.findViewById(R.id.etreturnedJobID);
                    etjobPosition = (EditText) v.findViewById(R.id.etreturnedJobPosition);
                    etjobExperience = (EditText) v.findViewById(R.id.etreturnedJobExperience);
                    etjobRemarks = (EditText) v.findViewById(R.id.etreturnedJobRemarks);
                    etjobLocation = (EditText) v.findViewById(R.id.etreturnedJobLocation);
                    etjobDescription = (EditText) v.findViewById(R.id.etreturnedJobDescription);
                    etjobDomain = (EditText) v.findViewById(R.id.etreturnedJobDomain);
                    bEditJob = (Button) v.findViewById(R.id.bEditJob);
                }
            }
        }
    }

}