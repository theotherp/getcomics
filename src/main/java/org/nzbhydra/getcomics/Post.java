package org.nzbhydra.getcomics;

import lombok.Data;

@Data
public class Post {

    private String coverUrl;
    private String link;
    private String title;
    private int year;
    private int size;
    private String description;

}
