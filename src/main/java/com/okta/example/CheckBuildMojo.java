package com.okta.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Named
@Singleton
@Mojo(name = "crash", defaultPhase = LifecyclePhase.INITIALIZE)
public class CheckBuildMojo extends AbstractMojo {


    @Parameter(property = "filenames")
    private String [] filenames;

    @Parameter(property = "words")
    private String [] words;

    @Inject
    FileContentsChecker fileContentsChecker;

    @Inject
    Validator validator;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            if (validator.validate(filenames, words)) {
                if (fileContentsChecker.startCheck(filenames, words)) {
                    getLog().info("Crash Build");
                } else {
                    getLog().info("You can do build");
                }
            } else {
                getLog().info("Incorrect plugin input parameters");
            }
        } catch (IOException e) {
            getLog().info("Error in File Path");
        }
    }
}
