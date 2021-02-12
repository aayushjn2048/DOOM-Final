package com.doom.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doom.DialogFragment;
import com.doom.MoodActivity;
import com.doom.R;
import com.doom.Settings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    List<String> images;
    Context context;
    DialogFragment dialogFragment;
    Settings settings;

    public AvatarAdapter(List<String> images, Context context, DialogFragment dialogFragment, Settings settings) {
        this.images = images;
        this.context = context;
        this.dialogFragment = dialogFragment;
        this.settings = settings;
    }

    public AvatarAdapter(List<String> images, Context context, DialogFragment dialogFragment) {
        this.images = images;
        this.context = context;
        this.dialogFragment = dialogFragment;
    }

    public AvatarAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_avatar_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        /*try {
            Log.d("tempolala", "it's temporary");
            final File file = File.createTempFile("image","jpg");
            FirebaseStorage.getInstance().getReferenceFromUrl("gs://doom-55ff4.appspot.com/").child(images.get(position))
                    .getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    holder.gridIcon.setImageBitmap(bitmap);
                }
            });
        } catch (IOException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            Toast.makeText(context, errors.toString(), Toast.LENGTH_SHORT).show();
            Log.d("PrintError", errors.toString() + " Lol!!!!");
        }*/
            try {
                final File file = File.createTempFile("image","jpg");
                FirebaseStorage.getInstance().getReference().child("images").child(images.get(position)).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        holder.gridIcon.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MoodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            holder.gridIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("profileImage").setValue(images.get(position)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            settings.recreate();
                            dialogFragment.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("failure",e.getMessage());
                        }
                    });
                }
            });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView gridIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gridIcon = itemView.findViewById(R.id.avatarImage);
        }
    }
}
