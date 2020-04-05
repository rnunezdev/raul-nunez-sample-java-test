package com.clip.assesment.services.impl;

import com.clip.assesment.dao.TransactionDao;
import com.clip.assesment.dao.UserDao;
import com.clip.assesment.dto.ReportLineDTO;
import com.clip.assesment.dto.TransactionDTO;
import com.clip.assesment.dto.TransactionSumDTO;
import com.clip.assesment.exceptions.NotFoundException;
import com.clip.assesment.model.Transaction;
import com.clip.assesment.model.User;
import com.clip.assesment.services.TransactionService;
import com.clip.assesment.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TransactionService transactionService = new TransactionServiceImpl();


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void findTransactionById() throws ParseException {
        // given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today = Calendar.getInstance().getTime();

        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setId(1L);

        Transaction transaction = new Transaction();
        transaction.setDate(today);
        transaction.setAmount(new BigDecimal("50.4956567"));
        transaction.setDescription("A description");
        transaction.setId(uuid);
        transaction.setUser(user);

        when(transactionDao.findById(uuid)).thenReturn(Optional.of(transaction));

        // when
        TransactionDTO transactionDTO = transactionService.findTransactionById(user.getId(), uuid);

        // then

        assertEquals(transactionDTO.getDate(), dateFormat.format(today));
        assertEquals(transactionDTO.getDateConverted(TimeZone.getDefault().getID()), dateFormat.parse(dateFormat.format(today)));
        assertEquals(transactionDTO.getDescription(), "A description");
        assertEquals(transactionDTO.getAmount(), new BigDecimal("50.50"));
        assertEquals(transactionDTO.getTransactionId(), uuid);
        assertEquals(transactionDTO.getUserId(), 1L);


    }

    @Test
    public void findTransactionByIdNotFoundExceptionTransactionNull() throws NotFoundException {

        // given
        UUID uuid = UUID.randomUUID();
        when(transactionDao.findById(uuid)).thenReturn(Optional.empty());

        // when
        try {
            transactionService.findTransactionById(1L, uuid);
            fail();
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Transaction not found");
        }
    }

    @Test
    public void findTransactionByIdNotFoundExceptionUserOfTransactionNull() throws NotFoundException {

        // given
        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setId(2L);

        Transaction transaction = new Transaction();
        transaction.setId(uuid);
        transaction.setUser(user);

        when(transactionDao.findById(uuid)).thenReturn(Optional.of(transaction));

        // when
        try {
            transactionService.findTransactionById(1L, uuid); // requesting the tx with different user
            fail();
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Transaction not found");
        }
    }

    @Test
    public void findTransactionByIdNotFoundExceptionUserNotCorrespondingToTransaction() throws NotFoundException {

        // given
        UUID uuid = UUID.randomUUID();

        Transaction transaction = new Transaction();
        transaction.setUser(null);

        when(transactionDao.findById(uuid)).thenReturn(Optional.of(transaction));

        // when
        try {
            transactionService.findTransactionById(1L, uuid);
            fail();
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Transaction not found");
        }
    }

    @Test
    void findAllTransactionsByUserId() throws ParseException {
        // given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getDefault());

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2020,Calendar.JUNE,20);
        Date date1 = cal1.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2020,Calendar.NOVEMBER,20);
        Date date2 = cal2.getTime();
        Calendar cal3 = Calendar.getInstance();
        cal3.set(2020,Calendar.DECEMBER,20);
        Date date3 = cal3.getTime();

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        User user = new User();
        user.setId(1L);

        List<Transaction> transactionList = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setDate(date1);
        transaction1.setAmount(new BigDecimal("50.59"));
        transaction1.setDescription("A description 1");
        transaction1.setId(uuid1);
        transaction1.setUser(user);

        Transaction transaction2 = new Transaction();
        transaction2.setDate(date2);
        transaction2.setAmount(new BigDecimal("100.67"));
        transaction2.setDescription("A description 2");
        transaction2.setId(uuid2);
        transaction2.setUser(user);

        Transaction transaction3 = new Transaction();
        transaction3.setDate(date3);
        transaction3.setAmount(new BigDecimal("200.34"));
        transaction3.setDescription("A description 3");
        transaction3.setId(uuid3);
        transaction3.setUser(user);

        transactionList.add(transaction1);
        transactionList.add(transaction2);
        transactionList.add(transaction3);

        when(transactionDao.findAllByUserOrderByDate(user)).thenReturn(transactionList);
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        List<TransactionDTO> listTransactionDTO = transactionService.findAllTransactionsByUserId(user.getId());

        // then

        assertEquals(listTransactionDTO.size(), 3);
        assertEquals(listTransactionDTO.get(0).getDate(), dateFormat.format(date1));
        assertEquals(listTransactionDTO.get(0).getDateConverted(TimeZone.getDefault().getID()), dateFormat.parse(dateFormat.format(date1)));
        assertEquals(listTransactionDTO.get(0).getDescription(), "A description 1");
        assertEquals(listTransactionDTO.get(0).getAmount(), new BigDecimal("50.59"));
        assertEquals(listTransactionDTO.get(0).getTransactionId(), uuid1);
        assertEquals(listTransactionDTO.get(0).getUserId(), 1L);

        assertEquals(listTransactionDTO.get(1).getDate(), dateFormat.format(date2));
        assertEquals(listTransactionDTO.get(1).getDateConverted(TimeZone.getDefault().getID()), dateFormat.parse(dateFormat.format(date2)));
        assertEquals(listTransactionDTO.get(1).getDescription(), "A description 2");
        assertEquals(listTransactionDTO.get(1).getAmount(), new BigDecimal("100.67"));
        assertEquals(listTransactionDTO.get(1).getTransactionId(), uuid2);
        assertEquals(listTransactionDTO.get(1).getUserId(), 1L);

        assertEquals(listTransactionDTO.get(2).getDate(), dateFormat.format(date3));
        assertEquals(listTransactionDTO.get(2).getDateConverted(TimeZone.getDefault().getID()), dateFormat.parse(dateFormat.format(date3)));
        assertEquals(listTransactionDTO.get(2).getDescription(), "A description 3");
        assertEquals(listTransactionDTO.get(2).getAmount(), new BigDecimal("200.34"));
        assertEquals(listTransactionDTO.get(2).getTransactionId(), uuid3);
        assertEquals(listTransactionDTO.get(2).getUserId(), 1L);
    }

    @Test
    void findAllTransactionsByUserIdUserNull() throws ParseException {
        // given

        User user = new User();
        user.setId(1L);

        when(userDao.findById(user.getId())).thenReturn(Optional.empty());

        // when
        List<TransactionDTO> listTransactionDTO = transactionService.findAllTransactionsByUserId(user.getId());

        // then
        assertEquals(listTransactionDTO.size(), 0);
    }

    @Test
    void create() throws ParseException {
        // given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today = Calendar.getInstance().getTime();

        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setId(1L);

        Transaction transaction = new Transaction();
        transaction.setDate(today);
        transaction.setAmount(new BigDecimal("60.853"));
        transaction.setDescription("A description");
        transaction.setId(uuid);
        transaction.setUser(user);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setUserId(1L);
        transactionDTO.setAmount(new BigDecimal("60.853"));
        transactionDTO.setDate(dateFormat.parse(dateFormat.format(today)), TimeZone.getDefault().getID());
        transactionDTO.setDescription("A description");

        when(transactionDao.save(isA(Transaction.class))).thenReturn(transaction);

        // when
        TransactionDTO txDTO = transactionService.create(transactionDTO);

        // then

        assertEquals(txDTO.getDate(), dateFormat.format(today));
        assertEquals(txDTO.getDateConverted(TimeZone.getDefault().getID()), dateFormat.parse(dateFormat.format(today)));
        assertEquals(txDTO.getDescription(), "A description");
        assertEquals(txDTO.getAmount(), new BigDecimal("60.85"));
        assertEquals(txDTO.getTransactionId(), uuid);
        assertEquals(txDTO.getUserId(), 1L);
    }

    @Test
    void summarizeTransactions() {
        // given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getDefault());

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2020,Calendar.JUNE,20);
        Date date1 = cal1.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2020,Calendar.NOVEMBER,20);
        Date date2 = cal2.getTime();
        Calendar cal3 = Calendar.getInstance();
        cal3.set(2020,Calendar.DECEMBER,20);
        Date date3 = cal3.getTime();

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        User user = new User();
        user.setId(1L);

        List<Transaction> transactionList = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setDate(date1);
        transaction1.setAmount(new BigDecimal("50.59"));
        transaction1.setDescription("A description 1");
        transaction1.setId(uuid1);
        transaction1.setUser(user);

        Transaction transaction2 = new Transaction();
        transaction2.setDate(date2);
        transaction2.setAmount(new BigDecimal("100.67"));
        transaction2.setDescription("A description 2");
        transaction2.setId(uuid2);
        transaction2.setUser(user);

        Transaction transaction3 = new Transaction();
        transaction3.setDate(date3);
        transaction3.setAmount(new BigDecimal("200.34"));
        transaction3.setDescription("A description 3");
        transaction3.setId(uuid3);
        transaction3.setUser(user);

        transactionList.add(transaction1);
        transactionList.add(transaction2);
        transactionList.add(transaction3);

        when(transactionDao.findAllByUser(user)).thenReturn(transactionList);
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        TransactionSumDTO listTransactionDTO = transactionService.summarizeTransactions(user.getId());

        // then

        assertEquals(listTransactionDTO.getUserId(), user.getId());
        assertEquals(listTransactionDTO.getSum(), new BigDecimal("351.60"));


    }

    @Test
    void summarizeTransactionsUserNull() {
        // given
        User user = new User();
        user.setId(1L);

        when(userDao.findById(user.getId())).thenReturn(Optional.empty());

        // when
        TransactionSumDTO listTransactionDTO = transactionService.summarizeTransactions(user.getId());

        // then
        assertEquals(listTransactionDTO.getUserId(), user.getId());
        assertEquals(listTransactionDTO.getSum(), new BigDecimal("0"));


    }

    @Test
    void generateWeeklyTransactionReportByUserId() {
        // given
        User user = new User();
        user.setId(1L);
        List<Transaction> transactions = TestUtils.generateTransactionListForReport(user);

        when(transactionDao.findAllByUserOrderByDate(user)).thenReturn(transactions);
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        List<ReportLineDTO> listReportLinesDTO = transactionService.generateWeeklyTransactionReportByUserId(user.getId());

        // then
        assertEquals(listReportLinesDTO.size(), 3);

        assertEquals(listReportLinesDTO.get(0).getUserId(), 1L);
        assertEquals(listReportLinesDTO.get(0).getWeekStart(), "2020-02-28 FRIDAY");
        assertEquals(listReportLinesDTO.get(0).getWeekEnd(),"2020-02-29 SATURDAY");
        assertEquals(listReportLinesDTO.get(0).getAmount(), new BigDecimal("807.94"));
        assertEquals(listReportLinesDTO.get(0).getTotalAmount(), new BigDecimal("0"));
        assertEquals(listReportLinesDTO.get(0).getQuantity(), 4);

        assertEquals(listReportLinesDTO.get(1).getUserId(), 1L);
        assertEquals(listReportLinesDTO.get(1).getWeekStart(), "2020-03-01 SUNDAY");
        assertEquals(listReportLinesDTO.get(1).getWeekEnd(),"2020-03-05 THURSDAY");
        assertEquals(listReportLinesDTO.get(1).getAmount(), new BigDecimal("1425.16"));
        assertEquals(listReportLinesDTO.get(1).getTotalAmount(), new BigDecimal("807.94"));
        assertEquals(listReportLinesDTO.get(1).getQuantity(), 6);

        assertEquals(listReportLinesDTO.get(2).getUserId(), 1L);
        assertEquals(listReportLinesDTO.get(2).getWeekStart(), "2020-03-13 FRIDAY");
        assertEquals(listReportLinesDTO.get(2).getWeekEnd(),"2020-03-19 THURSDAY");
        assertEquals(listReportLinesDTO.get(2).getAmount(), new BigDecimal("3071.23"));
        assertEquals(listReportLinesDTO.get(2).getTotalAmount(), new BigDecimal("2233.10"));
        assertEquals(listReportLinesDTO.get(2).getQuantity(), 8);

    }

    @Test
    void findRandomTransaction() {
        // given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getDefault());

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2020,Calendar.JUNE,20);
        Date date1 = cal1.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2020,Calendar.NOVEMBER,20);
        Date date2 = cal2.getTime();
        Calendar cal3 = Calendar.getInstance();
        cal3.set(2020,Calendar.DECEMBER,20);
        Date date3 = cal3.getTime();

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        User user = new User();
        user.setId(1L);

        List<Transaction> transactionList = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setDate(date1);
        transaction1.setAmount(new BigDecimal("50.59"));
        transaction1.setDescription("A description 1");
        transaction1.setId(uuid1);
        transaction1.setUser(user);

        Transaction transaction2 = new Transaction();
        transaction2.setDate(date2);
        transaction2.setAmount(new BigDecimal("100.67"));
        transaction2.setDescription("A description 2");
        transaction2.setId(uuid2);
        transaction2.setUser(user);

        Transaction transaction3 = new Transaction();
        transaction3.setDate(date3);
        transaction3.setAmount(new BigDecimal("200.34"));
        transaction3.setDescription("A description 3");
        transaction3.setId(uuid3);
        transaction3.setUser(user);

        transactionList.add(transaction1);
        transactionList.add(transaction2);
        transactionList.add(transaction3);

        when(transactionDao.findAll()).thenReturn(transactionList);

        // when
        TransactionDTO transactionDTO = transactionService.findRandomTransaction();

        // then
        assertTrue(transactionList.stream().anyMatch(elem -> elem.getUser().getId() == transactionDTO.getUserId()));
        assertTrue(transactionList.stream().anyMatch(elem -> elem.getAmount() == transactionDTO.getAmount()));
        assertTrue(transactionList.stream().anyMatch(elem -> dateFormat.format(elem.getDate()).equals(transactionDTO.getDate())));
        assertTrue(transactionList.stream().anyMatch(elem -> elem.getDescription().equals(transactionDTO.getDescription())));
        assertTrue(transactionList.stream().anyMatch(elem -> elem.getId().equals(transactionDTO.getTransactionId())));
    }
}