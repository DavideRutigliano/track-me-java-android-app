package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class IndividualSpecification {

    public static Specification<Individual> withFirstname(String firstname) {
        if (firstname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("firstname"), firstname);
        }
    }

    public static Specification<Individual> likeFirstname(String firstname) {
        if (firstname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("firstname"), firstname);
        }
    }

    public static Specification<Individual> withLastname(String lastname) {
        if (lastname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("lastname"), lastname);
        }
    }

    public static Specification<Individual> likeLastname(String lastname) {
        if (lastname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("lastname"), lastname);
        }
    }

    public static Specification<Individual> inState(String state) {
        if (state == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("state"), state);
        }
    }

    public static Specification<Individual> inCity(String city) {
        if (city == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("city"), city);
        }
    }

    public static Specification<Individual> withAddress(String address) {
        if (address == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("address"), address);
        }
    }

    public static Specification<Individual> withBirthDate(String birthDate) {
        if (birthDate == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("birthdate"), convertStringToDate(birthDate));
        }
    }

    public static Specification<Individual> greaterThanBirthDate(String birthDate) {
        if (birthDate == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.greaterThan(root.get("birthdate"), convertStringToDate(birthDate));
        }
    }

    public static Specification<Individual> lessThanBirthDate(String birthDate) {
        if (birthDate == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.lessThan(root.get("birthdate"), convertStringToDate(birthDate));
        }
    }

    public static Specification<Individual> beetweenBirthDates(String from, String to) {
        if (from == null || to == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.between(root.get("birthdate"), convertStringToDate(from), convertStringToDate(to));
        }
    }

    public static Specification<Individual> withHeight(String height) {
        if (height == null) {
            return null;
        } else {
            float h = convertStringToFloat(height);
            return (root, query, cb) -> cb.equal(root.get("height"), h);
        }
    }

    public static Specification<Individual> tallerThanHeight(String height) {
        if (height == null) {
            return null;
        } else {
            float taller = convertStringToFloat(height);
            return (root, query, cb) -> cb.greaterThan(root.get("height"), taller);
        }
    }

    public static Specification<Individual> shorterThanHeight(String height) {
        if (height == null) {
            return null;
        } else {
            float shorter = convertStringToFloat(height);
            return (root, query, cb) -> cb.lessThan(root.get("height"), shorter);
        }
    }

    public static Specification<Individual> betweenHeights(String from, String to) {
        if (from == null || to == null) {
            return null;
        } else {
            float fromHeight = convertStringToFloat(from);
            float toHeight = convertStringToFloat(to);
            return (root, query, cb) -> cb.between(root.get("height"), fromHeight, toHeight);
        }
    }

    public static Specification<Individual> withWeight(String weight) {
        if (weight == null) {
            return null;
        } else {
            float w = convertStringToFloat(weight);
            return (root, query, cb) -> cb.equal(root.get("weight"), w);
        }
    }

    public static Specification<Individual> heavierThanWeight(String weight) {
        if (weight == null) {
            return null;
        } else {
            float heavier = convertStringToFloat(weight);
            return (root, query, cb) -> cb.greaterThan(root.get("weight"), heavier);
        }
    }

    public static Specification<Individual> lighterThanWeight(String weight) {
        if (weight == null) {
            return null;
        } else {
            float lighter = convertStringToFloat(weight);
            return (root, query, cb) -> cb.lessThan(root.get("weight"), lighter);
        }
    }

    public static Specification<Individual> betweenWeights(String from, String to) {
        if (from == null || to == null) {
            return null;
        } else {
            float fromWeight = convertStringToFloat(from);
            float toWeight = convertStringToFloat(to);
            return (root, query, cb) -> cb.between(root.get("weight"), fromWeight, toWeight);
        }
    }

    private static float convertStringToFloat(String value) {
        return Float.parseFloat(value);
    }

    private static Date convertStringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date parsed;
        try {
            parsed = format.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return new java.sql.Date(parsed.getTime());
    }
}