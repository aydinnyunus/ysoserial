package ysoserial.test.payloads;

import org.junit.Assert;
import ysoserial.test.CustomTest;
import ysoserial.test.util.Files;
import ysoserial.test.util.OS;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Callable;

public class CommandExecTest implements CustomTest {
    protected final File testFile =
        new File(OS.getTmpDir(), "ysoserial-test-" + UUID.randomUUID().toString().replaceAll("-", ""));

    @Override
    public void run(Callable<Object> payload) throws Exception {
        Assert.assertFalse("test file should not exist", testFile.exists());
        try {
            payload.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Files.waitForFile(testFile, 5000);
        Assert.assertTrue("test file should exist", testFile.exists());
        testFile.deleteOnExit();
    }

    @Override
    public String getPayloadArgs() {
        return getTouchCmd(testFile.toString());
    }

    public static String getTouchCmd(String file) {
        switch (OS.get()) {
            case OSX:
            case LINUX: return "touch " + file;
            case WINDOWS: return "powershell -command new-item -type file " + file;
            default: throw new UnsupportedOperationException("unsupported os");
        }
    }

}
