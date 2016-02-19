package com.nova.hro.novamaterial;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        Activity parentActivity;
        ArrayAdapter<User> userArrayAdapter;

        public FragmentQueryDB() {
        }

        @Override
        public void onAttach(Activity activity) {
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
        Activity parentActivity;
        String[] openings = {"Sales", "Marketing", "Advertising", "Business Development", "Accounts", "Finance", "Treasury", "Audit", "Compliance", "Taxation", "Procurement", "Manufacturing", "Quality", "IT Networking", "Software Development", "Java", "Database Administration", "SQL server", "Oracle", "mySQL", "Progress", "DB2", "Mainframe Technologies", "HR operations", "Learning", "Talent Management", "OD", "HR Compliances", "Compensation & Benefits", "Storage Management", "IT Infrastructure Mangement", "Big Data", "Web Development", "Analytics", "General Management", "Microsoft Technologies", "SAP", "Oracle ERP", "Peoplesoft", "ERP - Other", "Cloud Technologies", "Administrator - UNIX", "Administrator - AIX", "Administrator - Windows", "Weblogic", "Websphere", "Tuxedo", "Business Intelligence Tools", "Hyperion", "MSBI", "CRM", "Android", "iOS", "AWS", "Microsoft Azure", "Investment Banking", "Corporate Banking", "Retail Banking", "Fund Management", "Wealth Management", "Stock Broking", "Commodity Broking", "Backoffice Jobs", "General Administration", "Teaching", "Others- specify"};
        String[] experience = {"Fresher", "1 year", "2 years", "3 years", "4 years", "5 years", "6 years", "7 years", "8 years", "9 years", "10 years", "11 years", "12 years", "13 years", "14 years", "15 years", "16 years", "17 years", "18 years", "19 years", "20 years", "21 years", "22 years", "23 years", "24 years", "25 years", "More than 25 years"};
        String[] type = {"Contract Job", "Permanent Job"};

        public FragmentNewJob() {
        }

        @Override
        public void onAttach(Activity activity) {
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

    public static class JobEditor extends Fragment {

        EditText searchJob;
        ImageView searchImage;
        ListView searchJobListView;
        ArrayList<Job> arrayJob;
        ServerRequests serverRequests;
        Activity parentActivity;
        UserLocalStore userLocalStore;
        TextView noJobSearch;
        ArrayAdapter<Job> jobArrayAdapter;
        User receivedUser;

        @Override
        public void onAttach(Activity activity) {
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
                    }
                }
            });
        }

        private class MyJobAdapter extends ArrayAdapter<Job> {

            LinearLayout show, edit;
            ArrayList<Job> arrayJobs;
            int status = 0;
            int position;
            Job temp;
            EditText returnedJobID1, returnedJobDomain1, returnedJobPosition1, returnedJobExperience1, returnedJobDescription1, returnedJobLocation1, returnedJobRemarks1;
            Button editJob, saveEdit, discardEdit;

            public MyJobAdapter(Context context, ArrayList<Job> arrayJobs) {
                super(context, R.layout.job_item_list, arrayJobs);
                this.arrayJobs = arrayJobs;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    System.out.println("View is null, inflating");

                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi.inflate(R.layout.job_edit_listview, null);
                    System.out.println("View has been inflated");
                }
                System.out.println("View not null");
                this.position = position;
                TextView returnedJobID = (TextView) convertView.findViewById(R.id.returnedJobID);
                TextView returnedJobType = (TextView) convertView.findViewById(R.id.returnedJobType);
                TextView returnedJobDomain = (TextView) convertView.findViewById(R.id.returnedJobDomain);
                TextView returnedJobPosition = (TextView) convertView.findViewById(R.id.returnedJobPosition);
                TextView returnedJobDescription = (TextView) convertView.findViewById(R.id.returnedJobDescription);
                TextView returnedJobExperience = (TextView) convertView.findViewById(R.id.returnedJobExperience);
                TextView returnedJobLocation = (TextView) convertView.findViewById(R.id.returnedJobLocation);
                TextView returnedJobRemarks = (TextView) convertView.findViewById(R.id.returnedJobRemarks);

                returnedJobID1 = (EditText) convertView.findViewById(R.id.etreturnedJobID);
                returnedJobDomain1 = (EditText) convertView.findViewById(R.id.etreturnedJobDomain);
                returnedJobPosition1 = (EditText) convertView.findViewById(R.id.etreturnedJobPosition);
                returnedJobDescription1 = (EditText) convertView.findViewById(R.id.etreturnedJobDescription);
                returnedJobExperience1 = (EditText) convertView.findViewById(R.id.etreturnedJobExperience);
                returnedJobLocation1 = (EditText) convertView.findViewById(R.id.etreturnedJobLocation);
                returnedJobRemarks1 = (EditText) convertView.findViewById(R.id.etreturnedJobRemarks);

                editJob = (Button) convertView.findViewById(R.id.bEditJob);
                saveEdit = (Button) convertView.findViewById(R.id.bSaveEdit);
                discardEdit = (Button) convertView.findViewById(R.id.bDiscardEdit);
                temp = arrayJobs.get(position);
                System.out.println("Received id " + temp.getID());

                returnedJobID.setText(temp.getID());
                returnedJobDomain.setText(temp.getDomain());
                returnedJobType.setText(temp.getType());
                returnedJobPosition.setText(temp.getPosition());
                returnedJobDescription.setText(temp.getDescription());
                returnedJobExperience.setText(temp.getExperience());
                returnedJobLocation.setText(temp.getLocation());
                returnedJobRemarks.setText(temp.getRemarks());

                returnedJobID1.setText(temp.getID());
                returnedJobDomain1.setText(temp.getDomain());
                returnedJobPosition1.setText(temp.getPosition());
                returnedJobDescription1.setText(temp.getDescription());
                returnedJobExperience1.setText(temp.getExperience());
                returnedJobLocation1.setText(temp.getLocation());
                returnedJobRemarks1.setText(temp.getRemarks());

                if (status == 2) {
                    show.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                }
                if (status == 1) {
                    show.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                }
                show = (LinearLayout) convertView.findViewById(R.id.jobViewLayout);
                edit = (LinearLayout) convertView.findViewById(R.id.jobEditLayout);

                editJob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditJob();
                    }
                });

                saveEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEditJob();
                    }
                });

                discardEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        discardEditJob();
                    }
                });

                return convertView;
            }

            public void showEditJob() {
                status = 2;
                View view = searchJobListView.getChildAt(position);
                //show = (LinearLayout)view.findViewById(R.id.jobViewLayout);
                //edit = (LinearLayout)view.findViewById(R.id.jobEditLayout);
                show.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }

            public void saveEditJob() {
                View view = searchJobListView.getChildAt(position);
                returnedJobID1 = (EditText) view.findViewById(R.id.etreturnedJobID);
                returnedJobDomain1 = (EditText) view.findViewById(R.id.etreturnedJobDomain);
                returnedJobPosition1 = (EditText) view.findViewById(R.id.etreturnedJobPosition);
                returnedJobDescription1 = (EditText) view.findViewById(R.id.etreturnedJobDescription);
                returnedJobExperience1 = (EditText) view.findViewById(R.id.etreturnedJobExperience);
                returnedJobLocation1 = (EditText) view.findViewById(R.id.etreturnedJobLocation);
                returnedJobRemarks1 = (EditText) view.findViewById(R.id.etreturnedJobRemarks);
                serverRequests.updateJob(new Job(returnedJobID1.getText().toString(), returnedJobDomain1.getText().toString(), returnedJobPosition1.getText().toString(), temp.getType(), returnedJobExperience1.getText().toString(), returnedJobDescription1.getText().toString(), returnedJobLocation1.getText().toString(), returnedJobRemarks1.getText().toString()), new GetJobCallBack() {
                    @Override
                    public void done(Job returnedJob) {
                        discardEditJob();
                    }
                });
            }

            public void discardEditJob() {
                View view = searchJobListView.getChildAt(position);
                status = 1;
                show.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                returnedJobID1.setText("");
                returnedJobDomain1.setText("");
                returnedJobPosition1.setText("");
                returnedJobExperience1.setText("");
                returnedJobDescription1.setText("");
                returnedJobLocation1.setText("");
                returnedJobRemarks1.setText("");
                notifyDataSetChanged();
            }
        }
    }
}
