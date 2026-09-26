package io.floci.gcp.core.common.docker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageCacheServiceTest {

    @ParameterizedTest
    @CsvSource({
            "busybox, busybox:latest",
            "library/busybox, library/busybox:latest",
            "busybox:1.36, busybox:1.36",
            "gcr.io/project/app, gcr.io/project/app:latest",
            "localhost:5000/app, localhost:5000/app:latest",
            "localhost:5000/team/app:v2, localhost:5000/team/app:v2",
            "gcr.io/project/app@sha256:0123abcd, gcr.io/project/app@sha256:0123abcd",
            "gcr.io/project/app:v1@sha256:0123abcd, gcr.io/project/app:v1@sha256:0123abcd"
    })
    void untaggedImagesPullLatest(String requested, String pulled) {
        assertEquals(pulled, ImageCacheService.withDefaultTag(requested));
    }
}
