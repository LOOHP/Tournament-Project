package com.loohp.sbaclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity<handler> extends AppCompatActivity {

    public Button connectButton;
    public EditText hostText;
    public Button promoteButton;
    public Button demoteButton;
    public EditText promoteText;
    public EditText demoteText;
    public Button cmdButton;
    public EditText cmdText;
    public WebView web;

    public Socket socket;
    public String host;
    public int port;

    public HashMap<String, String> lang = new HashMap<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = findViewById(R.id.connectButton);
        hostText = findViewById(R.id.hostText);
        promoteButton = findViewById(R.id.promoteButton);
        demoteButton = findViewById(R.id.demoteButton);
        promoteText = findViewById(R.id.promoteText);
        demoteText = findViewById(R.id.demoteText);
        cmdButton = findViewById(R.id.cmdButton);
        cmdText = findViewById(R.id.cmdText);
        web = findViewById(R.id.web);

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        String str = "Tournament System                              ";
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(str);
        } else {
            getActionBar().setTitle(str);
        }

        hostText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onConnect(null);
                    return true;
                }
                return false;
            }
        });

        promoteText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onPromote(null);
                    return true;
                }
                return false;
            }
        });

        demoteText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onDemote(null);
                    return true;
                }
                return false;
            }
        });

        hostText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        promoteText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        demoteText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        cmdText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void onConnect(View view) {
        host = hostText.getText().toString();
        port = 1720;
        if (host.contains(":")) {
            port = Integer.parseInt(host.substring(host.indexOf(":") + 1));
            host = host.substring(0, host.indexOf(":"));
        }
        connect();
    }

    public void reload() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (socket.isConnected()) {
                    web.loadUrl(web.getUrl());
                    reload();
                }
            }
        }, 4000);
    }

    public void onPromote(View view) {
        if (promoteText.getText().toString().equals("")) {
            return;
        }
        String cmd = "promote " + promoteText.getText().toString();
        send(cmd);
        promoteText.setText("");
    }

    public void onDemote(View view) {
        if (demoteText.getText().toString().equals("")) {
            return;
        }
        String cmd = "unpromote " + demoteText.getText().toString();
        send(cmd);
        demoteText.setText("");
    }

    public void onSendCmd(View view) {
        if (cmdText.getText().toString().equals("")) {
            return;
        }
        String cmd = cmdText.getText().toString();
        if (!cmd.startsWith("function:")) {
            send(cmd);
        }
        cmdText.setText("");
    }

    public void connect() {
        try {
            if (socket != null) {
                if (socket.isConnected()) {
                    socket.close();
                }
            }
            new Thread(new Runnable(){
                public void run() {
                    try {
                        socket = new Socket(host, port);
                    } catch (final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setPositiveButton("OK", null).setMessage(e.getLocalizedMessage()).setTitle("Disconnected");
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        });
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listen();

                            final String url = "http://" + host + ":8080/";
                            web.loadUrl(url);

                            reload();
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        new Thread(new Runnable(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                while (true) {
                    if (socket != null) {
                        if (socket.isConnected()) {
                            try {
                                boolean done = false;
                                String in = "";
                                byte[] byteArray = new byte[0];
                                while (!done) {
                                    DataInputStream dIn = new DataInputStream(socket.getInputStream());
                                    byte[] each = new byte[dIn.readInt()];
                                    dIn.readFully(each);
                                    byteArray = joinArrayBytes(byteArray, each);
                                    in = new String(byteArray, StandardCharsets.UTF_8);
                                    if (in.startsWith("<Packet>") && in.endsWith("</Packet>")) {
                                        done = true;
                                    }
                                }

                                in = in.replaceAll("^<Packet>", "");
                                in = in.replaceAll("<\\/Packet>$","");

                                if (in.startsWith("function:serverlang")) {
                                    String data = in.substring(in.indexOf("function:serverlang=") + 20);
                                    lang.clear();
                                    lang.putAll((HashMap<String, String>) deserialize(data));

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (lang.get("PromoteButton.Text") != null) {
                                                promoteButton.setText(lang.get("PromoteButton.Text"));
                                            }
                                            if (lang.get("PromoteText.Text") != null) {
                                                promoteText.setText(lang.get("PromoteText.Text"));
                                            }

                                            if (lang.get("DemoteButton.Text") != null) {
                                                demoteButton.setText(lang.get("DemoteButton.Text"));
                                            }

                                            if (lang.get("DemoteText.Text") != null) {
                                                demoteText.setText(lang.get("DemoteText.Text"));
                                            }
                                        }
                                    });

                                    if (lang.get("Title") != null) {
                                        String str = lang.get("Title").replace("%s", socket.getInetAddress().getHostName());
                                        if (getSupportActionBar() != null) {
                                            getSupportActionBar().setTitle(str);
                                        } else {
                                            getActionBar().setTitle(str);
                                        }
                                    }
                                }
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void send(final String cmd) {
        if (socket == null) {
            return;
        } else if (!socket.isConnected()) {
            connect();
        }
        new Thread(new Runnable(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run(){
                try {
                    String args = "<Packet>" + cmd + "</Packet>";
                    byte[] byteArray = args.getBytes(StandardCharsets.UTF_8);
                    byte[][] splitArray = splitArrayBytes(byteArray, 32768);

                    for (byte[] each : splitArray) {
                        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                        dOut.writeInt(each.length);
                        dOut.write(each);
                        dOut.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static Object deserialize(String s) {
        try {
            byte[] data = android.util.Base64.decode(s, android.util.Base64.DEFAULT);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[][] splitArrayBytes(byte[] arrayToSplit, int chunkSize){
        if(chunkSize<=0){
            return null;
        }
        int rest = arrayToSplit.length % chunkSize;
        int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);
        byte[][] arrays = new byte[chunks][];
        for(int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++){
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
        }
        if(rest > 0){
            arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
        }
        return arrays;
    }

    public static byte[] joinArrayBytes(byte[] one, byte[] two) {
        byte[] combined = new byte[one.length + two.length];

        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }
        return combined;
    }
}
