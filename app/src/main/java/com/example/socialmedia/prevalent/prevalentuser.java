package com.example.socialmedia.prevalent;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialmedia.models.FeedModel;
import com.example.socialmedia.models.Requests;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.utils.FeedAdapterUtil;
import com.example.socialmedia.utils.FirebaseUtil;
import com.example.socialmedia.utils.RequestsAdapterUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class prevalentuser {


    private static final String TAG = "prevalent";
    public static String following = "0";
    public static String followers = "0";
    public static String privacyStatus = "";
    public static UsersData ourData;
    public static List<FeedModel> feed;
    public static List<FeedModel> feedModelData;
    public static List<Requests> requests;


    public static void firebaseProfileData() {

        feedModelData = new ArrayList<>();
        requests = new ArrayList<>();
        feed=new ArrayList<>();

        FeedAdapterUtil.setAdapter();
        RequestsAdapterUtil.setRequestAdapter();

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        ourData = new UsersData();


        String uid = currentUser.getUid();
        firestore.collection("USERINFO").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String user = documentSnapshot.getString("username");
                        ourData.setUsername(user);
                        ourData.setName(documentSnapshot.getString("name"));
                        ourData.setBio(documentSnapshot.getString("bio"));
                        ourData.setProfilePic(documentSnapshot.getString("profileUrl"));

                        //getting users
                        FirebaseUtil.firebaseRetrieveFollowings(user, new FirebaseUtil.FollowingInterface() {
                            @Override
                            public void onFollowingRetrieved(List<String> users) {

                                for (String x : users) {

                                    FirebaseUtil.snapFirebase(x, new FirebaseUtil.snapInterface() {
                                        @Override
                                        public void onUpdateRetrieved(ArrayList<FeedModel> fm) {


                                            feedModelData.addAll(0, fm);

                                            FeedAdapterUtil.getAdapter().notifyItemRangeInserted(0, fm.size());

                                        }
                                    });

                                }


                            }
                        });

//                        getting your posts

                        FirebaseUtil.firebaseRetrivePosts(user, new FirebaseUtil.PostsInterface() {
                            @Override
                            public void onPostsRetrieved(List<UsersData.PostData> postsData) {

                                ourData.setPostData(postsData);
                                ourData.setTotalPosts(String.valueOf(postsData.size()));

                            }
                        }, 50);

//                        getting your follow requests

                        FirebaseUtil.retrieveRequests(user, new FirebaseUtil.UsernameProfile() {
                            @Override
                            public void onResult(List<Requests> list) {
                                requests.addAll(0, list);
                                RequestsAdapterUtil.getAdapter().notifyItemRangeInserted(0, list.size());
                            }
                        });


//                        getting feed

                        firestore.collection("POSTSANDSTORIES").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot doc :
                                        queryDocumentSnapshots) {
                                    FirebaseUtil.firebaseRetrieveFeed(doc.getId(), 1, new FirebaseUtil.Feed() {
                                        @Override
                                        public void onResult(FeedModel f) {
                                            System.out.println("update received" + f.getUsername());
                                            feed.add(f);
                                        }
                                    });
                                }

                            }
                        });


                        privacyStatus();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());

            }
        });


    }


    public static void privacyStatus() {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("POSTSANDSTORIES").document(ourData.getUsername())
                .get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                privacyStatus = documentSnapshot.getString("status");
            }
        });
    }

    public static void changePrivacy(boolean bool) {

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if (bool == false) {
            firestore.collection("POSTSANDSTORIES").document(ourData.getUsername()).update("status", "open")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            privacyStatus = "open";
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
        } else if (bool) {
            firestore.collection("POSTSANDSTORIES").document(ourData.getUsername()).update("status", "hidden")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            privacyStatus = "hidden";
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
        }

    }
}


/*
usersDat.setUsername(user);

                                    Task<QuerySnapshot> task1, task2;

                                    //getting another user posts
                                    task1 = firestore.collection("POSTSANDSTORIES").document(user).collection("POSTS").orderBy("date",Query.Direction.DESCENDING).limit(2)
                                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                    List<UsersData.PostData> postsData=new ArrayList<>();

                                                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                                                        postsData.add(new UsersData.PostData(doc.getId(),doc.getString("imageUrl"),doc.getString("thumbnailUrl"),doc.getString("caption")));

                                                    }
                                                    usersDat.setPostData(postsData);
                                                    usersDat.setTotalPosts(String.valueOf(queryDocumentSnapshots.size()));

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                                }
                                            });


                                    //getting anotheruser followers
                                    task2 = firestore.collection("POSTSANDSTORIES").document(user).collection("FOLLOWERS")
                                            .get().addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "onFailure: " + e.getMessage());

                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    System.out.println("another user followers " + user);

                                                    List<String> followers = new ArrayList<>();

                                                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                                        followers.add(doc.getId());
                                                    }

                                                    usersDat.setFollowersList(followers);
                                                    usersDat.setFollowers(String.valueOf(followers.size()));
                                                }
                                            });


                                    Tasks.whenAllSuccess(task1, task2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                        @Override
                                        public void onSuccess(List<Object> objects) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.getMessage());
                                        }
                                    });
 */