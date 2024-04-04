package com.geekyhacker.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
public class PropertyParameterTest {

    @InjectMocks
    private PropertyParameter propertyParameter;

    @Mock
    private PropertyParameter.Cache cache;

    @ParameterizedTest
    @MethodSource("cacheTestCases")
    void testRetrieve(String key, boolean expected) {
        when(cache.get(any())).thenAnswer((Answer<String>) invocation -> StringUtils.isBlank(invocation.getArgument(0)) ? null : UUID.randomUUID().toString());

        assertEquals(expected, propertyParameter.retrieve(key).isPresent());
    }

    private static Stream<Arguments> cacheTestCases() {
        return Stream.of(arguments("", false), arguments(null, false), arguments("random", true));
    }
}
