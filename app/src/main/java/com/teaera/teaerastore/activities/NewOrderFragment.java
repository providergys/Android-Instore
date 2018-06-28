package com.teaera.teaerastore.activities;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderFragment extends Fragment implements View.OnClickListener, OrderListAdapter.OnOrderItemClickListener {

    private OrderListAdapter orderListAdapter;
    private ListView orderListView;
    private DetailsOrderListAdapter detailsOrderListAdapter;
    private ListView detailsOrderListView;

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
    Activity ac;


    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
            Font.BOLD);
    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.NORMAL);
    private static Font dateFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);

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

    @Override
    public void onResume() {
        super.onResume();

        init();
        ac = getActivity();
    }

    private void init() {

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
        detailsOrderListAdapter = new DetailsOrderListAdapter(getActivity(), info.getDetails());
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

                    if (status == "1") {
                        //printOrders();
                        Dexter.withActivity(ac).withPermissions(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                ).withListener(new MultiplePermissionsListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                Toast.makeText(ac, "permission granted", Toast.LENGTH_SHORT).show();
                                printPDF();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            }

                        }).check();
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
    private void printPDF() {

        float left      = 20;
        float right     = 20;
        float top       = 30;
        float bottom    = 20;

        ArrayList<OrderItemInfo> orderItemInfos = orders.get(selectedOrder).getDetails();
        if (orderItemInfos.size() == 0) {
            return;
        }

//        ArrayList<OrderItemInfo> printInfos = new ArrayList<OrderItemInfo>();
//        for (int i=0; i<orderItemInfos.size(); i++) {
//
//            OrderItemInfo item = orderItemInfos.get(i);
//            int quantity = Integer.parseInt(item.getQuantity()) - Integer.parseInt(item.getRefundQuantity());
//
//            for (int j=0; j<quantity; j++) {
//                printInfos.add(item);
//            }
//        }

        File dir = new File(Environment.getExternalStorageDirectory() + "/teaera");

        if (!dir.exists()){
            dir.mkdirs();
        }

        File newFile = new File(Environment.getExternalStorageDirectory() + "/teaera/" + "order11.pdf");
        try {
            newFile.createNewFile();
            Document document = new Document(PageSize.A4, left, right, top, bottom);
            try {
                PdfWriter.getInstance(document, new FileOutputStream(newFile));
                document.open();
                document.setMargins(left, right, 0, bottom);

                Paragraph preface = new Paragraph();
                preface.add(new Paragraph("Tea Era, " + StorePrefs.getStoreInfo(getActivity()).getName(), titleFont));
                preface.add(new Paragraph("Order Detail", titleFont));
//                preface.add(new Paragraph(" "));

                preface.add(new Paragraph("Printed Date & Time:", dateFont));
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy, hh:mm a");
                preface.add(new Paragraph(curFormater.format(currentTime), dateFont));
                preface.add(new Paragraph(" "));

                preface.add(new Paragraph("Customer Name:- " + orders.get(selectedOrder).getUserName(), font));
                preface.add(new Paragraph("Email:- " + emailTextView.getText().toString(), font));


                int orderId = Integer.parseInt(orders.get(selectedOrder).getId());
                preface.add(new Paragraph("Order:- #00" + orderId, font));
                preface.add(new Paragraph(" "));
                document.add(preface);
                document.add(new LineSeparator());

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setSpacingBefore(0f);
                table.setSpacingAfter(0f);

                PdfPCell c1 = new PdfPCell(new Phrase("Item Name", font));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Quantity", font));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Price ($)", font));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
                table.setHeaderRows(1);


                for (int i=0; i<orderItemInfos.size(); i++) {

                    PdfPCell cell;
                    cell = new PdfPCell(new Phrase(orderItemInfos.get(i).getMenuName(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(orderItemInfos.get(i).getQuantity(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(orderItemInfos.get(i).getPrice(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                }
                document.add(table);

                Paragraph preface2 = new Paragraph();
                preface2.add(new Paragraph(" "));
                preface2.add(new Paragraph(" "));
                document.add(preface2);

                PdfPTable table1 = new PdfPTable(2);
                table1.setWidthPercentage(100);
                table1.setSpacingBefore(0f);
                table1.setSpacingAfter(0f);

                PdfPCell c2 = new PdfPCell(new Phrase("Rewards:- ", font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase(rewardTextView.getText().toString()+"*", font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase("Subtotal:- $", font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase(subtotalTextView.getText().toString(), font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase("Reward Cradit:- $", font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase(creditTextView.getText().toString(), font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase("Tax:- $", font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase(taxTextView.getText().toString(), font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase("Total:- $", font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase(totalTextView.getText().toString(), font));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(c2);

                document.add(table1);


//                Paragraph preface1 = new Paragraph();
//
//                preface1.add(new Paragraph("Subtotal:- $" + subtotalTextView.getText().toString(), font));
//                preface1.add(new Paragraph("Reward Cradit:- $" + creditTextView.getText().toString(), font));
//                preface1.add(new Paragraph("Tax:- $" + taxTextView.getText().toString(), font));
//                preface1.add(new Paragraph("Total:- $" + totalTextView.getText().toString(), font));
//
//                document.add(preface1);

                document.add(new LineSeparator());

                document.close();
                PrintManager printManager = (PrintManager) ac.getSystemService(Context.PRINT_SERVICE);

                String jobName = "Order";
                printManager.print(jobName, pda, null);

                Toast.makeText(getActivity(), "PDF is generated successfully!",
                        Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();

                System.out.print( "jhsfffffffffffff 1"+ e);
            }
        } catch(Exception e) {
            System.out.print( "jhsfffffffffffff 2"+ e);
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
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras){

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

                if (!firstName.isEmpty()){
                    if (!lastName.isEmpty()){
                        if (!newOrder.isEmpty()){
                            if (!fromDate.isEmpty() ) {
                                if (!toDate.isEmpty()){
                                    if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                                        try {
                                            Date from = formatter.parse(fromDate);
                                            Date to = formatter.parse(toDate);

                                            if (from.before(to)) {
                                                searchOrder(firstName, lastName, order, fromDate, toDate);

                                            }
                                            else {
                                                DialogUtils.showDialog(getActivity(), "Error", " To date should be higher than From date in order to start searching", null, null);
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    }
                                    else {
                                        DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
                                    }
                                }
                                else {
                                    DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
                                }


                            }
                            else {
                                DialogUtils.showDialog(getActivity(), "Error", "Please choose date range", null, null);
                            }

                        }
                        else {
                            DialogUtils.showDialog(getActivity(), "Error", "Please choose order id.", null, null);
                        }

                    }
                    else {
                        DialogUtils.showDialog(getActivity(), "Error", "Please choose last name.", null, null);
                    }

                }
                else {
                    DialogUtils.showDialog(getActivity(), "Error", "Please choose first name.", null, null);
                }


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
}
