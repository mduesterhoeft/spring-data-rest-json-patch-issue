package com.md;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.hateoas.Identifiable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Gtin gtin;
}
