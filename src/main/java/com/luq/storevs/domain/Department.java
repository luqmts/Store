package com.luq.storevs.domain;

import lombok.Getter;

@Getter
public enum Department {
    TECHNOLOGY ("Technology"),
    FOOD ("Food"),
    CLOTHES ("Clothes");

    private final String description;

    Department(String description) {
        this.description = description;
    }

    public static Department getDepartment(String description) {
        for (Department department : Department.values()) {
            if (department.getDescription().toUpperCase().equals(description)) {
                return department;
            }
        }
        return null;
    }
}