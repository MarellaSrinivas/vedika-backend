
package com.thevedika.controller;

import com.thevedika.entity.Farmhouse;
import com.thevedika.service.FarmhouseService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SitemapController {

    private static final String SITE_URL = "https://thevedika.com";

    private final FarmhouseService farmhouseService;

    public SitemapController(FarmhouseService farmhouseService) {
        this.farmhouseService = farmhouseService;
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String getSitemap() {

        List<Farmhouse> farmhouses = farmhouseService.getAllFarmhouses();

        StringBuilder xml = new StringBuilder();

        xml.append("""
                <?xml version="1.0" encoding="UTF-8"?>
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                    <url>
                        <loc>https://thevedika.com/</loc>
                        <changefreq>daily</changefreq>
                        <priority>1.0</priority>
                    </url>
                    <url>
                        <loc>https://thevedika.com/farmhouses</loc>
                        <changefreq>daily</changefreq>
                        <priority>0.9</priority>
                    </url>
                """);

        for (Farmhouse farmhouse : farmhouses) {
            xml.append("""
                    <url>
                        <loc>%s/farmhouses/%s</loc>
                        <changefreq>weekly</changefreq>
                        <priority>0.8</priority>
                    </url>
                    """.formatted(SITE_URL, farmhouse.getId()));
        }

        xml.append("</urlset>");

        return xml.toString();
    }
}
