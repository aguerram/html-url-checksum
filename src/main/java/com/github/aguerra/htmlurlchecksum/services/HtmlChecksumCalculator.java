package com.github.aguerra.htmlurlchecksum.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Document document = Jsoup.parse(file, null);

            Elements cssElements = document.select("link");
            Elements jsElements = document.select("script[src]");
            for (Element element : cssElements) {
                logger.info("Found " + element);
                var newLink = this.calculateCheckSum(element, "href");
                logger.info("Generated link " + newLink);
                element.attr("href", newLink);
            }

            for (Element element : jsElements) {
                logger.info("Found " + element);
                var newLink = this.calculateCheckSum(element, "src");
                logger.info("Generated link " + newLink);
                element.attr("src", newLink);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String calculateCheckSum(Element element, String linkAttr) {
        String link = element.attr(linkAttr);
        if (link == null) return null;
        try {
            String md5 = this.hashFile(link, "MD5");
            return link + "?md5=" + md5;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return link;
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
