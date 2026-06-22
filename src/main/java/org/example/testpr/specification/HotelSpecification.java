package org.example.testpr.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.example.testpr.model.Amenity;
import org.example.testpr.model.Hotel;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class HotelSpecification {

    public static Specification<Hotel> notDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("deleted"));
    }

    public static Specification<Hotel> hasName(String name) {
        if (name == null) return null;

        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasBrand(String brand) {
        if (brand == null) return null;

        return (root, query, cb) ->
                cb.like(cb.lower(root.get("brand")),
                        "%" + brand.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasCity(String city) {
        if (city == null) return null;

        return (root, query, cb) ->
                cb.like(cb.lower(root.get("address").get("city")),
                        "%" + city.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasCountry(String country) {
        if (country == null) return null;

        return (root, query, cb) ->
                cb.like(cb.lower(root.get("address").get("country")),
                        "%" + country.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasAmenityNames(Set<String> names) {
        if (names == null || names.isEmpty()) return null;

        return (root, query, cb) -> {
            query.distinct(true);

            Join<Hotel, Amenity> join =
                    root.join("amenities", JoinType.LEFT);

            return join.get("name").in(names);
        };
    }
}
