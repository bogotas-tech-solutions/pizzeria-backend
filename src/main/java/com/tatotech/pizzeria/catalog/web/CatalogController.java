package com.tatotech.pizzeria.catalog.web;

import com.tatotech.pizzeria.catalog.service.CatalogService;
import com.tatotech.pizzeria.catalog.web.dto.CatalogResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants/{slug}/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * GET /api/v1/restaurants/pizzaria-teste/catalog?lang=es
     */
    @GetMapping
    public CatalogResponse getCatalog(@PathVariable("slug") String slug,
                                      @RequestParam(name = "lang", required = false) String lang) {
        return catalogService.getPublicCatalog(slug, lang);
    }
}
