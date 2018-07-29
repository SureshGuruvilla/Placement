package dev.svssdc.com.placement;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
public class Tab_3 extends Fragment {
    View view;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView tab3_recyclerview;
    private DatabaseReference notificationdatabaseReference;
    private DatabaseReference logindatabasereference;
    private DatabaseReference databaseReference;
    private DatabaseReference database;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private String u_id;
    private OnFragmentInteractionListener mListener;
    private TextView notificationRead;
    private String ourname,s_postnotificationkey,s_posteduid,s_postedname,s_posted,s_typeofpost,s_typepost;
    private Boolean readnotification,read;
    private String time;
    public String spinner,spinnerAuto;
    public Tab_3() {
    }
    public static Tab_3 newInstance(String param1, String param2) {
        Tab_3 fragment = new Tab_3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_3, container, false);
        try {
            Start();
        }
        catch (Exception e){

        }

        return view;
    }
    public void Start() {
        tab3_recyclerview = (RecyclerView) view.findViewById(R.id.notification_recyclerview);
        tab3_recyclerview.setHasFixedSize(false);
        tab3_recyclerview.setNestedScrollingEnabled(false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        notificationdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        notificationdatabaseReference.keepSynced(true);
        logindatabasereference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        logindatabasereference.keepSynced(true);
        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        firebaseAuth = FirebaseAuth.getInstance();
        u_id = firebaseAuth.getCurrentUser().getUid();
        notificationRead = (TextView) view.findViewById(R.id.notification_read);
        notificationRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                read = true;
                notificationdatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            readnotification = true;
                            String key = data.getRef().getKey().toString();
                            if(readnotification == true && read == true){
                                if(data.hasChild(u_id)){
                                    readnotification = false;
                                }
                                else{
                                    notificationdatabaseReference.child(key).child(u_id).setValue("seen");
                                    readnotification = false;
                                }
                            }
                        }
                        read = false;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
        FirebaseRecyclerAdapter<NotificationHelper,Tab_3.Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <NotificationHelper, Tab_3.Holder>(
                NotificationHelper.class,R.layout.notoficationlayout, Tab_3.Holder.class,notificationdatabaseReference
        ) {
            @Override
            protected void populateViewHolder(final Tab_3.Holder holder, final NotificationHelper notificationHelper, int i) {
                final String s_key = getRef(i).getKey();
                if (u_id.equals(notificationHelper.getS_uid())) {
                    holder.v.setVisibility(View.GONE);
                    holder.notificationName.setVisibility(View.GONE);
                    holder.notificationIcon.setVisibility(View.GONE);
                    holder.notificationDp.setVisibility(View.GONE);
                    holder.notification.setVisibility(LinearLayout.GONE);
                    holder.linearLayout.setVisibility(LinearLayout.GONE);
                    holder.v.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
                else {
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.dp(getContext(), notificationHelper.getDp());
                    holder.notification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notificationdatabaseReference.child(s_key).child(u_id).setValue("seen");
                            notificationdatabaseReference.child(s_key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    s_posted = dataSnapshot.child("s_post").getValue(String.class);
                                    s_typeofpost = dataSnapshot.child("posts").getValue(String.class);
                                    Intent intent = new Intent(getActivity(), SinglePostActivity.class);
                                    intent.putExtra("s_post", s_posted);
                                    intent.putExtra("posts", s_typeofpost);
                                    startActivity(intent);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    });
                    notificationdatabaseReference.child(s_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(u_id)) {
                                    holder.notification.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else {
                                holder.notification.setBackgroundColor(Color.parseColor("#8adcdcdc"));
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    notificationdatabaseReference.child(s_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            s_postnotificationkey = dataSnapshot.child("s_post").getValue(String.class);
                            s_typepost = dataSnapshot.child("posts").getValue(String.class);
                            ourname = dataSnapshot.child("name").getValue(String.class);
                            time = dataSnapshot.child("time").getValue(String.class);
                            if (dataSnapshot.child("type").getValue(String.class).equals("Post1Like")) {
                                holder.notificationIcon.setBackgroundResource(R.drawable.ic_thumb_up_rose);

                                databaseReference.child("Post1").child(s_postnotificationkey).child("name").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot1) {
                                        s_postedname = dataSnapshot1.getValue(String.class);
                                        String s = "<b><font color='#000000'>"+String.valueOf(ourname)+"</font></b> liked "+"<b><font color='#000000'>"+String.valueOf(s_postedname)+"</font></b> "+"'s Post";

                                        holder.notificationName.setText(Html.fromHtml(s));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                            if (dataSnapshot.child("type").getValue(String.class).equals("Post2Like")) {
                                database.child("Post2").child(dataSnapshot.child("s_post").getValue().toString())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                                String postspinner = dataSnapshot1.child("spinner").getValue(String.class);
                                                String postspinnerauto = dataSnapshot1.child("spinnerAuto").getValue(String.class);
                                                if(postspinner.equals("All")||
                                                        postspinner.equals("Department") && postspinnerauto.equals(Tab_2.Dept)
                                                        ||postspinner.equals("CGPA") &&
                                                        Float.parseFloat(postspinnerauto) <= Tab_2.CGPA ||Tab_2.Dept.equals("admin")) {
                                                    databaseReference.child("Post2").child(s_postnotificationkey).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot2) {
                                                            s_postedname = dataSnapshot2.child("name").getValue(String.class);
                                                            String s = "<b><font color='#000000'>"+String.valueOf(ourname)+"</font></b> liked "+"<b><font color='#000000'>"+String.valueOf(s_postedname)+"</font></b> "+"'s Post";
                                                            holder.notificationIcon.setBackgroundResource(R.drawable.ic_thumb_up_blue);
                                                            holder.notificationName.setText(Html.fromHtml(s));
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                                else {
                                                    holder.v.setVisibility(View.GONE);
                                                    holder.notificationName.setVisibility(View.GONE);
                                                    holder.notificationIcon.setVisibility(View.GONE);
                                                    holder.notificationDp.setVisibility(View.GONE);
                                                    holder.notification.setVisibility(LinearLayout.GONE);
                                                    holder.linearLayout.setVisibility(LinearLayout.GONE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                            }
                            if (dataSnapshot.child("type").getValue(String.class).equals("Post1Comment")) {
                                final String comment = dataSnapshot.child("comment").getValue(String.class);
                                holder.notificationIcon.setBackgroundResource(R.drawable.ic_comment);
                                databaseReference.child("Post1").child(s_postnotificationkey).child("name").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot1) {
                                        s_postedname = dataSnapshot1.getValue(String.class);
                                        String s = "<b><font color='#000000'>"+String.valueOf(ourname)+"</font></b> commented "+"<b><font color='#000000'>"+String.valueOf(s_postedname)+"</font></b> "+"'s Post";

                                        holder.notificationName.setText(Html.fromHtml(s));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                            if (dataSnapshot.child("type").getValue(String.class).equals("Post2Comment")) {
                                database.child("Post2").child(dataSnapshot.child("s_post").getValue(String.class))
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                                String postspinner = dataSnapshot1.child("spinner").getValue(String.class);
                                                String postspinnerauto = dataSnapshot1.child("spinnerAuto").getValue(String.class);
                                                assert postspinner != null;
                                                assert postspinnerauto != null;
                                                if(postspinner.equals("All")||
                                                        postspinner.equals("Department") && postspinnerauto.equals(Tab_2.Dept)
                                                        ||postspinner.equals("CGPA") &&
                                                        Float.parseFloat(postspinnerauto) <= Tab_2.CGPA ||Tab_2.Dept.equals("admin")) {
                                                    holder.notificationIcon.setBackgroundResource(R.drawable.ic_comment_rose);
                                                    databaseReference.child("Post2").child(s_postnotificationkey).child("name").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot1) {
                                                            s_postedname = dataSnapshot1.getValue(String.class);
                                                            String s = "<b><font color='#000000'>"+String.valueOf(ourname)+"</font></b> commented "+"<b><font color='#000000'>"+String.valueOf(s_postedname)+"</font></b> "+"'s Post";

                                                            holder.notificationName.setText(Html.fromHtml(s));
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                                else {
                                                    holder.v.setVisibility(View.GONE);
                                                    holder.notificationName.setVisibility(View.GONE);
                                                    holder.notificationIcon.setVisibility(View.GONE);
                                                    holder.notificationDp.setVisibility(View.GONE);
                                                    holder.notification.setVisibility(LinearLayout.GONE);
                                                    holder.linearLayout.setVisibility(LinearLayout.GONE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                            }
                            if (dataSnapshot.child("type").getValue(String.class).equals("Post1")) {
                                databaseReference.child("Post1").child(s_postnotificationkey).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot1) {
                                        s_postedname = dataSnapshot1.child("name").getValue(String.class);
                                        s_posteduid = dataSnapshot1.child("u_id").getValue(String.class);
                                        holder.notificationIcon.setBackgroundResource(R.drawable.ic_navhome);
                                        String s = "<b><font color='#000000'>"+String.valueOf(s_postedname)+"</font></b>"+" posted a new Post";
                                        holder.notificationName.setText(Html.fromHtml(s));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            }
                            if (dataSnapshot.child("type").getValue(String.class).equals("Post2")) {
                                String postspinner = dataSnapshot.child("spinner").getValue(String.class);
                                String postspinnerauto = dataSnapshot.child("spinnerAuto").getValue(String.class);
                                assert postspinner != null;
                                assert postspinnerauto != null;
                                if(postspinner.equals("All")||
                                        postspinner.equals("Department") && postspinnerauto.equals(Tab_2.Dept)
                                        ||postspinner.equals("CGPA") &&
                                        Float.parseFloat(postspinnerauto) <= Tab_2.CGPA ||Tab_2.Dept.equals("admin")) {
                                    databaseReference.child("Post2").child(s_postnotificationkey).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot1) {
                                            s_postedname = dataSnapshot1.child("name").getValue(String.class);
                                            s_posteduid = dataSnapshot1.child("u_id").getValue(String.class);
                                            holder.notificationIcon.setBackgroundResource(R.drawable.ic_chrome_reader_red);
                                            String s = "<b><font color='#000000'>"+String.valueOf(s_postedname)+"</font></b>"+" posted a new Post";
                                            holder.notificationName.setText(Html.fromHtml(s));
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            }
                            else {
                                    holder.v.setVisibility(View.GONE);
                                    holder.notificationName.setVisibility(View.GONE);
                                    holder.notificationIcon.setVisibility(View.GONE);
                                    holder.notificationDp.setVisibility(View.GONE);
                                    holder.notification.setVisibility(LinearLayout.GONE);
                                    holder.linearLayout.setVisibility(LinearLayout.GONE);
//                                    holder.v.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                }
                        }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        };
        tab3_recyclerview.setLayoutManager(linearLayoutManager);
        tab3_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public static class Holder extends RecyclerView.ViewHolder{
        private View v;
        private LinearLayout linearLayout;
        private TextView notificationName;
        private CircleImageView notificationDp;
        private LinearLayout notification;
        private TextView notificationIcon;
        public Holder(View itemView)
        {
            super(itemView);
            v = itemView;
            linearLayout = (LinearLayout) v.findViewById(R.id.notification_layout);
            notificationName = (TextView) v.findViewById(R.id.notification_name);
            notificationDp = (CircleImageView) v.findViewById(R.id.notification_dp);
            notification = (LinearLayout) v.findViewById(R.id.notification);
            notificationIcon = (TextView) v.findViewById(R.id.notification_icon);
        }
        public void dp(final Context ctx , final String image){
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(notificationDp, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(notificationDp);
                }
            });
        }
    }
}