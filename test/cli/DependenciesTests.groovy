import grails.test.AbstractCliTestCase

class DependenciesTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDependencies() {

        execute(["dependencies"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
