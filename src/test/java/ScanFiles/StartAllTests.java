package ScanFiles;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({MainTest.class,
        FileVisitorCallableTest.class,
        ScanFileWriterRunnableTest.class})

public class StartAllTests {

}
