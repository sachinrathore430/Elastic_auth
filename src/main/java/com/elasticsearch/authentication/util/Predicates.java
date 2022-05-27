package com.elasticsearch.authentication.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Predicates {

    public static final Predicate<String> isNullOrEmpty = (str) -> str == null || str.isEmpty();

    public static final Predicate<String> isNotNullAndNotEmpty = (str) -> str != null && !str.isEmpty();

    public static final Predicate<List> isNullOrEmptyList = (list) -> list == null || list.isEmpty();

    public static Predicate<Object> isNull = Objects::isNull;

    public static Predicate<List> isNotNullAndNotEmptyList=(list -> list!=null && !list.isEmpty());
}
