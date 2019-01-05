package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class IndividualSpecification {

    public static Specification<Individual> findByCriteriaSpecification(String[] criteria) {
        Specification<Individual> specification = null;

        for (String criterion : criteria) {

            if (criterion.contains("state")) {
                String state = StringUtils.substringAfter(criterion, "state=");

                specification = (specification != null) ? specification.and(inState(state)) : inState(state);
            }

            if (criterion.contains("city")) {
                String city = StringUtils.substringAfter(criterion, "city=");

                specification = (specification != null) ? specification.and(inCity(city)) : inCity(city);
            }

            if (criterion.contains("address")) {
                String address = StringUtils.substringAfter(criterion, "address=");

                specification = (specification != null) ? specification.and(withAddress(address)) : withAddress(address);
            }

            if (criterion.contains("firstname")) {
                String firstname;
                if (criterion.contains("like")) {
                    firstname = StringUtils.substringAfter(criterion, "like-firstname=");

                    specification = (specification != null) ? specification.and(likeFirstname(firstname)) : likeFirstname(firstname);
                } else {
                    firstname = StringUtils.substringAfter(criterion, "firstname=");

                    specification = (specification != null) ? specification.and(withFirstname(firstname)) : withFirstname(firstname);
                }
            }

            if (criterion.contains("lastname")) {
                String lastname;

                if (criterion.contains("like")) {
                    lastname = StringUtils.substringAfter(criterion, "like-lastname=");

                    specification = (specification != null) ? specification.and(likeLastname(lastname)) : likeLastname(lastname);
                } else {
                    lastname = StringUtils.substringAfter(criterion, "lastname=");

                    specification = (specification != null) ? specification.and(withLastname(lastname)) : withLastname(lastname);
                }
            }

            if (criterion.contains("birtdate")) {

                if (criterion.contains(",")) {
                    String from = StringUtils.substringBetween(criterion, "birthdate:", ",");
                    String to = StringUtils.substringAfter(criterion, ",");

                    specification = (specification != null) ? specification.and(beetweenBirthDates(from, to)) : beetweenBirthDates(from, to);
                } else {
                    String birtdate;
                    if (criterion.contains("<")) {

                        if (criterion.contains("=")) {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate<=");

                            specification = (specification != null)
                                    ? specification.and(lessThanBirthDate(birtdate).or(withBirthDate(birtdate)))
                                    : lessThanBirthDate(birtdate).or(withBirthDate(birtdate));
                        } else {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate<");

                            specification = (specification != null) ? specification.and(lessThanBirthDate(birtdate)) : lessThanBirthDate(birtdate);
                        }
                    } else if (criterion.contains(">")) {

                        if (criterion.contains("=")) {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate>=");

                            specification = (specification != null)
                                    ? specification.and(greaterThanBirthDate(birtdate).or(withBirthDate(birtdate)))
                                    : greaterThanBirthDate(birtdate).or(withBirthDate(birtdate));
                        } else {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate>");

                            specification = (specification != null) ? specification.and(greaterThanBirthDate(birtdate)) : greaterThanBirthDate(birtdate);
                        }
                    } else if (criterion.contains("=")) {
                        birtdate = StringUtils.substringAfter(criterion, "birthdate=");

                        specification = (specification != null) ? specification.and(withBirthDate(birtdate)) : withBirthDate(birtdate);
                    }
                }
            }

            if (criterion.contains("height")) {
                if (criterion.contains(",")) {
                    String from = StringUtils.substringBetween(criterion, "height:", ",");
                    String to = StringUtils.substringAfter(criterion, ",");

                    specification = (specification != null) ? specification.and(betweenHeights(from, to)) : betweenHeights(from, to);
                } else {
                    String height;

                    if (criterion.contains("<")) {
                        if (criterion.contains("=")) {
                            height = StringUtils.substringAfter(criterion, "height<=");

                            specification = (specification != null)
                                    ? specification.and(shorterThanHeight(height).or(withHeight(height)))
                                    : shorterThanHeight(height).or(withHeight(height));
                        } else {
                            height = StringUtils.substringAfter(criterion, "height<");

                            specification = (specification != null) ? specification.and(shorterThanHeight(height)) : shorterThanHeight(height);
                        }
                    } else if (criterion.contains(">")) {
                        if (criterion.contains("=")) {
                            height = StringUtils.substringAfter(criterion, "height>=");

                            specification = (specification != null)
                                    ? specification.and(tallerThanHeight(height).or(withHeight(height)))
                                    : tallerThanHeight(height).or(withHeight(height));
                        } else {
                            height = StringUtils.substringAfter(criterion, "height>");

                            specification = (specification != null) ? specification.and(tallerThanHeight(height)) : tallerThanHeight(height);
                        }
                    } else if (criterion.contains("=")) {
                        height = StringUtils.substringAfter(criterion, "height=");

                        specification = (specification != null) ? specification.and(withHeight(height)) : withHeight(height);

                    }
                }
            }
            if (criterion.contains("weight")) {
                if (criterion.contains(",")) {
                    String from = StringUtils.substringBetween(criterion, "weight:", ",");
                    String to = StringUtils.substringAfter(criterion, ",");

                    specification = (specification != null) ? specification.and(betweenWeights(from, to)) : betweenWeights(from, to);
                } else {
                    String weight;

                    if (criterion.contains("<")) {
                        if (criterion.contains("=")) {
                            weight = StringUtils.substringAfter(criterion, "weight<=");

                            specification = (specification != null)
                                    ? specification.and(lighterThanWeight(weight).or(withWeight(weight)))
                                    : lighterThanWeight(weight).or(withWeight(weight));
                        } else {
                            weight = StringUtils.substringAfter(criterion, "weight<");

                            specification = (specification != null) ? specification.and(lighterThanWeight(weight)) : lighterThanWeight(weight);
                        }
                    } else if (criterion.contains(">")) {
                        if (criterion.contains("=")) {
                            weight = StringUtils.substringAfter(criterion, "weight>=");

                            specification = (specification != null)
                                    ? specification.and(heavierThanWeight(weight).or(withWeight(weight)))
                                    : heavierThanWeight(weight).or(withWeight(weight));
                        } else {
                            weight = StringUtils.substringAfter(criterion, "weight>");

                            specification = (specification != null) ? specification.and(heavierThanWeight(weight)) : heavierThanWeight(weight);
                        }
                    } else if (criterion.contains("=")) {
                        weight = StringUtils.substringAfter(criterion, "weight=");

                        specification = (specification != null) ? specification.and(withWeight(weight)) : withWeight(weight);
                    }
                }
            }
        }

        return specification;
    }

    private static Specification<Individual> withFirstname(String firstname) {
        if (firstname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("firstname"), firstname);
        }
    }

    private static Specification<Individual> likeFirstname(String firstname) {
        if (firstname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("firstname"), firstname);
        }
    }

    private static Specification<Individual> withLastname(String lastname) {
        if (lastname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("lastname"), lastname);
        }
    }

    private static Specification<Individual> likeLastname(String lastname) {
        if (lastname == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("lastname"), lastname);
        }
    }

    private static Specification<Individual> inState(String state) {
        if (state == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("state"), state);
        }
    }

    private static Specification<Individual> inCity(String city) {
        if (city == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("city"), city);
        }
    }

    private static Specification<Individual> withAddress(String address) {
        if (address == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("address"), address);
        }
    }

    private static Specification<Individual> withBirthDate(String birthDate) {
        if (birthDate == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("birthdate"), convertStringToDate(birthDate));
        }
    }

    private static Specification<Individual> greaterThanBirthDate(String birthDate) {
        if (birthDate == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.greaterThan(root.get("birthdate"), convertStringToDate(birthDate));
        }
    }

    private static Specification<Individual> lessThanBirthDate(String birthDate) {
        if (birthDate == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.lessThan(root.get("birthdate"), convertStringToDate(birthDate));
        }
    }

    private static Specification<Individual> beetweenBirthDates(String from, String to) {
        if (from == null || to == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.between(root.get("birthdate"), convertStringToDate(from), convertStringToDate(to));
        }
    }

    private static Specification<Individual> withHeight(String height) {
        if (height == null) {
            return null;
        } else {
            float h = convertStringToFloat(height);
            return (root, query, cb) -> cb.equal(root.get("height"), h);
        }
    }

    private static Specification<Individual> tallerThanHeight(String height) {
        if (height == null) {
            return null;
        } else {
            float taller = convertStringToFloat(height);
            return (root, query, cb) -> cb.greaterThan(root.get("height"), taller);
        }
    }

    private static Specification<Individual> shorterThanHeight(String height) {
        if (height == null) {
            return null;
        } else {
            float shorter = convertStringToFloat(height);
            return (root, query, cb) -> cb.lessThan(root.get("height"), shorter);
        }
    }

    private static Specification<Individual> betweenHeights(String from, String to) {
        if (from == null || to == null) {
            return null;
        } else {
            float fromHeight = convertStringToFloat(from);
            float toHeight = convertStringToFloat(to);
            return (root, query, cb) -> cb.between(root.get("height"), fromHeight, toHeight);
        }
    }

    private static Specification<Individual> withWeight(String weight) {
        if (weight == null) {
            return null;
        } else {
            float w = convertStringToFloat(weight);
            return (root, query, cb) -> cb.equal(root.get("weight"), w);
        }
    }

    private static Specification<Individual> heavierThanWeight(String weight) {
        if (weight == null) {
            return null;
        } else {
            float heavier = convertStringToFloat(weight);
            return (root, query, cb) -> cb.greaterThan(root.get("weight"), heavier);
        }
    }

    private static Specification<Individual> lighterThanWeight(String weight) {
        if (weight == null) {
            return null;
        } else {
            float lighter = convertStringToFloat(weight);
            return (root, query, cb) -> cb.lessThan(root.get("weight"), lighter);
        }
    }

    private static Specification<Individual> betweenWeights(String from, String to) {
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