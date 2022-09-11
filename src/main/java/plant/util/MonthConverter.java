package plant.util;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * Class that converts months between integer, string, and Swing component forms
 */
public class MonthConverter {
    public static int labelHeight = 1;
    public static int labelWidth = 5;
    public static Hashtable numToLabel() {
        Hashtable table = new Hashtable();

        for(int i=1; i<=12; i++) {
            JLabel label = new JLabel(numToStringShort(i));
            label.setForeground(Color.gray);
            label.setSize(labelWidth,labelHeight);
            table.put(i, label);
        }

        return table;
    }

    public static String numToStringShort(int i) {
        switch(i) {
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            default: return "Dec";
        }
    }

    public static String numToStringLong(int i) {
        switch(i) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            default: return "December";
        }
    }

    public static int stringShortToNum(String s) {
        if (s.equals("Jan")) {
            return 1;
        } else if (s.equals("Feb")) {
            return 2;
        } else if (s.equals("Mar")) {
            return 3;
        } else if (s.equals("Apr")) {
            return 4;
        } else if (s.equals("May")) {
            return 5;
        } else if (s.equals("Jun")) {
            return 6;
        } else if (s.equals("Jul")) {
            return 7;
        } else if (s.equals("Aug")) {
            return 8;
        } else if (s.equals("Sep")) {
            return 9;
        } else if (s.equals("Oct")) {
            return 10;
        } else if (s.equals("Nov")) {
            return 11;
        } else {
            return 12;
        }
    }
}
