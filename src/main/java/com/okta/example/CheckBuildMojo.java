package com.okta.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;


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
            if (!validator.validate(filenames, words)) {
                throw new MojoExecutionException("Incorrect plugin input parameters");
            }
            if (fileContentsChecker.startCheck(filenames, words)) {
                throw new MojoExecutionException("Find forbidden word\nBan build process");
            } else {
                getLog().info("Nothing forbidden, Build allowed");
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error with file path");
        }
    }
}
