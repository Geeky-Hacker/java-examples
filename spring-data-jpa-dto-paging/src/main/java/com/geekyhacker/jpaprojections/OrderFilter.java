package com.geekyhacker.jpaprojections;

/**
 * Optional filters: a null value means "do not filter on it".
 */
public record OrderFilter(String status, String country) {
}
