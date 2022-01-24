package com.janetfilter.plugins.dump;

import com.janetfilter.core.Environment;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginConfig;
import com.janetfilter.core.plugin.PluginEntry;

import java.util.ArrayList;
import java.util.List;

public class DumpPlugin implements PluginEntry {
    private final List<MyTransformer> transformers = new ArrayList<>();

    @Override
    public void init(Environment environment, PluginConfig config) {
        transformers.add(new DumpTransformer(environment, config.getBySection("Class")));
    }

    @Override
    public String getName() {
        return "Dump";
    }

    @Override
    public String getAuthor() {
        return "neo";
    }

    @Override
    public String getVersion() {
        return "v1.0.0";
    }

    @Override
    public String getDescription() {
        return "A plugin for the ja-netfilter, it can dump the transformed classes.";
    }

    @Override
    public List<MyTransformer> getTransformers() {
        return transformers;
    }
}
