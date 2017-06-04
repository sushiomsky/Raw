package xyz.bitnaesser.raw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.ByteString;

import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.script.ScriptBuilder;

public class DonateActivity extends AppCompatActivity {

    private static final long AMOUNT = 500000;
    private static final String[] DONATION_ADDRESSES_MAINNET = {"18CK5k1gajRKKSC7yVSTXT9LUzbheh1XY4",
            "1PZmMahjbfsTy6DsaRyfStzoWTPppWwDnZ"};
    private static final String MEMO = "Sample donation";
    private static final int REQUEST_CODE = 0;

    private Button donateButton, requestButton;
    private TextView donateMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donate);

        donateButton = (Button) findViewById(R.id.sample_donate_button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                handleDonate();
            }
        });

        donateMessage = (TextView) findViewById(R.id.sample_donate_message);
    }

    private String[] donationAddresses() {
        return DONATION_ADDRESSES_MAINNET;
    }

    private void handleDonate() {
        final String[] addresses = donationAddresses();

        BitcoinIntegration.requestForResult(DonateActivity.this, REQUEST_CODE, addresses[0]);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                final String txHash = BitcoinIntegration.transactionHashFromResult(data);
                if (txHash != null) {
                    final SpannableStringBuilder messageBuilder = new SpannableStringBuilder("Transaction hash:\n");
                    messageBuilder.append(txHash);
                    messageBuilder.setSpan(new TypefaceSpan("monospace"), messageBuilder.length() - txHash.length(),
                            messageBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (BitcoinIntegration.paymentFromResult(data) != null)
                        messageBuilder.append("\n(also a BIP70 payment message was received)");

                    donateMessage.setText(messageBuilder);
                    donateMessage.setVisibility(View.VISIBLE);
                }

                Toast.makeText(this, "Thank you!", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Unknown result.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
