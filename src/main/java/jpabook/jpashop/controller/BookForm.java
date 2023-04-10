package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;


    public BookForm updateBookForm(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        BookForm form = new BookForm();
        form.setId(id);
        form.setName(name);
        form.setPrice(price);
        form.setStockQuantity(stockQuantity);
        form.setAuthor(author);
        form.setIsbn(isbn);
        return form;
    }
}
