package com.md;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Gtin {

    private String type;
    private String value;
}
