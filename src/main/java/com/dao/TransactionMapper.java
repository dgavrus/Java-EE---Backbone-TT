package com.dao;

import com.model.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionMapper implements RowMapper<Transaction> {

    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(rs.getInt("source"));
        transaction.setDestAccountId(rs.getInt("dest"));
        transaction.setMoneyAmount(rs.getInt("moneyamount"));
        try {
            transaction.setDate(stringToDateConverter(rs.getString("date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    private String dateToStringConverter(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private Date stringToDateConverter(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
    }
}
