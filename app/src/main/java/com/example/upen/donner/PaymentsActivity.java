package com.example.upen.donner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalPayment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckoutButton launchPayPalButton;
    final static public int PAYPAL_BUTTON_ID = 10001;
    private static final int REQUEST_PAYPAL_CHECKOUT = 2;
    // Keeps a reference to the progress dialog
    private ProgressDialog _progressDialog;
    private boolean _paypalLibraryInit = false;
    private boolean _progressDialogRunning = false;
    private int amount = 0;

    private Organization thisOrg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        // The organisation that they are donating
        Intent intent = getIntent();
        String id = intent.getStringExtra("orgId");
        ParseQuery<Organization> query = ParseQuery.getQuery("Organisation");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<Organization>() {
            @Override
            public void done(List<Organization> list, ParseException e) {
                if (e == null && thisOrg != null) {
                    thisOrg = list.get(0);
                }
            }
        });

        // Initialize the library.
        if (this.isOnline()) {
            Thread libraryInitializationThread = new Thread() {
                public void run() {
                    initLibrary();
                }
            };

            libraryInitializationThread.start();
        }

        if (_paypalLibraryInit) {
            showPayPalButton();
        } else {
            // Display a progress dialog to the user and start checking for when
            // the initialization is completed
            _progressDialog = new ProgressDialog(this);
            _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            _progressDialog.setMessage("Loading PayPal Payment Library");
            _progressDialog.setCancelable(false);
            _progressDialog.show();
            _progressDialogRunning = true;
            Thread newThread = new Thread(checkforPayPalInitRunnable);
            newThread.start();
        }
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id == PAYPAL_BUTTON_ID){
            PayPalButtonClick(v);
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**********************************
     * PayPal library related methods
     **********************************/

    // This lets us show the PayPal Button after the library has been
    // initialized
    final Runnable showPayPalButtonRunnable = new Runnable() {
        public void run() {
            showPayPalButton();
        }
    };

    // This lets us run a loop to check the status of the PayPal Library init
    final Runnable checkforPayPalInitRunnable = new Runnable() {
        public void run() {
            checkForPayPalLibraryInit();
        }
    };

    // This method is called if the Review page is being loaded but the PayPal
    // Library is not
    // initialized yet.
    private void checkForPayPalLibraryInit() {
        // Loop as long as the library is not initialized
        while (!_paypalLibraryInit) {
            try {
                // wait 1/2 a second then check again
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Show an error to the user
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Error initializing PayPal Library")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // Could do anything here to handle the
                                        // error
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        // If we got here, it means the library is initialized.
        // So, add the "Pay with PayPal" button to the screen
        _progressDialog.dismiss();
        runOnUiThread(showPayPalButtonRunnable);
    }

    /**
     * The initLibrary function takes care of all the basic Library
     * initialization.
     *
     * @return The return will be true if the initialization was successful and
     *         false if
     */
    public void initLibrary() {
        PayPal pp = PayPal.getInstance();
        // If the library is already initialized, then we don't need to
        // initialize it again.
        if (pp == null) {
            // This is the main initialization call that takes in your Context,
            // the Application ID, and the server you would like to connect to.
            pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
                    PayPal.ENV_SANDBOX);

            // -- These are required settings.
            pp.setLanguage("en_US"); // Sets the language for the library.
            // --

            // -- These are a few of the optional settings.
            // Sets the fees payer. If there are fees for the transaction, this
            // person will pay for them. Possible values are FEEPAYER_SENDER,
            // FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and
            // FEEPAYER_SECONDARYONLY.
            pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
            // Set to true if the transaction will require shipping.
            pp.setShippingEnabled(false);
            // --
            _paypalLibraryInit = true;
        }
    }

    /** this method generates the PayPal checkout button and adds it to the current view
     *  using the relative layout params
     */
    private void showPayPalButton() {
        removePayPalButton();
        // Back in the UI thread -- show the "Pay with PayPal" button
        // Generate the PayPal Checkout button and save it for later use
        PayPal pp = PayPal.getInstance();
        launchPayPalButton = pp.getCheckoutButton(this, PayPal.BUTTON_278x43,
                CheckoutButton.TEXT_PAY);
        // You'll need to have an OnClickListener for the CheckoutButton.
        launchPayPalButton.setOnClickListener(this);
        // add it to the layout
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 10;

        launchPayPalButton.setLayoutParams(params);
        launchPayPalButton.setId(PAYPAL_BUTTON_ID);
        ((RelativeLayout) findViewById(R.id.relative_layout_payments))
                .addView(launchPayPalButton);
        ((RelativeLayout) findViewById(R.id.relative_layout_payments))
                .setGravity(Gravity.CENTER_HORIZONTAL);
        if (_progressDialogRunning) {
            _progressDialog.dismiss();
            _progressDialogRunning = false;
        }
    }

    /* this method removes the PayPal button from the view
     */
    private void removePayPalButton() {
        // Avoid an exception for setting a parent more than once
        if (launchPayPalButton != null) {
            ((RelativeLayout) findViewById(R.id.relative_layout_payments))
                    .removeView(launchPayPalButton);
        }
    }

    /* method to handle PayPal checkout button onClick event
     * - this must be called from the onClick() method implemented by the application
     */
    public void PayPalButtonClick(View arg0) {

        // Create a basic PayPalPayment.
        PayPalPayment payment = new PayPalPayment();
        // Sets the currency type for this payment.
        payment.setCurrencyType("USD");
        // Sets the recipient for the payment. This can also be a phone
        // number.
        String email = thisOrg.getEmail();
        payment.setRecipient(email);
        // Sets the amount of the payment, not including tax and shipping
        // amounts.
        amount = Integer.parseInt(
                ((EditText) (findViewById(R.id.text_donate_amount)))
                        .getText().toString());
        BigDecimal st = new BigDecimal(amount);
        st = st.setScale(2, RoundingMode.HALF_UP);
        payment.setSubtotal(st);
        // Sets the payment type. This can be PAYMENT_TYPE_GOODS,
        // PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or
        // PAYMENT_TYPE_NONE.
        payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);

        // PayPalInvoiceData can contain tax and shipping amounts. It also
        // contains an ArrayList of PayPalInvoiceItem which can
        // be filled out. These are not required for any transaction.
        PayPalInvoiceData invoice = new PayPalInvoiceData();
        // Sets the tax amount.
        BigDecimal tax = new BigDecimal(0);
        tax = tax.setScale(2, RoundingMode.HALF_UP);
        invoice.setTax(tax);


        // Sets the PayPalPayment invoice data.
        payment.setInvoiceData(invoice);
        // Sets the merchant name. This is the name of your Application or
        // Company.
        payment.setMerchantName(thisOrg.getName());


        // Use checkout to create our Intent.
        Intent checkoutIntent = PayPal.getInstance()
                .checkout(payment, this /*, new ResultDelegate()*/);
        // Use the android's startActivityForResult() and pass in our
        // Intent.
        // This will start the library.
        startActivityForResult(checkoutIntent, REQUEST_PAYPAL_CHECKOUT);
    }

    /* This method handles the PayPal Activity Results. This handles all the responses from the PayPal
     * Payments Library.
     *  This method must be called from the application's onActivityResult() handler
     */
    public void PayPalActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                // The payment succeeded
                String payKey = intent
                        .getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                Toast.makeText(this, "Payment Succeeded! Thank You!", Toast.LENGTH_LONG).show();
                thisOrg.setAmount(thisOrg.getAmount()+amount);
                thisOrg.saveEventually();
                break;
            case Activity.RESULT_CANCELED:
                // The payment was canceled
                Toast.makeText(this, "Payment Canceled", Toast.LENGTH_LONG).show();
                break;
            case PayPalActivity.RESULT_FAILURE:
                // The payment failed -- we get the error from the
                // EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
                String errorID = intent
                        .getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                String errorMessage = intent
                        .getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                Toast.makeText(this, "Payment Failed: \t" + errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
