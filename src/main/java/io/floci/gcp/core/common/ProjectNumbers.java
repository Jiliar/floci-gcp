package io.floci.gcp.core.common;

public final class ProjectNumbers {

    private ProjectNumbers() {}

    public static String of(String projectId) {
        return String.valueOf(100_000_000_000L + Math.floorMod((long) projectId.hashCode(), 900_000_000_000L));
    }
}
