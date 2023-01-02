package com.example.socialmedia.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialmedia.models.FeedModel;
import com.example.socialmedia.models.LikesFollowersFollowing;
import com.example.socialmedia.models.Requests;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.prevalent.prevalentuser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUtil {

    private static final String TAG = "FirebaseUtil";
    final static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void firebaseRetriveData(String uname, FirebaseInterface fi, int limit) {

        UsersData ud = new UsersData();

        ud.setUsername(uname);

        Task<DocumentSnapshot> task3 = firestore.collection("POSTSANDSTORIES").document(uname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                ud.setName(documentSnapshot.getString("name"));
                ud.setBio(documentSnapshot.getString("bio"));
                ud.setProfilePic(documentSnapshot.getString("profileUrl"));

            }
        });

        Task<QuerySnapshot> task1 = firestore.collection("POSTSANDSTORIES").document(uname).collection("POSTS").orderBy("date", Query.Direction.DESCENDING).limit(limit)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        List<UsersData.PostData> postsData = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {

                            postsData.add(new UsersData.PostData(doc.getId(), doc.getString("imageUrl"), doc.getString("thumbnailUrl"), doc.getString("caption")));

                        }

                        ud.setPostData(postsData);
                        ud.setTotalPosts(String.valueOf(queryDocumentSnapshots.size()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        Task<QuerySnapshot> task2 = firestore.collection("POSTSANDSTORIES").document(uname).collection("FOLLOWERS").get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<String> follower = new ArrayList<>();

                for (DocumentSnapshot data :
                        queryDocumentSnapshots) {
                    follower.add(data.getId());
                }

                ud.setFollowersList(follower);
                ud.setFollowers(String.valueOf(follower.size()));
            }
        });

        Tasks.whenAllSuccess(task1, task2, task3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {

                fi.onFirebaseRetrieveComplete(ud);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    public static void searchProfile(String username, FirebaseInterface firebaseInterface) {
        firestore.collection("POSTSANDSTORIES").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UsersData ud = new UsersData();
                    ud.setProfilePic(documentSnapshot.getString("profileUrl"));
                    firebaseInterface.onFirebaseRetrieveComplete(ud);
                }
            }
        });

    }

    public static void firebaseRetrivePosts(String uname, PostsInterface po, int limit) {


        firestore.collection("POSTSANDSTORIES").document(uname).collection("POSTS").orderBy("date", Query.Direction.DESCENDING).limit(limit)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                List<UsersData.PostData> postsData = new ArrayList<>();

                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    postsData.add(new UsersData.PostData(doc.getId(), doc.getString("imageUrl"), doc.getString("thumbnailUrl"), doc.getString("caption")));

                }

                po.onPostsRetrieved(postsData);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public static void firebaseRetrieveFeed(String uname, int limit,Feed feed) {

        firestore.collection("POSTSANDSTORIES").document(uname).collection("POSTS").limit(limit)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                FeedModel feedModel = new FeedModel();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    feedModel.setUsername(uname);
                    feedModel.setThumbnailUrl(doc.getString("thumbnailUrl"));
                    feedModel.setImageUrl(doc.getString("imageUrl"));
                    feedModel.setCaption(doc.getString("caption"));
                    feedModel.setId(doc.getId());
                    feed.onResult(feedModel);

                }
            }
        });


    }


    public static void firebaseRetrieveFollowings(String uname, FollowingInterface fi) {


        firestore.collection("POSTSANDSTORIES").document(uname).collection("FOLLOWING").get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> usersf = new ArrayList<>();

                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    usersf.add(doc.getId());

                }

                fi.onFollowingRetrieved(usersf);
            }
        });

    }


    public static void snapFirebase(String u, snapInterface si) {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int strDate = Integer.parseInt(formatter.format(date));

        ListenerRegistration var = firestore.collection("POSTSANDSTORIES").document(u).collection("POSTS")
                .whereEqualTo("date", strDate).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value != null) {
                            ArrayList<FeedModel> arr = new ArrayList<>();
                            for (DocumentChange doc : value.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    FeedModel f = new FeedModel();

                                    getLikes(doc.getDocument().getId(), u, new LikesInterface() {
                                        @Override
                                        public void onLikesReceived(int likes, boolean liked) {
                                            f.setLikes(likes);
                                            f.setLiked(liked);
                                            FeedAdapterUtil.getAdapter().notifyDataSetChanged();
                                        }
                                    });
                                    f.setId(doc.getDocument().getId());
                                    f.setUsername(u);
                                    f.setCaption(doc.getDocument().getString("caption"));
                                    f.setImageUrl(doc.getDocument().getString("imageUrl"));
                                    f.setThumbnailUrl(doc.getDocument().getString("thumbnailUrl"));
                                    arr.add(f);
                                }
                            }

                            si.onUpdateRetrieved(arr);
                        }
                    }
                });


    }

    public static void likeOrRemove(String username, String postId, LikesORRemoves lor) {

        firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("LIKES")
                .document(prevalentuser.ourData.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("LIKES")
                            .document(prevalentuser.ourData.getUsername()).delete();
                    lor.onResult(true);
                } else {
                    firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("LIKES")
                            .document(prevalentuser.ourData.getUsername()).set(new HashMap<>());
                    lor.onResult(false);
                }
            }
        });
    }


    public static void getLikes(String id, String username, LikesInterface lf) {
        firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(id).collection("LIKES").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean liked = false;
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.getId().equals(prevalentuser.ourData.getUsername())) {
                                liked = true;
                                break;
                            }
                        }
                        lf.onLikesReceived(queryDocumentSnapshots.size(), liked);
                    }
                });
    }

    public static void getTotalLikes(String postId, String username, TotalLikes tl) {

        List<LikesFollowersFollowing> list = new ArrayList<>();

        firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("LIKES")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    LikesFollowersFollowing model = new LikesFollowersFollowing();
                    model.setUsername(doc.getId());

                    firestore.collection("POSTSANDSTORIES").document(doc.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            model.setName(documentSnapshot.getString("name"));
                            model.setProfileUrl(documentSnapshot.getString("profileUrl"));
                            list.add(model);

                        }
                    });

                }

                tl.onResult(list);
            }
        });
    }

    public static void getTotalComments(String postId, String username, TotalComments tc) {

        firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("COMMENTS")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<LikesFollowersFollowing> list = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    List<String> group = (List<String>) doc.get("comment");

                    if (group != null) {

                        for (String s : group) {


                            LikesFollowersFollowing l = new LikesFollowersFollowing();
                            l.setUsername(doc.getId());
                            l.setComment(s);

                            firestore.collection("POSTSANDSTORIES").document(doc.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    l.setProfileUrl(documentSnapshot.getString("profileUrl"));
                                }
                            });
                            list.add(l);

                        }

                    }

                }
                tc.onResult(list);
            }
        });

    }

    public static void postComment(String username, String postId, String comment, PostComments pc) {

        firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("COMMENTS").document(prevalentuser.ourData.getUsername())
                .update("comment", FieldValue.arrayUnion(new String[]{comment})).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pc.onResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Map<String, Object> map = new HashMap<>();
                ArrayList<String> s = new ArrayList<>();
                s.add(comment);
                map.put("comment", s);

                firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("COMMENTS").document(prevalentuser.ourData.getUsername())
                        .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pc.onResult();
                    }
                });
            }
        });


    }

    public static void deleteComment(String username, String postId, String comment) {

        firestore.collection("POSTSANDSTORIES").document(username).collection("POSTS").document(postId).collection("COMMENTS")
                .document(prevalentuser.ourData.getUsername()).update("comment", FieldValue.arrayRemove(comment)).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("success");
            }
        });

    }

    public static void retrieveRequests(String username, UsernameProfile usernameProfile) {

        firestore.collection("POSTSANDSTORIES").document(username).collection("REQUESTS").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {
                    List<Requests> list = new ArrayList<>();
                    for (DocumentChange doc :
                            value.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Requests r = new Requests();
                            r.setUsername(doc.getDocument().getId());

                            searchProfile(doc.getDocument().getId(), new FirebaseInterface() {
                                @Override
                                public void onFirebaseRetrieveComplete(UsersData usersData) {
                                    r.setProfilePic(usersData.getProfilePic());
                                    RequestsAdapterUtil.getAdapter().notifyDataSetChanged();
                                }
                            });
                            list.add(r);
                        }

                    }
                    usernameProfile.onResult(list);
                }
            }
        });


    }


    public interface FirebaseInterface {
        void onFirebaseRetrieveComplete(UsersData usersData);
    }

    public interface PostsInterface {
        void onPostsRetrieved(List<UsersData.PostData> postsData);
    }

    public interface snapInterface {
        void onUpdateRetrieved(ArrayList<FeedModel> fm);
    }

    public interface FollowingInterface {
        void onFollowingRetrieved(List<String> users);
    }

    public interface LikesInterface {
        void onLikesReceived(int likes, boolean liked);
    }

    public interface LikesORRemoves {
        void onResult(boolean exists);
    }

    public interface TotalLikes {
        void onResult(List<LikesFollowersFollowing> list);
    }

    public interface TotalComments {
        void onResult(List<LikesFollowersFollowing> list);
    }

    public interface PostComments {
        void onResult();
    }

    public interface UsernameProfile {
        void onResult(List<Requests> list);
    }

    public interface Feed {
        void onResult(FeedModel feed);
    }
}
