package com.tatotech.pizzeria.catalog.service;

import com.tatotech.pizzeria.catalog.model.Category;
import com.tatotech.pizzeria.catalog.model.NameTranslation;
import com.tatotech.pizzeria.catalog.model.Product;
import com.tatotech.pizzeria.catalog.model.ProductVariant;
import com.tatotech.pizzeria.catalog.repository.CategoryRepository;
import com.tatotech.pizzeria.catalog.repository.ProductRepository;
import com.tatotech.pizzeria.catalog.web.dto.CatalogResponse;
import com.tatotech.pizzeria.catalog.web.dto.CatalogResponse.CategoryResponse;
import com.tatotech.pizzeria.catalog.web.dto.CatalogResponse.ProductResponse;
import com.tatotech.pizzeria.catalog.web.dto.CatalogResponse.RestaurantResponse;
import com.tatotech.pizzeria.catalog.web.dto.CatalogResponse.VariantResponse;
import com.tatotech.pizzeria.restaurant.service.RestaurantInfo;
import com.tatotech.pizzeria.restaurant.service.RestaurantService;
import com.tatotech.pizzeria.shared.i18n.LocalizedTexts;
import com.tatotech.pizzeria.shared.i18n.SupportedLocale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final RestaurantService restaurantService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CatalogService(RestaurantService restaurantService,
                          CategoryRepository categoryRepository,
                          ProductRepository productRepository) {
        this.restaurantService = restaurantService;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Catálogo público traducido.
     * Si lang no se envía o no está soportado, se usa el idioma por defecto del restaurante.
     * Se omiten productos sin variantes activas y categorías que quedan vacías.
     */
    public CatalogResponse getPublicCatalog(String slug, String lang) {
        RestaurantInfo restaurant = restaurantService.getActiveBySlug(slug);
        SupportedLocale locale = SupportedLocale.fromTag(lang).orElse(restaurant.defaultLocale());

        List<Category> categories =
                categoryRepository.findByRestaurantIdAndActiveTrueOrderBySortOrderAscIdAsc(restaurant.id());

        Map<Long, List<Product>> productsByCategory = productRepository.findVisibleByRestaurant(restaurant.id())
                .stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().getId()));

        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> toCategoryResponse(
                        category,
                        productsByCategory.getOrDefault(category.getId(), List.of()),
                        locale))
                .filter(category -> !category.products().isEmpty())
                .toList();

        return new CatalogResponse(
                new RestaurantResponse(restaurant.name(), restaurant.whatsappPhone()),
                locale.tag(),
                categoryResponses
        );
    }

    private CategoryResponse toCategoryResponse(Category category, List<Product> products, SupportedLocale locale) {
        List<ProductResponse> productResponses = products.stream()
                .map(product -> toProductResponse(product, locale))
                .flatMap(Optional::stream)
                .toList();
        return new CategoryResponse(category.getId(), nameOf(category.getTranslations(), locale), productResponses);
    }

    /** Vacío si el producto no tiene variantes activas o no tiene ninguna traducción. */
    private Optional<ProductResponse> toProductResponse(Product product, SupportedLocale locale) {
        List<VariantResponse> variants = product.getVariants().stream()
                .filter(ProductVariant::isActive)
                .map(variant -> new VariantResponse(
                        variant.getId(),
                        nameOf(variant.getTranslations(), locale),
                        variant.getPrice()))
                .toList();

        if (variants.isEmpty()) {
            return Optional.empty();
        }

        return LocalizedTexts.resolve(product.getTranslations(), locale)
                .map(translation -> new ProductResponse(
                        product.getId(),
                        translation.name(),
                        translation.description(),
                        product.getImageUrl(),
                        product.isAvailable(),
                        variants));
    }

    private static String nameOf(Collection<NameTranslation> translations, SupportedLocale locale) {
        return LocalizedTexts.resolve(translations, locale)
                .map(NameTranslation::name)
                .orElse("");
    }
}
