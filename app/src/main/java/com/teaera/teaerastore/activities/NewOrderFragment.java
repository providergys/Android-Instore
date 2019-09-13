package com.teaera.teaerastore.activities;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.teaera.teaerastore.R;
import com.teaera.teaerastore.adapter.DetailsOrderListAdapter;
import com.teaera.teaerastore.adapter.OrderListAdapter;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Model.OrderItemInfo;
import com.teaera.teaerastore.net.Request.GetOrdersRequest;
import com.teaera.teaerastore.net.Request.SearchOrderRequest;
import com.teaera.teaerastore.net.Request.UpdateOrderRequest;
import com.teaera.teaerastore.net.Response.BaseResponse;
import com.teaera.teaerastore.net.Response.GetOrdersResponse;
import com.teaera.teaerastore.net.Response.SearchOrdersResponse;
import com.teaera.teaerastore.preference.StorePrefs;

import com.teaera.teaerastore.utils.DialogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.UserDictionary.AUTHORITY;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderFragment extends Fragment implements View.OnClickListener, OrderListAdapter.OnOrderItemClickListener {

    private OrderListAdapter orderListAdapter;
    private ListView orderListView;
    private DetailsOrderListAdapter detailsOrderListAdapter;
    private ListView detailsOrderListView;
    public static boolean progress_status= false;
    private TextView orderNumberTextView;
    private TextView dateTextView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView subtotalTextView;
    private TextView creditTextView;
    private TextView taxTextView;
    private TextView totalTextView;
    private TextView rewardTextView;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText orderEditText;
    private TextView fromTextView;
    private TextView toTextView;

    private ImageView statusImageView;
    private Button progressStatusButton;
    private Button readyStatusButton;
    private Button completedStatusButton;
    private Button refundButton;

    private RelativeLayout customerRelativeLayout;
    private RelativeLayout searchRelativeLayout;
    private RelativeLayout noResultLayout;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private ProgressDialog dialog;
    int pageNumber = 1;
    int selectedOrder = 0;
    int order_print=0;
    public ArrayList<OrderInfo> orders = new ArrayList<OrderInfo>();
    public boolean isSearched = false;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    Button test_pdf;
    Activity ac;
    AlertDialog alertDialog;

//    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
//            Font.BOLD);
//    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14,
//            Font.NORMAL);
//    private static Font dateFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
//            Font.BOLD);

    public NewOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_order, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    @Override
    public void onResume() {
        super.onResume();

        init();
        ac = getActivity();
    }
    BluetoothAdapter getBluetoothAdapter() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            final Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBtIntent);
        }
        return bluetoothAdapter;
    }
    private void init() {
//        System.out.println("dgggggggggggggggg"+"yha aaya new order");
//        mDialog = new MsgDialog(getActivity());
//        mHandle = new MsgHandle(getActivity(), mDialog);
//        myPrint = new PdfPrint(getActivity(), mHandle, mDialog);
//        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
//        myPrint.setBluetoothAdapter(bluetoothAdapter);
//        connectPrinter();
        orderListView = getActivity().findViewById(R.id.orderListView);
        orderListAdapter = new OrderListAdapter(getActivity(), orders, (OrderListAdapter.OnOrderItemClickListener) this);
        orderListView.setAdapter(orderListAdapter);
        detailsOrderListView = getActivity().findViewById(R.id.detailsOrderListView);

        orderNumberTextView = getActivity().findViewById(R.id.orderNumberTextView);

        dateTextView = getActivity().findViewById(R.id.dateTextView);
        nameTextView = getActivity().findViewById(R.id.nameTextView);
        emailTextView = getActivity().findViewById(R.id.emailTextView);
        subtotalTextView = getActivity().findViewById(R.id.subtotalTextView);
        creditTextView = getActivity().findViewById(R.id.creditTextView);
        taxTextView = getActivity().findViewById(R.id.taxTextView);
        totalTextView = getActivity().findViewById(R.id.totalTextView);
        rewardTextView = getActivity().findViewById(R.id.rewardTextView);

        firstNameEditText = getActivity().findViewById(R.id.firstNameEditText);
        lastNameEditText = getActivity().findViewById(R.id.lastNameEditText);
        orderEditText = getActivity().findViewById(R.id.orderEditText);
        fromTextView = getActivity().findViewById(R.id.fromTextView);
        toTextView = getActivity().findViewById(R.id.toTextView);
        fromTextView.setOnClickListener(this);
        toTextView.setOnClickListener(this);

//        test_pdf= getActivity().findViewById(R.id.test_pdf);
//
//        test_pdf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dexter.withActivity(ac).withPermissions(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                ).withListener(new MultiplePermissionsListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//                        Toast.makeText(ac, "permission granted", Toast.LENGTH_SHORT).show();
//
//                        try {
//
//                                printPDF();
//
//
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                    }
//
//                }).check();
//
//            }
//        });

        statusImageView = getActivity().findViewById(R.id.statusImageView);
        progressStatusButton = getActivity().findViewById(R.id.progressStatusButton);
        progressStatusButton.setOnClickListener(this);
        readyStatusButton = getActivity().findViewById(R.id.readyStatusButton);
        readyStatusButton.setOnClickListener(this);
        completedStatusButton = getActivity().findViewById(R.id.completedStatusButton);
        completedStatusButton.setOnClickListener(this);

        customerRelativeLayout = getActivity().findViewById(R.id.customerRelativeLayout);
        customerRelativeLayout.setOnClickListener(this);

        searchRelativeLayout = getActivity().findViewById(R.id.searchRelativeLayout);
        searchRelativeLayout.setVisibility(View.GONE);

        noResultLayout = getActivity().findViewById(R.id.noResultLayout);
        noResultLayout.setVisibility(View.GONE);

        refundButton = getActivity().findViewById(R.id.refundButton);
        refundButton.setOnClickListener(this);

        ImageButton searchButton = getActivity().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        ImageButton searchImageButton = getActivity().findViewById(R.id.searchImageButton);
        searchImageButton.setOnClickListener(this);

        ImageButton closeSearchButton = getActivity().findViewById(R.id.closeSearchButton);
        closeSearchButton.setOnClickListener(this);

        Calendar now = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                fromTextView.setText(i + "-" + (i1 + 1) + "-" + i2);
            }
        };

        fromDatePickerDialog = new DatePickerDialog(
                getActivity(), fromDateSetListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                toTextView.setText(i + "-" + (i1 + 1) + "-" + i2);
            }
        };

        toDatePickerDialog = new DatePickerDialog(
                getActivity(), toDateSetListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        pageNumber = 1;
        loadOrders(pageNumber);
        isSearched = false;

    }

    private void loadOrders(int page) {
        showLoader(R.string.empty);

        Application.getServerApi().getNewOrders(new GetOrdersRequest(StorePrefs.getStoreInfo(getActivity()).getId(), page)).enqueue(new Callback<GetOrdersResponse>(){

            @Override
            public void onResponse(Call<GetOrdersResponse> call, Response<GetOrdersResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    orders = response.body().getOrders();
                    pageNumber = Integer.parseInt(response.body().getPageNumber());

                    updateOrderList(0);
                }
            }

            @Override
            public void onFailure(Call<GetOrdersResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("New Order", t.getLocalizedMessage());
                } else {
                    Log.d("New Order", "Unknown error");
                }
            }
        });
    }

    private void updateOrderList(int position) {
        orderListAdapter.updateOrders(orders, position);
        orderListAdapter.notifyDataSetChanged();
        updateOrderDetails(position);
        selectedOrder = position;
        order_print=position;
    }

    private void updateOrderDetails(int position) {

        if (orders.size() == 0) {
            orderNumberTextView.setText("");
            dateTextView.setText("");
            nameTextView.setText("");
            emailTextView.setText("");
            subtotalTextView.setText("");
            creditTextView.setText("");
            taxTextView.setText("");
            totalTextView.setText("");
            rewardTextView.setText("");

            showStatus("4");
            noResultLayout.setVisibility(View.VISIBLE);
            return;
        }

        noResultLayout.setVisibility(View.GONE);

        OrderInfo info = orders.get(position);
        int orderId = Integer.parseInt(info.getId());
        orderNumberTextView.setText(String.format("ORDER #%05d", orderId));
        dateTextView.setText(getReceivedDate(info.getTimestamp()));
        nameTextView.setText(info.getUserName());
        emailTextView.setText(info.getEmail());
        subtotalTextView.setText(String.format("$%.2f",Float.parseFloat(info.getSubTotal())));
        creditTextView.setText(String.format("$%.2f",Float.parseFloat(info.getRewardsCredit())));
        taxTextView.setText(String.format("$%.2f",Float.parseFloat(info.getTaxAmount())));
        totalTextView.setText(String.format("$%.2f",Float.parseFloat(info.getTotalPrice())));
        rewardTextView.setText("+" + info.getRewards());

        showStatus(info.getStatus());

        detailsOrderListView.setVisibility(View.VISIBLE);
        detailsOrderListAdapter = new DetailsOrderListAdapter(getActivity(), info.getDetails(),orders);
        detailsOrderListView.setAdapter(detailsOrderListAdapter);
    }

    private void showStatus(String status) {
        refundButton.setVisibility(View.GONE);
        progressStatusButton.setEnabled(false);
        readyStatusButton.setEnabled(false);
        completedStatusButton.setEnabled(false);

        switch (status) {
            case "0":
                refundButton.setVisibility(View.VISIBLE);
                statusImageView.setVisibility(View.VISIBLE);
                statusImageView.setImageResource(R.drawable.progress_new);
                progressStatusButton.setEnabled(true);
                break;
            case "1":
                statusImageView.setVisibility(View.VISIBLE);
                statusImageView.setImageResource(R.drawable.progress_in_progress);
                readyStatusButton.setEnabled(true);
                break;
            case "2":
                statusImageView.setVisibility(View.VISIBLE);
                statusImageView.setImageResource(R.drawable.progress_ready);
                completedStatusButton.setEnabled(true);
                break;

            default:
                statusImageView.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSearch() {
        searchRelativeLayout.setVisibility(View.GONE);
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        orderEditText.setText("");
        fromTextView.setText("");
        toTextView.setText("");
        hideKeyboard();
    }

    private void updateStatus(final String status) {

        OrderInfo info = orders.get(selectedOrder);
        showLoader(R.string.empty);

        Application.getServerApi().updateOrderStatus(new UpdateOrderRequest(info.getId(), status)).enqueue(new Callback<BaseResponse>(){
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    if (status != "3") {
                        orders.get(selectedOrder).setStatus(status);
                    } else {
                        orders.remove(selectedOrder);
                    }
                    updateOrderList(selectedOrder);
                    progress_status=false;
                    if (status == "1") {
                        //printOrders();
//                        Dexter.withActivity(ac).withPermissions(
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                        Manifest.permission.READ_EXTERNAL_STORAGE
//                                ).withListener(new MultiplePermissionsListener() {
//                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//                                Toast.makeText(ac, "permission granted", Toast.LENGTH_SHORT).show();
//
//                                try {
//
//
//
//                                    ();
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                            }
//
//                        }).check();
                        progress_status = true;
                        detailsOrderListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("New Order", t.getLocalizedMessage());
                } else {
                    Log.d("New Order", "Unknown error");
                }
            }
        });

    }
    ////////////////// Print//////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() throws FileNotFoundException {

        ArrayList<OrderItemInfo> orderItemInfos = orders.get(order_print).getDetails();
        if (orderItemInfos.size() == 0) {
            return;
        }


        for (int i = 0; i <orderItemInfos.size() ; i++) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/teaera");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File newFile = new File(Environment.getExternalStorageDirectory() + "/teaera/" + "order11.pdf");

            PdfDocument pdf = new PdfDocument(new PdfWriter(newFile));
            pdf.setDefaultPageSize(PageSize.A6);

            Document document = new Document(pdf,  new PageSize(109, 340));
            document.setMargins(0,0,0,0);


//            Document document = new Document(pdf);

            Table table = new Table(1);
            table.setBorder(new SolidBorder(3));
            table.setWidth(109).setHorizontalAlignment(HorizontalAlignment.CENTER);
            // table.setWidthPercent(30).setMarginBottom(10);

            table.addCell(new Cell().add(""+orderItemInfos.get(order_print).getMenuName()).setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(table);

            Table table2 = new Table(2);
            table2.setBorder(new SolidBorder(3));
            table2.setBorderTop(null);
            table2.setBorderBottom(null);
            table2.setWidth(109).setHorizontalAlignment(HorizontalAlignment.CENTER);

            table2.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+orderItemInfos.get(order_print).getQuantity()).setPadding(3).setBorderRight(new SolidBorder(3)).setBold().setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+orderItemInfos.get(order_print).getDrinkable()).setPadding(3).setBorderLeft(new SolidBorder(3)).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(table2);

            Table table3 = new Table(1);
            table3.setBorder(new SolidBorder(3));

            table3.setWidth(109).setHorizontalAlignment(HorizontalAlignment.CENTER);
            String first="",second="",third="",fourth="",fifth="",sixth="",seventh="";
            try{
                String currentString = orderItemInfos.get(order_print).getOptions();
                String[] separated = currentString.split("---");
                first=separated[0];
                second=separated[1];
                third=separated[2];
                fourth=separated[3];
                fifth=separated[4];
                sixth=separated[5];
            }catch (Exception e){

            }
            table3.addCell(new Cell().setBorderBottom(null).add(""+first.trim()));
            table3.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+second.trim()));
            table3.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+third.trim()));
            table3.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+fourth.trim()));
            table3.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+fifth.trim()));
            table3.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+sixth.trim()));
            table3.addCell(new Cell().setBorderTop(null).add(""+seventh.trim()));

            document.add(table3);

            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            String currentDateandTime = date.format(new Date());
            System.out.println("jsioogogjog"+currentDateandTime);

            SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
            String current_time = sdf1.format(new Date());
            System.out.println("jsioogogjog"+current_time);
            Table table4 = new Table(1);
            table4.setBorder(new SolidBorder(3));
            table4.setBorderTop(null);
            table4.setWidth(109).setHorizontalAlignment(HorizontalAlignment.CENTER);
            table4.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+orders.get(0).getEmail()).setBold().setFontSize(10));
            table4.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+orders.get(0).getUserName()).setFontSize(10));
            table4.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+currentDateandTime).setFontSize(8));
            table4.addCell(new Cell().setBorderTop(null).add(""+current_time).setFontSize(8));
            document.add(table4);
            document.close();


            try{
                final String path = Environment.getExternalStorageDirectory() + "/teaera/" + "order11.pdf";
                File file = new File(path);
//                    Uri contentUri = FileProvider.getUriForFile(getActivity(), AUTHORITY, file);
                Uri contentUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".my.package.name.provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.dynamixsoftware.printershare");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(contentUri, "application/pdf");
                startActivity(intent);
            }catch (Exception e){
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();

            }
        }




    }

    public static PrintDocumentAdapter pda = new PrintDocumentAdapter(){

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback){
            InputStream input = null;
            OutputStream output = null;
            File pdf = new  File(Environment.getExternalStorageDirectory() + "/teaera/" + "order11.pdf");
            System.out.print( "jhsfffffffffffff 3"+pdf);

            try {

                input = new FileInputStream(pdf);
                output = new FileOutputStream(destination.getFileDescriptor());

                byte[] buf = new byte[1024];
                int bytesRead;

                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }

                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

            } catch (FileNotFoundException ee){
                ee.printStackTrace();

                //Catch exception
            } catch (Exception e) {
                e.printStackTrace();
                //Catch exception
            } finally {
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras){

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("order11.pdf").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
            callback.onLayoutFinished(pdi, true);
        }
    };

//    private void printOrders() {
//
//        try {
//            findPrinter();
//            openPrinter();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//    private void findPrinter() {
//        try {
//            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//            if(mBluetoothAdapter == null) {
//                Toast.makeText(getActivity(), "No bluetooth adapter available",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            if(!mBluetoothAdapter.isEnabled()) {
//                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBluetooth, 0);
//            }
//
//            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//            String printNames = "";
//            if(pairedDevices.size() > 0) {
//                for (BluetoothDevice device : pairedDevices) {
//
//                    // RPP300 is the name of the bluetooth printer device
//                    // we got this name from the list of paired devices
//                    printNames = printNames + device.getName() + "\n";
//                    if (device.getName().equals("your Device Name")) {
//                        mmDevice = device;
//                        break;
//                    }
//                }
//            }
//
////            Toast.makeText(getApplicationContext(), "Bluetooth device found.",
////                    Toast.LENGTH_SHORT).show();
//
//            Toast.makeText(getActivity(), printNames,
//                    Toast.LENGTH_LONG).show();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    // tries to open a connection to the bluetooth printer device
//    void openPrinter() throws IOException {
//        try {
//
//            // Standard SerialPortService ID
//            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//            mmSocket.connect();
//            mmOutputStream = mmSocket.getOutputStream();
//            //mmInputStream = mmSocket.getInputStream();
//
//
////            myLabel.setText("Bluetooth Opened");
////            Toast.makeText(getApplicationContext(), "Your toast message",
////                    Toast.LENGTH_LONG).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // close the connection to bluetooth printer.
//    void closePrinter() throws IOException {
//        try {
//
//            mmOutputStream.close();
//            //mmInputStream.close();
//            mmSocket.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void searchOrder(String firstName, String lastName, String order, String fromDate, String toDate) {

        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            fromDate = fromDate + " 00:00:00";
            toDate = toDate + " 23:59:59";
        }

        showLoader(R.string.empty);
        Application.getServerApi().searchOrders(new SearchOrderRequest(firstName, lastName, order, fromDate, toDate, StorePrefs.getStoreInfo(getActivity()).getId(), "0")).enqueue(new Callback<SearchOrdersResponse>(){
            @Override
            public void onResponse(Call<SearchOrdersResponse> call, Response<SearchOrdersResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    hideSearch();
                    orders = response.body().getOrders();
                    isSearched = true;
                    updateOrderList(0);
                }
            }

            @Override
            public void onFailure(Call<SearchOrdersResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("New Order", t.getLocalizedMessage());
                } else {
                    Log.d("New Order", "Unknown error");
                }
            }
        });
    }


    @Override
    public void onOrderItemClickListener(OrderInfo info, int position) {
        progress_status = false;
        selectedOrder = position;
        updateOrderList(position);
        updateOrderDetails(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refundButton:
                Intent intent = new Intent(getActivity(), refundActivity.class);
                intent.putExtra("orderInfo", orders.get(selectedOrder));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            break;

            case R.id.customerRelativeLayout:
                intent = new Intent(getActivity(), CustomerInfoActivity.class);
                intent.putExtra("userId", orders.get(selectedOrder).getUserId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.searchButton:
                searchRelativeLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.closeSearchButton:
                if (isSearched) {
                    pageNumber = 1;
                    loadOrders(pageNumber);
                    isSearched = false;
                }
                hideSearch();
                break;

            case R.id.searchImageButton:
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String order = orderEditText.getText().toString();
                String newOrder = "";
                String fromDate = fromTextView.getText().toString();
                String toDate = toTextView.getText().toString();

                if (!order.isEmpty()){
                    newOrder     = Integer.toString(Integer.parseInt(order));
                }

//                if (!firstName.isEmpty()){
//                    if (!lastName.isEmpty()){
//                        if (!newOrder.isEmpty()){
//                            if (!fromDate.isEmpty() ) {
//                                if (!toDate.isEmpty()){
//                                    if (!fromDate.isEmpty() && !toDate.isEmpty()) {
//                                        try {
//                                            Date from = formatter.parse(fromDate);
//                                            Date to = formatter.parse(toDate);
//
//                                            if (from.before(to)) {
                                                searchOrder(firstName, lastName, order, fromDate, toDate);

//                                            }
//                                            else {
//                                                DialogUtils.showDialog(getActivity(), "Error", " To date should be higher than From date in order to start searching", null, null);
//                                            }
//
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                            return;
//                                        }
//                                    }
//                                    else {
//                                        DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
//                                    }
//                                }
//                                else {
//                                    DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
//                                }
//
//
//                            }
//                            else {
//                                DialogUtils.showDialog(getActivity(), "Error", "Please choose date range", null, null);
//                            }
//
//                        }
//                        else {
//                            DialogUtils.showDialog(getActivity(), "Error", "Please choose order id.", null, null);
//                        }
//
//                    }
//                    else {
//                        DialogUtils.showDialog(getActivity(), "Error", "Please choose last name.", null, null);
//                    }
//
//                }
//                else {
//                    DialogUtils.showDialog(getActivity(), "Error", "Please choose first name.", null, null);
//                }


//
//                if (firstName.isEmpty() && lastName.isEmpty() && newOrder.isEmpty() && fromDate.isEmpty() && toDate.isEmpty()) {
//                    DialogUtils.showDialog(getActivity(), "Error", getString(R.string.empty_search_options), null, null);
//                    break;
//                } else {
//                    if (fromDate.isEmpty() && !toDate.isEmpty()) {
//                        DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
//                    }
//                    if (!fromDate.isEmpty() && toDate.isEmpty()) {
//                        DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
//                        break;
//                    }
//
//                    if (!fromDate.isEmpty() && !toDate.isEmpty()) {
//                        try {
//                            Date from = formatter.parse(fromDate);
//                            Date to = formatter.parse(toDate);
//                            if(from.compareTo(to) > 0) {
//                                DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
//                                return;
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                            return;
//                        }
//                    }
//                }
//
//                searchOrder(firstName, lastName, newOrder, fromDate, toDate);

                break;

            case R.id.progressStatusButton:
                updateStatus("1");
                break;

            case R.id.readyStatusButton:
                updateStatus("2");
                break;

            case R.id.completedStatusButton:
                updateStatus("3");
                break;

            case R.id.fromTextView:
                fromDatePickerDialog.show();
                break;

            case R.id.toTextView:
                toDatePickerDialog.show();
                break;
        }
    }

    public void showLoader(int resId) {
        dialog = ProgressDialog.show(getActivity(), "",
                getString(resId), true);
    }

    public void hideLoader() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String getReceivedDate(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newSdf = new SimpleDateFormat("MM/dd/yy, hh:mm a");

        try {
            Date date = sdf.parse(dateString);
            return  newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
    private void connectPrinter() {
        try {
            findPrinter();
            openPrinter();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /////vvvv
    private void findPrinter() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                Toast.makeText(getActivity(), "No bluetooth adapter available",
                        Toast.LENGTH_SHORT).show();
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            String printNames = "";
            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    printNames = printNames + device.getName() + "\n";
                    if (device.getName().equals("your Device Name")) {
                        mmDevice = device;
                        break;
                    }
                }
            }


//            Toast.makeText(getApplicationContext(), "Bluetooth device found.",
//                    Toast.LENGTH_SHORT).show();

            Toast.makeText(getActivity(), printNames,
                    Toast.LENGTH_LONG).show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openPrinter() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            //mmInputStream = mmSocket.getInputStream();

//            Bitmap bitmap = bitmapFromView();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//            mmOutputStream.write(byteArray);

//            myLabel.setText("Bluetooth Opened");
//            Toast.makeText(getApplicationContext(), "Your toast message",
//                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
