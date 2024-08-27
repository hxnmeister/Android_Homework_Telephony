package com.ua.project.android_homework_telephony.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ua.project.android_homework_telephony.R;
import com.ua.project.android_homework_telephony.interfaces.CallHandler;
import com.ua.project.android_homework_telephony.interfaces.SmsHandler;
import com.ua.project.android_homework_telephony.models.Contact;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private final Activity activity;
    private final SmsHandler smsHandler;
    private final CallHandler callHandler;
    private final List<Contact> contactsList;

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private Integer id;
        private final TextView nameTextView;
        private final TextView phoneNumberTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactsList.get(position);

        holder.id = contact.getId();
        holder.nameTextView.setText(String.format("%s %s",
                contact.getFirstName() == null ? "" : contact.getFirstName(),
                contact.getLastName() == null ? "" : contact.getLastName()));
        holder.phoneNumberTextView.setText(contact.getPhoneNumber());

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(activity, v);

            popupMenu.inflate(R.menu.contact_popup);
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if(itemId == R.id.callContactMenuItem) {
                    callHandler.makeCall(contact.getPhoneNumber());
                }
                else if(itemId == R.id.smsContactMenuItem) {
                    showSmsSendDialog(contact.getPhoneNumber());
                }

                return true;
            });
            popupMenu.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    private void showSmsSendDialog(String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final EditText input = new EditText(activity);

        builder.setTitle("Send SMS");
        input.setHint("Enter your message");
        builder.setView(input);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String message = input.getText().toString();
            smsHandler.sendSms(phoneNumber, message);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
