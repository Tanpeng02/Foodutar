package com.example.foodie;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.foodie.menu.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DiscussionCommunity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseFirestore db; // Firestore instance
    private StorageReference storageRef; // Reference to Firebase Storage
    private LinearLayout commentBar;
    private EditText contentPose;
    private TextView postMessage;
    private ImageView uploadButton;
    private ProgressBar uploadProgressBar;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private ImageView selectedImage;
    private LinearLayout postLinearLayout;
    private ImageView cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_community);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Find views
        selectedImage = findViewById(R.id.importPictureButton);
        commentBar = findViewById(R.id.commentBar);
        contentPose = findViewById(R.id.content_pose);
        postMessage = findViewById(R.id.post_message);
        uploadButton = findViewById(R.id.uploadButton);
        uploadProgressBar = findViewById(R.id.uploadprogressBar);
        selectedImageView = findViewById(R.id.selectedImageView);
        postLinearLayout = findViewById(R.id.dcpostLinearLayout);
        cancelButton = findViewById(R.id.cancel_button);

        // Initially hide the progress bar
        uploadProgressBar.setVisibility(View.GONE);
        selectedImageView.setVisibility(View.GONE);

        fetchPostsFromDatabase();

        // Set click listener for the upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // Inside onCreate() method, set OnClickListener for the closeButton
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the selected image URI
                selectedImageUri = null;
                // Hide the ImageView
                selectedImageView.setVisibility(View.GONE);
                // Hide the close button
                cancelButton.setVisibility(View.GONE);
            }
        });

    }

    public void selectImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select or Capture Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                // Load the selected image into selectedImageView
                Glide.with(this)
                        .load(selectedImageUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(selectedImageView);
                selectedImageView.setVisibility(View.VISIBLE); // Make the ImageView visible
                cancelButton.setVisibility(View.VISIBLE);
            } else if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // Convert the captured bitmap to a URI
                selectedImageUri = getImageUri(getApplicationContext(), imageBitmap);
                // Load the selected image into selectedImageView
                selectedImageView.setImageBitmap(imageBitmap);
                selectedImageView.setVisibility(View.VISIBLE); // Make the ImageView visible

            }
        }
    }

    public Uri getImageUri(android.content.Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageToFirebase(Uri imageUri, UploadImageCallback uploadImageCallback) {
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        final StorageReference imagesRef = storageRef.child("images/" + fileName);

        imagesRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadProgressBar.setVisibility(View.GONE); // Hide progress bar on success
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                uploadImageCallback.onSuccess(imageUrl);
                                showToast("Image uploaded successfully");
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadProgressBar.setVisibility(View.GONE); // Hide progress bar on failure
                        showToast("Failed to upload image");
                    }
                });
    }

    public void uploadPost() {
        final String content = contentPose.getText().toString().trim();
        String username = UserData.getInstance().getUsername();

        if (TextUtils.isEmpty(content)) {
            showToast("Please enter content");
            return;
        }

        // Get current date and time
        final String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final long timestamp = System.currentTimeMillis(); // Get current timestamp

        uploadProgressBar.setVisibility(View.VISIBLE); // Show progress bar

        // Check if an image is selected
        if (selectedImageUri != null) {
            // Upload the image to Firebase Storage
            uploadImageToFirebase(selectedImageUri, new UploadImageCallback() {
                @Override
                public void onSuccess(String imageUrl) {

                    // Image uploaded successfully, create a Post object with image URL
                    Map<String, Object> post = new HashMap<>();
                    post.put("content", content);
                    post.put("imageUrl", imageUrl);
                    post.put("date", currentDate);
                    post.put("time", currentTime);
                    post.put("timestamp", timestamp);
                    post.put("username", username);
                    post.put("totallike", 0);

                    // Add the post to Firestore
                    db.collection("posts")
                            .add(post)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    showToast("Post uploaded successfully");
                                    contentPose.setText(""); // Clear the content field after uploading
                                    // Clear the selected image URI
                                    selectedImageUri = null;
                                    // Hide the ImageView
                                    cancelButton.setVisibility(View.GONE);
                                    selectedImageView.setVisibility(View.GONE);
                                    uploadProgressBar.setVisibility(View.GONE); // Hide progress bar on success
                                    fetchPostsFromDatabase();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    cancelButton.setVisibility(View.GONE);
                                    uploadProgressBar.setVisibility(View.GONE); // Hide progress bar on success
                                    showToast("Failed to upload post");
                                }
                            });
                }

                @Override
                public void onFailure() {
                    cancelButton.setVisibility(View.GONE);
                    uploadProgressBar.setVisibility(View.GONE); // Hide progress bar on success
                    showToast("Failed to upload image");
                }
            });
        } else {
            // No image selected, upload the post without an image
            Map<String, Object> post = new HashMap<>();
            post.put("content", content);
            post.put("imageUrl", "");
            post.put("date", currentDate);
            post.put("time", currentTime);
            post.put("timestamp", timestamp);
            post.put("username", username);
            post.put("totallike", 0);

            // Add the post to Firestore
            db.collection("posts")
                    .add(post)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            showToast("Post uploaded successfully");
                            contentPose.setText(""); // Clear the content field after uploading
                            // Hide the ImageView
                            selectedImageView.setVisibility(View.GONE);
                            uploadProgressBar.setVisibility(View.GONE); // Hide progress bar on success
                            fetchPostsFromDatabase();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            uploadProgressBar.setVisibility(View.GONE); // Hide progress bar on success
                            showToast("Failed to upload post");
                        }
                    });
        }
    }

    interface UploadImageCallback {
        void onSuccess(String imageUrl);
        void onFailure();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Fetch posts data from the database
    public void fetchPostsFromDatabase() {
        db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING) // Sort by timestamp in descending order
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Post> posts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String postId = document.getId();
                                // Retrieve each post's data and add them to the list
                                Post post = document.toObject(Post.class);
                                post.setId(postId);
                                posts.add(post);
                            }

                            if (!posts.isEmpty()){
                                LinearLayout postLinearLayout = findViewById(R.id.dcpostLinearLayout);
                                postLinearLayout.removeAllViews(); // Clear previous posts
                                // Update the layout with the sorted post data
                                updatePostsFromDatabase(posts);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Update the post list view
    public void updatePostsFromDatabase(List<Post> posts) {
        LinearLayout postLinearLayout = findViewById(R.id.dcpostLinearLayout);

        for (Post post : posts) {
            // Inflate the cardview layout
            View postView = getLayoutInflater().inflate(R.layout.post, null);

            // Find views in the inflated layout
            TextView postUsername = postView.findViewById(R.id.post_username);
            TextView postContent = postView.findViewById(R.id.post_content);
            TextView postDate = postView.findViewById(R.id.post_date);
            TextView postTime = postView.findViewById(R.id.post_time);
            ImageView postImage = postView.findViewById(R.id.post_image);
            ImageView postLikeButton = postView.findViewById(R.id.post_like_button);
            ImageView postDislikeButton = postView.findViewById(R.id.post_dislike_button);
            TextView postTotalLike = postView.findViewById(R.id.post_like_total);

            // Populate views with post data
            postUsername.setText(post.getUsername());
            postContent.setText(post.getContent());
            postDate.setText(post.getDate());
            postTime.setText(post.getTime());
            postTotalLike.setText(String.valueOf(post.getTotalLike()));
            postView.setTag(post.getId());

            // Load image using Glide or Picasso if imageUrl is not empty
            if (!TextUtils.isEmpty(post.getImageUrl())) {
                Glide.with(this)
                        .load(post.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(postImage);
            } else {
                // If imageUrl is empty, hide the ImageView
                postImage.setVisibility(View.GONE);
            }
            checkUserLikeDislikeInteraction(post.getId(), UserData.getInstance().getUsername(),post, postLikeButton, postDislikeButton);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Toggle the selection state of the like button when clicked
                    postLikeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            postLikeButton.setSelected(true);
                            checkUserInteraction(post.getId(), UserData.getInstance().getUsername(), "like", post, postLikeButton, postDislikeButton);
                        }
                    });

                    // Toggle the selection state of the dislike button when clicked
                    postDislikeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            postDislikeButton.setSelected(true);
                            checkUserInteraction(post.getId(), UserData.getInstance().getUsername(), "dislike", post, postLikeButton, postDislikeButton);
                        }
                    });

                    // Add the cardview to the LinearLayout
                    postLinearLayout.addView(postView);
                }
            },300);

        }
    }

    private void checkUserLikeDislikeInteraction(String postId, String userId, Post post, ImageView postLikeButton, ImageView postDislikeButton) {
        db.collection("userInteractions")
                .whereEqualTo("postId", postId)
                .whereEqualTo("userId", userId)
                .whereIn("interactionType", Arrays.asList("like", "dislike"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean userLiked = false;
                            boolean userDisliked = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String interactionType = document.getString("interactionType");
                                if ("like".equals(interactionType)) {
                                    userLiked = true;
                                } else if ("dislike".equals(interactionType)) {
                                    userDisliked = true;
                                }
                            }
                            if (userLiked) {
                                postLikeButton.setSelected(true);
                            }
                            if (userDisliked) {
                                postDislikeButton.setSelected(true);
                            }
                        } else {
                            Log.e(TAG, "Error getting user interactions: ", task.getException());
                        }
                    }
                });
    }


    private void updateTotalLikesInDatabase(Post post) {
        // Update total likes count in Firestore
        db.collection("posts")
                .document(post.getId())
                .update("totallike", post.getTotalLike())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update total likes count
                        // Handle failure
                    }
                });
    }


    private void updateTotalLikesInUI(String postId, int totalLikes) {
        Log.d("UpdateTotalLikes", "postId: " + postId); // Log postId before finding the view
        // Find the view corresponding to the post ID
        View postView = postLinearLayout.findViewWithTag(postId);
        Log.d("UpdateTotalLikes", "postView: " + String.valueOf(postView)); // Log postView
        if (postView != null) {
            // Update the TextView or any other UI component with the new total likes count
            TextView totalLikeTextView = postView.findViewById(R.id.post_like_total);
            if (totalLikeTextView != null) {
                totalLikeTextView.setText(String.valueOf(totalLikes));
            } else {
                Log.e("UpdateTotalLikes", "totalLikeTextView is null"); // Log if totalLikeTextView is null
            }
        } else {
            Log.e("UpdateTotalLikes", "postView is null"); // Log if postView is null
        }
    }



    // Track user interactions in Firestore
    private void recordUserInteraction(String postId, String userId, String interactionType) {
        Map<String, Object> interactionData = new HashMap<>();
        interactionData.put("postId", postId);
        interactionData.put("userId", userId);
        interactionData.put("interactionType", interactionType);

        // Add the interaction to the 'userInteractions' collection in Firestore
        db.collection("userInteractions")
                .add(interactionData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Interaction recorded successfully
                        // Handle success
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to record interaction
                        // Handle failure
                    }
                });
    }

    // Check if the user has already liked or disliked the post
    private void checkUserInteraction(String postId, String userId, final String interactionType, final Post post, final ImageView postLikeButton, final ImageView postDislikeButton) {
        db.collection("userInteractions")
                .whereEqualTo("postId", postId)
                .whereEqualTo("userId", userId)
                .whereEqualTo("interactionType", interactionType)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // User hasn't interacted with this post before for this interaction type
                                // Allow interaction
                                handleInteraction(postId, userId, interactionType, post, postLikeButton, postDislikeButton);
                            } else {
                                // User has already interacted with this post for this interaction type
                                // Prevent multiple interactions

                                showToast("You have already " + interactionType + "d this post.");
                            }
                        } else {
                            // Error occurred while checking user interaction
                            showToast("Failed to check user interaction.");
                        }
                    }
                });
    }

    // Inside handleInteraction() method
    private void handleInteraction(String postId, String userId, String interactionType, Post post, ImageView postLikeButton, ImageView postDislikeButton) {
        if (interactionType.equals("like")) {
            // Check if the user has already disliked the post
            checkDislikeInteraction(postId, userId, post, postLikeButton, postDislikeButton);
        } else if (interactionType.equals("dislike")) {
            // Check if the user has already liked the post
            checkLikeInteraction(postId, userId, post, postLikeButton, postDislikeButton);
        }
    }

    // Inside checkDislikeInteraction() method
    private void checkDislikeInteraction(String postId, String userId, Post post, ImageView postLikeButton, ImageView postDislikeButton) {
        db.collection("userInteractions")
                .whereEqualTo("postId", postId)
                .whereEqualTo("userId", userId)
                .whereEqualTo("interactionType", "dislike")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // User has disliked the post before, delete the dislike interaction
                                deleteDislikeInteraction(task.getResult().getDocuments().get(0).getId(), post, postLikeButton, postDislikeButton);
                            } else {
                                // User hasn't disliked the post before, continue with like action
                                updateLikes(post, true, postLikeButton, postDislikeButton);
                            }
                        } else {
                            // Error occurred while checking dislike interaction
                            showToast("Failed to check dislike interaction");
                        }
                    }
                });
    }

    // Inside checkLikeInteraction() method
    private void checkLikeInteraction(String postId, String userId, Post post, ImageView postLikeButton, ImageView postDislikeButton) {
        db.collection("userInteractions")
                .whereEqualTo("postId", postId)
                .whereEqualTo("userId", userId)
                .whereEqualTo("interactionType", "like")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // User has liked the post before, delete the like interaction
                                deleteLikeInteraction(task.getResult().getDocuments().get(0).getId(), post, postLikeButton, postDislikeButton);
                            } else {
                                // User hasn't liked the post before, continue with dislike action
                                updateLikes(post, false, postLikeButton, postDislikeButton);
                            }
                        } else {
                            // Error occurred while checking like interaction
                            showToast("Failed to check like interaction");
                        }
                    }
                });
    }

    // Inside deleteDislikeInteraction() method
    private void deleteDislikeInteraction(String interactionId, Post post, ImageView postLikeButton, ImageView postDislikeButton) {
        db.collection("userInteractions")
                .document(interactionId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Dislike interaction deleted successfully, continue with like action
                        updateLikes(post, true, postLikeButton, postDislikeButton);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete dislike interaction
                        showToast("Failed to delete dislike interaction");
                    }
                });
    }

    // Inside deleteLikeInteraction() method
    private void deleteLikeInteraction(String interactionId, Post post, ImageView postLikeButton, ImageView postDislikeButton) {
        db.collection("userInteractions")
                .document(interactionId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Like interaction deleted successfully, continue with dislike action
                        updateLikes(post, false, postLikeButton, postDislikeButton);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete like interaction
                        showToast("Failed to delete like interaction");
                    }
                });
    }

    private void updateLikes(Post post, boolean isLike, ImageView postLikeButton, ImageView postDislikeButton) {
        // Perform like or dislike action here
        int currentLikes = post.getTotalLike();
        if (isLike) {
            // Update total likes count and record user interaction
            post.setTotalLike(currentLikes + 1);
            recordUserInteraction(post.getId(), UserData.getInstance().getUsername(), "like");
        } else {
            // Update total dislikes count and record user interaction
            post.setTotalLike(currentLikes - 1);
            recordUserInteraction(post.getId(), UserData.getInstance().getUsername(), "dislike");
        }
        // Update total likes count in the database
        updateTotalLikesInDatabase(post);
        // Update the UI
        updateTotalLikesInUI(post.getId(), post.getTotalLike());


        // Update the selected state of like and dislike buttons based on the current user interaction
        if (postLikeButton != null && postDislikeButton != null) {
            // Check if the user has liked the post
            if (isLike) {
                postLikeButton.setSelected(true); // Set like button selected
                postDislikeButton.setSelected(false); // Clear dislike button selection
            } else {
                postLikeButton.setSelected(false); // Clear like button selection
                postDislikeButton.setSelected(true); // Set dislike button selected
            }
        }
    }


}