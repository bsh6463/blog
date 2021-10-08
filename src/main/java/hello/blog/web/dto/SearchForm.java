package hello.blog.web.dto;

import lombok.Data;

@Data
public class SearchForm {

    String keyword;

    public SearchForm(String keyword) {
        this.keyword = keyword;
    }

    public SearchForm() {
    }
}
