package com.teaera.teaerastore.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.teaera.teaerastore.R;
import com.teaera.teaerastore.net.Model.OrderInfo;
import com.teaera.teaerastore.net.Model.OrderItemInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.teaera.teaerastore.activities.NewOrderFragment.progress_status;

/**
 * Created by admin on 17/10/2017.
 */

public class DetailsOrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private Context context;
    private ArrayList<OrderItemInfo> orderItems;
    ArrayList<OrderInfo> orders;

    public DetailsOrderListAdapter(Activity activity, ArrayList<OrderItemInfo> orderItems, ArrayList<OrderInfo> orders) {

        this.context = activity;
        this.orderItems = orderItems;
        this.orders = orders;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView menuTextView;
        TextView costTextView;
        TextView quantityTextView;
        TextView detailsTextView;
        TextView option_1,option_2,option_3,option_4,option_5,option_6;
        Button print_label;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.order_details_list_item, null);

        holder.menuTextView = rowView.findViewById(R.id.menuTextView);

        holder.costTextView = rowView.findViewById(R.id.costTextView);
        holder.quantityTextView = rowView.findViewById(R.id.quantityTextView);
        holder.print_label = rowView.findViewById(R.id.print_label);

        if(progress_status){
            holder.print_label.setVisibility(View.VISIBLE);
        }else{
            holder.print_label.setVisibility(View.GONE);
        }

        holder.option_1 = rowView.findViewById(R.id.option_1);
        holder.option_2 = rowView.findViewById(R.id.option_2);
        holder.option_3 = rowView.findViewById(R.id.option_3);
        holder.option_4 = rowView.findViewById(R.id.option_4);
        holder.option_5 = rowView.findViewById(R.id.option_5);
        holder.option_6 = rowView.findViewById(R.id.option_6);


        String first="",second="",third="",fourth="",fifth="",sixth="",seventh="";
        try{
            String currentString = orderItems.get(position).getOptions();
            String[] separated = currentString.split("---");
            first=separated[0];
            second=separated[1];
            third=separated[2];
            fourth=separated[3];
            fifth=separated[4];
            sixth=separated[5];
        }catch (Exception e){

        }
        holder.option_1.setText(first);
        holder.option_2.setText(second);
        holder.option_3.setText(third);
        holder.option_4.setText(fourth);
        holder.option_5.setText(fifth);
//        holder.option_6.setText(sixth);

        holder.menuTextView.setText(orderItems.get(position).getMenuName());

        int quantity = Integer.parseInt(orderItems.get(position).getQuantity()) - Integer.parseInt(orderItems.get(position).getRefundQuantity());
        holder.quantityTextView.setText(Integer.toString(quantity));

        //float totalPrice = Float.parseFloat(orderItems.get(position).getPrice())*Integer.parseInt(orderItems.get(position).getQuantity());
        holder.costTextView.setText(String.format("$%.2f", Float.parseFloat(orderItems.get(position).getPrice())));

        holder.print_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity((Activity) context)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {


                                try {
                                    printPDF(position);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show();
                            }
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();


            }
        });

        return rowView;
    }

    private void printPDF(int item_pos) throws FileNotFoundException {


        if (this.orderItems.size() == 0) {
            return;
        }



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

            table.addCell(new Cell().add(""+orderItems.get(item_pos).getMenuName()).setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(table);

            Table table2 = new Table(2);
            table2.setBorder(new SolidBorder(3));
            table2.setBorderTop(null);
            table2.setBorderBottom(null);
            table2.setWidth(109).setHorizontalAlignment(HorizontalAlignment.CENTER);

            table2.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+orderItems.get(item_pos).getQuantity()).setPadding(3).setBorderRight(new SolidBorder(3)).setBold().setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().setBorderTop(null).setBorderBottom(null).add(""+orderItems.get(item_pos).getDrinkable()).setPadding(3).setBorderLeft(new SolidBorder(3)).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(table2);

            Table table3 = new Table(1);
            table3.setBorder(new SolidBorder(3));

            table3.setWidth(109).setHorizontalAlignment(HorizontalAlignment.CENTER);
            String first="",second="",third="",fourth="",fifth="",sixth="",seventh="";
            try{
                String currentString = orderItems.get(item_pos).getOptions();
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


            SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
            String current_time = sdf1.format(new Date());
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


                    final String path = Environment.getExternalStorageDirectory() + "/teaera/" + "order11.pdf";
                    File file = new File(path);
            try{

                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".my.package.name.provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.dynamixsoftware.printershare");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(contentUri, "application/pdf");
                context.startActivity(intent);
            }catch (Exception e){
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();

            }





    }
}
