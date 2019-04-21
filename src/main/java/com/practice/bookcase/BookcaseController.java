package com.practice.bookcase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//first endpoint for web-API.
@Controller
public class BookcaseController {
    public int count = 0;
//Updating viewBooks method & consequently Removing init method after creating table and using SQLite db. init originally seed the data.
//data to be stored within the database will be done via the frontend
//    private void init(){
//        if (count < 1){
//            Book.addNewBook(new Book(1, "To Kill a Mocking Bird", "Harper Lee", "1960"));
//            Book.addNewBook(new Book(2, "Pride and Prejudice", "Jane Austen", "1813"));
//            Book.addNewBook(new Book(3, "Of Mice and Men", "John Steinbeck", "1937"));
//            count++;
//        }
//    }
    Book book = new Book(); //creating book object of class type Book meaning it requires everything that is in constructor
    @GetMapping("/viewBooks")
    public String viewBooks(Model model){
//        init();
        book.getAllBooks();
        model.addAttribute("book", Book.books);
        return "viewBooks";
    }
    @GetMapping("/addBook")
    public String addBook(Model model){
        int nextID = Book.books.size() + 1;
        System.out.println(nextID);
        Book book = new Book();
        book.setBookID(nextID);
        model.addAttribute("book", book);
        return "addBook";
    }
    @PostMapping("/bookAdded")
    public String bookAdded(@ModelAttribute Book book){
        book.addNewBook(book);
        return"index";
    }
    @GetMapping("/editDeleteBook")
    public String editDeleteBook(Model model){
//        init();
        model.addAttribute("book",Book.books);
        model.addAttribute("aBook", new Book());
        return "editDeleteBook";
    }
    @PostMapping("/bookEdit")
    public String bookToEdit(@ModelAttribute Book book, Model model){
        Book b = new Book();
        for(Book bk : Book.books){
            if(bk.getBookID() == book.getBookID()){
                b = bk;
            }
        }
        model.addAttribute("book", b);
        return "bookEdit";
    }
    @PostMapping("/bookSaved")
    public String bookSaved(@ModelAttribute Book book){
//        removing the code below and updating booksaved to change db
//        for(Book b : Book.books){
//            if(b.getBookID() == book.getBookID()){
//                int index = Book.books.indexOf(b);
//                Book.books.set(index, book);
//                break;
//            }
//        }
        book.updateBookRecord(book);
        return "/index";
    }
    @PostMapping("/bookDelete")
    public String bookDelete(@ModelAttribute Book book, Model model){
        Book b = new Book();
        for(Book bk : Book.books){
            if(bk.getBookID() == book.getBookID()){
                b = bk;
            }
        }
        model.addAttribute("book", b);
        return "bookDelete";
    }
    @PostMapping("/bookDeleted")
    public String bookDeleted(@ModelAttribute Book book){
//        Removing for loop because getting info from db and method against (SQLite)db
//        for(Book b : Book.books){
//            if(b.getBookID() == book.getBookID()){
//                int index = Book.books.indexOf(b);
//                Book.books.remove(index);
//                break;
//            }
//        }
        book.removeBook(book);
        return "/index";
    }

}
