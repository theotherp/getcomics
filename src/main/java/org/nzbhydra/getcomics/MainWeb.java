package org.nzbhydra.getcomics;

import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class MainWeb {

    private static final Logger logger = LoggerFactory.getLogger(MainWeb.class);

    private static final Set<String> KNOWN_ROUTES = Sets.newHashSet("/jobs", "/history", "/movies", "/wantedMovies", "/myRequests", "/news", "/search", "/fileSearch", "/search", "/admin");

    @Autowired
    private Searcher searcher;

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    @ResponseBody
    public Object browse(HttpSession session, Principal principal, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletPath();
        boolean notFound = false;
        if (!"/".equals(path)) {

            InputStream resourceAsStream = MainWeb.class.getResourceAsStream(path);
            if (resourceAsStream != null) {
                return new InputStreamResource(resourceAsStream);
            } else if (KNOWN_ROUTES.stream().noneMatch(path::startsWith)) {
                return ResponseEntity.notFound();
            }
        }

        String indexHtml = Resources.toString(MainWeb.class.getResource("/static/html/index.html"), Charset.defaultCharset())
                .replace("{baseHref}", "/");
        return ResponseEntity.ok(indexHtml);
    }

    @RequestMapping(value = "/search/{query}/{page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object search(@PathVariable String query, @PathVariable int page) throws IOException {
        Page search = searcher.search(query, page);
        return ResponseEntity.ok(search);
    }

    @RequestMapping(value = "/download", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object download(@RequestBody List<String> links) {
        links.forEach(x -> {
            try {
                searcher.sendLinkToJdownloader(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok();
    }


}
