package org.nzbhydra.getcomics;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Page {

    private int currentPage;
    private int pageCount;

    private List<Post> posts = new ArrayList<>();


}
