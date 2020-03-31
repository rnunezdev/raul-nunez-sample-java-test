package com.clip.assesment.utils;

import com.clip.assesment.model.Transaction;
import com.clip.assesment.model.User;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class TestUtils {

    public static List<Transaction> generateTransactionListForReport(User user) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getDefault());

        // 9 different days
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2020,Calendar.FEBRUARY,28);
        Date date1 = cal1.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.set(2020,Calendar.FEBRUARY,29);
        Date date2 = cal2.getTime();

        Calendar cal3 = Calendar.getInstance();
        cal3.set(2020,Calendar.MARCH,01);
        Date date3 = cal3.getTime();


        Calendar cal4 = Calendar.getInstance();
        cal4.set(2020,Calendar.MARCH,02);
        Date date4 = cal4.getTime();


        Calendar cal5 = Calendar.getInstance();
        cal5.set(2020,Calendar.MARCH,04);
        Date date5 = cal5.getTime();

        Calendar cal6 = Calendar.getInstance();
        cal6.set(2020,Calendar.MARCH,13);
        Date date6 = cal6.getTime();

        Calendar cal7 = Calendar.getInstance();
        cal7.set(2020,Calendar.MARCH,15);
        Date date7 = cal7.getTime();

        Calendar cal8 = Calendar.getInstance();
        cal8.set(2020,Calendar.MARCH,17);
        Date date8 = cal8.getTime();

        Calendar cal9 = Calendar.getInstance();
        cal9.set(2020,Calendar.MARCH,19);
        Date date9 = cal9.getTime();

        // 2 transactions per day
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();
        UUID uuid4 = UUID.randomUUID();
        UUID uuid5 = UUID.randomUUID();
        UUID uuid6 = UUID.randomUUID();
        UUID uuid7 = UUID.randomUUID();
        UUID uuid8 = UUID.randomUUID();
        UUID uuid9 = UUID.randomUUID();
        UUID uuid10 = UUID.randomUUID();
        UUID uuid11 = UUID.randomUUID();
        UUID uuid12 = UUID.randomUUID();
        UUID uuid13 = UUID.randomUUID();
        UUID uuid14 = UUID.randomUUID();
        UUID uuid15 = UUID.randomUUID();
        UUID uuid16 = UUID.randomUUID();
        UUID uuid17 = UUID.randomUUID();
        UUID uuid18 = UUID.randomUUID();


        List<Transaction> transactionList = new ArrayList<>();

        // 18 transactions total
        Transaction transaction1 = new Transaction();
        transaction1.setDate(date1);
        transaction1.setAmount(new BigDecimal("50.59"));
        transaction1.setDescription("A description 1");
        transaction1.setId(uuid1);
        transaction1.setUser(user);

        Transaction transaction2 = new Transaction();
        transaction2.setDate(date1);
        transaction2.setAmount(new BigDecimal("100.67"));
        transaction2.setDescription("A description 2");
        transaction2.setId(uuid2);
        transaction2.setUser(user);

        Transaction transaction3 = new Transaction();
        transaction3.setDate(date2);
        transaction3.setAmount(new BigDecimal("200.34"));
        transaction3.setDescription("A description 3");
        transaction3.setId(uuid3);
        transaction3.setUser(user);

        Transaction transaction4 = new Transaction();
        transaction4.setDate(date2);
        transaction4.setAmount(new BigDecimal("456.34"));
        transaction4.setDescription("A description 4");
        transaction4.setId(uuid4);
        transaction4.setUser(user);

        Transaction transaction5 = new Transaction();
        transaction5.setDate(date3);
        transaction5.setAmount(new BigDecimal("4.34"));
        transaction5.setDescription("A description 5");
        transaction5.setId(uuid5);
        transaction5.setUser(user);

        Transaction transaction6 = new Transaction();
        transaction6.setDate(date3);
        transaction6.setAmount(new BigDecimal("56.34"));
        transaction6.setDescription("A description 6");
        transaction6.setId(uuid6);
        transaction6.setUser(user);

        Transaction transaction7 = new Transaction();
        transaction7.setDate(date4);
        transaction7.setAmount(new BigDecimal("21.34"));
        transaction7.setDescription("A description 7");
        transaction7.setId(uuid7);
        transaction7.setUser(user);

        Transaction transaction8 = new Transaction();
        transaction8.setDate(date4);
        transaction8.setAmount(new BigDecimal("1290.34"));
        transaction8.setDescription("A description 8");
        transaction8.setId(uuid8);
        transaction8.setUser(user);

        Transaction transaction9 = new Transaction();
        transaction9.setDate(date5);
        transaction9.setAmount(new BigDecimal("3.46"));
        transaction9.setDescription("A description 9");
        transaction9.setId(uuid9);
        transaction9.setUser(user);

        Transaction transaction10 = new Transaction();
        transaction10.setDate(date5);
        transaction10.setAmount(new BigDecimal("49.34"));
        transaction10.setDescription("A description 10");
        transaction10.setId(uuid10);
        transaction10.setUser(user);

        Transaction transaction11 = new Transaction();
        transaction11.setDate(date6);
        transaction11.setAmount(new BigDecimal("456.34"));
        transaction11.setDescription("A description 11");
        transaction11.setId(uuid11);
        transaction11.setUser(user);

        Transaction transaction12 = new Transaction();
        transaction12.setDate(date6);
        transaction12.setAmount(new BigDecimal("175.34"));
        transaction12.setDescription("A description 12");
        transaction12.setId(uuid12);
        transaction12.setUser(user);

        Transaction transaction13 = new Transaction();
        transaction13.setDate(date7);
        transaction13.setAmount(new BigDecimal("65.34"));
        transaction13.setDescription("A description 13");
        transaction13.setId(uuid13);
        transaction13.setUser(user);

        Transaction transaction14 = new Transaction();
        transaction14.setDate(date7);
        transaction14.setAmount(new BigDecimal("42.51"));
        transaction14.setDescription("A description 14");
        transaction14.setId(uuid14);
        transaction14.setUser(user);

        Transaction transaction15 = new Transaction();
        transaction15.setDate(date8);
        transaction15.setAmount(new BigDecimal("143.34"));
        transaction15.setDescription("A description 15");
        transaction15.setId(uuid15);
        transaction15.setUser(user);

        Transaction transaction16 = new Transaction();
        transaction16.setDate(date8);
        transaction16.setAmount(new BigDecimal("1234.51"));
        transaction16.setDescription("A description 16");
        transaction16.setId(uuid16);
        transaction16.setUser(user);

        Transaction transaction17 = new Transaction();
        transaction17.setDate(date9);
        transaction17.setAmount(new BigDecimal("945.34"));
        transaction17.setDescription("A description 17");
        transaction17.setId(uuid17);
        transaction17.setUser(user);

        Transaction transaction18 = new Transaction();
        transaction18.setDate(date9);
        transaction18.setAmount(new BigDecimal("8.51"));
        transaction18.setDescription("A description 18");
        transaction18.setId(uuid18);
        transaction18.setUser(user);

        // adding
        transactionList.add(transaction1);
        transactionList.add(transaction2);
        transactionList.add(transaction3);
        transactionList.add(transaction4);
        transactionList.add(transaction5);
        transactionList.add(transaction6);
        transactionList.add(transaction7);
        transactionList.add(transaction8);
        transactionList.add(transaction9);
        transactionList.add(transaction10);
        transactionList.add(transaction11);
        transactionList.add(transaction12);
        transactionList.add(transaction13);
        transactionList.add(transaction14);
        transactionList.add(transaction15);
        transactionList.add(transaction16);
        transactionList.add(transaction17);
        transactionList.add(transaction18);

        return transactionList;
    }


}
