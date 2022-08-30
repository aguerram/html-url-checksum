package com.github.aguerra.htmlurlchecksum.services;

import org.apache.hc.core5.net.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlChecksumCalculator {
    private final Logger logger = Logger.getLogger("CSS JS in HTML parser");
    private final List<File> files;
    private final Map<String, String> cache = new HashMap<>();

    public HtmlChecksumCalculator(String path, List<File> files) {
        this.files = files;
        this.startParser();
    }

    public void startParser() {
        for (File file : files) {
            this.cssJsParser(file);
        }
    }

    public void cssJsParser(File file) {
        try {
            //don't judge this coding style, this is speedrun coding
            Document document = Jsoup.parse(file, null);

            Elements cssElements = document.select("link");
            Elements jsElements = document.select("script[src]");
            for (Element element : cssElements) {
                processElement(element, "href");
            }

            for (Element element : jsElements) {
                processElement(element, "src");
            }

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

            byte[] htmlBytes = document.html().getBytes(StandardCharsets.UTF_8);
            outputStream.write(htmlBytes, 0, htmlBytes.length);
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processElement(Element element, String linkAttr) throws URISyntaxException {
        logger.info("Found " + element);
        var link = element.attr(linkAttr);
        //if link doesn't exist, do nothing
        if (StringUtil.isBlank(link)) {
            logger.info(element + " doesn't have an href or src attribute or it's null");
            return;
        }
        URIBuilder builder = new URIBuilder(link);
        //if the link contains already a md5 ignore it
        if (builder.getQueryParams().size() > 0 && !StringUtil.isBlank(
                Optional.ofNullable(builder.getFirstQueryParam("md5"))
                        .map(String::valueOf)
                        .orElse("")
        )) {
            logger.info(element + " has already md5 hash calculated");
            return;
        }
        logger.info("Calculating md5 hash for element " + element);

        var md5 = this.calculateCheckSum(element, linkAttr);
        builder.addParameter("md5", md5);
        element.attr(linkAttr, builder.toString());
    }

    private String calculateCheckSum(Element element, String linkAttr) {
        String link = element.attr(linkAttr);
        if (link == null) return null;
        try {
            String md5 = this.hashFile(link, "MD5");
            return md5;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return "";
    }

    private String hashFile(String link, String algorithm) {
        var cachedHash = this.cache.get(link);
        if (cachedHash != null) return cachedHash;
        try {
            URL remoteFile = new URL(link);
            var contentStream = remoteFile.openStream();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            DigestInputStream dis = new DigestInputStream(contentStream, md);
            byte[] buffer = new byte[1024 * 8];
            while (dis.read(buffer) != -1) {
            }
            dis.close();
            byte[] raw = md.digest();
            String md5 = new String(raw);
            BigInteger bigInt = new BigInteger(1, raw);
            var hash = bigInt.toString(16);
            this.cache.put(link, hash);
            return hash;
        } catch (Exception ex) {

        }
        return "";
    }

}
