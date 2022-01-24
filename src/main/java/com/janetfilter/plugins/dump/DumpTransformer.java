package com.janetfilter.plugins.dump;

import com.janetfilter.core.Environment;
import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class DumpTransformer implements MyTransformer {
    private final Environment env;
    private final List<FilterRule> rules;
    private File dumpDir;

    public DumpTransformer(Environment env, List<FilterRule> rules) {
        this.env = env;
        this.rules = rules;

        String appName = env.getAppName();
        File dir = new File(env.getBaseDir(), appName.isEmpty() ? "dumped" : "dumped-" + appName);

        if (!dir.exists() && !dir.mkdirs()) {
            DebugInfo.error("Can't make directory: " + dir);
            return;
        }

        if (!dir.isDirectory()) {
            DebugInfo.error("It's not a directory: " + dir);
            return;
        }

        if (!dir.canWrite()) {
            DebugInfo.error("Read-only directory: " + dir);
            return;
        }

        dumpDir = dir;
    }

    @Override
    public String getHookClassName() {
        return null;
    }

    @Override
    public void before(String className, byte[] classBytes) throws Exception {
        if (null == dumpDir || !classMatched(className)) {
            return;
        }

        File classFile = new File(dumpDir, className.replace('/', '_') + ".origin.class");
        Files.write(classFile.toPath(), classBytes);
    }

    @Override
    public void after(String className, byte[] classBytes) throws Exception {
        if (null == dumpDir || !classMatched(className)) {
            return;
        }

        File classFile = new File(dumpDir, className.replace('/', '_') + ".patched.class");
        Files.write(classFile.toPath(), classBytes);
    }

    private boolean classMatched(String className) {
        for (FilterRule rule : rules) {
            if (rule.test(className)) {
                DebugInfo.debug("Dump: " + className + ", rule: " + rule);
                return true;
            }
        }

        return false;
    }
}
