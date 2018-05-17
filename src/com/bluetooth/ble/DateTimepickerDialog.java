package com.bluetooth.ble;

import java.util.Calendar;  

import com.bluetooth.ble.DateTimepicker.OnDateTimeChangedListener; 
import android.app.AlertDialog;  
import android.content.Context;  
import android.content.DialogInterface;  
  
public class DateTimepickerDialog extends AlertDialog  {  
    private DateTimepicker mDateTimePicker;  
    private Calendar mDate = Calendar.getInstance();  
    private int Year,Month,Day,Hour,Minute,Second;  
    private OnDateTimeSetListener mOnDateTimeSetListener;  
    private String datetimeStr;  
    public DateTimepickerDialog(Context context, long date) {  
        super(context);  
        mDateTimePicker = new DateTimepicker(context);  
        setView(mDateTimePicker);//װ�ظղŽ����Ĳ��֣��Ѷ���õ�����ʱ�䲼����ʾ������Զ���Ի�����  
        /*  
         *ʵ��<span style="font-family:Arial, Helvetica, sans-serif;">DateTimepicker���</span><span style="font-family:Arial, Helvetica, sans-serif;">�ӿ�</span> 
         */    
        mDateTimePicker.setOnDateTimeChangedListener(new OnDateTimeChangedListener() {    
            public void onDateTimeChanged(DateTimepicker view,   
                    int year, int month, int day, int hour, int minute, int second) {  
                Year = year;  
                Month = month;  
                Day = day;   
                Hour = hour;  
                Minute = minute;  
                Second = second;  
            }    
        });  
        setTitle("���������ں�ʱ��");  
        Year = mDate.get(Calendar.YEAR);  
        Month = mDate.get(Calendar.MONTH)+1;  
        Day = mDate.get(Calendar.DAY_OF_MONTH);  
        Hour = mDate.get(Calendar.HOUR_OF_DAY);  
        Minute = mDate.get(Calendar.MINUTE);  
        Second = mDate.get(Calendar.SECOND);  
        setButton(DialogInterface.BUTTON_POSITIVE,"ȷ��", new DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                // TODO Auto-generated method stub  
                datetimeStr = Year +"-"+ Month +"-"+ Day +"-"+Hour+":"+Minute+":"+Second;  
                if (mOnDateTimeSetListener != null) {  
                    mOnDateTimeSetListener.OnDateTimeSet(dialog, datetimeStr);  
                    }  
                }  
            });   
        setButton(DialogInterface.BUTTON_NEGATIVE,"ȡ��", (OnClickListener) null);   
        setCanceledOnTouchOutside(false);//����Ի������޷��رնԻ���  
    }    
    /*  
     *    
     *�ӿڻص� 
     */    
    public interface OnDateTimeSetListener {    
        void OnDateTimeSet(DialogInterface dialog, String datetimestr);  
    }    
    
  
    /*  
     *���⹫��������Activityʵ��  
     */    
    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {    
        mOnDateTimeSetListener = onDateTimeSetListener;    
    }    
}  
