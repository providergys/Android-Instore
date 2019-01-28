package com.teaera.teaerastore.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.teaera.teaerastore.R;
import com.teaera.teaerastore.app.Application;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Model.PrintInfo;
import com.teaera.teaerastore.net.Model.StoreInfo;
import com.teaera.teaerastore.net.Request.GetPrintRequest;
import com.teaera.teaerastore.net.Request.UpdateStoreRequest;
import com.teaera.teaerastore.net.Response.GetPrintResponse;
import com.teaera.teaerastore.net.Response.StoreResponse;
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
public class StoreProfileFragment extends Fragment implements View.OnClickListener {

    private TextView storeNametextView;
    private TextView storeAddressTextView;

    private Spinner waitTimeSpinner;
    private TextView waitTimeTextView;
    private Spinner openHourSpinner;
    private TextView openHourTextView;
    private Spinner closeHourSpinner;
    private TextView closeHourTextView;

    private ImageView timeCheckImageView;
    private ImageView hourCheckImageView;

    private TextView fromTextView;
    private TextView toTextView;

    private String[] waitingTimes = {"- None -", "5 MINS", "10 MINS", "15 MINS", "20 MINS", "25 MINS", "30 MINS", "35 MINS", "40 MINS", "45 MINS", "50 MINS", "55 MINS", "60 MINS"};
    private String[] hours = {"- None -", "12:00 AM", "12:30 AM", "1:00 AM", "1:30 AM", "2:00 AM", "2:30 AM", "3:00 AM", "3:30 AM", "4:00 AM", "4:30 AM", "5:00 AM", "5:30 AM", "6:00 AM", "6:30 AM", "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM", "9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM", "12:00 AM"};

    private ProgressDialog dialog;
    Activity ac;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;


    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
            Font.BOLD);
    private static Font bigfont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.NORMAL);
    private static Font dateFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.NORMAL);

    private static Font tableHeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font tableContentFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);


    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;


    public StoreProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
        updateTimeSpinners();
        ac = getActivity();

        System.out.println("dgggggggggggggggg"+"yha aaya");
//        connectPrinter();
    }

    private void init() {
        storeNametextView = getActivity().findViewById(R.id.storeNametextView);
        storeAddressTextView = getActivity().findViewById(R.id.storeAddressTextView);
        storeNametextView.setText(StorePrefs.getStoreInfo(getActivity()).getName());
        storeAddressTextView.setText(StorePrefs.getStoreInfo(getActivity()).getAddress());

        waitTimeSpinner = getActivity().findViewById(R.id.waitTimeSpinner);
        openHourSpinner = getActivity().findViewById(R.id.openHourSpinner);
        closeHourSpinner = getActivity().findViewById(R.id.closeHourSpinner);
        waitTimeTextView = getActivity().findViewById(R.id.waitTimeTextView);
        openHourTextView = getActivity().findViewById(R.id.openHourTextView);
        closeHourTextView = getActivity().findViewById(R.id.closeHourTextView);

        timeCheckImageView = getActivity().findViewById(R.id.timeCheckImageView);
        hourCheckImageView = getActivity().findViewById(R.id.hourCheckImageView);

        fromTextView = getActivity().findViewById(R.id.fromTextView);
        toTextView = getActivity().findViewById(R.id.toTextView);
        fromTextView.setOnClickListener(this);
        toTextView.setOnClickListener(this);

        ImageButton saveImageButton = getActivity().findViewById(R.id.saveImageButton);
        saveImageButton.setOnClickListener(this);

        ImageButton processImageButton = getActivity().findViewById(R.id.processImageButton);
        processImageButton.setOnClickListener(this);

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

    }

    void updateTimeSpinners() {
        StoreInfo info = StorePrefs.getStoreInfo(getActivity());
        int selectedWaitTime = Integer.parseInt(info.getWaitingTime())/5;

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,waitingTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waitTimeSpinner.setAdapter(adapter);
        waitTimeSpinner.setSelection(selectedWaitTime);
        waitTimeTextView.setOnClickListener(this);

        waitTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                waitTimeTextView.setText(waitTimeSpinner.getSelectedItem().toString());
                if (position != 0) {
                    timeCheckImageView.setImageResource(R.drawable.check);
                } else {
                    timeCheckImageView.setImageResource(R.drawable.uncheck);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,hours);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        openHourSpinner.setAdapter(adapter1);
        openHourSpinner.setSelection(getIndexFromHours(info.getOpeningHour()));
        openHourTextView.setOnClickListener(this);

        openHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0 ) {
                    hourCheckImageView.setImageResource(R.drawable.uncheck);
                } else {
                    openHourTextView.setText(openHourSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,hours);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        closeHourSpinner.setAdapter(adapter1);
        closeHourSpinner.setSelection(getIndexFromHours(info.getClosingHour()));
        closeHourTextView.setOnClickListener(this);

        closeHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (openHourSpinner.getSelectedItemPosition() > position) {
                    hourCheckImageView.setImageResource(R.drawable.uncheck);
                    DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_closing_hour), null, null);
                } else {
                    closeHourTextView.setText(closeHourSpinner.getSelectedItem().toString());
                    hourCheckImageView.setImageResource(R.drawable.check);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private int getIndexFromHours(String hour) {
        for (int i=0; i<hours.length; i++) {
            if (hours[i].equals(hour))
                return i;
        }
        return 0;
    }

    private void saveStoreData() {
        if (waitTimeSpinner.getSelectedItemPosition() !=0 && openHourSpinner.getSelectedItemPosition() != 0 && closeHourSpinner.getSelectedItemPosition() != 0) {

            String waiting = Integer.toString(waitTimeSpinner.getSelectedItemPosition() * 5);
            showLoader(R.string.empty);

            Application.getServerApi().updateStore(new UpdateStoreRequest(StorePrefs.getStoreInfo(getActivity()).getId(), waiting, openHourTextView.getText().toString(), closeHourTextView.getText().toString())).enqueue(new Callback<StoreResponse>(){

                @Override
                public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                    hideLoader();
                    if (response.body().isError()) {
                        DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                    } else {
                        StorePrefs.saveStoreInfo(getActivity(), response.body().getStoreInfo());
                        DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);

                    }
                }

                @Override
                public void onFailure(Call<StoreResponse> call, Throwable t) {
                    hideLoader();
                    if (t.getLocalizedMessage() != null) {
                        Log.d("StoreInfo", t.getLocalizedMessage());
                    } else {
                        Log.d("StoreInfo", "Unknown error");
                    }
                }
            });
        }
    }

    private void checkDateValidation() {

        String fromDateStr = fromTextView.getText().toString();
        String toDateStr = toTextView.getText().toString();

        if (fromDateStr.isEmpty() || toDateStr.isEmpty()) {
            DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_print_date), null, null);
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date fromDate = simpleDateFormat.parse(fromDateStr);
            Date toDate = simpleDateFormat.parse(toDateStr);

            if(fromDate.compareTo(toDate) > 0) {
                DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_date), null, null);
                return;
            }

            long diff = (toDate.getTime() - fromDate.getTime()) / (1000*60*60*24);
            if (diff > 31 && diff < 0) {
                DialogUtils.showDialog(getActivity(), "Error", getString(R.string.error_print_date), null, null);
                return;
            }

            printSalesReport(fromDateStr, toDateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void printSalesReport(String fromDateStr, String toDateStr) {

        final String fromDate = fromDateStr + " 00:00:00";
        final String toDate = toDateStr + " 23:59:59";

        showLoader(R.string.empty);

        Application.getServerApi().getPrintData(new GetPrintRequest(fromDateStr, toDateStr, StorePrefs.getStoreInfo(getActivity()).getId())).enqueue(new Callback<GetPrintResponse>(){

            @Override
            public void onResponse(Call<GetPrintResponse> call, Response<GetPrintResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    ArrayList<PrintInfo> printInfos = response.body().getPrintInfo();
                    if (printInfos.size() == 0) {
                        DialogUtils.showDialog(getActivity(), "Confirm", getString(R.string.no_print_date), null, null);
                        return;
                    }

                    printPDF(printInfos, fromDate, toDate);
                }
            }

            @Override
            public void onFailure(Call<GetPrintResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Store Profile", t.getLocalizedMessage());
                } else {
                    Log.d("Store Profile", "Unknown error");
                }
            }
        });
    }

    // Print

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void printPDF(ArrayList<PrintInfo> printInfos, String fromDate, String toDate) {

        float left = 20;
        float right = 20;
        float top = 30;
        float bottom = 20;

//      File newFile = new File(Environment.getExternalStorageDirectory() + "/" + "report.pdf");
//      File newFile = new File(Environment.getExternalStorageDirectory().toString() + "/teaera/" + "report.pdf");

        File dir = new File(Environment.getExternalStorageDirectory() + "/teaera");

        if (!dir.exists()){
            dir.mkdirs();
        }

        File newFile = new File(Environment.getExternalStorageDirectory() + "/teaera/" + "report.pdf");

        try {
            newFile.createNewFile();
            Document document = new Document(PageSize.A4, left, right, top, bottom);
            try {
                PdfWriter.getInstance(document, new FileOutputStream(newFile));
                document.open();
                document.setMargins(left, right, 0, bottom);

                Paragraph preface = new Paragraph();
                preface.add(new Paragraph("Tea Era, " + StorePrefs.getStoreInfo(getActivity()).getName(), titleFont));
                preface.add(new Paragraph("Sales Report", titleFont));
                preface.add(new Paragraph(" "));
                document.add(preface);

                preface = new Paragraph();
                preface.add(new Paragraph("Printed Date & Time:", dateFont));
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy, hh:mm a");
                preface.add(new Paragraph(curFormater.format(currentTime), dateFont));
                preface.add(new Paragraph(" "));
                document.add(preface);
                document.add(new LineSeparator());

                preface = new Paragraph();
                preface.add(new Paragraph("Transaction Requestes:", font));
                preface.add(new Paragraph(convertDate(fromDate) + " to " + convertDate(toDate), font));
                preface.add(new Paragraph(" "));
                document.add(preface);

                float total = 0;
                float refundTotal = 0;
                for (int i=0; i < printInfos.size(); i++) {
                    total = total + Float.parseFloat(printInfos.get(i).getTotalPrice());
                    refundTotal = refundTotal + printInfos.get(i).getTotalRefund();
                }

                preface = new Paragraph();
                preface.add(new Paragraph(String.format("Total Sales ($): %.2f", total), font));
                preface.add(new Paragraph(" "));
                document.add(preface);
                document.add(new LineSeparator());

                preface = new Paragraph();
                preface.add(new Paragraph(String.format("Total Refund ($): %.2f", refundTotal), font));
                preface.add(new Paragraph(" "));
                document.add(preface);
                document.add(new LineSeparator());

                preface = new Paragraph();
                preface.add(new Paragraph(" "));
                document.add(preface);
                document.add(new LineSeparator());

                preface = new Paragraph();
                preface.add(new Paragraph("Transaction Details: ", bigfont));
                preface.add(new Paragraph(" "));
                document.add(preface);

                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(0f);
                table.setSpacingAfter(0f);

                PdfPCell c1 = new PdfPCell(new Phrase("Order #",tableHeaderFont ));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Customer Name", tableHeaderFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Order Time", tableHeaderFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Total ($)", tableHeaderFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Rewards (Stars)", tableHeaderFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Status", tableHeaderFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
                table.setHeaderRows(1);

                for (int i=0; i<printInfos.size(); i++) {
                    int orderId = Integer.parseInt(printInfos.get(i).getId());

                    c1 = new PdfPCell(new Phrase(String.format("#%05d", orderId), tableContentFont));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);

                    c1 = new PdfPCell(new Phrase(printInfos.get(i).getUserName(), tableContentFont));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);

                    c1 = new PdfPCell(new Phrase(printInfos.get(i).getTimestamp(), tableContentFont));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);

                    c1 = new PdfPCell(new Phrase("$" + printInfos.get(i).getTotalPrice(), tableContentFont));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);

                    c1 = new PdfPCell(new Phrase(Integer.toString(printInfos.get(i).getRewards()), tableContentFont));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c1);

                    switch (printInfos.get(i).getStatus()) {
                        case "0":
                            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                                    Font.NORMAL, BaseColor.RED);
                            c1 = new PdfPCell(new Phrase("New", font));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            break;
                        case "1":
                            font = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                                    Font.NORMAL, BaseColor.BLUE);
                            c1 = new PdfPCell(new Phrase("In Progress", font));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);
                            break;
                        case "2":
                            font = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                                    Font.NORMAL, BaseColor.YELLOW);
                            c1 = new PdfPCell(new Phrase("Ready for Pick Up", font));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);
                            break;
                        case "3":
                            font = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                                    Font.NORMAL, BaseColor.GREEN);
                            c1 = new PdfPCell(new Phrase("Completed", font));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);
                            break;

                    }

                }

                document.add(table);
                document.close();
//
//                PrintManager printManager = (PrintManager) ac.getSystemService(Context.PRINT_SERVICE);
//
//                String jobName = " Report";
//                printManager.print(jobName, pda, null);


                String path = Environment.getExternalStorageDirectory() + "/teaera/" + "order11.pdf";
                Printer myPrinter = new Printer();
                PrinterInfo myPrinterInfo = new PrinterInfo();

                try {

                    myPrinterInfo.printerModel = PrinterInfo.Model.QL_820NWB;
                    myPrinterInfo.ipAddress = "192.168.160.164";//not real ip
                    myPrinterInfo.macAddress = "";
                    myPrinterInfo.port = PrinterInfo.Port.NET;
                    myPrinterInfo.paperSize = PrinterInfo.PaperSize.A7;
                    myPrinterInfo.printMode=PrinterInfo.PrintMode.FIT_TO_PAGE;
                    myPrinterInfo.numberOfCopies = 1;

                    myPrinter.setPrinterInfo(myPrinterInfo);


                    Log.i("HEYYYY", "startCommunication = " + myPrinter.startCommunication());

                    myPrinter.printPDF(path,1);
                    myPrinter.endCommunication();




                } catch(Exception e){
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

                Toast.makeText(getActivity(), "PDF is generated successfully!",
                        Toast.LENGTH_SHORT).show();

            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } catch(IOException e) {
            // ...
        }
    }

    public static PrintDocumentAdapter pda = new PrintDocumentAdapter(){

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback){
            InputStream input = null;
            OutputStream output = null;
            File pdf = new  File(Environment.getExternalStorageDirectory() + "/teaera/" + "report.pdf");
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

            PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Report.pdf").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
            callback.onLayoutFinished(pdi, true);
        }
    };

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

    // close the connection to bluetooth printer.
    void closePrinter() throws IOException {
        try {
            mmOutputStream.close();
            //mmInputStream.close();
            mmSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.waitTimeTextView:
                waitTimeSpinner.performClick();
                break;
            case R.id.openHourTextView:
                openHourSpinner.performClick();
                break;
            case R.id.closeHourTextView:
                closeHourSpinner.performClick();
                break;
            case R.id.fromTextView:
                fromDatePickerDialog.show();
                break;
            case R.id.toTextView:
                toDatePickerDialog.show();
                break;
            case R.id.saveImageButton:
                saveStoreData();
                break;
            case R.id.processImageButton:
                //Permission check and ask for required permission
                Dexter.withActivity(ac)
                        .withPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        Toast.makeText(ac, "permission granted", Toast.LENGTH_SHORT).show();
                        checkDateValidation();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                    }

                }).check();

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

    public String convertDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newSdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");

        try {
            Date date = sdf.parse(dateString);
            return  newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

}
