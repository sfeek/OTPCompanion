package geekshavenlabs.otpcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;
import android.view.View;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.text.InputFilter;
import android.content.ClipDescription;


public class MainActivity extends AppCompatActivity {
    boolean fiveChars = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get fields
        final EditText editTextOTP = findViewById(R.id.txtOTP);
        final EditText editTextMessage = findViewById(R.id.txtMessage);
        final TextView textView = findViewById(R.id.txtOutput);

        editTextOTP.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        // **** Setup OTP Field Change Event****
        editTextOTP.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                update(editTextOTP,editTextMessage,textView,fiveChars);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // **** Setup Message Field Change Event****
        editTextMessage.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                update(editTextOTP,editTextMessage,textView,fiveChars);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // **** Setup TextView Field Click Event ****
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiveChars = !fiveChars;
                update(editTextOTP,editTextMessage,textView,fiveChars);
            }
        });
    }

    // Show the Action Bar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Menu for clear and help
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get fields
        final EditText editTextOTP = findViewById(R.id.txtOTP);
        final EditText editTextMessage = findViewById(R.id.txtMessage);
        final TextView textView = findViewById(R.id.txtOutput);

        switch (item.getItemId()) {

            // Clear the fields
            case R.id.action_clear:
                editTextMessage.setText("");
                editTextOTP.setText("");
                textView.setText("");
                return true;

            // Show the Help Pop-up
            case R.id.action_help:
                Toast.makeText(getApplicationContext(), getString(R.string.help), Toast.LENGTH_LONG).show();
                return true;

            // Handle copy from Text Output
            case R.id.action_copy:
                ClipboardManager clipboardc = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);
                ClipData clipc = ClipData.newPlainText("Output Text", textView.getText());
                clipboardc.setPrimaryClip(clipc);
                return true;

            // Handle paster to Text Message
            case R.id.action_paste:
                String textToPaste = "";

                ClipboardManager clipboardp = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);

                if (clipboardp.hasPrimaryClip()) {
                    ClipData clipp = clipboardp.getPrimaryClip();

                    if (clipp.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        textToPaste = clipp.getItemAt(0).getText().toString();
                    }
                }
                editTextMessage.setText(textToPaste);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // Update the Output Window
    private void update(EditText editTextOTP,EditText editTextMessage,TextView textView, Boolean fiveChars)
    {
        // Get clean versions of Message and OTP fields and their lengths
        String cleanMessage = editTextMessage.getText().toString().replaceAll("[^a-zA-Z]","").toUpperCase();
        String cleanOTP = editTextOTP.getText().toString().replaceAll("[^a-zA-Z]","").toUpperCase();
        String complete = "";
        int MLen = cleanMessage.length();
        int OLen = cleanOTP.length();
        int total = Math.min(MLen,OLen);

        // In case one of the other fields is empty
        if (total == 0) textView.setText("");

        // Red if there is not enough OTP text for message
        if (MLen <= OLen)
        {
            editTextOTP.setTextColor(Color.GREEN);
        }
        else
        {
            editTextOTP.setTextColor(Color.RED);
        }

        // Go through characters and convert
        for (int i=0; i < total; i++)
        {
            int v1 = cleanMessage.charAt(i)-65;
            int v2 = cleanOTP.charAt(i)-65;

            int ch = 90-v1-v2;

            if (ch < 65) ch = 90+ch-64;

            if (i % 5 == 0 && fiveChars == true && i != 0) complete += (char)32;

            complete += (char)ch;
            textView.setText(complete);
        }
    }
}
