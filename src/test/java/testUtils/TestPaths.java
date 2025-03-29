package testUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPaths {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path testDir = projectRoot.resolve("src/test/resources");
    public static final Path BASE_C2KA = testDir.resolve("C2KA-BaseRepresentations");
    public static final Path MANUFACTURING_CELL = testDir.resolve("ManufacturingCell");
}
