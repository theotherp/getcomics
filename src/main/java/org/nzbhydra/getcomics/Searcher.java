package org.nzbhydra.getcomics;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class Searcher {

    private static final Logger logger = LoggerFactory.getLogger(Searcher.class);


    private OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public void sendLinkToJdownloader(String decodedMegaLink) throws InterruptedException {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(decodedMegaLink), null);
            Thread.sleep(100);
            clipboard.setContents(new StringSelection(""), null);
            Thread.sleep(100);
            clipboard.setContents(new StringSelection(decodedMegaLink), null);
        } catch (Exception e) {
            logger.error("Error sending link to downloader", e);
        }
    }

    public Page search(final String query, int pageNumber) throws IOException {
        String url = buildUri(query, pageNumber);

        String html = callUrl(url);

        Document doc = Jsoup.parse(html);
        Elements posts = doc.select(".post");
        Page page = new Page();
        for (Element comicPost : posts) {
            Post post = new Post();
            String coverUrl = comicPost.selectFirst("img").attr("src");
            post.setCoverUrl(coverUrl);

            Element postTitle = comicPost.selectFirst(".post-title");
            String postLink = postTitle.selectFirst("a").attr("href");
            post.setLink(postLink);
            String title = postTitle.text();
            post.setTitle(title);

            page.getPosts().add(post);
        }

        Element currentPage = doc.selectFirst(".page-numbers.current");
        page.setCurrentPage(Integer.parseInt(currentPage.text()));
        Element pageNumbers = doc.selectFirst(".page-numbers");
        Elements pages = pageNumbers.select("a.page-numbers");
        page.setPageCount(Integer.parseInt(pages.get(pages.size() - 1).text()));

        return page;
    }

    private String callUrl(String url) throws IOException {
        String cacheFilename = url.hashCode() + ".cache";
        File cacheFile = new File("cache", cacheFilename);

        if (cacheFile.exists()) {
            return new String(Files.readAllBytes(cacheFile.toPath()));
        }

        System.out.println("Calling " + url);
        Call call = httpClient.newCall(new Request.Builder().url(url).build());
        Response response = call.execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException(response.message());
        }
        String html;
        try (ResponseBody body = response.body()) {
            html = body.string();
        }

        Files.write(cacheFile.toPath(), html.getBytes());

        return html;
    }

    private String buildUri(String query, int page) {
        return UriComponentsBuilder.fromHttpUrl("https://getcomics.info/page/").path(String.valueOf(page)).queryParam("s", query).toUriString();
    }


}
