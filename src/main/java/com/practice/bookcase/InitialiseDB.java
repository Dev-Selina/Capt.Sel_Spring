package com.practice.bookcase;

import org.sqlite.SQLiteConfig;
import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class InitialiseDB {
    private Connection connectDB() { //use sql import
        Connection con = null;
        try {//error handling, or exception handling "try" block
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            con = DriverManager.getConnection("jdbc:sqlite:" + "A:/Users/Selina/Desktop/Back-end/bookcase/lib/BooksDatabase.db", config.toProperties());
            //the above creates a new connection get connection, and new db file in the pathway explicitly given, and updates properties
        } catch (Exception ex) {
            System.out.println(ex.getClass());
            ex.printStackTrace();
        }
        return con;
    }
    private void createTables(Connection con) {
        try {
            Statement stmnt = con.createStatement();
            String createBooksTable = "CREATE TABLE IF NOT EXISTS tblBooks" + "(book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " book_cover TEXT NOT NULL" + " book_title TEXT NOT NULL," + " book_blurb TEXT NOT NULL," + " book_author TEXT NOT NULL," +
                    " book_released TEXT NOT NULL,";
            stmnt.executeUpdate(createBooksTable);
        } catch (Exception ex) {
            System.out.println(ex.getClass());
        }
    }
    public Connection getDBConnection() {//if it is the first time the db table is being created
        Connection con = connectDB();
        createTables(con);
        return con;
    }
    public ArrayList<Book> getBooks(Connection con) {
        ArrayList<Book> books = new ArrayList<>();
        Statement stmnt = null; //can't leave it empty because gives error of not being initialised, it gets initialised at #wolf
        try {//#wolf the statement becomes initialised
            String getBooksQuery = "SELECT * FROM tblBooks"; //grabs everything from the table, gets using a query
            stmnt = con.createStatement();
            ResultSet resultSet = stmnt.executeQuery(getBooksQuery); //existing class that stores it like a list

            while (resultSet.next()) { //while there is a next element of result set
                Book book = new Book();
                book.setBookID(resultSet.getInt("book_id")); //gets an integer from bookid column then setting it to BookID
                book.setBookTitle(resultSet.getString("book_title"));
                book.setBookAuthor(resultSet.getString("book_author"));
                book.setBookYear(resultSet.getString("book_released"));
                book.setBookBlurb(resultSet.getString("book_blurb"));
                book.setCoverURL(resultSet.getString("book_cover"));
                books.add(book); //then add all of that data into book, put it into an array list #cat
            }
        } catch (Exception ex) {
            System.out.println(ex.getClass());
            ex.printStackTrace();
        } finally {//after using a try and catch, the finally block always runs regardless of throwing an exception. it is used to "clean house"
            // i.e. closing the statement to make sure that if it gets stuck then it will be stopped because of finally
            try {
                stmnt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return books; //#cat returns the arraylist
    }
    public void addNewBook(Connection con, Book book){
        try{
            String addBooks = "INSERT INTO tblBooks (book_title, book_author, book_released, book_blurb, book_cover) VALUES" + "(?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(addBooks);
            pst.setString(1, book.getBookTitle());
            pst.setString(2, book.getBookAuthor());
            pst.setString(3, book.getBookYear());
            pst.setString(4, book.getBookBlurb());
            pst.setString(5, book.getCoverURL());
            pst.executeUpdate();
            pst.close();
        }
        catch(Exception ex){
            System.out.println(ex.getClass());
            ex.printStackTrace();
        }
        finally{
            try{
                con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    public boolean updateBookRecord(Connection con, Book book){
        try{
            String updateRecord = "UPDATE tblBooks SET book_title = ?, book_author = ?, book_released = ?, book_blurb = ?, book_cover = ? WHERE book_id = ?";
            PreparedStatement prepStatement = con.prepareStatement(updateRecord);
            prepStatement.setString(1,book.getBookTitle());
            prepStatement.setString(2,book.getBookAuthor());
            prepStatement.setString(3,book.getBookYear());
            prepStatement.setString(4,book.getBookBlurb());
            prepStatement.setString(5,book.getCoverURL());
            prepStatement.setInt(6,book.getBookID());

            prepStatement.executeUpdate();
            prepStatement.close();
        } catch(Exception ex){
            System.out.println(ex.getClass());
            ex.printStackTrace();
            return false;
        }
        finally {
            try{
                con.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    return true;
}
public boolean removeBook(Connection con, Book book){
        try{
            String removeBook = "DELETE FROM tblBooks WHERE book_id = ?";
            PreparedStatement prepStatement = con.prepareStatement(removeBook);
            prepStatement.setInt(1, book.getBookID());
            prepStatement.executeUpdate();
            prepStatement.close();
        }
        catch(Exception ex){
            System.out.println(ex.getClass());
            ex.printStackTrace();
            return false;
        } finally {
            try{
                con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return true;
}
//      Main method not required for application
//    public static void main(String[] args) {
//        InitialiseDB initDB = new InitialiseDB();
//        //initDB.connectDB(); modified after createtable method is created
//        initDB.createTables(initDB.connectDB());//adding createtables
//    }
}

